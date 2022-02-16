package com.ocaml.lang.utils;

import com.intellij.openapi.util.Pair;
import com.intellij.psi.PsiComment;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiWhiteSpace;
import com.intellij.psi.impl.source.tree.LeafPsiElement;
import com.intellij.psi.tree.IElementType;
import com.ocaml.OCamlLanguage;
import com.ocaml.ide.insight.OCamlAnnotResultsService;
import com.ocaml.sdk.annot.OCamlInferredSignature;
import com.or.lang.OCamlTypes;
import com.or.lang.core.psi.PsiLiteralExpression;
import com.or.lang.core.psi.PsiLowerSymbol;
import com.or.lang.core.psi.PsiUpperSymbol;
import com.or.lang.core.psi.impl.PsiScopedExpr;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;

/**
 * Find a function given an element that may be part of a function call.
 * From what I know, there is no way in the code from ReasonML to do this,
 * so I have to do this :(. This parser is quite bugged, sorry for that. I through
 * of relying on the .annot file to do this easily, but I can't think of something ><'.
 */
public class OCamlFindFunction {

    private final OCamlAnnotResultsService annot;
    private final PsiElement startingElement;

    public OCamlFindFunction(@NotNull PsiElement startingElement) {
        this.startingElement = startingElement;
        this.annot = this.startingElement.getProject().getService(OCamlAnnotResultsService.class);
    }

    public @NotNull ArrayList<Pair<PsiElement, Integer>> lookForFunction() {
        return lookForFunction(startingElement, -1, new ArrayList<>());
    }

    private @NotNull ArrayList<Pair<PsiElement, Integer>> lookForFunction(@NotNull PsiElement caret,
                                                                          int index,
                                                                          ArrayList<Pair<PsiElement, Integer>> candidates) {
        @Nullable PsiElement prevSibling = caret.getPrevSibling();
        // skip spaces and comments
        if (caret instanceof PsiWhiteSpace || caret instanceof PsiComment)
            return callLookForFunction(prevSibling, index, candidates);

        // todo: remove
        System.out.println("current is:"+caret.getClass());
        System.out.println("current is:"+caret.getText());
        if (prevSibling != null) System.out.println("  previous will be:"+prevSibling.getClass());

        if (caret instanceof PsiLiteralExpression || caret instanceof PsiScopedExpr)
            return callLookForFunction(prevSibling, index+1, candidates); // value => new argument

        if (caret instanceof LeafPsiElement) {
            IElementType elementType = ((LeafPsiElement) caret).getElementType();
            // todo: remove
            System.out.println("  with type:"+elementType);

            if (elementType.equals(OCamlTypes.INT_VALUE) || elementType.equals(OCamlTypes.FLOAT_VALUE)
                    || elementType.equals(OCamlTypes.BOOL_VALUE))
                // value => new argument
                return callLookForFunction(prevSibling, index+1, candidates);
            // ref <value> is still the same argument
            if (elementType.equals(OCamlTypes.REF))
                return callLookForFunction(prevSibling, index, candidates);
            if (elementType.equals(OCamlTypes.LPAREN)) {
                boolean isUnit = OCamlPsiUtils.isNextMeaningfulNextSibling(caret, OCamlTypes.RPAREN);
                if (isUnit)
                    // get out of the Scoped expression
                    // unit = value => new argument
                    return callLookForFunction(caret.getParent().getPrevSibling(), index+1, candidates);
            }

            if (elementType.equals(OCamlTypes.RPAREN)) {
                // we are moving from ')' to before '('
                PsiElement newPrev  = OCamlPsiUtils.getPreviousMeaningfulNextSibling(caret, OCamlTypes.LPAREN);
                if (newPrev != null)
                    // get out of the Scoped expression
                    // unit = value => new argument
                    return callLookForFunction(newPrev.getParent().getPrevSibling(), index+1, candidates);
            }

            // that's a variable
            if (elementType.equals(OCamlTypes.LIDENT))
                // value => new argument
                return callLookForFunction(caret.getParent().getPrevSibling(), index+1, candidates);

            // assuming that the syntax is OK, then skip
            if (elementType.equals(OCamlTypes.EXCLAMATION_MARK))
                return callLookForFunction(prevSibling, index, candidates);

            if (elementType.equals(OCamlTypes.COMMA)) {
                if (candidates.isEmpty()) // index must be set to 0, because we are leaving the
                    // couple, meaning we are not in the scope of a function
                    // nested in the couple
                    return callLookForFunction(caret.getParent().getPrevSibling(), 0, candidates);
                else return candidates; // done
            }
        } else // cannot be function if it's a token

            // name of a function
            if (caret instanceof PsiLowerSymbol) {
                if (prevSibling instanceof LeafPsiElement && ((LeafPsiElement) prevSibling).getElementType().equals(OCamlTypes.DOT)) {
                    // Module.something
                    if (prevSibling.getPrevSibling() instanceof PsiUpperSymbol) {
                        // skip
                        prevSibling = prevSibling.getPrevSibling().getPrevSibling();
                    }
                }

                OCamlInferredSignature signature = annot.findAnnotationFor(caret);
                // this is a real function, the token is present
                if (signature != null && signature.type.contains(OCamlLanguage.FUNCTION_SIGNATURE_SEPARATOR)) {
                    candidates.add(0, new Pair<>(caret, index));
                }

                return callLookForFunction(prevSibling, index+1, candidates);
            }

        // at the end, because it could have been unit for ()
        // and we may not have to do anything, if we were inside this scoped expression,
        // and we found a candidate
        if (candidates.isEmpty() && caret.getParent() instanceof PsiScopedExpr) {
            // this expression is a new argument
            return callLookForFunction(caret.getParent().getPrevSibling(), 0, candidates);
        }

        // no function
        return candidates;
    }

    private @NotNull ArrayList<Pair<PsiElement, Integer>> callLookForFunction(@Nullable PsiElement prevSibling, int index,
                                                                              ArrayList<Pair<PsiElement, Integer>> candidates) {
        return prevSibling == null ? candidates : lookForFunction(prevSibling, index, candidates);
    }

}
