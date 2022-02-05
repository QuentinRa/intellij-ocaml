package com.or.lang.core.psi;

import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiNameIdentifierOwner;
import org.jetbrains.annotations.Nullable;

public interface PsiAnnotation extends PsiNameIdentifierOwner {
    @Nullable PsiElement getValue();
}
