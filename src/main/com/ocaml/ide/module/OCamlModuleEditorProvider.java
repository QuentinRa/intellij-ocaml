package com.ocaml.ide.module;

import com.intellij.openapi.module.*;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.roots.*;
import com.intellij.openapi.roots.ui.configuration.*;
import com.intellij.util.ui.*;

import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.util.List;

/**
 * The {@link DefaultModuleEditorsProvider} is only available for
 * JAVA. This is an implementation for OCaml. Basically, in the editor,
 * in "Project Structure" > Modules, you will be able to see some tabs when you
 * are clicking on an OCaml module. These tabs are configured here.
 */
public class OCamlModuleEditorProvider implements ModuleConfigurationEditorProvider  {

    @Override public ModuleConfigurationEditor[] createEditors(ModuleConfigurationState state) {
        Module module = state.getCurrentRootModel().getModule();
        ModuleType<?> moduleType = ModuleType.get(module);

        // We are not handling non-ocaml modules
        if (!(moduleType instanceof OCamlModuleType) &&
                (!GeneralModuleType.INSTANCE.equals(moduleType) || ProjectRootManager.getInstance(state.getProject()).getProjectSdk() == null)) {
            return ModuleConfigurationEditor.EMPTY;
        }

        // creating the tabs
        List<ModuleConfigurationEditor> editors = new ArrayList<>();
        editors.add(new OCamlOutputEditor(state));

        return editors.toArray(ModuleConfigurationEditor.EMPTY);
    }

    /**
     * OutputEditor without Javadoc and annotations panels
     * todo: handle "exclude output paths"
     */
    private static final class OCamlOutputEditor extends OutputEditor {

        public OCamlOutputEditor(ModuleConfigurationState state) {
            super(state);
        }

        @Override protected JComponent createComponentImpl() {
            JPanel panel = (JPanel) super.createComponentImpl();
            panel.getComponent(1).setVisible(false); // javadocPanel
            panel.getComponent(2).setVisible(false); // annotationsPanel
            // adding some glue to fill the empty space
            final GridBagConstraints gc = new GridBagConstraints(
                    0, GridBagConstraints.RELATIVE, 1, 1, 1, 1,
                    GridBagConstraints.NORTHWEST, GridBagConstraints.BOTH, JBUI.emptyInsets(),
                    0, 0
            );
            panel.add(Box.createVerticalGlue(), gc);
            return panel; // :pray:
        }
    }
}
