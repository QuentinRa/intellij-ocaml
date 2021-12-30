package com.ocaml.ide.sdk.runConfiguration;

import com.intellij.execution.configurations.*;
import icons.*;
import org.jetbrains.annotations.*;

import javax.swing.*;

public class OCamlcRunConfigurationType implements ConfigurationType {

    static final String ID = "OCAMLC_RUN_CONFIGURATION";

    @Override
    public @NotNull String getDisplayName() {
        return "OCamlc";
    }

    @Override
    public @NotNull String getConfigurationTypeDescription() {
        return "Compile OCaml programs";
    }

    @Override
    public @NotNull Icon getIcon() {
        return ORIcons.OCAML;
    }

    @Override
    public @NotNull String getId() {
        return ID;
    }

    @Override
    public ConfigurationFactory @NotNull [] getConfigurationFactories() {
        return new ConfigurationFactory[]{new OCamlcConfigurationFactory(this)};
    }
}
