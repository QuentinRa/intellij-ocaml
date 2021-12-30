package com.ocaml.ide.sdk.runConfiguration;

import com.intellij.execution.*;
import com.intellij.execution.configurations.*;
import com.intellij.execution.process.*;
import com.intellij.execution.runners.*;
import com.intellij.openapi.options.*;
import com.intellij.openapi.project.*;
import com.ocaml.comp.opam.*;
import org.jetbrains.annotations.*;

/**
 * The configuration itself.
 * We are only saving the file that we will compile, for now.
 */
public class OCamlcRunConfiguration extends RunConfigurationBase<OcamlcRunConfigurationOptions> {

    public OCamlcRunConfiguration(@NotNull Project project,
                                  OCamlcConfigurationFactory factory,
                                  String name) {
        super(project, factory, name);
    }

    @NotNull @Override protected OcamlcRunConfigurationOptions getOptions() {
        return (OcamlcRunConfigurationOptions) super.getOptions();
    }

    public String getScriptName() {
        return getOptions().getScriptName();
    }

    public void setScriptName(String scriptName) {
        getOptions().setScriptName(scriptName);
    }

    @Override public @NotNull SettingsEditor<? extends RunConfiguration> getConfigurationEditor() {
        return new OCamlcSettingsEditor(getProject());
    }

    @Override public void checkConfiguration() {
    }

    @Override public @Nullable RunProfileState getState(@NotNull Executor executor,
                                                        @NotNull ExecutionEnvironment executionEnvironment) {
        return new CommandLineState(executionEnvironment) {
            @Override protected @NotNull ProcessHandler startProcess() throws ExecutionException {
                Project project = getProject();
                GeneralCommandLine cli = project.getService(OpamService.class).ocamlc(getScriptName());
                OSProcessHandler processHandler = ProcessHandlerFactory.getInstance()
                        .createColoredProcessHandler(cli);
                ProcessTerminatedListener.attach(processHandler);
                return processHandler;
            }
        };
    }
}
