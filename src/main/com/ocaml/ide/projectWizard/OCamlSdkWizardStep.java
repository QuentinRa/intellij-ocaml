package com.ocaml.ide.projectWizard;

import com.intellij.ide.util.projectWizard.*;
import com.intellij.openapi.project.*;
import com.intellij.openapi.projectRoots.*;
import com.intellij.openapi.roots.ui.configuration.*;
import com.intellij.openapi.roots.ui.configuration.projectRoot.*;
import com.intellij.openapi.ui.*;
import com.intellij.openapi.util.*;
import com.ocaml.*;
import com.ocaml.ide.sdk.*;
import org.jetbrains.annotations.*;

import javax.swing.*;
import java.awt.*;

/**
 * @see SdkSettingsStep
 * @see JavaSettingsStep
 * @see ProjectJdkForModuleStep
 */
public class OCamlSdkWizardStep extends ModuleWizardStep {
    @NotNull private final WizardContext myWizardContext;
    @NotNull private final OCamlModuleBuilder myModuleBuilder;
    @NotNull private JPanel myPanel;

    // to prompt "Project" or "Module"
    private JLabel labelSdk;

    // choice
    private ButtonGroup myUseSdkChoice;
    private boolean isUseSelected;
    // use
    @NotNull private JdkComboBox myJdkChooser;
    // create
    private JPanel myUseComponents;
    private JPanel myCreateComponents;
    private TextFieldWithBrowseButton ocamlBinLocation;
    private JLabel opamLocation;

    public OCamlSdkWizardStep(@NotNull WizardContext wizardContext,
                              @NotNull OCamlModuleBuilder moduleBuilder) {
        myWizardContext = wizardContext;
        myModuleBuilder = moduleBuilder;

        // are we inside a project?
        boolean isProject = myWizardContext.getProject() == null;
        labelSdk.setText(OCamlBundle.message("module.prompt.sdk", isProject ? "Project" : "Module"));
        labelSdk.setLabelFor(myJdkChooser);

        // Disable create
        setEnabledPanel(myCreateComponents, false);
        isUseSelected = true;

        // Add the listener on one choice, triggered
        // when the button is enabled/disabled
        // Enable/Disable the right panel
        myUseSdkChoice.getSelection().addItemListener((c) -> {
            isUseSelected = !isUseSelected;
            setEnabledPanel(myCreateComponents, !isUseSelected);
            setEnabledPanel(myUseComponents, isUseSelected);
        });
    }

    // re-enable every component in a panel
    private void setEnabledPanel(JPanel panel, boolean enabled) {
        for (Component component : panel.getComponents()) {
            component.setEnabled(enabled);
        }
    }

    @Override public JComponent getComponent() {
        return myPanel;
    }

    @Override public void updateDataModel() {
        //// are we inside a project?
        //boolean isProject = myWizardContext.getProject() == null;
        //Sdk jdk = myJdkChooser.getSelectedJdk();
        //
        //if (isProject) {
        //    myWizardContext.setProjectJdk(jdk);
        //} else {
        //    myModuleBuilder.setModuleJdk(jdk);
        //}
    }

    public void createUIComponents() {
        Project project = myWizardContext.getProject();
        project = project != null ? project : ProjectManager.getInstance().getDefaultProject();

        final ProjectStructureConfigurable projectConfig = ProjectStructureConfigurable.getInstance(project);
        ProjectSdksModel sdksModel = projectConfig.getProjectJdksModel();

        SdkType type = OCamlSdkType.getInstance();
        Condition<? super SdkTypeId> sdkTypeFilter = sdk -> sdk instanceof SdkType && (type == null || type.equals(sdk));

        myJdkChooser = new JdkComboBox(project,
                sdksModel,
                sdkTypeFilter,
                null,
                null,
                null);
    }
}
