package com.dune.utils.psi;

import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.TokenType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class DunePsiTreeUtil {

    public static <T extends PsiElement> @Nullable T findImmediateFirstChildOfClass(@Nullable PsiElement element, @NotNull Class<T> clazz) {
        PsiElement child = element == null ? null : element.getFirstChild();

        while (child != null) {
            if (clazz.isInstance(child)) {
                return clazz.cast(child);
            }
            child = child.getNextSibling();
        }

        return null;
    }

    public static @NotNull ASTNode nextSiblingNode(@NotNull ASTNode node) {
        ASTNode nextSibling = node.getTreeNext();
        while (nextSibling.getElementType() == TokenType.WHITE_SPACE) {
            nextSibling = nextSibling.getTreeNext();
        }
        return nextSibling;
    }

}
