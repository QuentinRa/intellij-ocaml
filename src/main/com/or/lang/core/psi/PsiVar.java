package com.or.lang.core.psi;

import org.jetbrains.annotations.NotNull;

import java.util.Collection;

/**
 * A PsiLet or a PsiVar are used to declare a variable. They both implement PsiVar.
 */
public interface PsiVar {

    boolean isFunction();

    @NotNull
    Collection<PsiRecordField> getRecordFields();
}
