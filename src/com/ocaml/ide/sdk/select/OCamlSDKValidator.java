package com.ocaml.ide.sdk.select;

import com.intellij.codeInsight.daemon.*;
import com.intellij.openapi.fileTypes.*;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.module.*;
import com.intellij.openapi.project.*;
import com.intellij.openapi.projectRoots.*;
import com.intellij.openapi.roots.*;
import com.intellij.openapi.vfs.*;
import com.intellij.ui.*;
import com.reason.ide.files.*;
import com.ocaml.ide.sdk.*;
import org.jetbrains.annotations.*;

/**
 * If the user open a file (.ml/.mli => FileHelper.isOCaml) and the SDK isn't set,
 * then he/she will see a message to set up the SDK, along with a button to fix this error.
 *
 * @see SelectSDKAction
 */
@Deprecated(forRemoval = true)
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
        // checking current SDK
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

    @Override public EditorNotificationPanel.@NotNull ActionHandler getFixHandler(@NotNull Project project, @NotNull VirtualFile file) {
        return SelectSDKAction.newBuilder(project).buildEditorNotificationPanelHandler();
    }
}