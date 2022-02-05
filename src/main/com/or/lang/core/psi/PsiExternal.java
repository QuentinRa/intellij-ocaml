package com.or.lang.core.psi;

import com.intellij.psi.StubBasedPsiElement;
import com.or.lang.core.stub.PsiExternalStub;

public interface PsiExternal extends PsiQualifiedPathElement, PsiSignatureElement, PsiStructuredElement, StubBasedPsiElement<PsiExternalStub> {
    boolean isFunction();

}
