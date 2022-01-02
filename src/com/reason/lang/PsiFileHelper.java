package com.reason.lang;

import com.intellij.psi.*;
import com.intellij.psi.util.*;
import com.reason.lang.core.*;
import com.reason.lang.core.psi.*;
import org.jetbrains.annotations.*;

import java.util.*;

public class PsiFileHelper {

    private PsiFileHelper() {
    }

    @NotNull
    public static Collection<PsiNamedElement> getExpressions(@Nullable PsiFile file, @NotNull ExpressionScope eScope, @Nullable ExpressionFilter filter) {
        ArrayList<PsiNamedElement> result = new ArrayList<>();
        return result;
    }

    @NotNull
    public static Collection<PsiNamedElement> getExpressions(@NotNull PsiFile file, @Nullable String name) {
        Collection<PsiNamedElement> result = new ArrayList<>();

        if (name != null) {
            PsiElement element = file.getFirstChild();
            while (element != null) {
                if (element instanceof PsiNamedElement
                        && name.equals(((PsiNamedElement) element).getName())) {
                    result.add((PsiNamedElement) element);
                }
                element = element.getNextSibling();
            }
        }

        return result;
    }

    @NotNull
    public static List<PsiModule> getModuleExpressions(@Nullable PsiFile file) {
        return PsiTreeUtil.getStubChildrenOfTypeAsList(file, PsiInnerModule.class);
    }

    @NotNull
    public static List<PsiFunctor> getFunctorExpressions(@Nullable PsiFile file) {
        return PsiTreeUtil.getStubChildrenOfTypeAsList(file, PsiFunctor.class);
    }

    @NotNull
    public static List<PsiKlass> getClassExpressions(@Nullable PsiFile file) {
        return PsiTreeUtil.getStubChildrenOfTypeAsList(file, PsiKlass.class);
    }

    @NotNull
    public static List<PsiVal> getValExpressions(@Nullable PsiFile file) {
        return PsiTreeUtil.getStubChildrenOfTypeAsList(file, PsiVal.class);
    }

    @NotNull
    public static List<PsiExternal> getExternalExpressions(@Nullable PsiFile file) {
        return PsiTreeUtil.getStubChildrenOfTypeAsList(file, PsiExternal.class);
    }

    @NotNull
    public static Collection<PsiOpen> getOpenExpressions(@Nullable PsiFile file) {
        return PsiTreeUtil.getStubChildrenOfTypeAsList(file, PsiOpen.class);
    }

    @NotNull
    public static List<PsiInclude> getIncludeExpressions(@Nullable PsiFile file) {
        return PsiTreeUtil.getStubChildrenOfTypeAsList(file, PsiInclude.class);
    }
}
