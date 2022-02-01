package com.or.lang.core.psi;

import com.intellij.psi.NavigatablePsiElement;
import com.intellij.psi.PsiNameIdentifierOwner;
import com.intellij.psi.StubBasedPsiElement;
import com.or.lang.core.stub.PsiRecordFieldStub;

public interface PsiRecordField extends PsiNameIdentifierOwner, PsiQualifiedPathElement, NavigatablePsiElement, PsiSignatureElement, StubBasedPsiElement<PsiRecordFieldStub> {
}
