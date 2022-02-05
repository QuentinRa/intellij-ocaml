package com.or.lang.core.psi.impl;

import com.intellij.psi.tree.IElementType;
import com.or.lang.core.CompositeTypePsiElement;
import com.or.lang.core.ORUtil;
import com.or.lang.core.psi.PsiSignature;
import com.or.lang.core.psi.PsiSignatureItem;
import com.or.lang.utils.ORLanguageProperties;
import com.or.utils.Joiner;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.stream.Collectors;

public class PsiSignatureImpl extends CompositeTypePsiElement implements PsiSignature {
    PsiSignatureImpl(@NotNull IElementType elementType) {
        super(elementType);
    }

    @Override
    public boolean isFunction() {
        return getItems().size() > 1;
    }

    @Override
    public @NotNull String asText(@Nullable ORLanguageProperties toLang) {
        List<PsiSignatureItem> items = getItems();
        if (items.isEmpty()) {
            return "";
        }

        boolean isFunction = 1 < items.size();

        String signatureText;
        if (toLang == null || toLang.equals(getLanguage())) {
            signatureText = getText();
        } else {
            StringBuilder sb = new StringBuilder();

            List<String> conversions = items.stream().map(item -> item.asText(toLang)).collect(Collectors.toList());
            String result = conversions.remove(items.size() - 1);

            if (isFunction) {
                sb.append(Joiner.join(toLang.getParameterSeparator(), conversions));
                sb.append(toLang.getFunctionSeparator());
            }
            sb.append(result);
            signatureText = sb.toString();
        }

        String text = signatureText.replaceAll("\\s+", " ");

        return text
                .replaceAll("\\( ", "(")
                .replaceAll(", \\)", ")");
    }

    @NotNull private List<PsiSignatureItem> getItems() {
        return ORUtil.findImmediateChildrenOfClass(this, PsiSignatureItem.class);
    }

    @Override
    public @NotNull String toString() {
        return "PsiSignature";
    }
}
