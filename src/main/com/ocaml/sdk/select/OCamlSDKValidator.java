package com.ocaml.sdk.select;

import com.intellij.codeInsight.daemon.ProjectSdkSetupValidator;
import com.intellij.openapi.fileTypes.FileType;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.module.ModuleUtilCore;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.projectRoots.Sdk;
import com.intellij.openapi.roots.ModuleRootManager;
import com.intellij.openapi.roots.ProjectFileIndex;
import com.intellij.openapi.roots.ProjectRootManager;
import com.intellij.openapi.roots.ui.configuration.SdkPopupFactory;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.ui.EditorNotificationPanel;
import com.ocaml.ide.files.FileHelper;
import com.ocaml.sdk.OCamlSdkType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * If the user open a file (.ml/.mli => FileHelper.isOCaml) and the SDK isn't set,
 * then he/she will see a message to set up the SDK, along with a button to fix this error.
 */
public class OCamlSDKValidator implements ProjectSdkSetupValidator {

    public static final String CONFIGURE_OCAMLC_SDK = "Please configure the OCaml SDK";

    @Override public boolean isApplicableFor(@NotNull Project project, @NotNull VirtualFile file) {
        FileType fileType = file.getFileType();

        // check that the file is in the project
        if (FileHelper.isOCaml(fileType)) {
            ProjectFileIndex projectFileIndex = ProjectRootManager.getInstance(project).getFileIndex();
            return projectFileIndex.isInContent(file);
        }

        return false;
    }

    @Override public @Nullable String getErrorMessage(@NotNull Project project, @NotNull VirtualFile file) {
        // checking module SDK
        final Module module = ModuleUtilCore.findModuleForFile(file, project);
        if (module != null && !module.isDisposed()) {
            final Sdk sdk = ModuleRootManager.getInstance(module).getSdk();
            if (sdk != null && sdk.getSdkType() instanceof OCamlSdkType) {
                return null;
            }
        }
        // error, not OCaml SDK
        return CONFIGURE_OCAMLC_SDK;
    }

    @Override public EditorNotificationPanel.@NotNull ActionHandler getFixHandler(@NotNull Project project,
                                                                                  @NotNull VirtualFile file) {
        return SdkPopupFactory.newBuilder().withProject(project)
                .withSdkType(OCamlSdkType.getInstance())
                // module SDK or project SDK
                .updateSdkForFile(file)
                .buildEditorNotificationPanelHandler();
    }
}