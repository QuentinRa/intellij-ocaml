package com.ocaml.ide.projectWizard;

import com.intellij.ide.util.projectWizard.*;
import com.intellij.openapi.*;
import com.intellij.openapi.module.*;
import com.intellij.openapi.projectRoots.*;
import com.intellij.openapi.roots.*;
import com.intellij.openapi.util.io.*;
import com.intellij.openapi.vfs.*;
import com.ocaml.ide.module.*;
import com.ocaml.ide.projectWizard.view.*;
import com.ocaml.ide.sdk.*;
import org.jetbrains.annotations.*;

import java.io.*;
import java.util.*;

/**
 * Set up an OCaml module/project.
 *
 * ---- getWeight
 * You may use this to move this module
 * to the top in the list, if needed (ex: 100).
 *
 * -- getGroupName / getParentGroup
 * You may use this if this module is inside a group.
 */
public class OCamlModuleBuilder extends ModuleBuilder {

    @Override public ModuleType<OCamlModuleBuilder> getModuleType() {
        return new OCamlModuleType();
    }

    @Override public boolean isSuitableSdkType(SdkTypeId sdkType) {
        return sdkType instanceof OCamlSdkType;
    }

    public List<String> getSourcePaths() {
        final List<String> paths = new ArrayList<>();
        paths.add(getContentEntryPath() + File.separator + "src");
        return paths;
    }

    @Override public void setupRootModel(@NotNull ModifiableRootModel rootModel) {
        final CompilerModuleExtension compilerModuleExtension = rootModel.getModuleExtension(CompilerModuleExtension.class);
        compilerModuleExtension.setExcludeOutput(true);
        compilerModuleExtension.inheritCompilerOutputPath(true);

        if (myJdk != null) rootModel.setSdk(myJdk);
        else rootModel.inheritSdk();

        ContentEntry contentEntry = doAddContentEntry(rootModel);
        if (contentEntry != null) {
            final List<String> sourcePaths = getSourcePaths();

            if (sourcePaths != null) {
                for (final String sourcePath : sourcePaths) {
                    //noinspection ResultOfMethodCallIgnored
                    new File(sourcePath).mkdirs();
                    final VirtualFile sourceRoot = LocalFileSystem.getInstance()
                            .refreshAndFindFileByPath(FileUtil.toSystemIndependentName(sourcePath));
                    if (sourceRoot != null) {
                        contentEntry.addSourceFolder(sourceRoot, false, "");
                    }
                }
            }
        }
    }

    @Nullable @Override public ModuleWizardStep getCustomOptionsStep(WizardContext context, Disposable parentDisposable) {
        return new OCamlSdkWizardStep(context, this);
    }
}