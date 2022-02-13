package com.ocaml.lang.utils;

import com.intellij.psi.PsiComment;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiWhiteSpace;
import com.or.lang.OCamlTypes;
import com.or.lang.core.psi.PsiInclude;
import com.or.lang.core.psi.PsiOpen;
import com.or.lang.core.psi.PsiStructuredElement;
import com.or.lang.core.psi.PsiUpperSymbol;
import com.or.lang.core.type.ORTokenElementType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashSet;
import java.util.Set;

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
            // and comments (fix, added)
            if (prevSibling instanceof PsiWhiteSpace || prevSibling instanceof PsiComment) {
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

    public static @NotNull Set<String> findDependencies(@NotNull PsiElement element) {
        Set<String> res = new HashSet<>();
        for (PsiElement child : element.getChildren()) {
            if (child instanceof PsiWhiteSpace) continue;

            // open ModuleName
            if (child instanceof PsiOpen) {
                res.add(((PsiOpen) child).getPath().toLowerCase());
            }

            if (child instanceof PsiInclude) {
                res.add(((PsiInclude) child).getIncludePath().toLowerCase());
            }

            // child instanceof PsiLocalOpen?

            // ModuleName.function
            if (child instanceof PsiUpperSymbol && OCamlPsiUtils.isNextMeaningfulNextSibling(child, OCamlTypes.DOT)) {
                res.add(child.getText().toLowerCase());
            }
            // next
            res.addAll(findDependencies(child));
        }
        return res;
    }

    public static boolean isNextMeaningfulNextSibling(@NotNull PsiElement element, ORTokenElementType type) {
        PsiElement nextSibling = element.getNextSibling();
        while (nextSibling != null) {
            // skip white spaces and comments
            if (nextSibling instanceof PsiWhiteSpace || nextSibling instanceof PsiComment) {
                nextSibling = nextSibling.getNextSibling();
                continue;
            }
            return nextSibling.getNode().getElementType().equals(type);
        }
        return false;
    }

    public static boolean isNextMeaningfulNextSibling(@NotNull PsiElement element, Class<? extends PsiElement> aClass) {
        PsiElement nextSibling = element.getNextSibling();
        while (nextSibling != null) {
            // skip white spaces and comments
            if (nextSibling instanceof PsiWhiteSpace || nextSibling instanceof PsiComment) {
                nextSibling = nextSibling.getNextSibling();
                continue;
            }
            return aClass.isInstance(nextSibling);
        }
        return false;
    }

    public static @Nullable PsiElement getNextMeaningfulNextSibling(@NotNull PsiElement element, ORTokenElementType type) {
        PsiElement nextSibling = element.getNextSibling();
        while (nextSibling != null) {
            // skip white spaces and comments
            if (nextSibling instanceof PsiWhiteSpace || nextSibling instanceof PsiComment) {
                nextSibling = nextSibling.getNextSibling();
                continue;
            }
            if (nextSibling.getNode().getElementType().equals(type)) return nextSibling;
            break;
        }
        return null;
    }

    public static @Nullable PsiElement getPreviousMeaningfulNextSibling(@NotNull PsiElement element, ORTokenElementType type) {
        PsiElement prevSibling = element.getPrevSibling();
        while (prevSibling != null) {
            // skip white spaces and comments
            if (prevSibling instanceof PsiWhiteSpace || prevSibling instanceof PsiComment) {
                prevSibling = prevSibling.getPrevSibling();
                continue;
            }
            if (prevSibling.getNode().getElementType().equals(type)) return prevSibling;
            break;
        }
        return null;
    }
}
