package com.ocaml.ide.wizard.minor.settings;

import com.intellij.openapi.options.Configurable;
import com.intellij.openapi.options.ConfigurationException;
import com.intellij.openapi.project.Project;
import com.ocaml.OCamlBundle;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

//    public static @NotNull SdkPopupBuilder newBuilder(Project project) {
//        return SdkPopupFactory.newBuilder().withProject(project)
//                .withSdkType(OCamlSdkType.getInstance())
//                .updateProjectSdkFromSelection();
//    }
public class OCamlProjectConfigurable implements Configurable {
    @NotNull private final Project myProject;

    public OCamlProjectConfigurable(@NotNull Project project) {
        this.myProject = project;
    }

    @Override public String getDisplayName() {
        return OCamlBundle.message("ocaml.project.settings", myProject.getName().replaceAll("\n", " "));
    }

    @Override public @Nullable JComponent createComponent() {
        return new JLabel("Same key");
    }

    @Override public boolean isModified() {
        return false;
    }

    @Override public void apply() throws ConfigurationException {
        System.out.println("apply changes");
    }
}
