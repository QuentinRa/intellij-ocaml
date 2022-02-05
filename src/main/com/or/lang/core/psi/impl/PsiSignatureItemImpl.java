package com.or.lang.core.psi.impl;

import com.intellij.psi.PsiElement;
import com.intellij.psi.tree.IElementType;
import com.or.lang.core.CompositeTypePsiElement;
import com.or.lang.core.ORUtil;
import com.or.lang.core.psi.PsiLanguageConverter;
import com.or.lang.core.psi.PsiNamedParam;
import com.or.lang.core.psi.PsiSignatureItem;
import com.or.lang.utils.ORLanguageProperties;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class PsiSignatureItemImpl extends CompositeTypePsiElement implements PsiSignatureItem {
    protected PsiSignatureItemImpl(@NotNull IElementType elementType) {
        super(elementType);
    }

    @Nullable private PsiNamedParam getNamedParam() {
        return ORUtil.findImmediateFirstChildOfClass(this, PsiNamedParam.class);
    }

    public @Nullable String getName() {
        PsiNamedParam param = getNamedParam();
        return param == null ? null : param.getName();
    }

    @Override
    public @NotNull String asText(@Nullable ORLanguageProperties toLang) {
        PsiElement firstChild = getFirstChild();
        if (firstChild instanceof PsiLanguageConverter) {
            return ((PsiLanguageConverter) firstChild).asText(toLang);
        }
        return getText();
    }

    @Override
    public @NotNull String toString() {
        return "Signature item";
    }
}
