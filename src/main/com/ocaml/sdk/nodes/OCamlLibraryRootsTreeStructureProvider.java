package com.ocaml.sdk.nodes;

import com.intellij.ide.projectView.TreeStructureProvider;
import com.intellij.ide.projectView.ViewSettings;
import com.intellij.ide.projectView.impl.nodes.PsiDirectoryNode;
import com.intellij.ide.projectView.impl.nodes.PsiFileNode;
import com.intellij.ide.util.treeView.AbstractTreeNode;
import com.intellij.psi.PsiFile;
import com.ocaml.sdk.utils.OCamlSdkRootsManager;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.regex.Pattern;

/**
 * Hides files that are not ending with .ml, .mli, or .cmt
 */
public class OCamlLibraryRootsTreeStructureProvider implements TreeStructureProvider {

    // This pattern is matching files ending with
    // - ending with .ml
    // - ending with .mli
    // - ending with .cmt
    private static final Pattern ALLOWED_FILES = Pattern.compile(".*[.](ml|mli|cmt)");

    @Override
    public @NotNull Collection<AbstractTreeNode<?>> modify(@NotNull AbstractTreeNode<?> parent,
                                                           @NotNull Collection<AbstractTreeNode<?>> children,
                                                           ViewSettings settings) {
        if(!(parent instanceof PsiDirectoryNode)) return children;
        AbstractTreeNode<?> t = parent.getParent();
        if (!isLibraryRootForOCamlSdkRecursive(t)) return children;
        // we are now sure that children is a list of the folders/files in the SDK
        // we need to remove every file that isn't
        // - ending with .ml
        // - ending with .mli
        // - ending with .cmt
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
            if (ALLOWED_FILES.matcher(filename).matches())
                filtered.add(child);
        }
        return filtered;
    }

    /**
     * The folder may be nested, but still inside the SDK
     */
    private static boolean isLibraryRootForOCamlSdkRecursive(AbstractTreeNode<?> current) {
        if (current == null) return false;
        // check parent instead
        if (current instanceof PsiDirectoryNode) return isLibraryRootForOCamlSdkRecursive(current.getParent());
        return OCamlSdkRootsManager.isLibraryRootForOCamlSdk(current);
    }
}
