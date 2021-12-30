package com.ocaml.ide.sdk.runConfiguration;

import com.intellij.ide.util.*;
import com.intellij.openapi.options.*;
import com.intellij.openapi.project.*;
import com.intellij.openapi.ui.*;
import org.jetbrains.annotations.*;

import javax.swing.*;

public class OCamlcSettingsEditor extends SettingsEditor<OCamlcRunConfiguration> {

    private JPanel myPanel;
    private LabeledComponent<TextFieldWithBrowseButton> myScriptName;

    public OCamlcSettingsEditor(@NotNull Project project) {
        // SINGLE_FILE_DESCRIPTOR => SINGLE_DIRECTORY_DESCRIPTOR for the WorkingDirectory
        myScriptName.getComponent()
                .addBrowseFolderListener(
                        "Choose Target OCaml File",
                        "",
                        project,
                        BrowseFilesListener.SINGLE_FILE_DESCRIPTOR
        );
    }

    @Override
    protected void resetEditorFrom(OCamlcRunConfiguration runConfiguration) {
        myScriptName.getComponent().setText(runConfiguration.getScriptName());
    }

    @Override
    protected void applyEditorTo(@NotNull OCamlcRunConfiguration runConfiguration) {
        runConfiguration.setScriptName(myScriptName.getComponent().getText());
    }

    @NotNull
    @Override
    protected JComponent createEditor() {
        return myPanel;
    }

    private void createUIComponents() {
        myScriptName = new LabeledComponent<>();
        myScriptName.setComponent(new TextFieldWithBrowseButton());
    }

}
