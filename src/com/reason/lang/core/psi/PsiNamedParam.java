package com.reason.lang.core.psi;

import com.intellij.lang.*;
import com.intellij.psi.*;
import com.intellij.psi.tree.*;
import com.reason.lang.*;
import com.reason.lang.core.*;
import com.reason.lang.core.type.*;
import com.reason.lang.ocaml.*;
import org.jetbrains.annotations.*;

public class PsiNamedParam extends CompositeTypePsiElement<ORTypes> implements PsiLanguageConverter {
    public PsiNamedParam(@NotNull ORTypes types, @NotNull IElementType elementType) {
        super(types, elementType);
    }

    @Override
    public @Nullable String getName() {
        PsiElement firstChild = getFirstChild();
        PsiElement name = firstChild == null ? null : firstChild.getNode().getElementType() == m_types.TILDE ? ORUtil.nextSibling(firstChild) : firstChild;
        return name == null ? null : name.getText();
    }

    public boolean isOptional() {
        return ORUtil.findImmediateFirstChildOfType(this, m_types.EQ) != null;
    }

    public @Nullable String getDefaultValue() {
        PsiDefaultValue defaultValue = ORUtil.findImmediateFirstChildOfClass(this, PsiDefaultValue.class);
        return defaultValue == null ? null : defaultValue.getText();
    }

    public @Nullable PsiSignature getSignature() {
        return ORUtil.findImmediateFirstChildOfClass(this, PsiSignature.class);
    }

    @Override
    public @NotNull String asText(@Nullable ORLanguageProperties toLang) {
        StringBuilder convertedText = null;
        Language fromLang = getLanguage();

        if (fromLang != toLang) {
            if (fromLang == OclLanguage.INSTANCE) {
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
