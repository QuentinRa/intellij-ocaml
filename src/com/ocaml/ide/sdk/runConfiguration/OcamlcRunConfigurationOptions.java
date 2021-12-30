package com.ocaml.ide.sdk.runConfiguration;

import com.intellij.execution.configurations.*;
import com.intellij.openapi.components.*;

public class OcamlcRunConfigurationOptions extends RunConfigurationOptions {

    private final StoredProperty<String> myScriptName;
    private final StoredProperty<String> myWorkingDirectory;

    public OcamlcRunConfigurationOptions() {
        myScriptName = string("").provideDelegate(this, "scriptName");
        myWorkingDirectory = string("").provideDelegate(this, "workingDirectory");
    }

    public String getScriptName() {
        return myScriptName.getValue(this);
    }

    public void setScriptName(String scriptName) {
        myScriptName.setValue(this, scriptName);
    }

    public String getWorkingDirectory() {
        return myWorkingDirectory.getValue(this);
    }

    public void setWorkingDirectory(String workingDirectory) {
        myWorkingDirectory.setValue(this, workingDirectory);
    }
}
