package com.or.lang.core.psi.impl;

import com.intellij.lang.Language;
import com.intellij.psi.impl.source.tree.CompositePsiElement;
import com.intellij.psi.tree.IElementType;
import com.ocaml.OCamlLanguage;
import com.or.lang.core.psi.PsiLanguageConverter;
import com.or.lang.utils.ORLanguageProperties;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class PsiObject extends CompositePsiElement implements PsiLanguageConverter {
    protected PsiObject(@NotNull IElementType type) {
        super(type);
    }

    @Override
    public @NotNull String asText(@Nullable ORLanguageProperties toLang) {
        StringBuilder convertedText = null;
        Language fromLang = getLanguage();

        if (fromLang != toLang) {
            if (toLang != OCamlLanguage.INSTANCE) {
                convertedText = new StringBuilder();
                convertedText.append("{. ").append(getText(), 1, getTextLength() - 1).append(" }");
            }
        }

        return convertedText == null ? getText() : convertedText.toString();
    }

    @Override
    public @NotNull String toString() {
        return "Object";
    }
}
