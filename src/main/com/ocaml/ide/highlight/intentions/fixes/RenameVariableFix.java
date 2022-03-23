package com.ocaml.ide.highlight.intentions.fixes;

import com.intellij.codeInsight.daemon.impl.quickfix.RenameElementFix;
import com.intellij.codeInsight.intention.IntentionAction;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiNamedElement;
import com.ocaml.ide.highlight.intentions.IntentionActionBuilder;
import com.ocaml.lang.utils.OCamlPsiUtils;
import com.or.lang.OCamlTypes;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Rename a variable
 */
public class RenameVariableFix implements IntentionActionBuilder {

    public final String newName;

    public RenameVariableFix(String newName) {
        this.newName = newName;
    }

    // if the variable 'x' is like this Some(x),
    // then '(x)' or '( x )' is matched by 'unused', so we can't naively
    // replace the given position with _ (we need to find the name of the variable)
    @Override @Nullable public IntentionAction build(@Nullable PsiElement start, @Nullable PsiElement end,
                                                     @NotNull PsiFile file) {
        if (start == null) return null;
        // skip
        if (start.getText().equals(OCamlTypes.LPAREN.getSymbol()))
            start = OCamlPsiUtils.skipMeaninglessNextSibling(start);
        if (start == null) return null; // not happening at this point
        // I can foresee a bug here one day (3-23-2022)
        // We should log this, shouldn't occur, but can occur...
        if (!(start instanceof PsiNamedElement)) return null;

        // there is already a fix for this
        return new RenameElementFix((PsiNamedElement) start, newName);
    }
}
