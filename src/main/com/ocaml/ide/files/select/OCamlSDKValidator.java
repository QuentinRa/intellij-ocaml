package com.ocaml.ide.files.select;

import com.intellij.codeInsight.daemon.ProjectSdkSetupValidator;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.projectRoots.Sdk;
import com.intellij.openapi.roots.ui.configuration.SdkPopupFactory;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.ui.EditorNotificationPanel;
import com.ocaml.OCamlBundle;
import com.ocaml.ide.files.FileHelper;
import com.ocaml.sdk.OCamlSdkType;
import com.ocaml.utils.files.OCamlProjectFilesUtils;
import com.ocaml.utils.sdk.OCamlSdkUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * If the user open a file (.ml/.mli => FileHelper.isOCaml) and the SDK isn't set,
 * then he/she will see a message to set up the SDK, along with a button to fix this error.
 */
public class OCamlSDKValidator implements ProjectSdkSetupValidator {

    public static final String CONFIGURE_OCAMLC_SDK = OCamlBundle.message("sdk.ask.configure");

    @Override public boolean isApplicableFor(@NotNull Project project, @NotNull VirtualFile file) {
        // check that the file is in the project
        return FileHelper.isOCaml(file) && OCamlProjectFilesUtils.isInProject(project, file);
    }

    @Override public @Nullable String getErrorMessage(@NotNull Project project, @NotNull VirtualFile file) {
        // checking module SDK
        Sdk sdk = OCamlSdkUtils.getModuleSdkForFile(project, file);
        if (sdk != null) return null; // we got one, no error

        // error, not OCaml SDK
        return CONFIGURE_OCAMLC_SDK;
    }

    @Override public EditorNotificationPanel.ActionHandler getFixHandler(@NotNull Project project,
                                                                         @NotNull VirtualFile file) {
        return SdkPopupFactory.newBuilder().withProject(project)
                .withSdkType(OCamlSdkType.getInstance())
                // module SDK or project SDK
                .updateSdkForFile(file)
                .buildEditorNotificationPanelHandler();
    }
}