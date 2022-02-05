package com.or.lang.core.psi.impl;

import com.intellij.lang.ASTNode;
import com.intellij.navigation.ItemPresentation;
import com.intellij.psi.PsiElement;
import com.intellij.psi.stubs.IStubElementType;
import com.intellij.util.IncorrectOperationException;
import com.ocaml.icons.OCamlIcons;
import com.or.lang.OCamlTypes;
import com.or.lang.core.ORUtil;
import com.or.lang.core.psi.PsiException;
import com.or.lang.core.stub.PsiExceptionStub;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

public class PsiExceptionImpl extends PsiTokenStub<PsiException, PsiExceptionStub> implements PsiException {
    // region Constructors
    public PsiExceptionImpl(@NotNull ASTNode node) {
        super(node);
    }

    public PsiExceptionImpl(@NotNull PsiExceptionStub stub, @NotNull IStubElementType nodeType) {
        super(stub, nodeType);
    }
    // endregion

    //region PsiNamedElement
    @Override public @Nullable PsiElement getNameIdentifier() {
        return ORUtil.findImmediateFirstChildOfClass(this, PsiUpperIdentifier.class);
    }

    @Override
    public @Nullable String getName() {
        PsiExceptionStub stub = getGreenStub();
        if (stub != null) {
            return stub.getName();
        }

        PsiElement nameIdentifier = getNameIdentifier();
        return nameIdentifier == null ? null : nameIdentifier.getText();
    }

    @Override
    public @NotNull PsiElement setName(@NotNull String name) throws IncorrectOperationException {
        return this;
    }
    //endregion

    //region PsiQualifiedPathName
    @Override
    public @Nullable String[] getPath() {
        PsiExceptionStub stub = getGreenStub();
        if (stub != null) {
            return stub.getPath();
        }

        return ORUtil.getQualifiedPath(this);
    }

    @Override
    public @NotNull String getQualifiedName() {
        PsiExceptionStub stub = getGreenStub();
        if (stub != null) {
            return stub.getQualifiedName();
        }

        return ORUtil.getQualifiedName(this);
    }
    //endregion

    @Override
    public @Nullable String getAlias() {
        PsiExceptionStub stub = getGreenStub();
        if (stub != null) {
            return stub.getQualifiedName();
        }

        PsiElement eq = findChildByType(OCamlTypes.EQ);
        return eq == null ? null : ORUtil.computeAlias(eq.getNextSibling(), false);
    }

    @Override
    public ItemPresentation getPresentation() {
        return new ItemPresentation() {
            @Override
            public @Nullable String getPresentableText() {
                return getName();
            }

            @Override
            public @Nullable String getLocationString() {
                return null;
            }

            @Override
            public @NotNull Icon getIcon(boolean unused) {
                return OCamlIcons.Nodes.EXCEPTION;
            }
        };
    }

    @Override
    public @NotNull String toString() {
        return "Exception " + getName();
    }
}
