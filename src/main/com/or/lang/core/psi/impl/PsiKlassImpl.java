package com.or.lang.core.psi.impl;

import com.intellij.lang.ASTNode;
import com.intellij.navigation.ItemPresentation;
import com.intellij.psi.PsiElement;
import com.intellij.psi.stubs.IStubElementType;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.util.IncorrectOperationException;
import com.ocaml.icons.OCamlIcons;
import com.or.lang.core.ORUtil;
import com.or.lang.core.psi.PsiKlass;
import com.or.lang.core.stub.PsiKlassStub;
import com.or.utils.Joiner;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

public class PsiKlassImpl extends PsiTokenStub<PsiKlass, PsiKlassStub> implements PsiKlass {
    // region Constructors
    public PsiKlassImpl(@NotNull ASTNode node) {
        super(node);
    }

    public PsiKlassImpl(@NotNull PsiKlassStub stub, @NotNull IStubElementType nodeType) {
        super(stub, nodeType);
    }
    // endregion

    // region PsiNamedElement
    public @Nullable PsiElement getNameIdentifier() {
        return findChildByClass(PsiLowerIdentifier.class);
    }

    @Override
    public @Nullable String getName() {
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
    public @Nullable String[] getPath() { // zzz stub
        return ORUtil.getQualifiedPath(this);
    }

    @Override
    public @NotNull String getQualifiedName() {
        String[] path = getPath();
        String name = getName();
        return name == null ? "" : Joiner.join(".", path) + "." + name;
    }
    //endregion

    @Override
    public @Nullable PsiElement getClassBody() {
        return PsiTreeUtil.findChildOfType(this, PsiObject.class);
    }

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
                return OCamlIcons.Nodes.CLASS;
            }
        };
    }

    @Override
    public @Nullable String toString() {
        return "Class " + getQualifiedName();
    }
}
