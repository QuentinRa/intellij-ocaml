package com.or.ide.search;

import com.intellij.psi.PsiElement;
import com.or.lang.core.psi.*;
import com.or.lang.core.psi.impl.PsiLowerIdentifier;
import com.or.lang.core.psi.impl.PsiUpperIdentifier;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class PsiTypeElementProvider {

    @Nullable
    public static String getType(@NotNull PsiElement element) {
        PsiElement parent = element.getParent();
        if (element instanceof PsiUpperIdentifier) {
            if (parent instanceof PsiModule) {
                return "module";
            }
            if (parent instanceof PsiVariantDeclaration) {
                return "variant";
            }
        } else if (element instanceof PsiLowerIdentifier) {
            if (parent instanceof PsiLet) {
                return "let";
            }
            if (parent instanceof PsiVal) {
                return "val";
            }
            if (parent instanceof PsiExternal) {
                return "external";
            }
            if (parent instanceof PsiType) {
                return "type";
            }
            if (parent instanceof PsiParameter) {
                return "parameter";
            }
        }

        return null;
    }
}
