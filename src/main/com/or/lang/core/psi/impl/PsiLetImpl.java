package com.or.lang.core.psi.impl;

import com.intellij.lang.ASTNode;
import com.intellij.navigation.ItemPresentation;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiWhiteSpace;
import com.intellij.psi.stubs.IStubElementType;
import com.intellij.psi.tree.IElementType;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.util.IncorrectOperationException;
import com.ocaml.icons.OCamlIcons;
import com.or.lang.OCamlTypes;
import com.or.lang.core.ORUtil;
import com.or.lang.core.psi.*;
import com.or.lang.core.psi.reference.PsiLowerSymbolReference;
import com.or.lang.core.stub.PsiLetStub;
import com.or.lang.utils.ORLanguageProperties;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class PsiLetImpl extends PsiTokenStub<PsiLet, PsiLetStub> implements PsiLet {
    // region Constructors
    public PsiLetImpl(@NotNull ASTNode node) {
        super(node);
    }

    public PsiLetImpl(@NotNull PsiLetStub stub, @NotNull IStubElementType nodeType) {
        super(stub, nodeType);
    }
    // endregion

    // region PsiNamedElement
    public @Nullable PsiElement getNameIdentifier() {
        return ORUtil.findImmediateFirstChildOfAnyClass(this, PsiLowerIdentifier.class, PsiScopedExpr.class, PsiDeconstruction.class, PsiLiteralExpression.class/*rescript custom operator*/, PsiUnit.class);
    }

    @Override
    public @Nullable String getName() {
        PsiLetStub stub = getGreenStub();
        if (stub != null) {
            return stub.getName();
        }

        PsiElement nameIdentifier = getNameIdentifier();
        IElementType nameType = nameIdentifier == null ? null : nameIdentifier.getNode().getElementType();
        return nameType == null || nameType == OCamlTypes.UNDERSCORE || nameType == OCamlTypes.C_UNIT
                ? null
                : nameIdentifier.getText();
    }

    @Override
    public @NotNull PsiElement setName(@NotNull String name) throws IncorrectOperationException {
        return this;
    }
    // endregion

    //region PsiQualifiedName
    @Override
    public String [] getPath() {
        PsiLetStub stub = getGreenStub();
        if (stub != null) {
            return stub.getPath();
        }

        //noinspection ConstantConditions
        return ORUtil.getQualifiedPath(this);
    }

    @Override
    public @NotNull String getQualifiedName() {
        PsiLetStub stub = getGreenStub();
        if (stub != null) {
            return stub.getQualifiedName();
        }

        return ORUtil.getQualifiedName(this);
    }
    //endregion

    @Override
    public @Nullable PsiLetBinding getBinding() {
        return findChildByClass(PsiLetBinding.class);
    }

    @Override
    public boolean isScopeIdentifier() {
        return ORUtil.findImmediateFirstChildOfClass(this, PsiDeconstruction.class) != null;
    }

    @Override
    public @NotNull Collection<PsiElement> getScopeChildren() {
        Collection<PsiElement> result = new ArrayList<>();

        PsiElement scope = ORUtil.findImmediateFirstChildOfClass(this, PsiDeconstruction.class);
        if (scope != null) {
            for (PsiElement element : scope.getChildren()) {
                if (element.getNode().getElementType() != OCamlTypes.COMMA) {
                    result.add(element);
                }
            }
        }

        return result;
    }

    @Override
    public @Nullable String getAlias() {
        PsiLetStub stub = getGreenStub();
        if (stub != null) {
            return stub.getAlias();
        }

        PsiElement binding = getBinding();
        if (binding != null) {
            return ORUtil.computeAlias(binding.getFirstChild(), true);
        }

        return null;
    }

    @Override
    public @Nullable PsiElement resolveAlias() {
        PsiElement binding = getBinding();
        PsiLowerSymbol lSymbol = binding == null ? null : ORUtil.findImmediateLastChildOfClass(binding, PsiLowerSymbol.class);
        PsiLowerSymbolReference lReference = lSymbol == null ? null : (PsiLowerSymbolReference) lSymbol.getReference();
        return lReference == null ? null : lReference.resolveInterface();
    }

    @Override
    public @Nullable PsiSignature getSignature() {
        return findChildByClass(PsiSignature.class);
    }

    @Override
    public boolean isFunction() {
        PsiLetStub stub = getGreenStub();
        if (stub != null) {
            return stub.isFunction();
        }

        PsiSignature inferredType = getInferredType();
        if (inferredType != null) {
            return inferredType.isFunction();
        } else {
            PsiSignature signature = getSignature();
            if (signature != null) {
                return signature.isFunction();
            }
        }

        return PsiTreeUtil.findChildOfType(this, PsiFunction.class) != null;
    }

    public @Nullable PsiFunction getFunction() {
        PsiLetBinding binding = getBinding();
        if (binding != null) {
            PsiElement child = binding.getFirstChild();
            if (child instanceof PsiFunction) {
                return (PsiFunction) child;
            }
        }
        return null;
    }

    @Override
    public boolean isRecord() {
        return findChildByClass(PsiRecord.class) != null;
    }

    @Override
    public boolean isJsObject() {
        PsiLetBinding binding = getBinding();
        return binding != null && binding.getFirstChild() instanceof PsiJsObject;
    }

    @Override
    public @NotNull Collection<PsiRecordField> getRecordFields() {
        return PsiTreeUtil.findChildrenOfType(this, PsiRecordField.class);
    }

    private boolean isRecursive() {
        // Find first element after the LET
        PsiElement firstChild = getFirstChild();
        PsiElement sibling = firstChild.getNextSibling();
        if (sibling instanceof PsiWhiteSpace) {
            sibling = sibling.getNextSibling();
        }

        return sibling != null && "rec".equals(sibling.getText());
    }

    // region Inferred type
    @Override
    public @Nullable PsiSignature getInferredType() {
        return null;
    }

    @Override
    public boolean hasInferredType() {
        return false;
    }
    // endregion

    @Override
    public boolean isDeconstruction() {
        PsiElement nameIdentifier = getNameIdentifier();
        return nameIdentifier instanceof PsiDeconstruction;
    }

    @Override
    public boolean isPrivate() {
        PsiLetAttribute attribute = ORUtil.findImmediateFirstChildOfClass(this, PsiLetAttribute.class);
        String value = attribute == null ? null : attribute.getValue();
        return value != null && value.equals("private");
    }

    @NotNull
    @Override
    public List<PsiElement> getDeconstructedElements() {
        PsiElement nameIdentifier = getNameIdentifier();
        if (nameIdentifier instanceof PsiDeconstruction) {
            return ((PsiDeconstruction) nameIdentifier).getDeconstructedElements();
        }
        return Collections.emptyList();
    }

    // region PsiStructuredElement
    @Override
    public boolean canBeDisplayed() {
        PsiElement nameIdentifier = getNameIdentifier();
        if (nameIdentifier instanceof PsiUnit) {
            return false;
        }
        if (nameIdentifier != null) {
            return true;
        }

        PsiScopedExpr scope = ORUtil.findImmediateFirstChildOfClass(this, PsiScopedExpr.class);
        return scope != null && !scope.isEmpty();
    }

    @Nullable
    @Override
    public ItemPresentation getPresentation() {
        final PsiLet let = this;

        return new ItemPresentation() {
            @NotNull
            @Override
            public String getPresentableText() {
                PsiElement letValueName = getNameIdentifier();
                if (letValueName == null) {
                    PsiScopedExpr scope = ORUtil.findImmediateFirstChildOfClass(let, PsiScopedExpr.class);
                    return scope == null || scope.isEmpty() ? "_" : scope.getText();
                }

                String letName = letValueName.getText();
                if (isFunction()) {
                    return letName + (isRecursive() ? " (rec)" : "");
                }

                return letName;
            }

            @Override
            public @Nullable String getLocationString() {
                PsiSignature signature = hasInferredType() ? getInferredType() : getSignature();
                return (signature == null ? null : signature.asText(ORLanguageProperties.cast(getLanguage())));
            }

            @Override
            public @NotNull Icon getIcon(boolean unused) {
                return isFunction() ? OCamlIcons.Nodes.FUNCTION : OCamlIcons.Nodes.LET;
            }
        };
    }
    // endregion

    @Nullable
    @Override
    public String toString() {
        return "Let " + getQualifiedName();
    }
}
