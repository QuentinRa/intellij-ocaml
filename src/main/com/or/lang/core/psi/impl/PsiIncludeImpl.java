package com.or.lang.core.psi.impl;

import com.intellij.lang.ASTNode;
import com.intellij.navigation.ItemPresentation;
import com.intellij.psi.PsiElement;
import com.intellij.psi.stubs.IStubElementType;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.util.xml.model.gotosymbol.GoToSymbolProvider;
import com.ocaml.icons.OCamlIcons;
import com.or.ide.search.PsiFinder;
import com.or.lang.core.ORUtil;
import com.or.lang.core.psi.PsiInclude;
import com.or.lang.core.psi.PsiQualifiedPathElement;
import com.or.lang.core.psi.PsiUpperSymbol;
import com.or.lang.core.stub.PsiIncludeStub;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class PsiIncludeImpl extends PsiTokenStub<PsiInclude, PsiIncludeStub> implements PsiInclude {
    // region Constructors
    public PsiIncludeImpl(@NotNull ASTNode node) {
        super(node);
    }

    public PsiIncludeImpl(@NotNull PsiIncludeStub stub, @NotNull IStubElementType nodeType) {
        super(stub, nodeType);
    }
    // endregion

    @Override public String[] getQualifiedPath() {
        PsiIncludeStub stub = getGreenStub();
        if (stub != null) {
            return stub.getQualifiedPath();
        }

        return ORUtil.getQualifiedPath(this);
    }

    @Override
    public @NotNull String getIncludePath() {
        PsiIncludeStub stub = getGreenStub();
        if (stub != null) {
            return stub.getIncludePath();
        }

        PsiElement firstChild = PsiTreeUtil.skipWhitespacesForward(getFirstChild());
        if (firstChild instanceof PsiFunctorCall) {
            return ((PsiFunctorCall) firstChild).getFunctorName();
        }
        return firstChild == null ? "" : ORUtil.getTextUntilClass(firstChild, PsiConstraints.class);
    }

    @Override
    public String @Nullable [] getResolvedPath() {
        PsiIncludeStub stub = getGreenStub();
        if (stub != null) {
            return stub.getResolvedPath();
        }

        // Iterate over previous elements, can't use references here because it needs to work during indexing
        String includePath = getIncludePath();
        PsiFinder psiFinder = getProject().getService(PsiFinder.class);
        PsiQualifiedPathElement resolvedElement = psiFinder.findModuleBack(this, includePath);

        String path = resolvedElement == null ? includePath : resolvedElement.getQualifiedName();
        return path == null ? null : path.split("\\.");
    }

    // deprecate ?
    @Override
    public @Nullable PsiUpperSymbol getModuleReference() {
        // Latest element in path
        return ORUtil.findImmediateLastChildOfClass(this, PsiUpperSymbol.class);
    }

    @Override
    public @Nullable PsiElement resolveModule() {
        PsiElement firstChild = PsiTreeUtil.skipWhitespacesForward(getFirstChild());
        if (firstChild instanceof PsiFunctorCall) {
            return ((PsiFunctorCall) firstChild).resolveFunctor();
        } else if (firstChild instanceof PsiUpperSymbol) {
            return ORUtil.resolveModuleSymbol((PsiUpperSymbol) firstChild);
        }
        return null;
    }

    @Override
    public ItemPresentation getPresentation() {
        return new GoToSymbolProvider.BaseNavigationItem(this, getIncludePath(), OCamlIcons.Nodes.INCLUDE);
    }

    @Override
    public boolean canBeDisplayed() {
        return !(getParent() instanceof PsiFunctionBody);
    }

    @Override
    public @NotNull String toString() {
        return "PsiInclude " + getIncludePath();
    }
}
