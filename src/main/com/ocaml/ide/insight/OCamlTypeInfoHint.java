package com.ocaml.ide.insight;

import com.intellij.lang.ExpressionTypeProvider;
import com.intellij.psi.PsiElement;
import com.ocaml.OCamlBundle;
import org.jetbrains.annotations.NotNull;

import java.util.List;

// CTRL + SHIFT + P
// expression -> type
// _ -> getErrorHint()
public class OCamlTypeInfoHint extends ExpressionTypeProvider<PsiElement> {

    @Override public @NotNull String getInformationHint(@NotNull PsiElement element) {
        return "Feature not support for now";
    }

    @Override public @NotNull String getErrorHint() {
        return OCamlBundle.message("editor.no.expression.found");
    }

    @Override public @NotNull List<PsiElement> getExpressionsAt(@NotNull PsiElement elementAt) {
        return List.of(elementAt);
    }
}
