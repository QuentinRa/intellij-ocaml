package com.ocaml.ide.sdk.runConfiguration;

import com.intellij.execution.configurations.*;
import com.intellij.openapi.components.*;
import com.intellij.openapi.project.*;
import org.jetbrains.annotations.*;

public class OCamlcConfigurationFactory extends ConfigurationFactory {
    OCamlcConfigurationFactory(@NotNull ConfigurationType type) {
        super(type);
    }

    @Override
    public @NotNull RunConfiguration createTemplateConfiguration(@NotNull Project project) {
        return new OCamlcRunConfiguration(project, this, "Ocamlc");
    }

    @Override public @Nullable Class<? extends BaseState> getOptionsClass() {
        return OcamlcRunConfigurationOptions.class;
    }

    // The default implementation is deprecated
    @Override public @NotNull @NonNls String getId() {
        return OCamlcRunConfigurationType.ID;
    }
}
