package com.ocaml.ide.wizard.minor.settings;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.options.ex.SingleConfigurableEditor;
import com.intellij.openapi.options.newEditor.SettingsDialog;
import com.intellij.openapi.project.Project;
import com.ocaml.OCamlBundle;
import com.ocaml.icons.OCamlIcons;
import com.ocaml.utils.OCamlPlatformUtils;
import org.jetbrains.annotations.NotNull;

public class OpenOCamlProjectConfigurable extends AnAction {

    public OpenOCamlProjectConfigurable() {
        super(OCamlBundle.message("ocaml.project.configuration"), "", OCamlIcons.Nodes.OCAML_SDK);
        // only for non-IntelliJ users
        getTemplatePresentation().setEnabledAndVisible(
                !OCamlPlatformUtils.isJavaPluginAvailable()
        );
    }

    public void actionPerformed(@NotNull AnActionEvent e) {
        final Project project = e.getProject();
        if (project == null) return;

        // show the project configurable
        new SingleConfigurableEditor(project, new OCamlProjectConfigurable(project), SettingsDialog.DIMENSION_KEY) {
            @NotNull @Override protected DialogStyle getStyle() {
                return DialogStyle.COMPACT;
            }
        }.show();
    }
}

