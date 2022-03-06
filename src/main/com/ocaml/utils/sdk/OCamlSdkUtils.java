package com.ocaml.utils.sdk;

import com.intellij.openapi.compiler.CompilerManager;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.module.ModuleUtilCore;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.projectRoots.Sdk;
import com.intellij.openapi.roots.ModuleRootManager;
import com.intellij.openapi.roots.ProjectFileIndex;
import com.intellij.openapi.vfs.VirtualFile;
import com.ocaml.sdk.OCamlSdkType;
import com.ocaml.utils.adaptor.RequireJavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@RequireJavaPlugin(what = "CompilerManager")
public class OCamlSdkUtils {

    /** Get the SDK for the module which contains this file **/
    public static @Nullable Sdk getModuleSdkForFile(@NotNull Project project, @NotNull VirtualFile file) {
        final Module module = ModuleUtilCore.findModuleForFile(file, project);
        if (module != null && !module.isDisposed()) {
            final Sdk sdk = ModuleRootManager.getInstance(module).getSdk();
            if (sdk != null && sdk.getSdkType() instanceof OCamlSdkType)
                return sdk;
        }
        return null;
    }

    @SuppressWarnings("unused")
    public static boolean isInSource(Project project, VirtualFile file) {
        // not excluded, nor ignored, and in a source folder
        return ProjectFileIndex.getInstance(project).isInSourceContent(file);

    }

    /** is not in a folder marked as "excluded" **/
    public static boolean isNotExcluded(Project project, VirtualFile virtualFile) {
        return !CompilerManager.getInstance(project).isExcludedFromCompilation(virtualFile);
    }
}
