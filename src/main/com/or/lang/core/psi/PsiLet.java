package com.or.lang.core.psi;

import com.intellij.psi.NavigatablePsiElement;
import com.intellij.psi.PsiElement;
import com.intellij.psi.StubBasedPsiElement;
import com.or.lang.core.stub.PsiLetStub;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.List;

public interface PsiLet extends PsiVar, PsiSignatureElement, PsiInferredType, PsiQualifiedPathElement, NavigatablePsiElement, PsiStructuredElement, StubBasedPsiElement<PsiLetStub> {
    @Nullable
    PsiLetBinding getBinding();

    @Nullable
    PsiFunction getFunction();

    boolean isRecord();

    boolean isJsObject();

    boolean isScopeIdentifier();

    @Nullable
    String getAlias();

    @Nullable
    PsiElement resolveAlias();

    @NotNull
    Collection<PsiElement> getScopeChildren();

    boolean isDeconstruction();

    @NotNull
    List<PsiElement> getDeconstructedElements();

    boolean isPrivate();
}
