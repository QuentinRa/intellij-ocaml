package com.or.lang.core.psi.impl;

import com.intellij.lang.ASTNode;
import com.intellij.navigation.ItemPresentation;
import com.intellij.psi.PsiElement;
import com.intellij.psi.stubs.IStubElementType;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.util.IncorrectOperationException;
import com.ocaml.icons.OCamlIcons;
import com.or.lang.core.ORUtil;
import com.or.lang.core.psi.PsiRecordField;
import com.or.lang.core.psi.PsiSignature;
import com.or.lang.core.psi.PsiVal;
import com.or.lang.core.stub.PsiValStub;
import com.or.lang.utils.ORLanguageProperties;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.util.Collection;

public class PsiValImpl extends PsiTokenStub<PsiVal, PsiValStub> implements PsiVal {
    // region Constructors
    public PsiValImpl(@NotNull ASTNode node) {
        super(node);
    }

    public PsiValImpl(@NotNull PsiValStub stub, @NotNull IStubElementType nodeType) {
        super(stub, nodeType);
    }
    // endregion

    // region PsiNamedElement
    public @Nullable PsiElement getNameIdentifier() {
        return ORUtil.findImmediateFirstChildOfAnyClass(this, PsiLowerIdentifier.class, PsiScopedExpr.class);
    }

    @Override
    public @Nullable String getName() {
        PsiValStub stub = getGreenStub();
        if (stub != null) {
            return stub.getName();
        }

        PsiElement nameIdentifier = getNameIdentifier();
        return nameIdentifier == null ? "" : nameIdentifier.getText();
    }

    @Override
    public @NotNull PsiElement setName(@NotNull String name) throws IncorrectOperationException {
        return this;
    }
    // endregion

    //region PsiQualifiedName
    @Override
    public String [] getPath() {
        PsiValStub stub = getGreenStub();
        if (stub != null) {
            return stub.getPath();
        }

        return ORUtil.getQualifiedPath(this);
    }

    @Override
    public @NotNull String getQualifiedName() {
        PsiValStub stub = getGreenStub();
        if (stub != null) {
            return stub.getQualifiedName();
        }

        return ORUtil.getQualifiedName(this);
    }
    //endregion

    @Override
    public boolean isFunction() {
        PsiValStub stub = getGreenStub();
        if (stub != null) {
            return stub.isFunction();
        }

        PsiSignature signature = getSignature();
        return signature != null && signature.isFunction();
    }

    @Override
    public @NotNull Collection<PsiRecordField> getRecordFields() {
        return PsiTreeUtil.findChildrenOfType(this, PsiRecordField.class);
    }

    @Override
    public @Nullable PsiSignature getSignature() {
        return findChildByClass(PsiSignature.class);
    }

    @Override
    public ItemPresentation getPresentation() {
        return new ItemPresentation() {
            @Override
            public @NotNull String getPresentableText() {
                String name = getName();
                return name == null ? "" : name;
            }

            @Override
            public @Nullable String getLocationString() {
                PsiSignature signature = getSignature();
                return signature == null ? null : signature.asText(ORLanguageProperties.cast(getLanguage()));
            }

            @Override
            public @NotNull Icon getIcon(boolean unused) {
                return OCamlIcons.Nodes.VAL;
            }
        };
    }

    @Override
    public @Nullable String toString() {
        return "Val " + getQualifiedName();
    }
}
