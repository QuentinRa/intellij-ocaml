package com.or.lang.core.psi.reference;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiPolyVariantReferenceBase;
import com.intellij.psi.ResolveResult;
import com.or.ide.files.FileBase;
import com.or.ide.search.index.LetIndex;
import com.or.lang.core.psi.*;
import com.or.lang.core.psi.impl.PsiLeafPropertyName;
import com.or.utils.Joiner;
import com.or.utils.Log;
import com.or.utils.Platform;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.Deque;
import java.util.List;

public class PsiPropertyNameReference extends PsiPolyVariantReferenceBase<PsiLeafPropertyName> {
    private static final Log LOG = Log.create("ref.params");

    private final @Nullable String myReferenceName;

    public PsiPropertyNameReference(@NotNull PsiLeafPropertyName element) {
        super(element, TextRange.from(0, element.getTextLength()));
        myReferenceName = element.getText();
    }

    @Override
    public ResolveResult [] multiResolve(boolean incompleteCode) {
        if (myReferenceName == null) {
            return ResolveResult.EMPTY_ARRAY;
        }

        LOG.debug("Find reference for propertyLeaf", myReferenceName);

        Project project = myElement.getProject();
        ORElementResolver.Resolutions resolutions = project.getService(ORElementResolver.class).getComputation();

        // Comp.make function from module
        // Find all elements by name and create a list of paths
        resolutions.add(LetIndex.getElements("make", project, null), false);

        // Gather instructions from element up to the file root
        Deque<PsiElement> instructions = ORReferenceAnalyzer.createInstructions(myElement);

        if (LOG.isTraceEnabled()) {
            LOG.trace("  Instructions: ", Joiner.join(" -> ", instructions));
        }

        // Resolve aliases in the stack of instructions, this time from file down to element
        Deque<CodeInstruction> resolvedInstructions = ORReferenceAnalyzer.resolveInstructions(instructions, myElement.getProject());

        if (LOG.isTraceEnabled()) {
            LOG.trace("  Resolved instructions: " + Joiner.join(" -> ", resolvedInstructions));
        }

        // Now that everything is resolved, we can use the stack of instructions to add weight to the paths

        for (CodeInstruction instruction : resolvedInstructions) {
            if (instruction.mySource instanceof FileBase) {
                resolutions.udpateTerminalWeight(((FileBase) instruction.mySource).getModuleName());
            } else if (instruction.mySource instanceof PsiLowerSymbol) {
                resolutions.removeUpper();
                resolutions.updateWeight(null, instruction.myAlternateValues);
            } else if (instruction.myValues != null) {
                for (String value : instruction.myValues) {
                    resolutions.updateWeight(value, instruction.myAlternateValues);
                }
            }
        }

        resolutions.removeIncomplete();
        Collection<PsiQualifiedPathElement> sortedResult = resolutions.resolvedElements();

        if (LOG.isDebugEnabled()) {
            LOG.debug("  => found", Joiner.join(", ", sortedResult,
                    element -> element.getQualifiedName()
                            + " [" + Platform.getRelativePathToModule(element.getContainingFile()) + "]"));
        }

        ResolveResult[] resolveResults = new ResolveResult[sortedResult.size()];
        int i = 0;
        for (PsiElement element : sortedResult) {
            resolveResults[i] = new JsxTagResolveResult(element, myReferenceName);
            i++;
        }

        return resolveResults;
    }

    @Override
    public @Nullable PsiElement resolve() {
        ResolveResult[] resolveResults = multiResolve(false);
        return 0 < resolveResults.length ? resolveResults[0].getElement() : null;
    }

    public static class JsxTagResolveResult implements ResolveResult {
        private @Nullable PsiElement myReferencedIdentifier = null;

        public JsxTagResolveResult(@NotNull PsiElement referencedElement, @NotNull String propertyName) {
            if (referencedElement instanceof PsiLet) {
                PsiFunction function = ((PsiLet) referencedElement).getFunction();
                if (function != null) {
                    List<PsiParameter> parameters = function.getParameters();
                    for (PsiParameter parameter : parameters) {
                        if (propertyName.equals(parameter.getName())) {
                            myReferencedIdentifier = parameter;
                            break;
                        }
                    }
                }
            }
        }

        @Override
        public @Nullable PsiElement getElement() {
            return myReferencedIdentifier;
        }

        @Override
        public boolean isValidResult() {
            return myReferencedIdentifier != null;
        }
    }
}
