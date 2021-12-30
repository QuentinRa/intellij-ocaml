package com.ocaml.ide.sdk.runConfiguration;

import com.intellij.execution.configurations.*;
import com.intellij.openapi.components.*;

public class OcamlcRunConfigurationOptions extends RunConfigurationOptions {

    private final StoredProperty<String> myScriptName;

    public OcamlcRunConfigurationOptions() {
        myScriptName = string("").provideDelegate(this, "scriptName");
    }

    public String getScriptName() {
        return myScriptName.getValue(this);
    }

    public void setScriptName(String scriptName) {
        myScriptName.setValue(this, scriptName);
    }
}
