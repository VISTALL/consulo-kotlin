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

package org.jetbrains.kotlin.psi

import com.intellij.lang.ASTNode
import com.intellij.psi.PsiElement
import com.intellij.psi.tree.IElementType
import com.intellij.psi.tree.TokenSet
import org.jetbrains.kotlin.psi.stubs.KotlinNameReferenceExpressionStub
import org.jetbrains.kotlin.psi.stubs.elements.JetStubElementTypes
import org.jetbrains.kotlin.name.Name
import org.jetbrains.kotlin.lexer.JetTokens

import org.jetbrains.kotlin.lexer.JetTokens.*

public class JetNameReferenceExpression : JetExpressionImplStub<KotlinNameReferenceExpressionStub>, JetSimpleNameExpression {
    public constructor(node: ASTNode) : super(node) {
    }

    public constructor(stub: KotlinNameReferenceExpressionStub) : super(stub, JetStubElementTypes.REFERENCE_EXPRESSION) {
    }

    override fun getReferencedName(): String {
        val stub = getStub()
        if (stub != null) {
            return stub.getReferencedName()
        }
        return JetSimpleNameExpressionImpl.getReferencedNameImpl(this)
    }

    override fun getReferencedNameAsName(): Name {
        return JetSimpleNameExpressionImpl.getReferencedNameAsNameImpl(this)
    }

    override fun getReferencedNameElement(): PsiElement {
        val element = findChildByType(NAME_REFERENCE_EXPRESSIONS) ?: return this
        return element
    }

    override fun getIdentifier(): PsiElement? {
        return findChildByType(JetTokens.IDENTIFIER)
    }

    override fun getReferencedNameElementType(): IElementType {
        return JetSimpleNameExpressionImpl.getReferencedNameElementTypeImpl(this)
    }

    override fun <R, D> accept(visitor: JetVisitor<R, D>, data: D): R {
        return visitor.visitSimpleNameExpression(this, data)
    }

    companion object {
        private val NAME_REFERENCE_EXPRESSIONS = TokenSet.create(IDENTIFIER, FIELD_IDENTIFIER, THIS_KEYWORD, SUPER_KEYWORD)
    }
}
