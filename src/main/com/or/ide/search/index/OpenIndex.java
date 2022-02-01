package com.or.ide.search.index;

import com.intellij.psi.stubs.StringStubIndexExtension;
import com.intellij.psi.stubs.StubIndexKey;
import com.or.lang.core.psi.PsiOpen;
import com.or.lang.core.stub.type.PsiOpenStubElementType;
import org.jetbrains.annotations.NotNull;

public class OpenIndex extends StringStubIndexExtension<PsiOpen> {

    @Override
    public int getVersion() {
        return super.getVersion() + PsiOpenStubElementType.VERSION;
    }

    @Override
    public @NotNull StubIndexKey<String, PsiOpen> getKey() {
        return IndexKeys.OPENS;
    }
}
