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

package org.jetbrains.kotlin.idea.debugger.evaluate

import com.intellij.debugger.engine.evaluation.EvaluateExceptionUtil
import com.intellij.debugger.engine.evaluation.EvaluationContextImpl
import consulo.internal.com.sun.jdi.ClassType
import consulo.internal.com.sun.jdi.InvalidStackFrameException
import consulo.internal.com.sun.jdi.ObjectReference
import org.jetbrains.eval4j.Value
import org.jetbrains.eval4j.jdi.asJdiValue
import org.jetbrains.eval4j.jdi.asValue
import org.jetbrains.eval4j.obj
import org.jetbrains.kotlin.codegen.AsmUtil
import org.jetbrains.kotlin.codegen.inline.InlineCodegenUtil
import org.jetbrains.kotlin.idea.util.application.runReadAction
import org.jetbrains.kotlin.resolve.DescriptorUtils
import org.jetbrains.kotlin.resolve.jvm.AsmTypes
import org.jetbrains.org.objectweb.asm.Type

class FrameVisitor(context: EvaluationContextImpl) {
    private val project = context.getDebugProcess().getProject()
    private val frame = context.getFrameProxy()?.getStackFrame()

    public fun findValue(name: String, asmType: Type?, checkType: Boolean, failIfNotFound: Boolean): Value? {
        if (frame == null) return null

        try {
            when (name) {
                THIS_NAME -> {
                    val thisValue = findThis(asmType)
                    if (thisValue != null) {
                        return thisValue
                    }
                }
                else -> {
                    val localVariable = if (isFunctionType(asmType))
                        findLocalVariableForLocalFun(name, asmType, checkType)
                    else
                        findLocalVariable(name, asmType, checkType)

                    if (localVariable != null) {
                        return localVariable
                    }

                    val capturedValName = getCapturedFieldName(name)
                    val capturedVal = findCapturedLocalVariable(capturedValName, asmType, checkType)
                    if (capturedVal != null) {
                        return capturedVal
                    }
                }
            }

            return fail("Cannot find local variable: name = $name${if (checkType) ", type = " + asmType.toString() else ""}", failIfNotFound)
        }
        catch(e: InvalidStackFrameException) {
            throw EvaluateExceptionUtil.createEvaluateException("Local variable $name is unavailable in current frame")
        }
    }

    private fun fail(message: String, shouldFail: Boolean): Value? {
        return if (shouldFail) throw EvaluateExceptionUtil.createEvaluateException(message) else null
    }

    private fun findThis(asmType: Type?): Value? {
        val thisObject = frame!!.thisObject()
        if (thisObject != null) {
            val eval4jValue = thisObject.asValue()
            if (isValueOfCorrectType(eval4jValue, asmType, true)) return eval4jValue
        }

        val receiver = findValue(RECEIVER_NAME, asmType, checkType = true, failIfNotFound = false)
        if (receiver != null) return receiver

        val this0 = findValue(AsmUtil.CAPTURED_THIS_FIELD, asmType, checkType = true, failIfNotFound = false)
        if (this0 != null) return this0

        val `$this` = findValue("\$this", asmType, checkType = false, failIfNotFound = false)
        if (`$this` != null) return `$this`

        return null
    }

    private fun findLocalVariableForLocalFun(name: String, asmType: Type?, checkType: Boolean): Value? {
        return findLocalVariable(name + "$", asmType, checkType)
    }

    private fun isFunctionType(type: Type?): Boolean {
        return type?.getSort() == Type.OBJECT &&
               type!!.getInternalName().startsWith(InlineCodegenUtil.NUMBERED_FUNCTION_PREFIX)
    }

    private fun findLocalVariable(name: String, asmType: Type?, checkType: Boolean): Value? {
        val localVariable = frame!!.visibleVariableByName(name)
        if (localVariable == null) return null

        val eval4jValue = frame.getValue(localVariable).asValue()
        val sharedVarValue = getValueIfSharedVar(eval4jValue, asmType, checkType)
        if (sharedVarValue != null) {
            return sharedVarValue
        }

        if (isValueOfCorrectType(eval4jValue, asmType, checkType)) {
            return eval4jValue
        }

        return null
    }

    private fun findCapturedLocalVariable(name: String, asmType: Type?, checkType: Boolean): Value? {
        val thisObject = frame?.thisObject() ?: return null

        var thisObj: Value? = thisObject.asValue()
        var capturedVal: Value? = null
        while (capturedVal == null && thisObj != null) {
            capturedVal = getField(thisObj, name, asmType, checkType)
            if (capturedVal == null) {
                thisObj = getField(thisObj, AsmUtil.CAPTURED_THIS_FIELD, null, false)
            }
        }

        if (capturedVal != null) {
            val sharedVarValue = getValueIfSharedVar(capturedVal, asmType, checkType)
            if (sharedVarValue != null) {
                return sharedVarValue
            }
            return capturedVal
        }

        return null
    }

    private fun isValueOfCorrectType(value: Value, asmType: Type?, shouldCheckType: Boolean): Boolean {
        if (!shouldCheckType || asmType == null || value.asmType == asmType) return true
        if (project == null) return false

        if ((value.obj() as? consulo.internal.com.sun.jdi.ObjectReference)?.referenceType().isSubclass(asmType.getClassName())) {
            return true
        }

        val thisDesc = value.asmType.getClassDescriptor(project)
        val expDesc = asmType.getClassDescriptor(project)
        return thisDesc != null && expDesc != null && runReadAction { DescriptorUtils.isSubclass(thisDesc, expDesc) }
    }

    private fun getField(owner: Value, name: String, asmType: Type?, checkType: Boolean): Value? {
        try {
            val obj = owner.asJdiValue(frame!!.virtualMachine(), owner.asmType)
            if (obj !is ObjectReference) return null

            val _class = obj.referenceType()
            val field = _class.fieldByName(name)
            if (field == null) return null

            val fieldValue = obj.getValue(field).asValue()
            if (isValueOfCorrectType(fieldValue, asmType, checkType)) return fieldValue
            return null
        }
        catch (e: Exception) {
            return null
        }
    }

    private fun Value.isSharedVar(): Boolean {
        return this.asmType.getSort() == Type.OBJECT && this.asmType.getInternalName().startsWith(AsmTypes.REF_TYPE_PREFIX)
    }

    private fun getValueIfSharedVar(value: Value, expectedType: Type?, checkType: Boolean): Value? {
        if (!value.isSharedVar()) return null

        val sharedVarValue = getField(value, "element", expectedType, checkType)
        if (sharedVarValue != null && isValueOfCorrectType(sharedVarValue, expectedType, checkType)) {
            return sharedVarValue
        }
        return null
    }

    private fun getCapturedFieldName(name: String) = when (name) {
        RECEIVER_NAME -> AsmUtil.CAPTURED_RECEIVER_FIELD
        THIS_NAME -> AsmUtil.CAPTURED_THIS_FIELD
        AsmUtil.CAPTURED_RECEIVER_FIELD -> name
        AsmUtil.CAPTURED_THIS_FIELD -> name
        else -> "$$name"
    }

    private fun consulo.internal.com.sun.jdi.Type?.isSubclass(superClassName: String): Boolean {
        if (this !is ClassType) return false
        if (allInterfaces().any { it.name() == superClassName }) {
            return true
        }

        var superClass = this.superclass()
        while (superClass != null) {
            if (superClass.name() == superClassName) {
                return true
            }
            superClass = superClass.superclass()
        }
        return false
    }
}
