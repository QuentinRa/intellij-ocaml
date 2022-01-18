package com.ocaml.ide.projectWizard.view;

import com.intellij.ide.projectWizard.*;
import com.intellij.ide.util.projectWizard.*;
import com.intellij.openapi.wm.*;
import com.intellij.platform.*;
import com.intellij.ui.components.*;
import com.ocaml.ide.projectWizard.*;

import javax.swing.*;
import java.util.*;

/**
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
    }

    @Override
    public boolean isStepVisible() {
        return myWizardContext.isCreatingNewProject() && !myAvailableTemplates.isEmpty();
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
