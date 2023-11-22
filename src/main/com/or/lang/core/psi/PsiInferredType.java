package com.or.lang.core.psi;

import com.intellij.psi.PsiElement;
import org.jetbrains.annotations.Nullable;

public interface PsiInferredType extends PsiElement {
    @Nullable
    PsiSignature getInferredType();

    boolean hasInferredType();
}
