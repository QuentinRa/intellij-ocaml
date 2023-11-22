package com.or.lang.core.psi;

import com.intellij.lang.Language;
import com.intellij.psi.PsiElement;
import com.intellij.psi.tree.IElementType;
import com.ocaml.OCamlLanguage;
import com.or.lang.OCamlTypes;
import com.or.lang.core.CompositeTypePsiElement;
import com.or.lang.core.ORUtil;
import com.or.lang.utils.ORLanguageProperties;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class PsiNamedParam extends CompositeTypePsiElement implements PsiLanguageConverter {
    public PsiNamedParam(@NotNull IElementType elementType) {
        super(elementType);
    }

    @Override
    public @Nullable String getName() {
        PsiElement firstChild = getFirstChild();
        PsiElement name = firstChild == null ? null : firstChild.getNode().getElementType() == OCamlTypes.TILDE ? ORUtil.nextSibling(firstChild) : firstChild;
        return name == null ? null : name.getText();
    }

    public @Nullable PsiSignature getSignature() {
        return ORUtil.findImmediateFirstChildOfClass(this, PsiSignature.class);
    }

    @Override
    public @NotNull String asText(@Nullable ORLanguageProperties toLang) {
        StringBuilder convertedText = null;
        Language fromLang = getLanguage();

        if (fromLang != toLang) {
            if (fromLang == OCamlLanguage.INSTANCE) {
                convertedText = new StringBuilder();
                convertedText.append("~").append(getName());
                PsiSignature signature = getSignature();
                if (signature != null) {
                    convertedText.append(":").append(signature.asText(toLang));
                }
            }
        }

        return convertedText == null ? getText() : convertedText.toString();
    }

    @Override
    public @NotNull String toString() {
        return "Named param";
    }
}
