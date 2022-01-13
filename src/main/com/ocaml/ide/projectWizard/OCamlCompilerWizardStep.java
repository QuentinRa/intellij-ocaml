package com.ocaml.ide.projectWizard;

import com.intellij.ide.util.projectWizard.*;
import com.intellij.openapi.projectRoots.*;
import com.ocaml.*;
import com.ocaml.ide.sdk.*;

import javax.swing.*;
import java.awt.*;

/**
 * Usually, this is used to set the SDK for the project.
 * But, I changed some code so that we are setting the SDK for the project
 * (if we are creating a project) or a module (else).
 *
 * -------------- OCamlCompilerWizardStep ----------
 * I changed the texts in the Labels.
 *
 * -------------- updateDataModel --------------
 * Now, the function is checking if we already create a project.
 * If we did, then we are setting the project SDK,
 * otherwise, we are setting the module SDK.
 *
 * todo: ensure that the SDK was set for the module
 *  if we are setting the project SDK.
 */
public class OCamlCompilerWizardStep extends ProjectJdkForModuleStep {
    private final WizardContext myWizardContext;
    private final OCamlModuleBuilder myModuleBuilder;

    public OCamlCompilerWizardStep(WizardContext wizardContext, OCamlModuleBuilder moduleBuilder) {
        super(wizardContext, OCamlSdkType.getInstance());
        myWizardContext = wizardContext;
        myModuleBuilder = moduleBuilder;

        JPanel component = (JPanel) getComponent();
        boolean isProject = wizardContext.getProject() == null;
        Component c;

        // edit labels
        // was: "Please select the {0} to be set for this module"
        c = component.getComponent(0);
        if (c instanceof JLabel)
            ((JLabel) c).setText(OCamlBundle.message("module.prompt.please.select.module.sdk", isProject ? "project" : "module"));

        // was: "Project SDK:"
        c = component.getComponent(1);
        if (c instanceof JLabel)
            ((JLabel) c).setText(OCamlBundle.message("module.prompt.sdk", isProject ? "Project" : "Module"));
    }

    @Override public void updateDataModel() {
        boolean isProject = myWizardContext.getProject() == null;
        Sdk jdk = getJdk();

        if (isProject) {
            myWizardContext.setProjectJdk(jdk);
        } else {
            myModuleBuilder.setModuleJdk(jdk);
        }
    }
}
