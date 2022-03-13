package com.ocaml.ide.insight;

import com.intellij.lang.ExpressionTypeProvider;
import com.intellij.psi.PsiElement;
import com.intellij.psi.SyntaxTraverser;
import com.ocaml.OCamlBundle;
import com.ocaml.sdk.annot.OCamlInferredSignature;
import org.jetbrains.annotations.NotNull;

import java.util.List;

// CTRL + SHIFT + P
// expression -> type
// _ -> getErrorHint()

// Way of improvements
// - disable or make it usable in the REPL
// - If .annot not loaded, show a proper message
public class OCamlTypeInfoHint extends ExpressionTypeProvider<PsiElement> {

    public static final String UNKNOWN_TYPE = OCamlBundle.message("ocaml.insight.unknown.type");

    @Override public @NotNull String getInformationHint(@NotNull PsiElement element) {
        OCamlAnnotResultsService annot = element.getProject().getService(OCamlAnnotResultsService.class);
        OCamlInferredSignature annotation = annot.findAnnotationFor(element);
        return annotation == null || annotation.type.isEmpty() ? UNKNOWN_TYPE : annotation.type;
    }

    @Override public @NotNull String getErrorHint() {
        return OCamlBundle.message("editor.no.expression.found");
    }

    @Override public @NotNull List<PsiElement> getExpressionsAt(@NotNull final PsiElement elementAt) {
        // small optimisation: skip
        if (OCamlInsightFilter.isWhiteSpace(elementAt)) return List.of();

//        System.out.println("e:"+elementAt+" ("+elementAt.getText()+")");
//        System.out.println("  range:"+ elementAt.getTextRange());
//        System.out.println("  p:"+elementAt.getParent()+" ("+elementAt.getParent().getText().replace("\n","\\n")+")");
//        System.out.println("  pp:"+elementAt.getParent().getParent()+" ("+elementAt.getParent().getParent().getText().replace("\n","\\n")+")");
//        System.out.println("  next:"+elementAt.getNextSibling()+" ("+(elementAt.getNextSibling() == null? "null" : elementAt.getNextSibling().getText().replace("\n","\\n"))+")");
//        System.out.println("  prev:"+elementAt.getPrevSibling()+" ("+(elementAt.getPrevSibling() == null? "null" : elementAt.getPrevSibling().getText().replace("\n","\\n"))+")");

        // if we got some info for this element, then this is a valid element
        OCamlAnnotResultsService annot = elementAt.getProject().getService(OCamlAnnotResultsService.class);

        // thank you SyntaxTraverser, I love you
        List<PsiElement> psiElements = SyntaxTraverser.psiApi().parents(elementAt).toList();
        for (PsiElement psiElement : psiElements) {
            if (!annot.hasInfoForElement(psiElement)) continue;
            return List.of(psiElement);
        }

        return List.of();
    }

    @Override public @NotNull String getAdvancedInformationHint(@NotNull PsiElement element) {
        return super.getAdvancedInformationHint(element);
    }
}
