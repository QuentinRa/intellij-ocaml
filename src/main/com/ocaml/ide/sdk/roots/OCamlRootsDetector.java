package com.ocaml.ide.sdk.roots;

import com.intellij.openapi.progress.*;
import com.intellij.openapi.vfs.*;
import com.intellij.util.containers.*;
import com.ocaml.compiler.*;
import org.jetbrains.annotations.*;

import java.util.*;

/**
 * Explore a folder and returns the roots.
 */
public class OCamlRootsDetector {

    public static Collection<VirtualFile> suggestOCamlRoots(VirtualFile rootCandidate) {
        return suggestOCamlRoots(rootCandidate, new EmptyProgressIndicator());
    }

    @NotNull
    public static List<VirtualFile> suggestOCamlRoots(@NotNull VirtualFile dir, @NotNull ProgressIndicator progressIndicator) {
        if (!dir.isDirectory()) {
            return ContainerUtil.emptyList();
        }

        final ArrayList<VirtualFile> foundDirectories = new ArrayList<>();

        VfsUtilCore.visitChildrenRecursively(
                dir,
                new VirtualFileVisitor<VirtualFile>() {
                    @Override
                    public @NotNull Result visitFileEx(@NotNull VirtualFile file) {
                        progressIndicator.checkCanceled();

                        if (!file.isDirectory()) {
                            if (file.getName().endsWith(OCamlConstants.FILE_EXTENSION) || file.getName().endsWith(OCamlConstants.FILE_INTERFACE_EXTENSION)) {
                                VirtualFile root = file.getParent();
                                if (root != null) {
                                    // unneeded
                                    if (!root.getPath().contains("testsuite")) {
                                        foundDirectories.add(root);
                                    }
                                    return skipTo(root);
                                }
                            }
                        }
                        return CONTINUE;
                    }
                });

        return foundDirectories;
    }
}
