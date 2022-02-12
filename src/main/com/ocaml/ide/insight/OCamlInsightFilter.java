package com.ocaml.ide.insight;

import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiWhiteSpace;
import com.intellij.psi.tree.IElementType;
import com.or.lang.OCamlTypes;
import com.or.lang.core.psi.PsiLiteralExpression;
import com.or.lang.core.psi.PsiLowerSymbol;
import com.or.lang.core.psi.impl.PsiScopedExpr;
import org.jetbrains.annotations.NotNull;

public class OCamlInsightFilter {

    public static boolean shouldSkip(@NotNull PsiElement element) {
        // small optimisation: skip
        if (element instanceof PsiWhiteSpace) return true;
        // supported: literals and scope expr
        if (!(element instanceof PsiLiteralExpression) && !(element instanceof PsiScopedExpr)
                && !(element instanceof PsiLowerSymbol)){
            boolean ok = false;
            IElementType elementType = element.getNode().getElementType();
            if (elementType.equals(OCamlTypes.FLOAT_VALUE)) ok = true;
            if (!ok && elementType.equals(OCamlTypes.INT_VALUE)) ok = true;
            if (!ok && elementType.equals(OCamlTypes.BOOL_VALUE)) ok = true;
            return !ok;
        }
        return false;
    }

}
