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
import com.or.lang.core.stub.PsiRecordFieldStub;
import com.or.lang.utils.ORLanguageProperties;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

public class PsiRecordFieldImpl extends PsiTokenStub<PsiRecordField, PsiRecordFieldStub> implements PsiRecordField {
    // region Constructors
    public PsiRecordFieldImpl(@NotNull ASTNode node) {
        super(node);
    }

    public PsiRecordFieldImpl(@NotNull PsiRecordFieldStub stub, @NotNull IStubElementType nodeType) {
        super(stub, nodeType);
    }
    // endregion

    // region PsiNamedElement
    @Override
    public @Nullable PsiElement getNameIdentifier() {
        return ORUtil.findImmediateFirstChildOfClass(this, PsiLowerIdentifier.class);
    }

    @Override
    public @Nullable String getName() {
        PsiRecordFieldStub stub = getGreenStub();
        if (stub != null) {
            return stub.getName();
        }

        PsiElement nameElement = getNameIdentifier();
        return nameElement == null ? "" : nameElement.getText().replaceAll("\"", "");
    }

    @Override
    public @NotNull PsiElement setName(@NotNull String name) throws IncorrectOperationException {
        return this;
    }
    // endregion

    //region PsiQualifiedName
    @Override
    public String [] getPath() {
        PsiRecordFieldStub stub = getGreenStub();
        if (stub != null) {
            return stub.getPath();
        }

        return ORUtil.getQualifiedPath(this);
    }

    @Override
    public @NotNull String getQualifiedName() {
        PsiRecordFieldStub stub = getGreenStub();
        if (stub != null) {
            return stub.getQualifiedName();
        }

        return ORUtil.getQualifiedName(this);
    }
    //endregion

    public @Nullable PsiSignature getSignature() {
        return PsiTreeUtil.findChildOfType(this, PsiSignature.class);
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
    public @NotNull String toString() {
        return "Record field " + getQualifiedName();
    }
}
