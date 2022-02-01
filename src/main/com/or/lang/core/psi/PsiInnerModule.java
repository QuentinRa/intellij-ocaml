package com.or.lang.core.psi;

import com.intellij.psi.PsiElement;
import com.intellij.psi.StubBasedPsiElement;
import com.or.lang.core.psi.impl.PsiModuleType;
import com.or.lang.core.stub.PsiModuleStub;
import org.jetbrains.annotations.Nullable;

public interface PsiInnerModule extends PsiModule, StubBasedPsiElement<PsiModuleStub> {
    boolean isComponent();

    boolean isFunctorCall();

    @Nullable
    PsiModuleType getModuleType();

    @Nullable
    PsiElement getBody();
}
