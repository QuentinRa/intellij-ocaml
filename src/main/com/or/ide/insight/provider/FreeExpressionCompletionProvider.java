package com.or.ide.insight.provider;

import com.intellij.codeInsight.completion.CompletionResultSet;
import com.intellij.codeInsight.completion.InsertionContext;
import com.intellij.codeInsight.lookup.LookupElement;
import com.intellij.codeInsight.lookup.LookupElementBuilder;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.EditorModificationUtil;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiManager;
import com.intellij.psi.PsiNamedElement;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.util.PsiIconUtil;
import com.ocaml.icons.OCamlIcons;
import com.or.ide.IconProvider;
import com.or.ide.files.FileBase;
import com.or.ide.search.FileModuleIndexService;
import com.or.ide.search.index.ModuleFqnIndex;
import com.or.ide.search.index.ModuleTopLevelIndex;
import com.or.lang.core.psi.*;
import com.or.lang.core.signature.PsiSignatureUtil;
import com.or.lang.utils.ORLanguageProperties;
import com.or.lang.utils.QNameFinder;
import com.or.utils.Log;
import com.or.utils.Platform;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.Set;

import static com.or.lang.core.ExpressionFilterConstants.NO_FILTER;
import static com.or.lang.core.psi.ExpressionScope.pub;

public class FreeExpressionCompletionProvider {
    private static final Log LOG = Log.create("insight.free");

    private FreeExpressionCompletionProvider() {
    }

    public static void addCompletions(@NotNull QNameFinder qnameFinder, @NotNull PsiElement element, @NotNull CompletionResultSet resultSet) {
        LOG.debug("FREE expression completion");

        Project project = element.getProject();
        GlobalSearchScope scope = GlobalSearchScope.allScope(project);
        FileBase containingFile = (FileBase) element.getContainingFile();

        // Add virtual namespaces
        Collection<String> namespaces = FileModuleIndexService.getService().getNamespaces(project);
        LOG.debug("  namespaces", namespaces);

        for (String namespace : namespaces) {
            resultSet.addElement(
                    LookupElementBuilder.create(namespace)
                            .withTypeText("Generated namespace")
                            .withIcon(OCamlIcons.Nodes.VIRTUAL_NAMESPACE));
        }

        // Add file modules (that are not a component and without namespaces)
        PsiManager psiManager = PsiManager.getInstance(project);
        ModuleTopLevelIndex.processModules(project, scope, topModule -> {
            FileBase topModuleFile = (FileBase) topModule.getContainingFile();
            if (!topModuleFile.equals(containingFile)) {
                VirtualFile virtualFile = topModuleFile.getVirtualFile();
                PsiFile psiFile = psiManager.findFile(virtualFile);
                resultSet.addElement(
                        LookupElementBuilder.create(topModule.getModuleName())
                                .withTypeText(psiFile == null ? virtualFile.getName() : Platform.shortLocation(psiFile))
                                .withIcon(IconProvider.getFileModuleIcon(topModuleFile)));
            }
        });

        Set<String> paths = qnameFinder.extractPotentialPaths(element);
        paths.add("Pervasives");
        LOG.debug("potential paths", paths);

        // Add paths (opens and local opens for example)
        for (String path : paths) {
            Collection<PsiModule> modulesFromQn = ModuleFqnIndex.getElements(path, project, scope);
            for (PsiModule module : modulesFromQn) {
                if (module.getContainingFile().equals(containingFile)) {
                    // if the module is already the containing file, we do nothing,
                    // local expressions will be added after
                    continue;
                }

                Collection<PsiNamedElement> expressions = module.getExpressions(pub, NO_FILTER);
                for (PsiNamedElement expression : expressions) {
                    if (!(expression instanceof PsiAnnotation)) {
                        resultSet.addElement(
                                LookupElementBuilder.create(expression)
                                        .withTypeText(PsiSignatureUtil.getSignature(expression, ORLanguageProperties.cast(element.getLanguage())))
                                        .withIcon(PsiIconUtil.getProvidersIcon(expression, 0))
                                        .withInsertHandler(FreeExpressionCompletionProvider::insertExpression));
                    }
                }
            }
        }

        // Add all local expressions
        PsiElement item = element.getPrevSibling();
        if (item == null) {
            item = element.getParent();
        }

        while (item != null) {
            if (item instanceof PsiInnerModule
                    || item instanceof PsiLet
                    || item instanceof PsiType
                    || item instanceof PsiExternal
                    || item instanceof PsiException
                    || item instanceof PsiVal) {
                if (item instanceof PsiLet && ((PsiLet) item).isDeconstruction()) {
                    for (PsiElement deconstructedElement : ((PsiLet) item).getDeconstructedElements()) {
                        resultSet.addElement(
                                LookupElementBuilder.create(deconstructedElement.getText())
                                        .withTypeText(PsiSignatureUtil.getSignature(item, ORLanguageProperties.cast(element.getLanguage())))
                                        .withIcon(OCamlIcons.Nodes.LET));
                    }
                } else {
                    PsiNamedElement expression = (PsiNamedElement) item;
                    resultSet.addElement(
                            LookupElementBuilder.create(expression)
                                    .withTypeText(PsiSignatureUtil.getSignature(expression, ORLanguageProperties.cast(element.getLanguage())))
                                    .withIcon(PsiIconUtil.getProvidersIcon(expression, 0)));
                    if (item instanceof PsiType) {
                        expandType((PsiType) item, resultSet);
                    }
                }
            }

            PsiElement prevItem = item.getPrevSibling();
            if (prevItem == null) {
                PsiElement parent = item.getParent();
                item = parent instanceof PsiInnerModule ? parent.getPrevSibling() : parent;
            } else {
                item = prevItem;
            }
        }
    }

    private static void expandType(@NotNull PsiType type, @NotNull CompletionResultSet resultSet) {
        Collection<PsiVariantDeclaration> variants = type.getVariants();
        if (!variants.isEmpty()) {
            for (PsiVariantDeclaration variant : variants) {
                resultSet.addElement(
                        LookupElementBuilder.create(variant)
                                .withTypeText(type.getName())
                                .withIcon(PsiIconUtil.getProvidersIcon(variant, 0)));
            }
        }
    }

    private static void insertExpression(
            @NotNull InsertionContext insertionContext, @NotNull LookupElement element) {
        PsiElement psiElement = element.getPsiElement();
        if (psiElement instanceof PsiLet) {
            PsiLet let = (PsiLet) psiElement;
            if (let.isFunction()) {
                insertionContext.setAddCompletionChar(false);
                Editor editor = insertionContext.getEditor();
                EditorModificationUtil.insertStringAtCaret(editor, "()");
                editor.getCaretModel().moveToOffset(editor.getCaretModel().getOffset() - 1);
            }
        }
    }
}
