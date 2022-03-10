package com.ocaml.ide.wizard.minor;

import com.intellij.ide.IdeBundle;
import com.intellij.ide.util.projectWizard.AbstractNewProjectStep;
import com.intellij.ide.util.projectWizard.ProjectSettingsStepBase;
import com.intellij.openapi.project.ProjectManager;
import com.intellij.openapi.projectRoots.SdkTypeId;
import com.intellij.openapi.roots.ui.configuration.projectRoot.ProjectSdksModel;
import com.intellij.openapi.ui.DialogWrapper;
import com.intellij.openapi.ui.LabeledComponent;
import com.intellij.openapi.util.Condition;
import com.intellij.openapi.util.Disposer;
import com.intellij.openapi.wm.IdeFocusManager;
import com.intellij.platform.DirectoryProjectGenerator;
import com.intellij.ui.components.JBCheckBox;
import com.intellij.ui.components.JBScrollPane;
import com.intellij.util.ui.UIUtil;
import com.ocaml.OCamlBundle;
import com.ocaml.ide.wizard.minor.java.OCamlSdkComboBox;
import com.ocaml.ide.wizard.minor.java.ProjectTemplateList;
import com.ocaml.ide.wizard.templates.OCamlTemplateProvider;
import com.ocaml.sdk.OCamlSdkType;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.awt.*;

import static com.intellij.openapi.wm.impl.welcomeScreen.FlatWelcomeFrame.BOTTOM_PANEL;

/**
 * The wizard shown in minor IDEs.
 */
public class OCamlSimpleSettingsStep extends ProjectSettingsStepBase<Object> {

    public OCamlSimpleSettingsStep(DirectoryProjectGenerator<Object> projectGenerator, AbstractNewProjectStep.AbstractCallback<Object> callback) {
        super(projectGenerator, callback);
    }

    @Override public JPanel createPanel() {
        myLazyGeneratorPeer = createLazyPeer();
        final JPanel mainPanel = new JPanel(new BorderLayout());

        myCreateButton = new JButton(IdeBundle.message("new.dir.project.create"));
        myCreateButton.addActionListener(createCloseActionListener());
        myCreateButton.putClientProperty(DialogWrapper.DEFAULT_ACTION, Boolean.TRUE);
        Disposer.register(this, () -> UIUtil.dispose(myCreateButton));

        final JLabel label = createErrorLabel();
        final JPanel scrollPanel = createContentPanelWithAdvancedSettingsPanel();
        initGeneratorListeners();
        registerValidators();
        final JBScrollPane scrollPane = new JBScrollPane(scrollPanel, ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
                ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setBorder(null);
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        final JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.setName(BOTTOM_PANEL);
        bottomPanel.add(label, BorderLayout.NORTH);
        mainPanel.add(bottomPanel, BorderLayout.SOUTH);

        return mainPanel;
    }

    @Override protected @Nullable JPanel createAdvancedSettings() {
        JPanel jPanel = super.createAdvancedSettings();
        if (jPanel == null) return null;

        // combobox to select the SDK
        ProjectSdksModel projectSdksModel = new ProjectSdksModel();
        Condition<? super SdkTypeId> sdkTypeFilter = sdk -> sdk instanceof OCamlSdkType;
        OCamlSdkComboBox sdkChooser = new OCamlSdkComboBox(ProjectManager.getInstance().getDefaultProject(),
                projectSdksModel,
                sdkTypeFilter,
                null,
                null,
                null);
        sdkChooser.setParentDisposable(this);
        getProjectGenerator().setSdkChooser(sdkChooser);

        // label + combobox
        final LabeledComponent<OCamlSdkComboBox> component =
                LabeledComponent.create(sdkChooser,
                        OCamlBundle.message("project.wizard.module.prompt.sdk", 0),
                        BorderLayout.WEST);
        jPanel.add(component);

        JBCheckBox pickATemplate = new JBCheckBox();
        ProjectTemplateList templateList = new ProjectTemplateList();
        templateList.setTemplates(OCamlTemplateProvider.getAvailableTemplates(), false);

        jPanel.add(new JLabel(" ")); // create some gap
        jPanel.add(pickATemplate);
        jPanel.add(templateList);

        pickATemplate.addActionListener(e -> {
            templateList.setEnabled(pickATemplate.isSelected());
            if (pickATemplate.isSelected()) {
                IdeFocusManager.getGlobalInstance()
                        .doWhenFocusSettlesDown(() -> IdeFocusManager
                                .getGlobalInstance()
                                .requestFocus(templateList.getList(), true));
            }
        });

        templateList.setEnabled(false);
        pickATemplate.setText(OCamlBundle.message(
                "project.wizard.create.from.template",0
        ));

        getProjectGenerator().setTemplateChooser(templateList);

        return jPanel;
    }

    @Override public OCamlDirectoryProjectGenerator getProjectGenerator() {
        return (OCamlDirectoryProjectGenerator) super.getProjectGenerator();
    }
}
