package com.ocaml.ide.wizard.minor;

import com.intellij.ide.util.projectWizard.AbstractModuleBuilder;
import com.intellij.ide.util.projectWizard.AbstractNewProjectStep;
import com.intellij.ide.util.projectWizard.CustomStepProjectGenerator;
import com.intellij.ide.util.projectWizard.EmptyModuleBuilder;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.projectRoots.ProjectJdkTable;
import com.intellij.openapi.projectRoots.Sdk;
import com.intellij.openapi.roots.ModifiableRootModel;
import com.intellij.openapi.roots.ModuleRootManager;
import com.intellij.openapi.roots.ProjectRootManager;
import com.intellij.openapi.ui.ValidationInfo;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.openapi.wm.impl.welcomeScreen.AbstractActionWithPanel;
import com.intellij.platform.DirectoryProjectGenerator;
import com.intellij.platform.DirectoryProjectGeneratorBase;
import com.intellij.platform.ProjectTemplate;
import com.ocaml.OCamlBundle;
import com.ocaml.icons.OCamlIcons;
import com.ocaml.ide.wizard.BaseOCamlModuleBuilder;
import com.ocaml.ide.wizard.minor.java.OCamlSdkComboBox;
import com.ocaml.ide.wizard.minor.java.ProjectTemplateList;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

/**
 * Allow the creation of an OCaml project in minor IDEs.
 */
public class OCamlDirectoryProjectGenerator extends DirectoryProjectGeneratorBase<Object>
        implements CustomStepProjectGenerator<Object>, ProjectTemplate {
    private OCamlSdkComboBox sdkChooser;
    private ProjectTemplateList templateList;

    // data

    @Override public @NotNull String getName() {
        return OCamlBundle.message("ocaml.project");
    }

    @Override public @Nullable @Nls(capitalization = Nls.Capitalization.Sentence) String getDescription() {
        return OCamlBundle.message("ocaml.project.description");
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
        // add sdk
        Sdk sdk = sdkChooser.getSelectedSdk();
        if (sdk != null) { // todo: verify
            var jdkTable = ProjectJdkTable.getInstance();
            ApplicationManager.getApplication().runWriteAction(() -> {
                // add to the table if needed
                if (jdkTable.findJdk(sdk.getName()) == null)
                    jdkTable.addJdk(sdk);
                // set
                ProjectRootManager.getInstance(project).setProjectSdk(sdk);
            });
        }

        ModuleRootManager instance = ModuleRootManager.getInstance(module);
        ModifiableRootModel rootModel = instance.getModifiableModel();

        // Create the files/folders
        new BaseOCamlModuleBuilder().setupRootModel(rootModel, () -> sdk,
                baseDir::getPath,
                templateList.getSelectedTemplate());
    }

    public void setSdkChooser(OCamlSdkComboBox sdkChooser) {
        this.sdkChooser = sdkChooser;
    }

    public void setTemplateChooser(ProjectTemplateList templateList) {
        this.templateList = templateList;
    }
}
