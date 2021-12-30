package com.ocaml.ide.sdk.runConfiguration;

import com.intellij.openapi.options.*;
import com.intellij.openapi.ui.*;
import org.jetbrains.annotations.*;

import javax.swing.*;

public class OCamlcSettingsEditor extends SettingsEditor<OCamlcRunConfiguration> {

    private JPanel myPanel;
    private LabeledComponent<TextFieldWithBrowseButton> myScriptName;

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
