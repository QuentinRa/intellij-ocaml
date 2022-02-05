package com.or.ide.go;

import com.intellij.navigation.ItemPresentation;
import com.intellij.navigation.NavigationItem;
import com.intellij.psi.PsiElement;
import com.intellij.util.IncorrectOperationException;
import com.or.lang.core.psi.PsiQualifiedPathElement;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

class ModuleDelegatePresentation extends ORDelegatePsiElement implements NavigationItem, PsiQualifiedPathElement {
    private final @NotNull ItemPresentation m_presentation;

    public ModuleDelegatePresentation(@NotNull PsiQualifiedPathElement source, @NotNull ItemPresentation presentation) {
        super(source);
        m_presentation = presentation;
    }

    //region PsiQualifiedPathElement
    @Override
    public @Nullable String[] getPath() {
        return m_source.getPath();
    }

    @Override
    public @Nullable String getQualifiedName() {
        return m_source.getQualifiedName();
    }
    //endregion

    @Override
    public @Nullable PsiElement setName(@NotNull String name) throws IncorrectOperationException {
        return null;
    }

    @Override
    public Icon getIcon(final int flags) {
        return m_presentation.getIcon(false);
    }

    @Override
    protected Icon getElementIcon(final int flags) {
        return m_presentation.getIcon(false);
    }

    @Override
    public @NotNull ItemPresentation getPresentation() {
        return m_presentation;
    }
}
