package com.or.ide.search;

import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiElement;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.psi.stubs.StubIndex;
import com.intellij.util.CommonProcessors;
import com.or.ide.search.index.IndexKeys;
import com.or.ide.search.index.ModuleFqnIndex;
import com.or.lang.core.ORFileType;
import com.or.lang.core.ORUtil;
import com.or.lang.core.psi.PsiInnerModule;
import com.or.lang.core.psi.PsiModule;
import com.or.lang.core.psi.PsiQualifiedPathElement;
import com.or.lang.core.psi.impl.PsiFunctorCall;
import com.or.lang.utils.QNameFinder;
import com.or.lang.utils.QNameFinderFactory;
import com.or.utils.Joiner;
import com.or.utils.Log;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static com.or.lang.core.ORFileType.*;

public final class PsiFinder {
    private static final Log LOG = Log.create("finder");
    private final Project myProject;

    public PsiFinder(@NotNull Project project) {
        myProject = project;
    }

    public @Nullable PsiQualifiedPathElement findModuleBack(@Nullable PsiElement root, @Nullable String path) {
        if (root != null && path != null) {
            PsiElement prev = ORUtil.prevSibling(root);
            PsiElement item = prev == null ? root.getParent() : prev;
            while (item != null) {
                if (item instanceof PsiInnerModule) {
                    PsiInnerModule module = (PsiInnerModule) item;
                    String name = module.getModuleName();
                    String alias = module.getAlias();
                    if (alias != null) {
                        // This is a local module alias, we'll need to replace it in final paths
                        Pattern compile = Pattern.compile("(\\.?)(" + name + ")(\\.?)");
                        String replace = "$1" + alias + "$3";
                        path = compile.matcher(path).replaceFirst(replace);
                    } else if (path.equals(name)) {
                        return module;
                    } else if (name != null && path.startsWith(name)) {
                        // Follow module from top to bottom to find real module
                        path = path.substring(name.length() + 1);
                        return findModuleForward(module.getBody(), path);
                    }
                }
                prev = ORUtil.prevSibling(item);
                item = prev == null ? item.getParent() : prev;
            }
        }

        return null;
    }

    private @Nullable PsiQualifiedPathElement findModuleForward(@Nullable PsiElement root, @Nullable String path) {
        if (root != null && path != null) {
            PsiElement next = ORUtil.nextSibling(root);
            PsiElement item = next == null ? root.getFirstChild() : next;
            while (item != null) {
                if (item instanceof PsiInnerModule) {
                    PsiInnerModule module = (PsiInnerModule) item;
                    String name = module.getModuleName();
                    if (path.equals(name)) {
                        return module;
                    } else if (name != null && path.startsWith(name)) {
                        // Go deeper
                        path = path.substring(name.length() + 1);
                        return findModuleForward(module.getBody(), path);
                    }
                }
                next = ORUtil.nextSibling(item);
                item = next == null ? item.getFirstChild() : next;
            }
        }

        return null;
    }

    public @NotNull Set<PsiModule> findModulesbyName(@NotNull String name, @NotNull ORFileType fileType) {
        Set<PsiModule> result = new HashSet<>();

        StubIndex.getInstance().processAllKeys(IndexKeys.MODULES,
                myProject,
                CommonProcessors.processAll(
                        moduleName -> {
                            if (name.equals(moduleName)) {
                                PartitionedModules partitionedModules =
                                        new PartitionedModules();

                                if (fileType == interfaceOrImplementation
                                        || fileType == both
                                        || fileType == interfaceOnly) {
                                    result.addAll(partitionedModules.getInterfaces());
                                }

                                if (fileType != interfaceOnly) {
                                    if (fileType == both
                                            || fileType == implementationOnly
                                            || !partitionedModules.hasInterfaces()) {
                                        result.addAll(partitionedModules.getImplementations());
                                    }
                                }
                            }
                        }));

        if (LOG.isTraceEnabled()) {
            LOG.trace(
                    "  modules "
                            + name
                            + " (found "
                            + result.size()
                            + "): "
                            + Joiner.join(
                            ", ", result.stream().map(PsiModule::getName).collect(Collectors.toList())));
        }

        return result;
    }

