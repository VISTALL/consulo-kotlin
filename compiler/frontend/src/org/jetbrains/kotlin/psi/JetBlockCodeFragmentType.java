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

package org.jetbrains.kotlin.psi;

import com.intellij.lang.*;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiElement;
import com.intellij.util.LanguageVersionUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.kotlin.parsing.JetParser;
import org.jetbrains.kotlin.psi.stubs.elements.JetFileElementType;

public class JetBlockCodeFragmentType extends JetFileElementType {
    private static final String NAME = "kotlin.BLOCK_CODE_FRAGMENT";

    public JetBlockCodeFragmentType() {
        super(NAME);
    }

    @NotNull
    @Override
    public String getExternalId() {
        return NAME;
    }

    @Override
    protected ASTNode doParseContents(@NotNull ASTNode chameleon, @NotNull PsiElement psi) {
        Project project = psi.getProject();
        Language languageForParser = getLanguageForParser(psi);
        LanguageVersion languageVersion = LanguageVersionUtil.findDefaultVersion(languageForParser);
        PsiBuilder builder = PsiBuilderFactory.getInstance().createBuilder(project, chameleon, null, languageForParser, languageVersion, chameleon.getChars());
        return JetParser.parseBlockCodeFragment(builder).getFirstChildNode();
    }
}

