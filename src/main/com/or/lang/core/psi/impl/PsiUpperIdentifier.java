package com.or.lang.core.psi.impl;

import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiNameIdentifierOwner;
import com.intellij.psi.tree.IElementType;
import com.intellij.util.IncorrectOperationException;
import com.or.lang.core.CompositeTypePsiElement;
import com.or.lang.core.ORCodeFactory;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class PsiUpperIdentifier extends CompositeTypePsiElement
        implements PsiNameIdentifierOwner {

    // region Constructors
    protected PsiUpperIdentifier(@NotNull IElementType elementType) {
        super(elementType);
    }
    // endregion

    // region NamedElement
    @Override
    public String getName() {
        return getText();
    }

    @Nullable
    @Override
    public PsiElement getNameIdentifier() {
        return this;
    }

    @NotNull
    @Override
    public PsiElement setName(@NotNull String newName) throws IncorrectOperationException {
        PsiUpperIdentifier newNameIdentifier = ORCodeFactory.createModuleName(getProject(), newName);

        ASTNode newNameNode =
                newNameIdentifier == null ? null : newNameIdentifier.getFirstChild().getNode();
        if (newNameNode != null) {
            PsiElement nameIdentifier = getFirstChild();
            if (nameIdentifier == null) {
                getNode().addChild(newNameNode);
            } else {
                ASTNode oldNameNode = nameIdentifier.getNode();
                getNode().replaceChild(oldNameNode, newNameNode);
            }
        }

        return this;
    }
    // endregion

    @Nullable
    @Override
    public String toString() {
        return "UID";
    }
}
