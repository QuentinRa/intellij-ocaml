package com.ocaml.ide.wizard.minor;

import com.intellij.ide.util.projectWizard.*;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.ValidationInfo;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.openapi.wm.impl.welcomeScreen.AbstractActionWithPanel;
import com.intellij.platform.DirectoryProjectGenerator;
import com.intellij.platform.DirectoryProjectGeneratorBase;
import com.intellij.platform.ProjectTemplate;
import com.ocaml.OCamlBundle;
import com.ocaml.icons.OCamlIcons;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

/**
 * Allow the creation of an OCaml project in minor IDEs.
 */
public class OCamlDirectoryProjectGenerator extends DirectoryProjectGeneratorBase<Object>
        implements CustomStepProjectGenerator<Object>, ProjectTemplate {

    // data

    @Override public @NotNull String getName() {
        return OCamlBundle.message("ocaml.project");
    }

    @Override public @Nullable @Nls(capitalization = Nls.Capitalization.Sentence) String getDescription() {
        return  OCamlBundle.message("ocaml.project.description");
    }

    @Override public Icon getIcon() {
        return OCamlIcons.Nodes.OCAML_MODULE;
    }

    @SuppressWarnings({"deprecation", "RedundantSuppression"})
    @Override public @Nullable ValidationInfo validateSettings() {
        return null;
    }

    @Override public @Nullable Icon getLogo() {
        return getIcon();
    }

    // Create

    @Override
    public AbstractActionWithPanel createStep(DirectoryProjectGenerator<Object> projectGenerator, AbstractNewProjectStep.AbstractCallback<Object> callback) {
        return new OCamlSimpleSettingsStep(projectGenerator, callback);
    }

    @Override public @NotNull AbstractModuleBuilder createModuleBuilder() {
        return new EmptyModuleBuilder();
    }

    @Override
    public void generateProject(@NotNull Project project, @NotNull VirtualFile baseDir, @NotNull Object settings, @NotNull Module module) {
        // ...
    }
}
