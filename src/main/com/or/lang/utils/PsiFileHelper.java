package com.or.lang.utils;

import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiNamedElement;
import com.or.ide.search.PsiFinder;
import com.or.lang.core.ExpressionFilter;
import com.or.lang.core.psi.ExpressionScope;
import com.or.lang.core.psi.PsiInclude;
import com.or.lang.core.psi.PsiLet;
import com.or.lang.core.psi.PsiModule;
import com.or.lang.core.psi.impl.PsiDirective;
import com.or.lang.core.psi.impl.PsiFakeModule;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import static com.or.lang.core.ORFileType.interfaceOrImplementation;

public class PsiFileHelper {

    private PsiFileHelper() {
    }

    @NotNull
    public static Collection<PsiNamedElement> getExpressions(@Nullable PsiFile file, @NotNull ExpressionScope eScope, @Nullable ExpressionFilter filter) {
        ArrayList<PsiNamedElement> result = new ArrayList<>();

        if (file != null) {
            PsiFinder psiFinder = file.getProject().getService(PsiFinder.class);
            QNameFinder qnameFinder = QNameFinderFactory.getQNameFinder();
            processSiblingExpressions(psiFinder, qnameFinder, file.getFirstChild(), eScope, result, filter);
        }

        return result;
    }

    private static void processSiblingExpressions(@Nullable PsiFinder psiFinder, @NotNull QNameFinder qnameFinder, @Nullable PsiElement element, @NotNull ExpressionScope eScope, @NotNull List<PsiNamedElement> result, @Nullable ExpressionFilter filter) {
        while (element != null) {
            if (element instanceof PsiInclude && psiFinder != null) {
                // Recursively include everything from referenced module
                PsiInclude include = (PsiInclude) element;

                PsiModule includedModule = null;

                String includedPath = include.getIncludePath();
                for (String path : qnameFinder.extractPotentialPaths(include)) {
                    Set<PsiModule> modulesFromQn = psiFinder.findModulesFromQn(path + "." + includedPath, true, interfaceOrImplementation);
                    if (!modulesFromQn.isEmpty()) {
                        includedModule = modulesFromQn.iterator().next();
                        break;
                    }
                }
                if (includedModule == null) {
                    Set<PsiModule> modulesFromQn = psiFinder.findModulesFromQn(includedPath, true, interfaceOrImplementation);
                    if (!modulesFromQn.isEmpty()) {
                        includedModule = modulesFromQn.iterator().next();
                    }
                }

                if (includedModule != null) {
                    Collection<PsiNamedElement> expressions = includedModule.getExpressions(eScope, filter);
                    result.addAll(expressions);
                }
            }

            if (element instanceof PsiDirective) {
                // add all elements found in a directive, can't be resolved
                processSiblingExpressions(
                        psiFinder, qnameFinder, element.getFirstChild(), eScope, result, filter);
            } else if (element instanceof PsiNamedElement) {
                boolean include =
                        eScope == ExpressionScope.all
                                || !(element instanceof PsiLet && ((PsiLet) element).isPrivate());
                if (include
                        && (!(element instanceof PsiFakeModule))
                        && (filter == null || filter.accept((PsiNamedElement) element))) {
                    result.add((PsiNamedElement) element);
                }
            }

            element = element.getNextSibling();
        }
    }
}
