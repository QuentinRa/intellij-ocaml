package com.or.lang.core.psi.impl;

import com.intellij.lang.ASTNode;
import com.intellij.navigation.ItemPresentation;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiNamedElement;
import com.intellij.psi.StubBasedPsiElement;
import com.intellij.psi.stubs.IStubElementType;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.util.IncorrectOperationException;
import com.or.ide.files.FileBase;
import com.or.lang.core.ExpressionFilter;
import com.or.lang.core.ORUtil;
import com.or.lang.core.psi.ExpressionScope;
import com.or.lang.core.psi.PsiInnerModule;
import com.or.lang.core.psi.PsiModule;
import com.or.lang.core.psi.PsiUpperSymbol;
import com.or.lang.core.stub.PsiModuleStub;
import com.or.lang.utils.PsiFileHelper;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class PsiFakeModule extends PsiTokenStub<PsiModule, PsiModuleStub> implements PsiModule, StubBasedPsiElement<PsiModuleStub> {
    // region Constructors
    public PsiFakeModule(@NotNull ASTNode node) {
        super(node);
    }

    public PsiFakeModule(@NotNull PsiModuleStub stub, @NotNull IStubElementType nodeType) {
        super(stub, nodeType);
    }
    // endregion

    @Override
    public @NotNull String getModuleName() {
        PsiFile file = getContainingFile();
        assert file instanceof FileBase;
        return ((FileBase) file).getModuleName();
    }

    //region PsiNamedElement
    @Override
    public @Nullable String getName() {
        PsiModuleStub greenStub = getGreenStub();
        if (greenStub != null) {
            return greenStub.getName();
        }

        return getModuleName();
    }

    @Override
    public @NotNull PsiElement setName(@NotNull String name) throws IncorrectOperationException {
        throw new RuntimeException("Not implemented, use FileBase");
    }
    //endregion

    //region PsiQualifiedPath
    @Override
    public String @Nullable [] getPath() {
        PsiModuleStub greenStub = getGreenStub();
        if (greenStub != null) {
            return greenStub.getPath();
        }

        return null;
    }

    @Override
    public @NotNull String getQualifiedName() {
        PsiModuleStub greenStub = getGreenStub();
        if (greenStub != null) {
            String name = greenStub.getName();
            return name == null ? "" : name;
        }

        // ?? Namespace ??
        return getModuleName();
    }
    //endregion

    @Override public @Nullable String[] getQualifiedNameAsPath() {
        return ORUtil.getQualifiedNameAsPath(this);
    }

    @Override
    public void navigate(boolean requestFocus) {
        PsiFile file = getContainingFile();
        file.navigate(requestFocus);
    }

    @Override
    public @NotNull PsiElement getComponentNavigationElement() {
        return ((FileBase) getContainingFile()).getComponentNavigationElement();
    }

    @Override
    public boolean isInterface() {
        PsiModuleStub greenStub = getGreenStub();
        if (greenStub != null) {
            return greenStub.isInterface();
        }
        return ((FileBase) getContainingFile()).isInterface();
    }

    @Override
    public @Nullable PsiFunctorCall getFunctorCall() {
        return null;
    }

    public boolean isComponent() {
        PsiModuleStub greenStub = getGreenStub();
        if (greenStub != null) {
            return greenStub.isComponent();
        }
        return ((FileBase) getContainingFile()).isComponent();
    }

    @Override
    public @Nullable String getAlias() {
        return null;
    }

    @Override
    public @Nullable PsiUpperSymbol getAliasSymbol() {
        return null;
    }

    @Override
    public @Nullable PsiElement getModuleType() {
        return null;
    }

    @Override
    public @Nullable PsiElement getBody() {
        return null;
    }

    @Override
    public @NotNull Collection<PsiNamedElement> getExpressions(@NotNull ExpressionScope eScope, @Nullable ExpressionFilter filter) {
        return PsiFileHelper.getExpressions(getContainingFile(), eScope, filter);
    }

    @Override
    public @Nullable PsiModule getModuleExpression(@Nullable String name) {
        if (name != null) {
            Collection<PsiInnerModule> modules = getExpressions(name, PsiInnerModule.class);
            for (PsiInnerModule module : modules) {
                if (name.equals(module.getName())) {
                    return module;
                }
            }
        }
        return null;
    }

    @NotNull
    private <T extends PsiNamedElement> List<T> getExpressions(@Nullable String name, @NotNull Class<T> clazz) {
        List<T> result = new ArrayList<>();

        if (name != null) {
            Collection<T> children = PsiTreeUtil.findChildrenOfType(getContainingFile(), clazz);
            for (T child : children) {
                if (name.equals(child.getName())) {
                    result.add(child);
                }
            }
        }

        return result;
    }

    public @Nullable ItemPresentation getPresentation() {
        // FileBase presentation should be used
        return null;
    }

    @Override
    public @NotNull String toString() {
        return "PsiFakeModule";
    }
}
