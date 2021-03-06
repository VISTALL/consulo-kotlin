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

package org.jetbrains.kotlin.idea.completion.test;

import com.intellij.codeInsight.CodeInsightSettings;
import com.intellij.codeInsight.completion.CompletionTestCase;
import com.intellij.openapi.vfs.newvfs.impl.VfsRootAccess;
import com.intellij.util.ArrayUtil;
import org.jetbrains.kotlin.test.JetTestUtils;

abstract public class KotlinCompletionTestCase extends CompletionTestCase {
    @Override
    protected void setUp() throws Exception {
        super.setUp();
        VfsRootAccess.allowRootAccess(JetTestUtils.getHomeDirectory());
        CodeInsightSettings.getInstance().EXCLUDED_PACKAGES = new String[]{"excludedPackage", "somePackage.ExcludedClass"};
    }

    @Override
    protected void tearDown() throws Exception {
        CodeInsightSettings.getInstance().EXCLUDED_PACKAGES = ArrayUtil.EMPTY_STRING_ARRAY;
        VfsRootAccess.disallowRootAccess(JetTestUtils.getHomeDirectory());
        super.tearDown();
    }
}
