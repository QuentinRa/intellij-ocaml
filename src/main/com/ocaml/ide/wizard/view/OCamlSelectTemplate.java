package com.ocaml.ide.wizard.view;

import com.intellij.ide.projectWizard.*;
import com.intellij.ide.util.projectWizard.*;
import com.intellij.openapi.wm.*;
import com.intellij.platform.*;
import com.intellij.ui.components.*;
import com.ocaml.*;
import com.ocaml.ide.wizard.*;

import javax.swing.*;
import java.util.*;

/**
 * The same interface used in ChooseTemplateStep, but we are using it
 * with different classes in the constructor/etc. as we are loading some
 * "unusual" templates.
 * @see com.intellij.ide.projectWizard.ChooseTemplateStep
 */
public class OCamlSelectTemplate extends ModuleWizardStep {
    private final WizardContext myWizardContext;
    private final ArrayList<ProjectTemplate> myAvailableTemplates;

    private JPanel myPanel;
    private JBCheckBox myCreateFromTemplateCheckBox;
    private ProjectTemplateList myTemplateList;

    public OCamlSelectTemplate(WizardContext wizardContext, ArrayList<ProjectTemplate> availableTemplates) {
        myWizardContext = wizardContext;
        myAvailableTemplates = availableTemplates;
        myCreateFromTemplateCheckBox.addActionListener(e -> {
            myTemplateList.setEnabled(myCreateFromTemplateCheckBox.isSelected());
            if (myCreateFromTemplateCheckBox.isSelected()) {
                IdeFocusManager.getGlobalInstance().doWhenFocusSettlesDown(() -> IdeFocusManager.getGlobalInstance().requestFocus(myTemplateList.getList(), true));
            }
        });
        myTemplateList.setEnabled(false);
        // show "project" or "module" according to what we are creating
        myCreateFromTemplateCheckBox.setText(OCamlBundle.message(
                "project.wizard.create.from.template",
                wizardContext.isCreatingNewProject() ? 0 : 1
                )
        );
    }

    @Override
    public boolean isStepVisible() {
        return !myAvailableTemplates.isEmpty();
    }

    @Override
    public JComponent getComponent() {
        return myPanel;
    }

    @Override
    public void updateStep() {
        myTemplateList.setTemplates(myAvailableTemplates, false);
    }

    @Override
    public void updateDataModel() {
        ProjectBuilder projectBuilder = myWizardContext.getProjectBuilder();
        if (projectBuilder instanceof OCamlModuleBuilder)
            ((OCamlModuleBuilder) projectBuilder).setProjectTemplate(myCreateFromTemplateCheckBox.isSelected() ? myTemplateList.getSelectedTemplate() : null);
    }
}
