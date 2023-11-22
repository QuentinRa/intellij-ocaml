package com.or.lang.core.psi.impl;

import com.intellij.lang.ASTNode;
import com.intellij.navigation.ItemPresentation;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiNamedElement;
import com.intellij.psi.stubs.IStubElementType;
import com.intellij.util.IncorrectOperationException;
import com.ocaml.icons.OCamlIcons;
import com.or.lang.core.ExpressionFilter;
import com.or.lang.core.ORUtil;
import com.or.lang.core.psi.ExpressionScope;
import com.or.lang.core.psi.PsiFunctor;
import com.or.lang.core.psi.PsiModule;
import com.or.lang.core.psi.PsiUpperSymbol;
import com.or.lang.core.stub.PsiModuleStub;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.util.Collection;

import static java.util.Collections.emptyList;

public class PsiFunctorImpl extends PsiTokenStub<PsiModule, PsiModuleStub> implements PsiFunctor {
    // region Constructors
    public PsiFunctorImpl(@NotNull ASTNode node) {
        super(node);
    }

    public PsiFunctorImpl(@NotNull PsiModuleStub stub, @NotNull IStubElementType nodeType) {
        super(stub, nodeType);
    }
    // endregion

    // region PsiNamedElement
    @Override
    public @Nullable PsiElement getNameIdentifier() {
        return ORUtil.findImmediateFirstChildOfClass(this, PsiUpperIdentifier.class);
    }

    @Override
    public @Nullable String getName() {
        PsiModuleStub stub = getGreenStub();
        if (stub != null) {
            return stub.getName();
        }

        PsiElement nameIdentifier = getNameIdentifier();
        return nameIdentifier == null ? null : nameIdentifier.getText();
    }

    @Override
    public @NotNull PsiElement setName(@NotNull String name) throws IncorrectOperationException {
        return this;
    }
    // endregion

    //region PsiQualifiedName
    @Override
    public @Nullable String[] getPath() {
        PsiModuleStub stub = getGreenStub();
        if (stub != null) {
            return stub.getPath();
        }

        return ORUtil.getQualifiedPath(this);
    }

    @Override
    public @NotNull String getQualifiedName() {
        PsiModuleStub stub = getGreenStub();
        if (stub != null) {
            return stub.getQualifiedName();
        }

        return ORUtil.getQualifiedName(this);
    }
    //endregion

    @Override public @Nullable String[] getQualifiedNameAsPath() {
        return ORUtil.getQualifiedNameAsPath(this);
    }

    @Override public @Nullable PsiElement getComponentNavigationElement() {
        return null;
    }

    @Override
    public boolean isInterface() {
        return false;
    }

    @Override
    public boolean isComponent() {
        return false;
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
        return ORUtil.findImmediateFirstChildOfClass(this, PsiFunctorBinding.class);
    }

    @Override
    public @NotNull String getModuleName() {
        String name = getName();
        return name == null ? "" : name;
    }

    @Override
    public @Nullable PsiFunctorCall getFunctorCall() {
        return null;
    }

    @Override
    public @NotNull Collection<PsiNamedElement> getExpressions(@NotNull ExpressionScope eScope, @Nullable ExpressionFilter filter) {
        //PsiElement returnType = getReturnType();
        //if (returnType instanceof PsiFunctorResult) {
        //    // Resolve return type, and get expressions from there
        //    PsiFunctorResult functorResult = (PsiFunctorResult) returnType;
        //    result = new ArrayList<>();
        //
        //    String name = functorResult.getText();
        //    Project project = getProject();
        //    PsiFinder psiFinder = project.getService(PsiFinder.class);
        //    QNameFinder qnameFinder = PsiFinder.getQNameFinder(getLanguage());
        //    GlobalSearchScope searchScope = GlobalSearchScope.allScope(project);
        //
        //    Set<String> potentialPaths = qnameFinder.extractPotentialPaths(functorResult);
        //    for (String potentialPath : potentialPaths) {
        //        Set<PsiModule> modulesFromQn =
        //                psiFinder.findModulesFromQn(
        //                        potentialPath + "." + name, true, interfaceOrImplementation);
        //        if (!modulesFromQn.isEmpty()) {
        //            PsiModule module = modulesFromQn.iterator().next();
        //            return module.getExpressions(eScope, filter);
        //        }
        //    }
        //    // nothing found, try without path
        //    Set<PsiModule> modulesFromQn =
        //            psiFinder.findModulesFromQn(name, true, interfaceOrImplementation);
        //    if (!modulesFromQn.isEmpty()) {
        //        PsiModule module = modulesFromQn.iterator().next();
        //        return module.getExpressions(eScope, filter);
        //    }
        //} else {
        //    // Get expressions from functor body
        //    PsiElement body = getBinding();
        //    if (body != null) {
        //        result = new ArrayList<>();
        //        PsiElement element = body.getFirstChild();
        //        while (element != null) {
        //            if (element instanceof PsiNamedElement) {
        //                if (filter == null || filter.accept((PsiNamedElement) element)) {
        //                    result.add((PsiNamedElement) element);
        //                }
        //            }
        //            element = element.getNextSibling();
        //        }
        //    }
        //}

        return emptyList();
    }

    @Override
    public @Nullable PsiModule getModuleExpression(@Nullable String name) {
        return null;
    }

    @Override
    public @Nullable PsiFunctorResult getReturnType() {
        return ORUtil.findImmediateFirstChildOfClass(this, PsiFunctorResult.class);
    }

    public ItemPresentation getPresentation() {
        return new ItemPresentation() {
            @Nullable
            @Override
            public String getPresentableText() {
                return getName();
            }

            @Nullable
            @Override
            public String getLocationString() {
                return null;
            }

            @NotNull
            @Override
            public Icon getIcon(boolean unused) {
                return OCamlIcons.Nodes.FUNCTOR;
            }
        };
    }

    @Override
    public @NotNull String toString() {
        return "Functor";
    }
}
