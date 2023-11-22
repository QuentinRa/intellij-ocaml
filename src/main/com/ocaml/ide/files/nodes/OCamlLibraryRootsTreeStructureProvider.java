package com.ocaml.ide.files.nodes;

import com.intellij.ide.projectView.TreeStructureProvider;
import com.intellij.ide.projectView.ViewSettings;
import com.intellij.ide.projectView.impl.nodes.PsiDirectoryNode;
import com.intellij.ide.projectView.impl.nodes.PsiFileNode;
import com.intellij.ide.util.treeView.AbstractTreeNode;
import com.intellij.psi.PsiFile;
import com.ocaml.ide.files.OCamlFileType;
import com.ocaml.ide.files.OCamlInterfaceFileType;
import com.ocaml.sdk.utils.OCamlSdkRootsManager;
import com.ocaml.utils.MayNeedToBeTested;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Predicate;

/**
 * Hides files that are not ending with either .ml or .mli
 */
@MayNeedToBeTested
public class OCamlLibraryRootsTreeStructureProvider implements TreeStructureProvider {


    /**
     * This pattern is matching files ending with
     * <ul>
     *     <li>ending with .ml</li>
     *     <li>ending with .mli</li>
     * </ul>
     */
    private static final Predicate<String> ALLOWED_FILES =
            s -> s.endsWith(OCamlFileType.DOT_DEFAULT_EXTENSION) || s.endsWith(OCamlInterfaceFileType.DOT_DEFAULT_EXTENSION);

    /**
     * The folder may be nested, but still inside the SDK
     */
    private static boolean isLibraryRootForOCamlSdkRecursive(AbstractTreeNode<?> current) {
        if (current == null) return false;
        // check parent instead
        if (current instanceof PsiDirectoryNode) return isLibraryRootForOCamlSdkRecursive(current.getParent());
        return OCamlSdkRootsManager.isLibraryRootForOCamlSdk(current);
    }

    @Override
    public @NotNull Collection<AbstractTreeNode<?>> modify(@NotNull AbstractTreeNode<?> parent,
                                                           @NotNull Collection<AbstractTreeNode<?>> children,
                                                           ViewSettings settings) {
        if (!(parent instanceof PsiDirectoryNode)) return children;
        AbstractTreeNode<?> t = parent.getParent();
        if (!isLibraryRootForOCamlSdkRecursive(t)) return children;
        // we are now sure that children is a list of the folders/files in the SDK
        // we need to remove every file that isn't
        // - ending with .ml
        // - ending with .mli
        List<AbstractTreeNode<?>> filtered = new ArrayList<>();
        for (AbstractTreeNode<?> child : children) {
            // Directories allowed
            if (!(child instanceof PsiFileNode)) {
                filtered.add(child);
                continue;
            }
            // Filter files
            PsiFile psiFile = (PsiFile) child.getValue();
            String filename = psiFile.getName();
            if (ALLOWED_FILES.test(filename))
                filtered.add(child);
        }
        return filtered;
    }
}
