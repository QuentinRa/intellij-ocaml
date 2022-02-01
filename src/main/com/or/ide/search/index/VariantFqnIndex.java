package com.or.ide.search.index;

import com.intellij.psi.stubs.IntStubIndexExtension;
import com.intellij.psi.stubs.StubIndexKey;
import com.or.lang.core.psi.PsiVariantDeclaration;
import com.or.lang.core.stub.type.PsiVariantStubElementType;
import org.jetbrains.annotations.NotNull;

public class VariantFqnIndex extends IntStubIndexExtension<PsiVariantDeclaration> {

    @Override
    public int getVersion() {
        return super.getVersion() + PsiVariantStubElementType.VERSION;
    }

    @Override
    public @NotNull StubIndexKey<Integer, PsiVariantDeclaration> getKey() {
        return IndexKeys.VARIANTS_FQN;
    }
}
