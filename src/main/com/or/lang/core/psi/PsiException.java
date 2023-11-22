package com.or.lang.core.psi;

import com.intellij.psi.NavigatablePsiElement;
import com.intellij.psi.PsiNameIdentifierOwner;
import com.intellij.psi.StubBasedPsiElement;
import com.or.lang.core.stub.PsiExceptionStub;
import org.jetbrains.annotations.Nullable;

public interface PsiException extends NavigatablePsiElement, PsiStructuredElement, PsiNameIdentifierOwner, PsiQualifiedPathElement, StubBasedPsiElement<PsiExceptionStub> {
    @Nullable
    String getAlias();
}
