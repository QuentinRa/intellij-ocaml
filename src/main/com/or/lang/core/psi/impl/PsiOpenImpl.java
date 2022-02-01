package com.or.lang.core.psi.impl;

import com.intellij.lang.ASTNode;
import com.intellij.navigation.ItemPresentation;
import com.intellij.psi.PsiElement;
import com.intellij.psi.stubs.IStubElementType;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.util.xml.model.gotosymbol.GoToSymbolProvider;
import com.ocaml.icons.OCamlIcons;
import com.or.lang.OCamlTypes;
import com.or.lang.core.ORUtil;
import com.or.lang.core.psi.PsiOpen;
import com.or.lang.core.stub.PsiOpenStub;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class PsiOpenImpl extends PsiTokenStub<PsiOpen, PsiOpenStub> implements PsiOpen {
    // region Constructors
    public PsiOpenImpl(@NotNull ASTNode node) {
        super(node);
    }

    public PsiOpenImpl(@NotNull PsiOpenStub stub, @NotNull IStubElementType nodeType) {
        super(stub, nodeType);
    }
    // endregion

    public @NotNull String getPath() {
        PsiOpenStub stub = getGreenStub();
        if (stub != null) {
            return stub.getOpenPath();
        }

        // Skip `let` and `open`
        PsiElement firstChild = getFirstChild();
        if (firstChild != null && firstChild.getNode().getElementType() == OCamlTypes.LET) { // `let open` in OCaml
            firstChild = ORUtil.nextSibling(firstChild);
        }
        // Skip force open
        PsiElement child = PsiTreeUtil.skipWhitespacesForward(firstChild);
        if (child != null && child.getNode().getElementType() == OCamlTypes.EXCLAMATION_MARK) {
            child = PsiTreeUtil.skipWhitespacesForward(child);
        }

        if (child instanceof PsiFunctorCall) {
            return ((PsiFunctorCall) child).getFunctorName();
        }
        return child == null ? "" : ORUtil.getTextUntilTokenType(child, null);
    }

    @Override
    public boolean canBeDisplayed() {
        return !(getParent() instanceof PsiFunctionBody);
    }

    @Override
    public ItemPresentation getPresentation() {
        return new GoToSymbolProvider.BaseNavigationItem(this, getPath(), OCamlIcons.Nodes.OPEN);
    }

    @Override
    public @Nullable String toString() {
        return "Open " + getPath();
    }
}
