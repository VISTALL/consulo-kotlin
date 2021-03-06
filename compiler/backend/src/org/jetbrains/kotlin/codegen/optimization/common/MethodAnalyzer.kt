/*
 * Copyright 2010-2015 JetBrains s.r.o.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.jetbrains.kotlin.codegen.optimization.common

import org.jetbrains.org.objectweb.asm.Opcodes
import org.jetbrains.org.objectweb.asm.Type
import org.jetbrains.org.objectweb.asm.tree.*
import org.jetbrains.org.objectweb.asm.tree.analysis
import org.jetbrains.org.objectweb.asm.tree.analysis.*
import org.jetbrains.org.objectweb.asm.util.Printer
import java.util.*
import kotlin.properties.Delegates

public open class MethodAnalyzer<V : Value>(
        public val owner: String,
        public val method: MethodNode,
        protected val interpreter: Interpreter<V>
) {
    public val instructions: InsnList = method.instructions
    public val nInsns: Int = instructions.size()

    public val frames: Array<Frame<V>?> = arrayOfNulls(nInsns)

    private val handlers: Array<MutableList<TryCatchBlockNode>?> = arrayOfNulls(nInsns)
    private val queued: BooleanArray = BooleanArray(nInsns)
    private val queue: IntArray = IntArray(nInsns)
    private var top: Int = 0

    protected open fun init(owner: String, m: MethodNode) {}

    protected open fun newFrame(nLocals: Int, nStack: Int): Frame<V> = Frame(nLocals, nStack)

    protected open fun newFrame(src: Frame<out V>): Frame<V> {
        val frame = newFrame(src.getLocals(), src.getMaxStackSize())
        frame.init(src)
        return frame
    }

    protected open fun visitControlFlowEdge(insn: Int, successor: Int): Boolean = true

    protected open fun visitControlFlowExceptionEdge(insn: Int, successor: Int): Boolean = true

    protected open fun visitControlFlowExceptionEdge(insn: Int, tcb: TryCatchBlockNode): Boolean =
            visitControlFlowExceptionEdge(insn, instructions.indexOf(tcb.handler))

    public fun analyze(): Array<Frame<V>?> {
        if (nInsns == 0) return frames

        checkAssertions()

        computeExceptionHandlersForEachInsn(method)

        val current = newFrame(method.maxLocals, method.maxStack)
        val handler = newFrame(method.maxLocals, method.maxStack)
        initControlFlowAnalysis(current, method, owner)

        while (top > 0) {
            val insn = queue[--top]
            val f = frames[insn]!!
            queued[insn] = false

            val insnNode = method.instructions[insn]
            try {
                val insnOpcode = insnNode.getOpcode()
                val insnType = insnNode.getType()

                if (insnType == AbstractInsnNode.LABEL || insnType == AbstractInsnNode.LINE || insnType == AbstractInsnNode.FRAME) {
                    visitNopInsn(f, insn)
                }
                else {
                    current.init(f).execute(insnNode, interpreter)

                    when {
                        insnNode is JumpInsnNode ->
                            visitJumpInsnNode(insnNode, current, insn, insnOpcode)
                        insnNode is LookupSwitchInsnNode ->
                            visitLookupSwitchInsnNode(insnNode, current, insn)
                        insnNode is TableSwitchInsnNode ->
                            visitTableSwitchInsnNode(insnNode, current, insn)
                        insnOpcode != Opcodes.ATHROW && (insnOpcode < Opcodes.IRETURN || insnOpcode > Opcodes.RETURN) ->
                            visitOpInsn(current, insn)
                        else -> {}
                    }
                }

                handlers[insn]?.forEach { tcb ->
                    val exnType = Type.getObjectType(tcb.type?:"java/lang/Throwable")
                    val jump = instructions.indexOf(tcb.handler)
                    if (visitControlFlowExceptionEdge(insn, tcb)) {
                        handler.init(f)
                        handler.clearStack()
                        handler.push(interpreter.newValue(exnType))
                        mergeControlFlowEdge(jump, handler)
                    }
                }

            }
            catch (e: AnalyzerException) {
                throw AnalyzerException(e.node, "Error at instruction " + insn + ": " + e.getMessage(), e)
            }
            catch (e: Exception) {
                throw AnalyzerException(insnNode, "Error at instruction " + insn + ": " + e.getMessage(), e)
            }

        }

        return frames
    }

    public fun getFrame(insn: AbstractInsnNode): Frame<V>? =
            frames[instructions.indexOf(insn)]

    private fun checkAssertions() {
        if (instructions.toArray() any { it.getOpcode() == Opcodes.JSR || it.getOpcode() == Opcodes.RET })
            throw AssertionError("Subroutines are deprecated since Java 6")
    }

    private fun visitOpInsn(current: Frame<V>, insn: Int) {
        processControlFlowEdge(current, insn, insn + 1)
    }

    private fun visitTableSwitchInsnNode(insnNode: TableSwitchInsnNode, current: Frame<V>, insn: Int) {
        var jump = instructions.indexOf(insnNode.dflt)
        processControlFlowEdge(current, insn, jump)
        for (label in insnNode.labels) {
            jump = instructions.indexOf(label)
            processControlFlowEdge(current, insn, jump)
        }
    }

    private fun visitLookupSwitchInsnNode(insnNode: LookupSwitchInsnNode, current: Frame<V>, insn: Int) {
        var jump = instructions.indexOf(insnNode.dflt)
        processControlFlowEdge(current, insn, jump)
        for (label in insnNode.labels) {
            jump = instructions.indexOf(label)
            processControlFlowEdge(current, insn, jump)
        }
    }

    private fun visitJumpInsnNode(insnNode: JumpInsnNode, current: Frame<V>, insn: Int, insnOpcode: Int) {
        if (insnOpcode != Opcodes.GOTO && insnOpcode != Opcodes.JSR) {
            processControlFlowEdge(current, insn, insn + 1)
        }
        val jump = instructions.indexOf(insnNode.label)
        processControlFlowEdge(current, insn, jump)
    }

    private fun visitNopInsn(f: Frame<V>, insn: Int) {
        processControlFlowEdge(f, insn, insn + 1)
    }

    private fun processControlFlowEdge(current: Frame<V>, insn: Int, jump: Int) {
        if (visitControlFlowEdge(insn, jump)) {
            mergeControlFlowEdge(jump, current)
        }
    }

    private fun initControlFlowAnalysis(current: Frame<V>, m: MethodNode, owner: String) {
        current.setReturn(interpreter.newValue(Type.getReturnType(m.desc)))
        val args = Type.getArgumentTypes(m.desc)
        var local = 0
        if ((m.access and Opcodes.ACC_STATIC) == 0) {
            val ctype = Type.getObjectType(owner)
            current.setLocal(local++, interpreter.newValue(ctype))
        }
        for (arg in args) {
            current.setLocal(local++, interpreter.newValue(arg))
            if (arg.getSize() == 2) {
                current.setLocal(local++, interpreter.newValue(null))
            }
        }
        while (local < m.maxLocals) {
            current.setLocal(local++, interpreter.newValue(null))
        }
        mergeControlFlowEdge(0, current)

        init(owner, m)
    }

    private fun computeExceptionHandlersForEachInsn(m: MethodNode) {
        for (tcb in m.tryCatchBlocks) {
            val begin = instructions.indexOf(tcb.start)
            val end = instructions.indexOf(tcb.end)
            for (j in begin..end - 1) {
                var insnHandlers: MutableList<TryCatchBlockNode>? = handlers[j]
                if (insnHandlers == null) {
                    insnHandlers = ArrayList<TryCatchBlockNode>()
                    handlers[j] = insnHandlers
                }
                insnHandlers.add(tcb)
            }
        }
    }

    private fun mergeControlFlowEdge(insn: Int, frame: Frame<V>) {
        val oldFrame = frames[insn]
        var changes: Boolean

        if (oldFrame == null) {
            frames[insn] = newFrame(frame)
            changes = true
        }
        else {
            changes = oldFrame.merge(frame, interpreter)
        }
        if (changes && !queued[insn]) {
            queued[insn] = true
            queue[top++] = insn
        }
    }

}