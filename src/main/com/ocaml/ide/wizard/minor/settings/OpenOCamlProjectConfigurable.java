package com.ocaml.ide.wizard.minor.settings;

import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.options.ex.SingleConfigurableEditor;
import com.intellij.openapi.options.newEditor.SettingsDialog;
import com.intellij.openapi.project.DumbAwareAction;
import com.intellij.openapi.project.Project;
import com.ocaml.OCamlBundle;
import com.ocaml.icons.OCamlIcons;
import com.ocaml.utils.OCamlPlatformUtils;
import org.jetbrains.annotations.NotNull;

// DumbAwareAction => shown during indexing / ...
public class OpenOCamlProjectConfigurable extends DumbAwareAction {

    private final boolean shouldEnable;

    public OpenOCamlProjectConfigurable() {
        super(OCamlBundle.message("ocaml.project.configuration"), "", OCamlIcons.Nodes.OCAML_SDK);
        // only for non-IntelliJ users
        shouldEnable = !OCamlPlatformUtils.isJavaPluginAvailable();
    }

    // ...
    // doing this in the constructor did nothing
    // ...
    @Override public void update(@NotNull AnActionEvent e) {
        super.update(e);
        e.getPresentation().setEnabledAndVisible(shouldEnable);
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

