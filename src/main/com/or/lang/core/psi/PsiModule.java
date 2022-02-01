package com.or.lang.core.psi;

import com.intellij.psi.NavigatablePsiElement;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiNamedElement;
import com.or.lang.core.ExpressionFilter;
import com.or.lang.core.psi.impl.PsiFunctorCall;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;

/**
 * Common interface to file-based modules and inner modules
 */
public interface PsiModule extends PsiQualifiedPathElement, NavigatablePsiElement, PsiStructuredElement {

    boolean isInterface();

    boolean isComponent();

    @Nullable
    String getAlias();

    @Nullable
    PsiUpperSymbol getAliasSymbol();

    @Nullable
    String getModuleName();

    @Nullable
    String[] getQualifiedNameAsPath();

    @Nullable
    PsiFunctorCall getFunctorCall();

    @NotNull
    Collection<PsiNamedElement> getExpressions(@NotNull ExpressionScope eScope, @Nullable ExpressionFilter filter);

    @Nullable
    PsiModule getModuleExpression(@Nullable String name);

    @Nullable
    PsiElement getModuleType();

    @Nullable
    PsiElement getBody();

    @Nullable
    PsiElement getComponentNavigationElement();
}
