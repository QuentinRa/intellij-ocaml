package com.or.ide.search.index;

import com.intellij.psi.stubs.StringStubIndexExtension;
import com.intellij.psi.stubs.StubIndexKey;
import com.or.lang.core.psi.PsiInclude;
import com.or.lang.core.stub.type.PsiIncludeStubElementType;
import org.jetbrains.annotations.NotNull;

public class IncludeIndex extends StringStubIndexExtension<PsiInclude> {

    @Override
    public int getVersion() {
        return super.getVersion() + PsiIncludeStubElementType.VERSION;
    }

    @Override
    public @NotNull StubIndexKey<String, PsiInclude> getKey() {
        return IndexKeys.INCLUDES;
    }
}
