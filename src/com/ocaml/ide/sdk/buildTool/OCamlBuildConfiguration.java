package com.ocaml.ide.sdk.buildTool;

import com.intellij.execution.runners.*;
import com.intellij.openapi.roots.*;
import com.ocaml.ide.sdk.runConfiguration.*;
import org.jetbrains.annotations.*;

public class OCamlBuildConfiguration implements ProjectModelBuildableElement {
    public final OCamlcRunConfiguration myConfiguration;
    public final ExecutionEnvironment myEnvironment;

    public OCamlBuildConfiguration(OCamlcRunConfiguration configuration,
                                   ExecutionEnvironment environment) {
        myConfiguration = configuration;
        myEnvironment = environment;
    }

    @SuppressWarnings("UnstableApiUsage")
    @Override public @Nullable ProjectModelExternalSource getExternalSource() {
        return null;
    }
}
