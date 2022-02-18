package com.ocaml.ide.insight;

import com.intellij.lang.ExpressionTypeProvider;
import com.intellij.psi.PsiElement;
import com.ocaml.OCamlBundle;
import com.ocaml.lang.utils.OCamlPsiUtils;
import com.ocaml.sdk.annot.OCamlInferredSignature;
import com.or.lang.OCamlTypes;
import com.or.lang.core.psi.PsiLet;
import com.or.lang.core.psi.PsiLetBinding;
import com.or.lang.core.psi.PsiLowerSymbol;
import com.or.lang.core.psi.PsiParameter;
import com.or.lang.core.psi.impl.PsiFunctionBody;
import com.or.lang.core.psi.impl.PsiLowerIdentifier;
import com.or.lang.core.psi.impl.PsiPatternMatch;
import com.or.lang.core.psi.impl.PsiScopedExpr;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Unmodifiable;

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
        // OK for literals
        if (OCamlInsightFilter.isLiteral(elementAt)) return getResultOk(elementAt);
        // Scoped Expressions (ex: [])
        if (elementAt instanceof PsiScopedExpr) return getResultOk(elementAt);
        if (elementAt.getParent() instanceof PsiScopedExpr) return getResultOk(elementAt.getParent());
        // unit
        if(elementAt.getText().equals(OCamlTypes.LPAREN.getSymbol())) {
            PsiElement element = OCamlPsiUtils.getNextMeaningfulSibling(elementAt, OCamlTypes.RPAREN);
            // it's crazy, but well it happens, and I don't care why
            if (element == null) element = OCamlPsiUtils.getPreviousMeaningfulSibling(elementAt, OCamlTypes.RPAREN);
            if (element != null) {
                return elementAt.getParent() instanceof PsiParameter ?
                        List.of(element.getParent()) :  List.of(elementAt, element);
            }
        }

        // lower identifiers
        PsiElement parent = elementAt.getParent();
        if (parent instanceof PsiLowerIdentifier || parent instanceof PsiLowerSymbol) {
            // LowerIdentifiers are nested in a LID
            // LowerSymbols are nested in a LSymbol
            parent = elementAt.getParent().getParent();
            if (parent instanceof PsiLet
                    || parent instanceof PsiParameter // ex: f x -> "x"
                    || parent instanceof PsiLetBinding // ex: in y -> "y"
                    || parent instanceof PsiFunctionBody) // some LetBinding are inside a function body
                return getResultOk(elementAt);
        }

        // | _ -> ... => _
        if (parent instanceof PsiPatternMatch) return getResultOk(elementAt);

        // is let _ ?
        // we need to check the text, otherwise ANY child of let will be marked as "OK"
        // (i.e.: and, =, ...)
        if (elementAt.getText().equals(OCamlTypes.UNDERSCORE.getSymbol()) && parent instanceof PsiLet) {
            return getResultOk(elementAt);
        }

        // useful when I needed to add new elements
//        PsiElement e = elementAt;
//        System.out.println("e:"+e);
//        System.out.println("  class:"+e.getClass());
//        System.out.println("  text:"+e.getText());
//        System.out.println("  parent:"+e.getParent());
//        System.out.println("    class:"+e.getParent().getClass());
//        System.out.println("    parent:"+e.getParent().getParent());
//        System.out.println("      class:"+e.getParent().getParent().getClass());
//        System.out.println("  next:"+e.getPrevSibling());
//        System.out.println("  previous:"+e.getNextSibling());
        return List.of();
    }

    @Contract(pure = true)
    private static @NotNull @Unmodifiable List<PsiElement> getResultOk(@NotNull PsiElement elementAt) {
        return List.of(elementAt);
    }
}
