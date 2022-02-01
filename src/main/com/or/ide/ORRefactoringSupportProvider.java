package com.or.ide;

import com.intellij.lang.refactoring.RefactoringSupportProvider;
import com.intellij.psi.PsiElement;
import com.or.lang.core.psi.impl.PsiLowerIdentifier;
import com.or.lang.core.psi.impl.PsiUpperIdentifier;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ORRefactoringSupportProvider extends RefactoringSupportProvider {
    @Override
    public boolean isMemberInplaceRenameAvailable(
            @NotNull PsiElement element, @Nullable PsiElement context) {
        return element instanceof PsiUpperIdentifier || element instanceof PsiLowerIdentifier;
    }
}
