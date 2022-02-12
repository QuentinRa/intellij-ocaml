package com.ocaml.ide.insight;

import com.intellij.lang.ExpressionTypeProvider;
import com.intellij.psi.PsiElement;
import com.ocaml.OCamlBundle;
import com.ocaml.sdk.annot.OCamlInferredSignature;
import com.or.lang.core.psi.PsiParameter;
import com.or.lang.core.psi.impl.PsiLowerIdentifier;
import com.or.lang.core.psi.impl.PsiLowerSymbolImpl;
import org.jetbrains.annotations.NotNull;

import java.util.List;

// CTRL + SHIFT + P
// expression -> type
// _ -> getErrorHint()
public class OCamlTypeInfoHint extends ExpressionTypeProvider<PsiElement> {

    @Override public @NotNull String getInformationHint(@NotNull PsiElement element) {
        OCamlAnnotResultsService annot = element.getProject().getService(OCamlAnnotResultsService.class);
        OCamlInferredSignature annotation = annot.findAnnotationFor(element);
        if (annotation == null) {
            // todo: bundle
            return "Please, wait until this file is compiled.";
        }
        // todo: bundle
        return annotation.type.isEmpty() ? "Unknown" : annotation.type;
    }

    @Override public @NotNull String getErrorHint() {
        return OCamlBundle.message("editor.no.expression.found");
    }

    @Override public @NotNull List<PsiElement> getExpressionsAt(@NotNull PsiElement elementAt) {
        // allow parameters too
        if (OCamlInsightFilter.shouldSkip(elementAt)
                // allow parameters
                && !(elementAt instanceof PsiParameter)
                // and variables/function names
                && !(elementAt.getParent() instanceof PsiLowerIdentifier)
                // and variables/function inside a function
                && !(elementAt.getParent() instanceof PsiLowerSymbolImpl))
            return List.of();
        return List.of(elementAt);
    }
}
