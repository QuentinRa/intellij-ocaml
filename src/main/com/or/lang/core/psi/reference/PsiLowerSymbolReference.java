package com.or.lang.core.psi.reference;

import com.intellij.lang.ASTNode;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiElement;
import com.intellij.psi.ResolveResult;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.util.IncorrectOperationException;
import com.ocaml.ide.files.FileHelper;
import com.or.ide.files.FileBase;
import com.or.ide.search.index.*;
import com.or.lang.core.ORCodeFactory;
import com.or.lang.core.ORUtil;
import com.or.lang.core.psi.*;
import com.or.lang.core.psi.impl.PsiLowerIdentifier;
import com.or.lang.core.psi.impl.PsiObjectField;
import com.or.utils.Joiner;
import com.or.utils.Log;
import com.or.utils.Platform;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.Deque;
import java.util.List;

public class PsiLowerSymbolReference extends ORMultiSymbolReference<PsiLowerSymbol> implements FetchLastForStatement {
    private static final Log LOG = Log.create("ref.lower");
    private static final Log LOG_PERF = Log.create("ref.perf.lower");
    private @Nullable PsiElement symbol;

    public PsiLowerSymbolReference(@NotNull PsiLowerSymbol element) {
        super(element);
    }

    @Override
    public void setResolveFor(PsiElement symbol) {
        this.symbol = symbol;
    }

    @Override
    public ResolveResult [] multiResolve(boolean incompleteCode) {
        PsiElement symbol = this.symbol;
        this.symbol = null;
        if (myReferenceName == null) {
            return ResolveResult.EMPTY_ARRAY;
        }

        // If name is used in a definition, it's a declaration not a usage: ie, it's not a reference
        // http://www.jetbrains.org/intellij/sdk/docs/basics/architectural_overview/psi_references.html
        if (myElement instanceof PsiLowerIdentifier) {
            return ResolveResult.EMPTY_ARRAY;
        }

        long startAll = System.currentTimeMillis();

        LOG.debug("Find reference for lower symbol", myReferenceName);


        // Gather instructions from element up to the file root
        Deque<PsiElement> instructions = ORReferenceAnalyzer.createInstructions(myElement);

        if (LOG.isTraceEnabled()) {
            LOG.trace("  Instructions: ", Joiner.join(" -> ", instructions));
        }

        long endInstructions = System.currentTimeMillis();

        // Resolve aliases in the stack of instructions, this time from file down to element
        Deque<CodeInstruction> resolvedInstructions = ORReferenceAnalyzer.resolveInstructions(instructions, myElement.getProject());

        if (LOG.isTraceEnabled()) {
            LOG.trace("  Resolved instructions: " + Joiner.join(" -> ", resolvedInstructions));
        }

        long endResolvedInstructions = System.currentTimeMillis();

        // Find all elements by name and create a list of paths
        Project project = myElement.getProject();
        ORElementResolver.Resolutions resolutions = project.getService(ORElementResolver.class).getComputation();
        GlobalSearchScope scope = GlobalSearchScope.allScope(project);

        Collection<PsiType> types = TypeIndex.getElements(myReferenceName, project, scope);
        Collection<PsiVal> vals = ValIndex.getElements(myReferenceName, project, scope);
        Collection<PsiLet> lets = LetIndex.getElements(myReferenceName, project, scope);
        Collection<PsiExternal> externals = ExternalIndex.getElements(myReferenceName, project, scope);
        Collection<PsiRecordField> recordFields = RecordFieldIndex.getElements(myReferenceName, project, scope);
        Collection<PsiObjectField> objectFields = ObjectFieldIndex.getElements(myReferenceName, project, scope);
        Collection<PsiParameter> parameters = ParameterIndex.getElements(myReferenceName, project, scope);

        if (LOG.isTraceEnabled()) {
            LOG.trace("  indexes: types=" + types.size() + ", vals=" + vals.size() + ", lets=" + lets.size() +
                    ", externals=" + externals.size() + ", fieds=" + (recordFields.size() + objectFields.size()) + ", params=" + parameters.size());
        }

        long endIndexes = System.currentTimeMillis();

        resolutions.add(types, false);
        resolutions.add(vals, false);
        resolutions.add(lets, false);
        resolutions.add(externals, false);
        resolutions.add(recordFields, false);
        resolutions.add(objectFields, false);
        resolutions.add(parameters, false);

        long endAddResolutions = System.currentTimeMillis();

        resolutions.addIncludesEquivalence();

        long endIncludes = System.currentTimeMillis();

        // Now that everything is resolved, we can use the stack of instructions to add weight to the paths

        for (CodeInstruction instruction : resolvedInstructions) {
            if (instruction.mySource instanceof FileBase) {
                resolutions.udpateTerminalWeight(((FileBase) instruction.mySource).getModuleName());
            } else if (instruction.mySource instanceof PsiLowerSymbol) {
                resolutions.removeUpper();
                resolutions.updateWeight(null, instruction.myAlternateValues);
            } else if (instruction.mySource instanceof PsiUpperSymbol) {
                // We're in a path, must be exact
                String value = instruction.getFirstValue();
                resolutions.removeIfNotFound(value, instruction.myAlternateValues);
                resolutions.updateWeight(value, instruction.myAlternateValues);
            } else if (instruction.myValues != null) {
                for (String value : instruction.myValues) {
                    resolutions.updateWeight(value, instruction.myAlternateValues);
                }
            }
        }

        long endUpdateResolutions = System.currentTimeMillis();

        resolutions.removeIncomplete();
        Collection<PsiQualifiedPathElement> sortedResult;
        if (symbol != null) {
            int max = symbol.getTextOffset();
            PsiQualifiedPathElement e = null;
            List<PsiQualifiedPathElement> choices = resolutions.resolveAllElements();
            for (PsiQualifiedPathElement element : choices) {
                if (element.getTextOffset() > max) break;
                e = element;
            }
            if (e != null) sortedResult = List.of(e);
            else if (choices.size() > 0) {
                sortedResult = List.of(choices.get(0));
            } else sortedResult = List.of();
        } else {
            sortedResult = resolutions.resolvedElements();
        }

        if (LOG.isDebugEnabled()) {
            LOG.debug("  => found", Joiner.join(", ", sortedResult,
                    element -> element.getQualifiedName()
                            + " [" + Platform.getRelativePathToModule(element.getContainingFile()) + "]"));
        }

        long endSort = System.currentTimeMillis();

        ResolveResult[] resolveResults = new ResolveResult[sortedResult.size()];
        int i = 0;
        for (PsiElement element : sortedResult) {
            resolveResults[i] = new LowerResolveResult(element, myReferenceName);
            i++;
        }

        long endAll = System.currentTimeMillis();
        if (LOG_PERF.isDebugEnabled()) {
            LOG_PERF.debug("Resolution of " + myReferenceName + " in " + (endAll - startAll) + "ms => " +
                    " i:" + (endInstructions - startAll) + "," +
                    " rI:" + (endResolvedInstructions - endInstructions) + "," +
                    " id:" + (endIndexes - endResolvedInstructions) + "," +
                    " aR:" + (endAddResolutions - endIndexes) + "," +
                    " aI:" + (endIncludes - endAddResolutions) + "," +
                    " uR:" + (endUpdateResolutions - endIncludes) + "," +
                    " sort: " + (endSort - endUpdateResolutions) + ""
            );
        }

        return resolveResults;
    }

