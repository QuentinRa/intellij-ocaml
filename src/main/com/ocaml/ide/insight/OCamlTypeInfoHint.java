package com.ocaml.ide.insight;

import com.intellij.lang.ExpressionTypeProvider;
import com.intellij.psi.PsiElement;
import com.ocaml.OCamlBundle;
import com.ocaml.lang.utils.OCamlAnnotUtils;
import com.ocaml.sdk.annot.OCamlInferredSignature;
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
            return OCamlBundle.message("ocaml.insight.warning.not.compiled");
        }
        return annotation.type.isEmpty() ? OCamlBundle.message("ocaml.insight.unknown.type") : annotation.type;
    }

    @Override public @NotNull String getErrorHint() {
        return OCamlBundle.message("editor.no.expression.found");
    }

    @Override public @NotNull List<PsiElement> getExpressionsAt(@NotNull final PsiElement elementAt) {
        // small optimisation: skip
        if (OCamlInsightFilter.isWhiteSpace(elementAt)) return List.of();
        // if we got some info for this element, then this is a valid element
        // note: we should check list made of multiples elements
        OCamlAnnotResultsService annot = elementAt.getProject().getService(OCamlAnnotResultsService.class);
        if(annot.hasInfoForElement(elementAt))
            return List.of(elementAt);
        return List.of();
    }
}
