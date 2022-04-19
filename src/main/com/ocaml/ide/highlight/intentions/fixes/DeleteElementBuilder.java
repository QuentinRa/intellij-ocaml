package com.ocaml.ide.highlight.intentions.fixes;

import com.intellij.codeInsight.daemon.impl.quickfix.DeleteElementFix;
import com.intellij.codeInsight.intention.IntentionAction;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiWhiteSpace;
import com.ocaml.ide.highlight.intentions.IntentionActionBuilder;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 * Remove an element
 */
public class DeleteElementBuilder implements IntentionActionBuilder {

    private final boolean reformat;

    public DeleteElementBuilder() {
        this(true);
    }

    public DeleteElementBuilder(boolean reformat) {
        this.reformat = reformat;
    }

    @Override
    public @Nullable List<IntentionAction> build(@Nullable PsiElement start, @Nullable PsiElement end, @NotNull PsiFile file) {

        // delete element
        if (start != null) {
            DeleteElementFix deleteElement = new DeleteElementFix(start);
            if (reformat) {
                PsiElement nextSibling = start.getNextSibling();
                // delete space too
                if (nextSibling instanceof PsiWhiteSpace)
                    return List.of(deleteElement, new DeleteElementFix(nextSibling));
            }
            return List.of(deleteElement);
        }

        return null;
    }
}
