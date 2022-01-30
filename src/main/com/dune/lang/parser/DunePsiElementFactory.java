package com.dune.lang.parser;

import com.dune.lang.core.psi.*;
import com.dune.lang.core.psi.DuneTypes;
import com.intellij.extapi.psi.ASTWrapperPsiElement;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.tree.IElementType;
import org.jetbrains.annotations.NotNull;

class DunePsiElementFactory {
    private DunePsiElementFactory() {
    }

    @NotNull
    static PsiElement createElement(@NotNull ASTNode node) {
        IElementType type = node.getElementType();

        if (type == DuneTypes.FIELD) {
            return new PsiDuneField(node);
        } else if (type == DuneTypes.FIELDS) {
            return new PsiDuneFields(node);
        } else if (type == DuneTypes.STANZA) {
            return new PsiStanza(node);
        } else if (type == DuneTypes.S_EXPR) {
            return new PsiSExpr(node);
        } else if (type == DuneTypes.VAR) {
            return new PsiDuneVar(node);
        }

        return new ASTWrapperPsiElement(node);
    }
}
