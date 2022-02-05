package com.or.lang.core.psi.impl;

import com.intellij.lang.Language;
import com.intellij.psi.PsiElement;
import com.intellij.psi.impl.source.tree.CompositePsiElement;
import com.intellij.psi.tree.IElementType;
import com.ocaml.OCamlLanguage;
import com.or.lang.core.ORUtil;
import com.or.lang.core.psi.PsiLanguageConverter;
import com.or.lang.utils.ORLanguageProperties;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;

public class PsiJsObject extends CompositePsiElement implements PsiLanguageConverter {
    protected PsiJsObject(@NotNull IElementType type) {
        super(type);
    }

    public @NotNull Collection<PsiObjectField> getFields() {
        return ORUtil.findImmediateChildrenOfClass(this, PsiObjectField.class);
    }

    @Override
    public @NotNull String asText(@Nullable ORLanguageProperties toLang) {
        StringBuilder convertedText = null;
        Language fromLang = getLanguage();

        if (fromLang != toLang) {
            convertedText = new StringBuilder();
            boolean firstField = true;
            if (toLang == OCamlLanguage.INSTANCE) {
                // Convert to OCaml
                convertedText.append("<");
                for (PsiElement element : getChildren()) {
                    if (element instanceof PsiObjectField) {
                        if (firstField) {
                            firstField = false;
                        } else {
                            convertedText.append("; ");
                        }
                        convertedText.append(((PsiObjectField) element).asText(toLang));
                    }
                }
                convertedText.append("> Js.t");
            } else {
                // Convert from OCaml
                convertedText.append("{. ");
                for (PsiElement element : getChildren()) {
                    if (element instanceof PsiObjectField) {
                        if (firstField) {
                            firstField = false;
                        } else {
                            convertedText.append(", ");
                        }
                        convertedText.append(((PsiObjectField) element).asText(toLang));
                    }
                }
                convertedText.append(" }");
            }
        }

        return convertedText == null ? getText() : convertedText.toString();
    }

    @Override
    public @NotNull String toString() {
        return "JsObject";
    }
}
