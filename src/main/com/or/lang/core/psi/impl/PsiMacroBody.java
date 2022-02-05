package com.or.lang.core.psi.impl;

import com.intellij.json.psi.impl.JSStringLiteralEscaper;
import com.intellij.lang.ASTNode;
import com.intellij.psi.LiteralTextEscaper;
import com.intellij.psi.PsiLanguageInjectionHost;
import com.intellij.psi.impl.source.tree.LeafElement;
import com.intellij.psi.tree.IElementType;
import com.or.lang.core.CompositeTypePsiElement;
import org.jetbrains.annotations.NotNull;

public class PsiMacroBody extends CompositeTypePsiElement implements PsiLanguageInjectionHost {
    protected PsiMacroBody(@NotNull IElementType elementType) {
        super(elementType);
    }

    @Override
    public boolean isValidHost() {
        return true;
    }

    @Override
    public @NotNull PsiLanguageInjectionHost updateText(@NotNull String text) {
        ASTNode valueNode = getNode().getFirstChildNode();
        if (valueNode instanceof LeafElement) {
            ((LeafElement) valueNode).replaceWithText(text);
        }
        return this;
    }

    @Override
    public @NotNull LiteralTextEscaper<? extends PsiLanguageInjectionHost> createLiteralTextEscaper() {
        return new JSStringLiteralEscaper<PsiLanguageInjectionHost>(this) {
            @Override
            protected boolean isRegExpLiteral() {
                return false;
            }
        };
    }

    @Override
    public @NotNull String toString() {
        return "PsiMacroBody";
    }
}
