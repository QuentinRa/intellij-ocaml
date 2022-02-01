package com.or.ide.search;

import com.intellij.lang.HelpID;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiNamedElement;
import com.intellij.psi.PsiQualifiedNamedElement;
import com.or.lang.core.psi.PsiModule;
import com.or.lang.core.psi.impl.PsiLowerIdentifier;
import com.or.lang.core.psi.impl.PsiUpperIdentifier;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public abstract class ORFindUsagesProvider implements com.intellij.lang.findUsages.FindUsagesProvider {
    @Override
    public boolean canFindUsagesFor(@NotNull PsiElement element) {
        return element instanceof PsiUpperIdentifier || element instanceof PsiLowerIdentifier;
    }

    @Override
    public @Nullable String getHelpId(@NotNull PsiElement psiElement) {
        return HelpID.FIND_OTHER_USAGES;
    }

    @Override
    public @NotNull String getType(@NotNull PsiElement element) {
        String type = PsiTypeElementProvider.getType(element);
        return type == null ? "unknown type" : type;
    }

    @Override
    public @NotNull String getDescriptiveName(@NotNull PsiElement element) {
        if (element instanceof PsiModule) {
            return "Module " + ((PsiModule) element).getName();
        } else if (element instanceof PsiNamedElement) {
            String name = ((PsiNamedElement) element).getName();
            return name == null ? "" : name;
        }

        return "";
    }

    @Override
    public @NotNull String getNodeText(@NotNull PsiElement element, boolean useFullName) {
        if (element instanceof PsiQualifiedNamedElement) {
            String qName = ((PsiQualifiedNamedElement) element).getQualifiedName();
            return qName == null ? "" : qName;
        }
        if (element instanceof PsiNamedElement) {
            String name = ((PsiNamedElement) element).getName();
            if (name != null) {
                return name;
            }
        }

        return element.getText();
    }
}
