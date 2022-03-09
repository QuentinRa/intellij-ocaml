package com.ocaml.ide.wizard.minor;

import com.intellij.ide.IdeBundle;
import com.intellij.ide.util.projectWizard.AbstractNewProjectStep;
import com.intellij.ide.util.projectWizard.ProjectSettingsStepBase;
import com.intellij.openapi.util.Disposer;
import com.intellij.platform.DirectoryProjectGenerator;
import com.intellij.ui.components.JBScrollPane;
import com.intellij.util.ui.UIUtil;
import org.jetbrains.annotations.NotNull;

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

        final JLabel label = createErrorLabel();
        final JPanel scrollPanel = createAndFillContentPanel();
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

    @Override public @NotNull JButton getActionButton() {
        JButton create = new JButton(IdeBundle.message("new.dir.project.create"));
        create.addActionListener(createCloseActionListener());
        Disposer.register(this, () -> UIUtil.dispose(create));
        return create;
    }
}
