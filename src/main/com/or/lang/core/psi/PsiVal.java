package com.or.lang.core.psi;

import com.intellij.psi.NavigatablePsiElement;
import com.intellij.psi.StubBasedPsiElement;
import com.or.lang.core.stub.PsiValStub;

public interface PsiVal extends PsiVar, PsiQualifiedPathElement, PsiSignatureElement, NavigatablePsiElement, PsiStructuredElement, StubBasedPsiElement<PsiValStub> {
}
