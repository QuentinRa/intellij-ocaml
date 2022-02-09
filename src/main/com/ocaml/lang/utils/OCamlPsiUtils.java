package com.ocaml.lang.utils;

import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiWhiteSpace;
import com.or.lang.core.psi.PsiStructuredElement;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public final class OCamlPsiUtils {

    public static @Nullable PsiElement findStatementBefore(@Nullable PsiElement elementAt) {
        if (elementAt == null) return null;
        if (elementAt instanceof PsiStructuredElement) return elementAt;
        PsiElement prevSibling = elementAt.getPrevSibling();
        if (prevSibling == null) prevSibling = elementAt.getParent();
        return findStatementBefore(prevSibling);
    }

    public static @Nullable PsiElement findStatementAfter(@Nullable PsiElement elementAt) {
        if (elementAt == null) return null;
        if (elementAt instanceof PsiStructuredElement) return elementAt;
        PsiElement nextSibling = elementAt.getNextSibling();
        if (nextSibling == null) nextSibling = elementAt.getParent();
        return findStatementAfter(nextSibling);
    }

    public static int findIndexOfParameter(@NotNull PsiElement element, String functionName) {
        PsiElement prevSibling = element.getPrevSibling();
        int count = 0;
        while (prevSibling != null) {
            // skip white spaces
            if (prevSibling instanceof PsiWhiteSpace) {
                prevSibling = prevSibling.getPrevSibling();
                continue;
            }
            // done, we found the name of the function
            if (prevSibling.getText().equals(functionName)) return count;
            // pass, this is an argument (I hope so ><)
            count++;
            prevSibling = prevSibling.getPrevSibling();
        }

        return -1; // not found
    }
}
