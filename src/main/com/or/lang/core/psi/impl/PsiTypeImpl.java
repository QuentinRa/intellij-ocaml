package com.or.lang.core.psi.impl;

import com.intellij.lang.ASTNode;
import com.intellij.navigation.ItemPresentation;
import com.intellij.psi.PsiElement;
import com.intellij.psi.stubs.IStubElementType;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.util.IncorrectOperationException;
import com.ocaml.icons.OCamlIcons;
import com.or.lang.OCamlTypes;
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
        String name;
        PsiTypeStub stub = getGreenStub();
        if (stub != null) {
            name = stub.getName();
            if (name != null && !name.isEmpty())
                return name;
        }
        PsiElement constrName = getNameIdentifier();
        name = constrName == null ? "" : constrName.getText();
        // this code is not pretty at all
        if (name == null || name.isEmpty()) {
            @NotNull String declaration = getText();
            String EQ = OCamlTypes.EQ.getSymbol();
            String TYPE = OCamlTypes.TYPE.getSymbol();
            int eq = declaration.indexOf(EQ);
            int type = declaration.indexOf(TYPE);
            // if we got "type something = ..."
            if (eq != -1 && type != -1) {
                // then we extract "something"
                name = declaration.substring(type + TYPE.length() + 1, eq - EQ.length())
                        .replace("\n", "")
                        .trim();
                // remove every variant
                int space = name.indexOf(" ");
                while (space != -1) {
                    name = name.substring(space + 1).trim();
                    space = name.indexOf(" ");
                }
            }
        }
        if (name == null || name.isEmpty()) {
            name = "<none>";
        }
        return name;
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
