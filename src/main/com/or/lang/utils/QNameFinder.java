package com.or.lang.utils;

import com.intellij.psi.PsiElement;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Set;

public interface QNameFinder {
    @NotNull Set<String> extractPotentialPaths(@Nullable PsiElement element);
}
