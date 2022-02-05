package com.or.lang.core.psi.impl;

import com.intellij.lang.ASTNode;
import com.intellij.navigation.ItemPresentation;
import com.intellij.psi.PsiElement;
import com.intellij.psi.stubs.IStubElementType;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.util.IncorrectOperationException;
import com.ocaml.icons.OCamlIcons;
import com.or.lang.core.ORUtil;
import com.or.lang.core.psi.PsiType;
import com.or.lang.core.psi.PsiVariantDeclaration;
import com.or.lang.core.stub.PsiTypeStub;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.util.Collection;

import static java.util.Collections.emptyList;

public class PsiTypeImpl extends PsiTokenStub<PsiType, PsiTypeStub> implements PsiType {
    // region Constructors
    public PsiTypeImpl(@NotNull ASTNode node) {
        super(node);
    }

    public PsiTypeImpl(@NotNull PsiTypeStub stub, @NotNull IStubElementType nodeType) {
        super(stub, nodeType);
    }
    // endregion

    // region PsiNamedElement
    @Override
    public @Nullable PsiElement getNameIdentifier() {
        return findChildByClass(PsiLowerIdentifier.class);
    }

    @Override
    public @Nullable String getName() {
        PsiTypeStub stub = getGreenStub();
        if (stub != null) {
            return stub.getName();
        }

        PsiElement constrName = getNameIdentifier();
        return constrName == null ? "" : constrName.getText();

    }

    @Override
    public @NotNull PsiElement setName(@NotNull String name) throws IncorrectOperationException {
        return this;
    }
    // endregion

    //region PsiQualifiedPathName
    @Override
    public @Nullable String[] getPath() {
        PsiTypeStub stub = getGreenStub();
        if (stub != null) {
            return stub.getPath();
        }

        return ORUtil.getQualifiedPath(this);
    }

    @Override
    public @NotNull String getQualifiedName() {
        PsiTypeStub stub = getGreenStub();
        if (stub != null) {
            return stub.getQualifiedName();
        }

        return ORUtil.getQualifiedName(this);
    }
    //endregion

    @Override
    public boolean isAbstract() {
        PsiTypeStub stub = getGreenStub();
        if (stub != null) {
            return stub.isAbstract();
        }

        return getBinding() == null;
    }

    @Override
    public boolean isJsObject() {
        PsiTypeStub stub = getGreenStub();
        if (stub != null) {
            return stub.isJsObject();
        }

        PsiTypeBinding binding = getBinding();
        return binding != null && binding.getFirstChild() instanceof PsiJsObject;
    }

    @Override
    public boolean isRecord() {
        PsiTypeStub stub = getGreenStub();
        if (stub != null) {
            return stub.isRecord();
        }

        PsiTypeBinding binding = getBinding();
        return binding != null && binding.getFirstChild() instanceof PsiRecord;
    }

    @Override
    public @Nullable PsiTypeBinding getBinding() {
        return findChildByClass(PsiTypeBinding.class);
    }

    @Override
    public @NotNull Collection<PsiVariantDeclaration> getVariants() {
        PsiTypeBinding binding = getBinding();
        if (binding != null) {
            return PsiTreeUtil.findChildrenOfType(binding, PsiVariantDeclaration.class);
        }
        return emptyList();
    }

    @Override
    public ItemPresentation getPresentation() {
        return new ItemPresentation() {
            @Override
            public String getPresentableText() {
                return getName();
            }

            @Override
            public @Nullable String getLocationString() {
                return null;
            }

            @Override
            public Icon getIcon(boolean unused) {
                return OCamlIcons.Nodes.TYPE;
            }
        };
    }

    @Override
    public @NotNull String toString() {
        return "Type " + getQualifiedName();
    }
}
