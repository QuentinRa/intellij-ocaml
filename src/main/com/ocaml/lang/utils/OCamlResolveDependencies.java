package com.ocaml.lang.utils;

import com.intellij.openapi.roots.ModuleRootManager;
import com.intellij.openapi.util.Pair;
import com.intellij.openapi.vfs.VfsUtil;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiManager;
import com.intellij.psi.PsiWhiteSpace;
import com.ocaml.ide.files.OCamlFileType;
import com.ocaml.ide.files.OCamlInterfaceFileType;
import com.or.lang.OCamlTypes;
import com.or.lang.core.psi.PsiInclude;
import com.or.lang.core.psi.PsiOpen;
import com.or.lang.core.psi.PsiUpperSymbol;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class OCamlResolveDependencies {

    /**
     * Return the name of the files that are explicit dependencies of this file, without
     * their extensions (it's up to the caller to find them, and pick either the .ml or the .mli)
     * @param element at first, it must be a file, then it's the element that we are exploring. It's working
     *                if the element isn't a file, but it also means that only the dependencies starting
     *                from this element, will be added in the set.
     * @return a set of file names without extensions, that are the explicit dependencies of this file
     */
    public static @NotNull Set<String> findDependencies(@NotNull PsiElement element) {
        Set<String> res = new HashSet<>();
        for (PsiElement child : element.getChildren()) {
            if (child instanceof PsiWhiteSpace) continue;

            // open ModuleName
            if (child instanceof PsiOpen) {
                res.add(((PsiOpen) child).getPath().toLowerCase());
            }

            if (child instanceof PsiInclude) {
                res.add(((PsiInclude) child).getIncludePath().toLowerCase());
            }

            // child instanceof PsiLocalOpen?

            // ModuleName.function
            if (child instanceof PsiUpperSymbol && OCamlPsiUtils.isNextMeaningfulNextSibling(child, OCamlTypes.DOT)) {
                res.add(child.getText().toLowerCase());
            }
            // next
            res.addAll(findDependencies(child));
        }
        return res;
    }

    /**
     * @param file              the file for which we need the dependencies
     * @param moduleRootManager used to get the content roots
     * @return a set of paths and the psiFile, that are the dependencies of the given file
     */
    public static Set<Pair<String, PsiFile>> resolveForFile(PsiFile file, ModuleRootManager moduleRootManager) {
        Set<String> dependencies = findDependencies(file);
        // small optimisation
        if (dependencies.isEmpty()) return Set.of();

        Set<Pair<String, VirtualFile>> deps = new HashSet<>();
        // look into every source folder
        HashMap<String, Pair<String, VirtualFile>> potential = new HashMap<>();

        // may not be in a source folder
        for (VirtualFile root : moduleRootManager.getContentRoots()) {
            exploreFolder(root, root, potential, deps, dependencies);
        }
        deps.addAll(potential.values());
        // there is no dependency graph
        // they should be sorted by dependency
        // is it needed trough? (we are using -c)
        Set<Pair<String, PsiFile>> psiDeps = new HashSet<>();
        for (Pair<String, VirtualFile> dep : deps) {
            PsiFile psiFile = PsiManager.getInstance(file.getProject()).findFile(dep.second);
            if (psiFile == null) continue;
            psiDeps.add(new Pair<>(dep.first, psiFile));
            psiDeps.addAll(resolveForFile(psiFile, moduleRootManager));
        }
        return psiDeps;
    }

    private static void exploreFolder(@NotNull VirtualFile f, VirtualFile originalRoot, HashMap<String, Pair<String, VirtualFile>> potential, Set<Pair<String, VirtualFile>> deps, Set<String> dependencies) {
        if (f.isDirectory()) {
            for (VirtualFile root : VfsUtil.getChildren(f)) {
                exploreFolder(root, originalRoot, potential, deps, dependencies);
            }
        } else {
            // is in the list of dependencies?
            if (!dependencies.contains(f.getNameWithoutExtension())) return;
            // yes
            // fix 98: using the parent of the root
            Pair<String, VirtualFile> pair = new Pair<>(VfsUtil.getRelativePath(f, originalRoot), f);
            // do we have the .mli?
            if (OCamlInterfaceFileType.isFile(f.getPath())) {
                String s = OCamlFileType.fromInterface(f.getPath());
                // remove the source
                potential.remove(s);
                // we are adding the interface instead
                deps.add(pair);
            } else if (OCamlFileType.isFile(f.getPath())) {
                // ensured that we do not have the .mli already in
                // (should not happen since they are sorted alphabetically but, ...)
                potential.put(f.getPath(), pair);
            }
        }
    }
}
