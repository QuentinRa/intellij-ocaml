package com.or.ide.search.index;

import com.intellij.psi.stubs.IntStubIndexExtension;
import com.intellij.psi.stubs.StubIndexKey;
import com.or.lang.core.psi.PsiException;
import com.or.lang.core.stub.type.PsiExceptionStubElementType;
import org.jetbrains.annotations.NotNull;

public class ExceptionFqnIndex extends IntStubIndexExtension<PsiException> {

    @Override
    public int getVersion() {
        return super.getVersion() + PsiExceptionStubElementType.VERSION;
    }

    @Override
    public @NotNull StubIndexKey<Integer, PsiException> getKey() {
        return IndexKeys.EXCEPTIONS_FQN;
    }
}
