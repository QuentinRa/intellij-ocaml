package com.or.ide.go;

import com.intellij.navigation.ChooseByNameContributorEx;
import com.intellij.navigation.GotoClassContributor;
import com.intellij.navigation.ItemPresentation;
import com.intellij.navigation.NavigationItem;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiQualifiedNamedElement;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.psi.stubs.StubIndex;
import com.intellij.util.Processor;
import com.intellij.util.indexing.FindSymbolParameters;
import com.intellij.util.indexing.IdFilter;
import com.ocaml.icons.OCamlIcons;
import com.or.ide.files.FileBase;
import com.or.ide.search.PsiFinder;
import com.or.ide.search.index.IndexKeys;
import com.or.lang.core.ORFileType;
import com.or.lang.core.psi.PsiInnerModule;
import com.or.lang.core.psi.PsiModule;
import com.or.utils.Joiner;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

// Implements the goto class
public class ORModuleContributor implements GotoClassContributor, ChooseByNameContributorEx {
    @Override
    public void processNames(@NotNull Processor<? super String> processor, @NotNull GlobalSearchScope scope, @Nullable IdFilter filter) {
        Project project = scope.getProject();
        if (project != null) {
            StubIndex.getInstance().processAllKeys(IndexKeys.MODULES, project, processor);
        }
    }

    @Override
    public void processElementsWithName(@NotNull String name, @NotNull Processor<? super NavigationItem> processor, @NotNull FindSymbolParameters parameters) {
        Project project = parameters.getProject();

        for (PsiModule psiModule : project.getService(PsiFinder.class).findModulesbyName(name, ORFileType.both)) {
            NavigationItem element = psiModule;
            if (psiModule instanceof PsiInnerModule) {
                Icon icon = psiModule.isInterface() ? OCamlIcons.Nodes.INNER_MODULE_INTF : OCamlIcons.Nodes.INNER_MODULE;

                element = new ModuleDelegatePresentation(
                        psiModule,
                        new ItemPresentation() {
                            @Override
                            public @Nullable String getPresentableText() {
                                return psiModule.getName();
                            }

                            @Override
                            public String getLocationString() {
                                return Joiner.join(".", psiModule.getPath());
                            }

                            @Override
                            public Icon getIcon(boolean unused) {
                                return icon;
                            }
                        });
            }

            processor.process(element);
        }
    }

    @Override
    public @Nullable String getQualifiedName(NavigationItem item) {
        if (item instanceof FileBase) {
            return ((FileBase) item).getModuleName();
        } else if (item instanceof PsiQualifiedNamedElement) {
            return ((PsiQualifiedNamedElement) item).getQualifiedName();
        }
        return null;
    }

    @Override
    public @Nullable String getQualifiedNameSeparator() {
        return null;
    }
}
