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
    private LabeledComponent<TextFieldWithBrowseButton> myWorkingDirectory;

    public OCamlcSettingsEditor(@NotNull Project project) {
        myScriptName.getComponent()
                .addBrowseFolderListener(
                        "Choose Target OCaml File",
                        "",
                        project,
                        BrowseFilesListener.SINGLE_FILE_DESCRIPTOR
        );
        myWorkingDirectory.getComponent()
                .addBrowseFolderListener(
                        "Choose Working Directory",
                        "",
                        project,
                        BrowseFilesListener.SINGLE_DIRECTORY_DESCRIPTOR
        );
    }

    @Override
    protected void resetEditorFrom(OCamlcRunConfiguration runConfiguration) {
        myScriptName.getComponent().setText(runConfiguration.getScriptName());
        myWorkingDirectory.getComponent().setText(runConfiguration.getWorkingDirectory());
    }

    @Override
    protected void applyEditorTo(@NotNull OCamlcRunConfiguration runConfiguration) {
        runConfiguration.setScriptName(myScriptName.getComponent().getText());
        runConfiguration.setWorkingDirectory(myWorkingDirectory.getComponent().getText());
    }

    @NotNull
    @Override
    protected JComponent createEditor() {
        return myPanel;
    }

    private void createUIComponents() {
        myScriptName = new LabeledComponent<>();
        myScriptName.setComponent(new TextFieldWithBrowseButton());

        myWorkingDirectory = new LabeledComponent<>();
        myWorkingDirectory.setComponent(new TextFieldWithBrowseButton());
    }

}
