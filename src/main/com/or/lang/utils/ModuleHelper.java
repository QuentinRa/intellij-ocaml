package com.or.lang.utils;

import com.intellij.psi.PsiElement;
import com.intellij.psi.util.PsiTreeUtil;
import com.or.lang.core.psi.PsiAnnotation;
import com.or.lang.core.psi.PsiExternal;
import com.or.lang.core.psi.PsiLet;
import com.or.lang.core.psi.PsiSignature;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class ModuleHelper {

    private ModuleHelper() {
    }

    public static boolean isComponent(@Nullable PsiElement element) {
        if (element == null) {
            return false;
        }

        PsiElement componentDef = null;
        PsiLet makeDef = null;

        // JSX 3

        // Try to find a react.component attribute
        List<PsiAnnotation> annotations = PsiTreeUtil.getStubChildrenOfTypeAsList(element, PsiAnnotation.class);
        for (PsiAnnotation annotation : annotations) {
            if ("@react.component".equals(annotation.getName())) {
                return true;
            }
        }

        // JSX 2

        // Try to find if it's a proxy to a React class
        List<PsiExternal> externals = PsiTreeUtil.getStubChildrenOfTypeAsList(element, PsiExternal.class);
        for (PsiExternal external : externals) {
            PsiSignature signature = external.getSignature();
            String signatureText = signature == null ? null : signature.asText(ORLanguageProperties.cast(element.getLanguage()));
            if ("ReasonReact.reactClass".equals(signatureText)) {
                componentDef = external;
                break;
            }
        }

        // Try to find a make function and a component (if not a proxy) functions
        List<PsiLet> expressions = PsiTreeUtil.getStubChildrenOfTypeAsList(element, PsiLet.class);
        for (PsiLet let : expressions) {
            if (componentDef == null && "component".equals(let.getName())) {
                componentDef = let;
            } else if (makeDef == null && "make".equals(let.getName())) {
                makeDef = let;
            } else if (componentDef != null && makeDef != null) {
                break;
            }
        }

        return componentDef != null && makeDef != null;
    }
}