    @Override
    public PsiElement handleElementRename(@NotNull String newName) throws IncorrectOperationException {
        PsiElement newNameIdentifier = ORCodeFactory.createLetName(myElement.getProject(), newName);

        ASTNode newNameNode = newNameIdentifier == null ? null : newNameIdentifier.getFirstChild().getNode();
        if (newNameNode != null) {
            PsiElement nameIdentifier = myElement.getFirstChild();
            if (nameIdentifier == null) {
                myElement.getNode().addChild(newNameNode);
            } else {
                ASTNode oldNameNode = nameIdentifier.getNode();
                myElement.getNode().replaceChild(oldNameNode, newNameNode);
            }
        }

        return myElement;
    }

    public static class LowerResolveResult implements ResolveResult {
        private final @NotNull PsiElement m_referencedIdentifier;

        public LowerResolveResult(@NotNull PsiElement referencedElement, String sourceName) {
            if (referencedElement instanceof PsiLet && ((PsiLet) referencedElement).isDeconstruction()) {
                PsiElement identifierElement = referencedElement;
                for (PsiElement deconstructedElement : ((PsiLet) referencedElement).getDeconstructedElements()) {
                    if (deconstructedElement.getText().equals(sourceName)) {
                        identifierElement = deconstructedElement;
                        break;
                    }
                }
                m_referencedIdentifier = identifierElement;
            } else {
                PsiLowerIdentifier identifier = ORUtil.findImmediateFirstChildOfClass(referencedElement, PsiLowerIdentifier.class);
                m_referencedIdentifier = identifier == null ? referencedElement : identifier;
            }
        }

        @Override
        public @Nullable PsiElement getElement() {
            return m_referencedIdentifier;
        }

        @Override
        public boolean isValidResult() {
            return true;
        }

        public boolean isInterface() {
            return FileHelper.isInterface(m_referencedIdentifier.getContainingFile().getFileType());
        }

        @Override public String toString() {
            return "LowerResolveResult{" + m_referencedIdentifier.getText() + '}';
        }
    }
}
