package com.ocaml.ide.wizard.minor.settings;

import com.intellij.openapi.Disposable;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.options.Configurable;
import com.intellij.openapi.options.ConfigurationException;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.projectRoots.Sdk;
import com.intellij.openapi.projectRoots.SdkTypeId;
import com.intellij.openapi.roots.ProjectRootManager;
import com.intellij.openapi.roots.ui.configuration.projectRoot.ProjectSdksModel;
import com.intellij.openapi.util.Condition;
import com.intellij.openapi.util.Disposer;
import com.ocaml.OCamlBundle;
import com.ocaml.ide.settings.OCamlSettings;
import com.ocaml.ide.wizard.minor.java.OCamlSdkComboBox;
import com.ocaml.sdk.OCamlSdkType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.io.File;

public class OCamlProjectConfigurable implements Configurable {
    @NotNull private final Project myProject;

    private final Disposable fakeDisposable = () -> {};

    // components
    private JPanel myMainPanel;
    private OCamlSdkComboBox myOCamlSdkComboBox;
    private JTextField myCompilerOutput;
    // saves
    private Sdk myProjectSdk;

    private final OCamlSettings mySettings;

    public OCamlProjectConfigurable(@NotNull Project project) {
        this.myProject = project;

        mySettings = OCamlSettings.getInstance(project);

        // On File selected
        myCompilerOutput.setText(mySettings.outputFolderName);
    }

    @Override public String getDisplayName() {
        return OCamlBundle.message("ocaml.project.settings", myProject.getName().replaceAll("\n", " "));
    }

    @Override public @Nullable JComponent createComponent() {
        return myMainPanel;
    }

    @Override public boolean isModified() {
        Sdk selectedSdk = myOCamlSdkComboBox.getSelectedSdk();
        boolean sdkSame = (selectedSdk == null && myProjectSdk == null)
                || (selectedSdk != null && myProjectSdk != null &&
                selectedSdk.getName().equals(myProjectSdk.getName()));
        boolean locationSame = myCompilerOutput.getText().equals(mySettings.outputFolderName);
        return !sdkSame || !locationSame;
    }

    @Override public void apply() {
        ProjectRootManager instance = ProjectRootManager.getInstance(myProject);

        myProjectSdk = myOCamlSdkComboBox.getSelectedSdk();
        ApplicationManager
                .getApplication()
                .runWriteAction(() -> instance.setProjectSdk(myProjectSdk));

        mySettings.outputFolderName = myCompilerOutput.getText();
    }

    // create components, needed by the ".form" since some must be created manually.
    // called somewhat in the middle/after of the constructor
    public void createUIComponents() {
        Condition<? super SdkTypeId> sdkTypeFilter = sdk -> sdk instanceof OCamlSdkType;

        // get sdk model
        ProjectSdksModel sdksModel = new ProjectSdksModel();
        sdksModel.reset(myProject);
        myProjectSdk = ProjectRootManager.getInstance(myProject).getProjectSdk();

        // combobox
        myOCamlSdkComboBox = new OCamlSdkComboBox(myProject,
                sdksModel,
                sdkTypeFilter,
                null,
                null,
                null);
        // we are not disable, so we need to fake
        myOCamlSdkComboBox.setParentDisposable(fakeDisposable);
        myOCamlSdkComboBox.setSelectedJdk(myProjectSdk);
        // todo: edit button
    }

    @Override public void disposeUIResources() {
        Configurable.super.disposeUIResources();
        Disposer.dispose(fakeDisposable);
    }
}
