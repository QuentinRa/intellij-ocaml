package com.or.lang.core.psi.impl;

import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiQualifiedNamedElement;
import com.intellij.psi.stubs.IStubElementType;
import com.intellij.psi.tree.IElementType;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.util.IncorrectOperationException;
import com.or.lang.OCamlTypes;
import com.or.lang.core.ORUtil;
import com.or.lang.core.psi.*;
import com.or.lang.core.stub.PsiParameterStub;
import com.or.utils.Joiner;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class PsiParameterImpl extends PsiTokenStub<PsiParameter, PsiParameterStub> implements PsiParameter {
    // region Constructors
    public PsiParameterImpl(@NotNull ASTNode node) {
        super(node);
    }

    public PsiParameterImpl(@NotNull PsiParameterStub stub, @NotNull IStubElementType nodeType) {
        super(stub, nodeType);
    }
    // endregion

    //region PsiNamedElement
    public @Nullable PsiElement getNameIdentifier() {
        PsiElement parent = getParent();
        PsiElement grandParent = parent == null ? null : parent.getParent();
        if (parent instanceof PsiFunctionCallParams || grandParent instanceof PsiFunctorCall) {
            return null;
        }

        PsiElement identifier = getFirstChild();
        IElementType elementType = identifier == null ? null : identifier.getNode().getElementType();
        if (elementType == OCamlTypes.TILDE || elementType == OCamlTypes.LPAREN || elementType == OCamlTypes.QUESTION_MARK) {
            PsiElement nextSibling = identifier.getNextSibling();
            IElementType nextElementType = nextSibling == null ? null : nextSibling.getNode().getElementType();
            identifier = nextElementType == OCamlTypes.LPAREN ? nextSibling.getNextSibling() : nextSibling;
        }

        if (identifier instanceof PsiNamedParam) {
            return ORUtil.findImmediateFirstChildOfClass(identifier, PsiLowerIdentifier.class);
        }

        return identifier;
    }

    @Override public @NotNull String getRealName() {
        String name = getName();
        // (t: type) -> t
        if (name != null && name.indexOf(':') != -1) {
            name = name.substring(name.startsWith("(") ? 1 : 0, name.indexOf(':')).trim();
        }
        return name == null ? getText() : name;
    }

    @Override
    public @Nullable String getName() {
        PsiParameterStub stub = getGreenStub();
        if (stub != null) {
            return stub.getName();
        }

        PsiElement identifier = getNameIdentifier();
        if (identifier != null) {
            return identifier.getText();
        }

        PsiElement parent = getParent();
        PsiElement grandParent = parent == null ? null : parent.getParent();

        if (parent instanceof PsiFunctionCallParams || grandParent instanceof PsiFunctorCall) {
            List<PsiParameter> parameters = parent instanceof PsiFunctionCallParams ?
                    ((PsiFunctionCallParams) parent).getParametersList() :
                    ((PsiParameters) parent).getParametersList();
            int i = 0;
            for (PsiParameter parameter : parameters) {
                if (parameter == this) {
                    PsiElement prevSibling = ORUtil.prevSibling(parent);
                    return (prevSibling == null ? "" : prevSibling.getText()) + "[" + i + "]";
                }
                i++;
            }
        }

        return null;
    }

    @Override
    public @NotNull PsiElement setName(@NotNull String name) throws IncorrectOperationException {
        return this;
    }
    //endregion

    //region PsiQualifiedName
    @Override
    public String [] getPath() {
        PsiParameterStub stub = getGreenStub();
        if (stub != null) {
            return stub.getPath();
        }

        PsiQualifiedNamedElement qualifiedParent = PsiTreeUtil.getParentOfType(this, PsiQualifiedNamedElement.class);
        String qName = qualifiedParent == null ? null : qualifiedParent.getQualifiedName();
        return qName == null ? null : qName.split("\\.");
    }

    @Override
    public @NotNull String getQualifiedName() {
        PsiParameterStub stub = getGreenStub();
        if (stub != null) {
            return stub.getQualifiedName();
        }

        PsiElement parent = getParent();
        PsiElement grandParent = parent == null ? null : parent.getParent();
        boolean isCall = parent instanceof PsiFunctionCallParams || grandParent instanceof PsiFunctorCall;

        String name = getName();
        String[] path = getPath();
        return Joiner.join(".", path) + (isCall ? "." + name : "[" + name + "]");
    }
    //endregion

    @Override
    public @Nullable PsiSignature getSignature() {
        return PsiTreeUtil.findChildOfType(this, PsiSignature.class);
    }

    @Override
    public @NotNull String toString() {
        return "Parameter " + getQualifiedName();
    }
}
