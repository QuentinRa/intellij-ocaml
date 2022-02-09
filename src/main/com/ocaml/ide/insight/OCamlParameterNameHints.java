package com.ocaml.ide.insight;

import com.intellij.codeInsight.hints.InlayInfo;
import com.intellij.codeInsight.hints.InlayParameterHintsProvider;
import com.intellij.openapi.compiler.CompilerManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiReference;
import com.intellij.psi.PsiWhiteSpace;
import com.intellij.psi.util.PsiTreeUtil;
import com.ocaml.lang.utils.OCamlPsiUtils;
import com.or.lang.core.psi.*;
import com.or.lang.core.psi.impl.PsiLowerIdentifier;
import com.or.lang.core.psi.impl.PsiScopedExpr;
import com.or.lang.core.psi.impl.PsiUpperIdentifier;
import com.or.lang.core.psi.reference.PsiLowerSymbolReference;
import org.jetbrains.annotations.NotNull;

import java.util.*;

@SuppressWarnings("UnstableApiUsage")
public class OCamlParameterNameHints implements InlayParameterHintsProvider {

    private static final List<InlayInfo> EMPTY_COLLECTION = Collections.emptyList();

    @SuppressWarnings("UnstableApiUsage")
    @Override public @NotNull Set<String> getDefaultBlackList() {
        return new HashSet<>();
    }

    @Override public @NotNull List<InlayInfo> getParameterHints(@NotNull PsiElement element, @NotNull PsiFile file) {
        // excluded : ignored
        VirtualFile virtualFile = file.getVirtualFile();
        Project project = file.getProject();
        if (CompilerManager.getInstance(project).isExcludedFromCompilation(virtualFile))
            return EMPTY_COLLECTION;
        // not excluded, do the job
        return getParameterHints(element);
    }

    @Override public @NotNull List<InlayInfo> getParameterHints(@NotNull PsiElement element) {
        // small optimisation: skip
        if (element instanceof PsiWhiteSpace ||
                // supported: literals and scope expr
                (!(element instanceof PsiLiteralExpression) && !(element instanceof PsiScopedExpr)))
            return EMPTY_COLLECTION;
        // get a reference to the function
        PsiLowerSymbol functionName = PsiTreeUtil.getPrevSiblingOfType(element, PsiLowerSymbol.class);
        PsiReference reference = functionName == null ? null : functionName.getReference();
        if (!(reference instanceof PsiLowerSymbolReference)) return EMPTY_COLLECTION;
        // resolve the real element?
        PsiElement resolvedRef = reference.resolve();
        PsiElement resolvedElement = (resolvedRef instanceof PsiLowerIdentifier || resolvedRef instanceof PsiUpperIdentifier) ? resolvedRef.getParent() : resolvedRef;
        if (!(resolvedElement instanceof PsiLet)) return EMPTY_COLLECTION;
        // get the parameters
        PsiLet psiLet = (PsiLet) resolvedElement;
        List<PsiParameter> parameters = null;
        if (psiLet.isFunction()) {
            PsiFunction psiFunction = psiLet.getFunction();
            if (psiFunction != null) {
                parameters = psiFunction.getParameters();
            }
        }
        if (parameters == null) return EMPTY_COLLECTION;
        // get the name for this value
        int indexOfParameter = OCamlPsiUtils.findIndexOfParameter(element, functionName.getText());
        if (indexOfParameter < parameters.size()) {
            PsiParameter psiParameter = parameters.get(indexOfParameter);
            return List.of(new InlayInfo(psiParameter.getRealName(), element.getTextOffset()));
        }

        return EMPTY_COLLECTION;
    }
}
