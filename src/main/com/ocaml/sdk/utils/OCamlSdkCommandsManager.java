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
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

// note that this class will be removed later, when the REPL will be remade
@ApiStatus.Internal
public final class OCamlSdkCommandsManager {

    // find an SDK that can be used to start the REPL
    public static @NotNull Sdk getSdk(Project project) {
        Sdk sdk = ProjectRootManager.getInstance(project).getProjectSdk();
        if (sdk == null || !(sdk.getSdkType() instanceof OCamlSdkType)) {
            for (Module module : ModuleManager.getInstance(project).getModules()) {
                Sdk sdkCandidate = ModuleRootManager.getInstance(module).getSdk();
                if (sdkCandidate == null || !(sdkCandidate.getSdkType() instanceof OCamlSdkType))
                    continue;
                sdk = sdkCandidate;
                break;
            }
        }
        if (sdk == null || !(sdk.getSdkType() instanceof OCamlSdkType))
            throw new IllegalStateException(OCamlBundle.message("repl.no.sdk"));
        return sdk;
    }

    // returns the command to start the REPL
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
