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

package org.jetbrains.kotlin.idea.copyright;

import com.intellij.psi.PsiFile;
import com.maddyhome.idea.copyright.CopyrightProfile;
import com.maddyhome.idea.copyright.psi.BaseUpdateCopyrightsProvider;
import com.maddyhome.idea.copyright.psi.UpdatePsiFileCopyright;
import org.jetbrains.annotations.NotNull;
import org.mustbe.consulo.copyright.config.CopyrightFileConfig;

public class UpdateKotlinCopyrightsProvider extends BaseUpdateCopyrightsProvider {
    @NotNull
    @Override
    public UpdatePsiFileCopyright<CopyrightFileConfig> createInstance(
            @NotNull PsiFile file, @NotNull CopyrightProfile profile
    ) {
        return new UpdateKotlinCopyright(file, profile);
    }
}
