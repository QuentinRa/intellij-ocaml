package com.or.lang.core.psi;

import com.intellij.psi.PsiNameIdentifierOwner;
import com.intellij.psi.StubBasedPsiElement;
import com.or.lang.core.stub.PsiParameterStub;

public interface PsiParameter extends PsiNameIdentifierOwner, PsiQualifiedPathElement, PsiSignatureElement, StubBasedPsiElement<PsiParameterStub> {

}
