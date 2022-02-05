package com.or.lang.core.psi.impl;

import com.intellij.lang.Language;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiWhiteSpace;
import com.intellij.psi.tree.IElementType;
import com.ocaml.OCamlLanguage;
import com.or.lang.OCamlTypes;
import com.or.lang.core.CompositeTypePsiElement;
import com.or.lang.core.ORUtil;
import com.or.lang.core.psi.PsiLanguageConverter;
import com.or.lang.utils.ORLanguageProperties;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class PsiOption extends CompositeTypePsiElement implements PsiLanguageConverter {
    protected PsiOption(@NotNull IElementType elementType) {
        super(elementType);
    }

    @Override
    public @NotNull String asText(@Nullable ORLanguageProperties toLang) {
        StringBuilder convertedText = null;
        Language fromLang = getLanguage();

        if (fromLang != toLang && toLang != null) {
            convertedText = new StringBuilder();

            if (toLang == OCamlLanguage.INSTANCE) {
                // Convert from Reason/Rescript to OCaml
                PsiScopedExpr scope = ORUtil.findImmediateFirstChildOfClass(this, PsiScopedExpr.class);
                if (scope != null) {
                    String scopeText = scope.getText();
                    convertedText.append(scopeText, 1, scopeText.length() - 1).append(" option");
                }
            } else if (fromLang == OCamlLanguage.INSTANCE) {
                // Convert from OCaml
                PsiElement[] children = getChildren();
                int lastChild = children[children.length - 2] instanceof PsiWhiteSpace ? children.length - 3 : children.length - 2;

                convertedText.append("option");
                convertedText.append(toLang.getTemplateStart());
                for (int i = 0; i <= lastChild; i++) {
                    PsiElement child = children[i];
                    IElementType childElementType = child.getNode().getElementType();
                    if (childElementType != OCamlTypes.OPTION) {
                        convertedText.append(child.getText());
                    }
                }
                convertedText.append(toLang.getTemplateEnd());
            } else {
                convertedText.append(getText());
            }
        }

        return convertedText == null ? getText() : convertedText.toString();
    }
}
