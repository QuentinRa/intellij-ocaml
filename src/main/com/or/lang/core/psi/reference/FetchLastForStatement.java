package com.or.lang.core.psi.reference;

import com.intellij.psi.PsiElement;

/**
 * This is me trying to fix an issue in ReasonML. Originally, the wrong element is resolved
 * when looking for the references of a symbol. It happens when you declared something twice, like
 * - let f x y = ...
 * - let x y = ...
 * If you are adding a new statement using x, the first one in f will be the one that will be detected
 * when going to the declaration, ...
 */
public interface FetchLastForStatement {

    /**
     * Ensure that resolve is doing its job properly. Needs to be set again each time
     * resolve is called.
     * @param symbol The element where the referenced element is used
     */
    void setResolveFor(PsiElement symbol);
}
