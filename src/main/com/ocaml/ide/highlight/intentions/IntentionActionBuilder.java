package com.ocaml.ide.highlight.intentions;

import com.intellij.codeInsight.intention.IntentionAction;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface IntentionActionBuilder {

    /**
     * Create an intention action
     * @param start may be null for file-level actions
     * @param end if null, then start = end
     * @param file the file
     * @return an intention action or null if failed to create one
     */
    @Nullable IntentionAction build(@Nullable PsiElement start, @Nullable PsiElement end,
                                    @NotNull PsiFile file);

}
