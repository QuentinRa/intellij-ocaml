package com.or.lang.core.psi;

import com.intellij.psi.PsiElement;

public interface PsiSignature extends PsiElement, PsiLanguageConverter {
    boolean isFunction();

}
