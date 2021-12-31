package com.ocaml.ide.sdk.buildTool;

import com.intellij.execution.*;
import com.intellij.execution.configurations.*;
import com.intellij.execution.process.*;
import com.intellij.openapi.project.*;
import com.ocaml.comp.opam.*;
import com.ocaml.ide.sdk.runConfiguration.*;
import org.jetbrains.annotations.*;

public class OCamlcBuildState extends CommandLineState {

    private final Project myProject;
    private final OCamlcRunConfiguration myConfiguration;

    public OCamlcBuildState(OCamlcBuildContext oCamlcBuildContext) {
        super(oCamlcBuildContext.myEnvironment);
        myProject = oCamlcBuildContext.myProject;
        myConfiguration = oCamlcBuildContext.myConfiguration;
    }

    @Override public @NotNull ProcessHandler startProcess() throws ExecutionException {
        GeneralCommandLine cli = myProject.getService(OpamService.class)
                .ocamlc(myConfiguration.getScriptName(), myConfiguration.getWorkingDirectory());
        OSProcessHandler processHandler = ProcessHandlerFactory.getInstance()
                .createColoredProcessHandler(cli);
        ProcessTerminatedListener.attach(processHandler);
        return processHandler;
    }
}
