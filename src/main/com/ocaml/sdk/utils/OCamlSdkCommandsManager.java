package com.ocaml.sdk.utils;

import com.esotericsoftware.minlog.Log;
import com.intellij.execution.configurations.GeneralCommandLine;
import com.intellij.execution.configurations.PtyCommandLine;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.module.ModuleManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.projectRoots.Sdk;
import com.intellij.openapi.roots.ModuleRootManager;
import com.intellij.openapi.roots.ProjectRootManager;
import com.ocaml.OCamlBundle;
import com.ocaml.sdk.OCamlSdkType;
import com.ocaml.sdk.providers.OCamlSdkProvidersManager;
import org.jetbrains.annotations.NotNull;

public final class OCamlSdkCommandsManager {

    public static @NotNull Sdk getSdk(Project project) {
        Sdk sdk = ProjectRootManager.getInstance(project).getProjectSdk();
        if (sdk == null || !(sdk.getSdkType() instanceof OCamlSdkType)) {
            for (Module module : ModuleManager.getInstance(project).getModules()) {
                Sdk sdkCandidate = ModuleRootManager.getInstance(module).getSdk();
                if (sdk == null || !(sdk.getSdkType() instanceof OCamlSdkType))
                    continue;
                sdk = sdkCandidate;
                break;
            }
        }
        if (!(sdk instanceof OCamlSdkType))
            throw new IllegalStateException(OCamlBundle.message("repl.no.sdk"));
        return sdk;
    }

    public static @NotNull GeneralCommandLine getREPLCommand(@NotNull Project project) {
        Sdk sdk = getSdk(project);

        // get command
        GeneralCommandLine replCommand = OCamlSdkProvidersManager.INSTANCE.getREPLCommand(sdk.getHomePath());

        // should NOT be null, even if everyone delegates the creation, the default provider isn't
        if (replCommand == null)
            throw new IllegalStateException("Unable to start the console.");

        Log.debug("REPL command is:" + replCommand.getCommandLineString());

        // return PtyCommand
        return new PtyCommandLine(replCommand).withInitialColumns(PtyCommandLine.MAX_COLUMNS);
    }
}
