package com.or.lang.core.psi;

import com.intellij.psi.NavigatablePsiElement;
import com.intellij.psi.PsiElement;
import com.intellij.psi.StubBasedPsiElement;
import com.or.lang.core.stub.PsiKlassStub;
import org.jetbrains.annotations.Nullable;

// Using a K to avoid confusion with PsiClass from IntelliJ
public interface PsiKlass extends PsiQualifiedPathElement, NavigatablePsiElement, PsiStructuredElement, StubBasedPsiElement<PsiKlassStub> {
    @Nullable
    PsiElement getClassBody();

}
