package com.ocaml.utils;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.roots.CompilerModuleExtension;
import com.intellij.openapi.roots.ModuleRootManager;
import com.intellij.openapi.vfs.pointers.VirtualFilePointer;
import com.ocaml.ide.settings.OCamlSettings;
import org.jetbrains.annotations.NotNull;

import java.util.function.Supplier;

public final class OCamlPlatformUtils {

    private static Boolean isJavaAvailable;

    public static boolean isJavaPluginAvailable() {
        if (isJavaAvailable == null) {
            try {
                Class.forName("com.intellij.ide.highlighter.JavaFileType");
                isJavaAvailable = true;
            } catch (ClassNotFoundException e) {
                isJavaAvailable = false;
            }
        }
        return isJavaAvailable;
    }

    @NotNull public static String findOutputFolder(ModuleRootManager moduleRootManager, Project project) {
        return findOutputFolder(moduleRootManager, project, project::getBasePath);
    }

    @NotNull public static String findOutputFolder(ModuleRootManager moduleRootManager, Project project,
                                                   Supplier<String> rootFolder) {
        if (OCamlPlatformUtils.isJavaPluginAvailable()) {
            // output folder
            CompilerModuleExtension compilerModuleExtension = moduleRootManager.getModuleExtension(CompilerModuleExtension.class);
            VirtualFilePointer outputPointer = compilerModuleExtension.getCompilerOutputPointer();
            return outputPointer.getPresentableUrl() + "/";
        } else {
            // get outputFolder
            String outputFolderName = project.getService(OCamlSettings.class).outputFolderName;
            String basePath = rootFolder.get();
            return basePath + "/" + outputFolderName;
        }
    }
}
