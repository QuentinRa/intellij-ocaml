package com.or.utils;

import com.intellij.openapi.module.Module;
import com.intellij.openapi.module.ModuleUtil;
import com.intellij.openapi.roots.ModuleRootManagerEx;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiFile;
import org.jetbrains.annotations.NotNull;

public final class Platform {

    private Platform() {
    }

    @NotNull
    public static String shortLocation(@NotNull PsiFile file) {
        String newPath = Platform.getRelativePathToModule(file);
        int pos = newPath.lastIndexOf("/");
        return 0 < pos ? newPath.substring(0, pos) : newPath;
    }

    public static @NotNull String getRelativePathToModule(@NotNull PsiFile file) {
        VirtualFile virtualFile = file.getVirtualFile();
        String relativePath = virtualFile == null ? file.getName() : virtualFile.getPath();

        Module module = ModuleUtil.findModuleForFile(file);
        if (module != null) {
            String fileName = file.getName();
            ModuleRootManagerEx moduleRootManager = ModuleRootManagerEx.getInstanceEx(module);
            for (VirtualFile sourceRoot : moduleRootManager.getSourceRoots()) {
                VirtualFile child = sourceRoot.findChild(fileName);
                if (child != null) {
                    relativePath = child.getPath().replace(sourceRoot.getPath(), sourceRoot.getName());
                    break;
                }
            }
        }

        return relativePath;
    }

}
