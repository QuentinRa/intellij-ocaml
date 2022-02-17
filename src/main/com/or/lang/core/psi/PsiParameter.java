package com.or.lang.core.psi;

import com.intellij.psi.PsiNameIdentifierOwner;
import com.intellij.psi.StubBasedPsiElement;
import com.or.lang.core.stub.PsiParameterStub;
import org.jetbrains.annotations.NotNull;

public interface PsiParameter extends PsiNameIdentifierOwner, PsiQualifiedPathElement, PsiSignatureElement, StubBasedPsiElement<PsiParameterStub> {

    /**
     * Return the name of this parameter. Will be not null,
     * even if the result is inaccurate.
     * @return the name of the parameter
     */
    @NotNull String getRealName();

}
