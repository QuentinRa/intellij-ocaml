package com.dune.lang.core.psi;

import com.dune.icons.DuneIcons;
import com.dune.lang.core.psi.impl.DuneTypes;
import com.intellij.extapi.psi.ASTWrapperPsiElement;
import com.intellij.lang.ASTNode;
import com.intellij.navigation.ItemPresentation;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiNameIdentifierOwner;
import com.intellij.util.IncorrectOperationException;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

public class PsiStanza extends ASTWrapperPsiElement implements PsiNameIdentifierOwner, PsiStructuredElement {
    public PsiStanza(@NotNull ASTNode node) {
        super(node);
    }

    @Nullable @Override public PsiElement getNameIdentifier() {
        PsiElement firstChild = getFirstChild();
        PsiElement nextSibling = firstChild.getNextSibling();
        return nextSibling != null && nextSibling.getNode().getElementType() == DuneTypes.ATOM
                ? nextSibling
                : null;
    }

    @Nullable @Override public String getName() {
        PsiElement identifier = getNameIdentifier();
        return identifier == null ? null : identifier.getText();
    }

    @Override
    public @Nullable PsiElement setName(@NotNull String name) throws IncorrectOperationException {
        return null;
    }

    @Nullable @Override public ItemPresentation getPresentation() {
        return new ItemPresentation() {
            @NotNull @Override public String getPresentableText() {
                String name = getName();
                return name == null ? "unknown" : name;
            }

            @Nullable @Override public String getLocationString() {
                return null;
            }

            @NotNull @Override public Icon getIcon(boolean unused) {
                return DuneIcons.Nodes.OBJECT;
            }
        };
    }

    @Override public @Nullable String toString() {
        return "Stanza " + getName();
    }
}
