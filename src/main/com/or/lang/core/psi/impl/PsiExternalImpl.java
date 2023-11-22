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
import com.or.lang.core.psi.PsiExternal;
import com.or.lang.core.psi.PsiSignature;
import com.or.lang.core.stub.PsiExternalStub;
import com.or.lang.utils.ORLanguageProperties;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.util.Objects;

public class PsiExternalImpl extends PsiTokenStub<PsiExternal, PsiExternalStub> implements PsiExternal {
    // region Constructors
    public PsiExternalImpl(@NotNull ASTNode node) {
        super(node);
    }

    public PsiExternalImpl(@NotNull PsiExternalStub stub, @NotNull IStubElementType nodeType) {
        super(stub, nodeType);
    }
    // endregion

    // region PsiNamedElement
    public @Nullable PsiElement getNameIdentifier() {
        PsiScopedExpr operatorOverride = findChildByClass(PsiScopedExpr.class);
        if (operatorOverride != null) {
            return operatorOverride;
        }

        return findChildByClass(PsiLowerIdentifier.class);
    }

    @Override
    public String getName() {
        PsiExternalStub stub = getGreenStub();
        if (stub != null) {
            return stub.getName();
        }

        PsiElement nameIdentifier = getNameIdentifier();
        if (nameIdentifier == null) {
            return "unknown";
        }

        return nameIdentifier.getText();
    }

    @Override
    public @NotNull PsiElement setName(@NotNull String name) throws IncorrectOperationException {
        return this;
    }
    // endregion

    //region PsiQualifiedName
    @Override
    public String @NotNull [] getPath() {
        PsiExternalStub stub = getGreenStub();
        if (stub != null) {
            return stub.getPath();
        }

        return ORUtil.getQualifiedPath(this);
    }

    @Override
    public @NotNull String getQualifiedName() {
        PsiExternalStub stub = getGreenStub();
        if (stub != null) {
            return stub.getQualifiedName();
        }

        return ORUtil.getQualifiedName(this);
    }
    //endregion

    @Override
    public @Nullable PsiSignature getSignature() {
        return findChildByClass(PsiSignature.class);
    }

    private @NotNull String getRealName() {
        PsiElement name = findChildByType(OCamlTypes.STRING_VALUE);
        return name == null ? "" : name.getText();
    }

    @Override
    public boolean isFunction() {
        PsiExternalStub stub = getGreenStub();
        if (stub != null) {
            return stub.isFunction();
        }

        PsiSignature signature = PsiTreeUtil.findChildOfType(this, PsiSignature.class);
        return signature != null && signature.isFunction();
    }

    @Override
    public ItemPresentation getPresentation() {
        return new ItemPresentation() {
            @Override
            public @Nullable String getPresentableText() {
                String aliasName = getName();

                String realName = getRealName();
                if (!realName.isEmpty()) {
                    String realNameText = realName.substring(1, realName.length() - 1);
                    if (!Objects.equals(aliasName, realNameText)) {
                        aliasName += " (" + realNameText + ")";
                    }
                }

                return aliasName;
            }

            @Override
            public @Nullable String getLocationString() {
                PsiSignature signature = getSignature();
                return signature == null ? null : signature.asText(ORLanguageProperties.cast(getLanguage()));
            }

            @Override
            public Icon getIcon(boolean unused) {
                return OCamlIcons.Nodes.EXTERNAL;
            }
        };
    }

    @Override
    public @Nullable String toString() {
        return "external " + getQualifiedName();
    }
}
