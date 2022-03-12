package com.ocaml.ide.wizard.minor.settings;

import com.intellij.openapi.Disposable;
import com.intellij.openapi.application.ApplicationBundle;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.options.Configurable;
import com.intellij.openapi.options.ConfigurationException;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.projectRoots.Sdk;
import com.intellij.openapi.roots.ProjectRootManager;
import com.intellij.openapi.roots.ui.configuration.projectRoot.ProjectSdksModel;
import com.intellij.openapi.util.Disposer;
import com.ocaml.OCamlBundle;
import com.ocaml.ide.settings.OCamlSettings;
import com.ocaml.ide.wizard.minor.java.OCamlSdkComboBox;
import com.ocaml.ide.wizard.minor.settings.java.SdkListConfigurable;
import com.ocaml.sdk.OCamlSdkType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.awt.*;
import java.util.concurrent.atomic.AtomicReference;

public class OCamlProjectConfigurable implements Configurable {
    @NotNull private final Project myProject;

    // model
    private final OCamlSettings mySettings;
    private final ProjectSdksModel mySdksModel;
    private Sdk myProjectSdk;

    // components
    private final Disposable fakeDisposable = Disposer.newDisposable();
    private final OCamlSdkComboBox myOCamlSdkComboBox;
    private final SdkListConfigurable mySdkConfigurable;
    private JPanel myMainPanel;
    private JTextField myCompilerOutput;
    private JTabbedPane myTabs;
    private JPanel mySdksPanel;
    private JPanel mySdkPanel;

    public OCamlProjectConfigurable(@NotNull Project project) {
        myProject = project;
        mySettings = OCamlSettings.getInstance(project);

        // sdk model
        mySdksModel = new ProjectSdksModel();
        mySdksModel.reset(myProject);

        // Edit
        JButton editButton = new JButton(ApplicationBundle.message("button.edit"));

        // combobox
        myOCamlSdkComboBox = new OCamlSdkComboBox(myProject,
                mySdksModel,
                sdk -> sdk instanceof OCamlSdkType,
                null,
                null,
                null);
        // we are not disable, so we need to fake
        myOCamlSdkComboBox.setParentDisposable(fakeDisposable);
        myOCamlSdkComboBox.setSelectedJdk(mySdksModel.getProjectSdk());
        myOCamlSdkComboBox.setEditButton(editButton, myOCamlSdkComboBox::getSelectedSdk, sdk -> {
            // ProjectStructureConfigurable.getInstance(project).select(projectJdk, true);
            System.out.println("select " + sdk);
            myTabs.setSelectedIndex(1);
        });

        // Sdk Panel
        mySdkPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        mySdkPanel.add(myOCamlSdkComboBox);
        mySdkPanel.add(editButton);

        // On File selected
        myCompilerOutput.setText(mySettings.outputFolderName);

        // load sdk configurable
        mySdkConfigurable = new SdkListConfigurable(this);
        mySdksPanel.add(mySdkConfigurable.createComponent());
        mySdkConfigurable.reset();
    }

    @Override public String getDisplayName() {
        return OCamlBundle.message("ocaml.project.settings", myProject.getName().replaceAll("\n", " "));
    }

    @Override public @Nullable JComponent createComponent() {
        return myMainPanel;
    }

    @Override public boolean isModified() {
        Sdk selectedSdk = myOCamlSdkComboBox.getSelectedSdk();
        boolean projectSdkSame = (selectedSdk == null && myProjectSdk == null)
                || (selectedSdk != null && myProjectSdk != null &&
                selectedSdk.getName().equals(myProjectSdk.getName()));
        boolean locationSame = myCompilerOutput.getText().equals(mySettings.outputFolderName);
        return !projectSdkSame || !locationSame || mySdkConfigurable.isModified();
    }

    @Override public void apply() throws ConfigurationException {
        AtomicReference<ConfigurationException> exception = new AtomicReference<>();
        ApplicationManager.getApplication().runWriteAction(() -> {
            // set project SDK
            ProjectRootManager instance = ProjectRootManager.getInstance(myProject);
            myProjectSdk = myOCamlSdkComboBox.getSelectedSdk();
            instance.setProjectSdk(myProjectSdk);

            // wrap sdkConfigurable#apply
            try {
                mySdkConfigurable.apply();
            } catch (ConfigurationException e) {
                exception.set(e);
            }
        });
        if (exception.get() != null) throw exception.get();

        mySettings.outputFolderName = myCompilerOutput.getText();
    }

    @Override public void disposeUIResources() {
        Configurable.super.disposeUIResources();
        mySdkConfigurable.disposeUIResources();
        Disposer.dispose(fakeDisposable);
    }

    // new

    public Project getProject() {
        return myProject;
    }

    public ProjectSdksModel getSdksModel() {
        return mySdksModel;
    }
}
