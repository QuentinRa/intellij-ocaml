package com.ocaml.ide.module.select;

import com.intellij.icons.AllIcons;
import com.intellij.openapi.actionSystem.*;
import com.intellij.openapi.project.*;
import com.intellij.openapi.roots.ui.configuration.*;
import com.intellij.openapi.ui.popup.*;
import com.ocaml.sdk.OCamlSdkType;
import org.jetbrains.annotations.*;

import java.awt.event.*;

/**
 * This class is using SdkPopupFactory to create
 * the popup to select the version of OCaml. Everything related to the download or
 * the detected SDK is loaded from OCamlSDKType. Here
 * - I'm filtering so that only OCaml SDK are printed
 * - I'm using SdkPopupFactory so the project is automatically reloaded with the new SDK
 */
public class SelectSDKAction extends AnAction {

    private JBPopup myPopup;

    public SelectSDKAction() {
        super("Select OCaml SDK", "Set an OCaml SDK as the project SDK", AllIcons.General.AddJdk);
        this.setEnabledInModalContext(true);
    }

    public static @NotNull SdkPopupBuilder newBuilder(Project project) {
        return SdkPopupFactory.newBuilder().withProject(project)
                .withSdkType(OCamlSdkType.getInstance())
                .updateProjectSdkFromSelection();
    }

    public void actionPerformed(@NotNull AnActionEvent e) {
        final Project project = e.getProject();
        if (project != null) {
            if (myPopup != null && myPopup.isVisible()) {
                myPopup.cancel();
            } else {
                // show the popup, next to build, run, ...
                // or in the middle of the screen if there is no event
                myPopup = newBuilder(project).buildPopup();

                InputEvent event = e.getInputEvent();
                if (event != null) {
                    myPopup.showUnderneathOf(e.getInputEvent().getComponent());
                } else {
                    myPopup.showCenteredInCurrentWindow(project);
                }
            }
        }
    }
}

