package com.or.lang.core.psi;

import com.intellij.psi.NavigatablePsiElement;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiNameIdentifierOwner;
import com.intellij.psi.StubBasedPsiElement;
import com.or.lang.core.stub.PsiTypeStub;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;

public interface PsiType extends PsiNameIdentifierOwner, PsiQualifiedPathElement, NavigatablePsiElement, PsiStructuredElement, StubBasedPsiElement<PsiTypeStub> {
    @Nullable
    PsiElement getBinding();

    @NotNull
    Collection<PsiVariantDeclaration> getVariants();

    boolean isJsObject();

    boolean isRecord();

    boolean isAbstract();
}
