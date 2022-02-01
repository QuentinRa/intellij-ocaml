package com.or.ide.search.index;

import com.intellij.psi.stubs.IntStubIndexExtension;
import com.intellij.psi.stubs.StubIndexKey;
import com.or.lang.core.psi.PsiParameter;
import com.or.lang.core.stub.type.PsiParameterStubElementType;
import org.jetbrains.annotations.NotNull;

public class ParameterFqnIndex extends IntStubIndexExtension<PsiParameter> {

    @Override
    public int getVersion() {
        return super.getVersion() + PsiParameterStubElementType.VERSION;
    }

    @Override
    public @NotNull StubIndexKey<Integer, PsiParameter> getKey() {
        return IndexKeys.PARAMETERS_FQN;
    }
}