    @NotNull
    public Set<PsiModule> findModuleAlias(@Nullable String qname) {
        if (qname == null) {
            return Collections.emptySet();
        }

        Set<PsiModule> result = new HashSet<>();

        GlobalSearchScope scope = GlobalSearchScope.allScope(myProject);
        Collection<PsiModule> psiModules = ModuleFqnIndex.getElements(qname, myProject, scope);
        for (PsiModule module : psiModules) {
            String alias = module.getAlias();
            if (alias != null) {
                Collection<PsiModule> aliasModules = ModuleFqnIndex.getElements(alias, myProject, scope);
                if (!aliasModules.isEmpty()) {
                    for (PsiModule aliasModule : aliasModules) {
                        Set<PsiModule> nextModuleAlias = findModuleAlias(aliasModule.getQualifiedName());
                        if (nextModuleAlias.isEmpty()) {
                            result.add(aliasModule);
                        } else {
                            result.addAll(nextModuleAlias);
                        }
                    }
                }
            }
        }

        return result;
    }

    @NotNull
    public Set<PsiModule> findModulesFromQn(@Nullable String qname, boolean resolveAlias, @NotNull ORFileType fileType) {
        if (qname == null) {
            return Collections.emptySet();
        }

        Set<PsiModule> result = new HashSet<>();
        GlobalSearchScope scope = GlobalSearchScope.allScope(myProject);

        // Try qn directly
        Collection<PsiModule> modules = ModuleFqnIndex.getElements(qname, myProject, scope);

        if (modules.isEmpty()) {
            // Qn not working, maybe because of aliases... try to navigate to each module
            String[] names = qname.split("\\.");

            // extract first token of path
            Set<PsiModule> firstModules =
                    findModulesbyName(names[0], interfaceOrImplementation);
            PsiModule firstModule = firstModules.isEmpty() ? null : firstModules.iterator().next();

            Set<PsiModule> firstModuleAliases =
                    findModuleAlias(firstModule == null ? null : firstModule.getQualifiedName());
            PsiModule currentModule =
                    firstModuleAliases.isEmpty() ? firstModule : firstModuleAliases.iterator().next();
            if (currentModule != null) {
                for (int i = 1; i < names.length; i++) {
                    if (currentModule == null) {
                        break;
                    }
                    currentModule = currentModule.getModuleExpression(names[i]);
                    String alias = currentModule == null ? null : currentModule.getAlias();
                    if (alias != null) {
                        Set<PsiModule> modulesByName = findModulesbyName(alias, fileType);
                        if (!modulesByName.isEmpty()) {
                            currentModule = modulesByName.iterator().next();
                        }
                    }
                }
            }

            if (currentModule != null) {
                result.add(currentModule);
            }
        } else {
            // Qn returned something
            for (PsiModule module : modules) {
                String alias = resolveAlias ? module.getAlias() : null;
                if (alias == null) {
                    // It's not an alias, but maybe it's a functor call that we must resolve if asked
                    PsiFunctorCall functorCall = module.getFunctorCall();
                    if (resolveAlias && functorCall != null) {
                        String functorName = functorCall.getFunctorName();
                        Set<PsiModule> modulesFromFunctor = null;

                        QNameFinder qnameFinder = QNameFinderFactory.getQNameFinder();
                        Set<String> potentialPaths = qnameFinder.extractPotentialPaths(functorCall);
                        for (String path : potentialPaths) {
                            modulesFromFunctor =
                                    findModulesFromQn(path + "." + functorName, true, fileType);
                        }
                        if (modulesFromFunctor == null || modulesFromFunctor.isEmpty()) {
                            modulesFromFunctor = findModulesFromQn(functorName, true, fileType);
                        }

                        if (modulesFromFunctor.isEmpty()) {
                            result.add(module);
                        } else {
                            result.addAll(modulesFromFunctor);
                        }
                    } else {
                        result.add(module);
                    }
                } else {
                    result.addAll(findModulesFromQn(alias, true, fileType));
                }
            }
        }

        return result;
    }

    static class PartitionedModules {
        private final List<PsiModule> m_interfaces = new ArrayList<>();
        private final List<PsiModule> m_implementations = new ArrayList<>();

        PartitionedModules() {
        }

        public boolean hasInterfaces() {
            return !m_interfaces.isEmpty();
        }

        public @NotNull List<PsiModule> getInterfaces() {
            return m_interfaces;
        }

        public @NotNull List<PsiModule> getImplementations() {
            return m_implementations;
        }
    }
}
