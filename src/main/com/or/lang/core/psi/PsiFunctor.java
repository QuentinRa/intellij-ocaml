package com.or.lang.core.psi;

import com.intellij.psi.PsiNameIdentifierOwner;
import com.intellij.psi.StubBasedPsiElement;
import com.or.lang.core.psi.impl.PsiFunctorResult;
import com.or.lang.core.stub.PsiModuleStub;
import org.jetbrains.annotations.Nullable;

public interface PsiFunctor extends PsiNameIdentifierOwner, PsiModule, StubBasedPsiElement<PsiModuleStub> {

    @Nullable
    PsiFunctorResult getReturnType();

}
