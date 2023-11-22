package com.or.ide.insight.provider;

import com.intellij.codeInsight.completion.CompletionResultSet;
import com.intellij.codeInsight.lookup.LookupElementBuilder;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiNamedElement;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.util.PsiIconUtil;
import com.ocaml.icons.OCamlIcons;
import com.or.ide.files.FileBase;
import com.or.ide.search.FileModuleIndexService;
import com.or.ide.search.index.ModuleTopLevelIndex;
import com.or.lang.core.ORUtil;
import com.or.lang.core.psi.PsiInclude;
import com.or.lang.core.psi.PsiModule;
import com.or.lang.core.psi.PsiUpperSymbol;
import com.or.lang.core.psi.impl.PsiFakeModule;
import com.or.lang.core.psi.impl.PsiUpperIdentifier;
import com.or.lang.core.psi.reference.PsiUpperSymbolReference;
import com.or.utils.Log;
import com.or.utils.Platform;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class ModuleCompletionProvider {
    private static final Log LOG = Log.create("insight.module");

    private ModuleCompletionProvider() {
    }

    public static void addCompletions(@NotNull PsiElement element, @NotNull CompletionResultSet resultSet) {
        LOG.debug("MODULE expression completion");

        Project project = element.getProject();
        GlobalSearchScope scope = GlobalSearchScope.allScope(project);
        PsiElement dotLeaf = PsiTreeUtil.prevVisibleLeaf(element);
        PsiElement previousElement = dotLeaf == null ? null : dotLeaf.getPrevSibling();

        final Collection<PsiElement> expressions = new ArrayList<>();

        if (previousElement instanceof PsiUpperSymbol) {
            LOG.debug(" -> upper symbol", previousElement);

            PsiUpperSymbolReference reference = (PsiUpperSymbolReference) previousElement.getReference();
            PsiElement resolvedElement = reference == null ? null : reference.resolveInterface();
            LOG.debug(" -> resolved to", resolvedElement);

            if (resolvedElement instanceof PsiUpperIdentifier) {
                PsiElement resolvedParent = resolvedElement.getParent();
                if (resolvedParent instanceof PsiModule) {
                    expressions.addAll(getInnerModules((PsiModule) resolvedParent));
                }
            } else if (resolvedElement instanceof FileBase) {
                expressions.addAll(getFileModules((FileBase) resolvedElement));
            }
        } else {
            // empty path

            // First module to complete, use the list of files
            ModuleTopLevelIndex.processModules(project, scope, fakeModule -> {
                FileBase topFile = (FileBase) fakeModule.getContainingFile();
                if (!topFile.equals(element.getContainingFile())) {
                    expressions.add(topFile);
                }
            });

            // Add virtual namespaces
            Collection<String> namespaces = FileModuleIndexService.getService().getNamespaces(project);
            LOG.debug("  namespaces", namespaces);

            for (String namespace : namespaces) {
                resultSet.addElement(
                        LookupElementBuilder.create(namespace)
                                .withTypeText("Generated namespace")
                                .withIcon(OCamlIcons.Nodes.VIRTUAL_NAMESPACE));
            }
        }

        if (expressions.isEmpty()) {
            LOG.trace(" -> no expressions found");
        } else {
            LOG.trace(" -> expressions", expressions);
            for (PsiElement expression : expressions) {
                if (expression instanceof FileBase) {
                    FileBase topFile = (FileBase) expression;
                    resultSet.addElement(LookupElementBuilder.
                            create(topFile.getModuleName())
                            .withTypeText(Platform.shortLocation(topFile))
                            .withIcon(PsiIconUtil.getProvidersIcon(expression, 0)));
                } else if (expression instanceof PsiNamedElement && !(expression instanceof PsiFakeModule)) {
                    String name = ((PsiNamedElement) expression).getName();
                    resultSet.addElement(LookupElementBuilder.
                            create(name == null ? "unknown" : name)
                            .withIcon(PsiIconUtil.getProvidersIcon(expression, 0)));
                }
            }
        }
    }

    private static @NotNull Collection<? extends PsiModule> getModules(@Nullable PsiElement element) {
        if (element instanceof PsiModule) {
            return getInnerModules((PsiModule) element);
        }
        if (element instanceof FileBase) {
            return getFileModules((FileBase) element);
        }
        return Collections.emptyList();
    }

    private static @NotNull Collection<PsiModule> getInnerModules(@NotNull PsiModule module) {
        List<PsiModule> result = new ArrayList<>();

        if (module.getAlias() != null) {
            PsiElement resolvedAlias = ORUtil.resolveModuleSymbol(module.getAliasSymbol());
            result.addAll(getModules(resolvedAlias));
        } else {
            PsiElement content = ORUtil.getModuleContent(module);

            List<PsiInclude> includes = PsiTreeUtil.getStubChildrenOfTypeAsList(content, PsiInclude.class);
            for (PsiInclude include : includes) {
                PsiElement includedModule = ORUtil.resolveModuleSymbol(include.getModuleReference());
                if (includedModule != null) {
                    result.addAll(getModules(includedModule));
                }
            }

            result.addAll(PsiTreeUtil.getStubChildrenOfTypeAsList(content, PsiModule.class));
        }

        return result;
    }

    private static @NotNull List<PsiModule> getFileModules(@NotNull FileBase element) {
        List<PsiModule> result = new ArrayList<>();

        List<PsiInclude> includes = PsiTreeUtil.getStubChildrenOfTypeAsList(element, PsiInclude.class);
        for (PsiInclude include : includes) {
            PsiElement includedModule = ORUtil.resolveModuleSymbol(include.getModuleReference());
            if (includedModule != null) {
                result.addAll(getModules(includedModule));
            }
        }

        result.addAll(PsiTreeUtil.getStubChildrenOfTypeAsList(element, PsiModule.class));

        return result;
    }
}
