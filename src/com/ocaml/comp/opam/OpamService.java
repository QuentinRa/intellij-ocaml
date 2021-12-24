package com.ocaml.comp.opam;

import com.intellij.execution.*;
import com.intellij.execution.configurations.*;
import com.intellij.execution.process.*;
import com.intellij.openapi.project.*;
import com.intellij.openapi.util.*;
import com.ocaml.comp.opam.process.*;
import com.ocaml.utils.*;
import org.jetbrains.annotations.*;

import java.util.*;

public class OpamService {

    private final @NotNull Project myProject;
    public OpamService(@NotNull Project project) {
        myProject = project;
    }

    public void listSwitch(@NotNull String opamLocation,
                           @NotNull ProcessCallback<List<FindSwitchProcess.OpamSwitch>> c) {
        GeneralCommandLine cli = FindSwitchProcess.getCommand();
        cli = OpamUtils.patchCommandLine(opamLocation, cli, myProject);
        FindSwitchProcess.findSwitch(opamLocation, cli, c);
    }

    public void list(@NotNull String opamRoot, @NotNull String version,
                     @NotNull ProcessCallback<List<String[]>> onProcessTerminated) {
        ArrayList<String[]> installedLibs = new ArrayList<>();

        GeneralCommandLine cli = new GeneralCommandLine("opam", "list", "--installed", "--safe", "--color=never", "--switch=" + version);
        cli.setRedirectErrorStream(true);

        cli = OpamUtils.patchCommandLine(opamRoot, cli, myProject);

        KillableProcessHandler processHandler;
        try {
            processHandler = new KillableProcessHandler(cli);
            processHandler.addProcessListener(new ProcessListener() {
                @Override
                public void startNotified(@NotNull ProcessEvent event) {
                }
                @Override
                public void processTerminated(@NotNull ProcessEvent event) {
                    onProcessTerminated.call(installedLibs);
                }

                @Override
                public void onTextAvailable(@NotNull ProcessEvent event, @NotNull Key outputType) {
                    if (ProcessOutputType.isStdout(outputType)) {
                        String text = event.getText().trim();
                        if (text.charAt(0) != '#') {
                            String[] split = text.split("\\s+", 3);
                            installedLibs.add(new String[]{split[0].trim(), split.length >= 2 ? split[1].trim() : "unknown", split.length >= 3 ? split[2].trim() : ""});
                        }
                    }
                }
            });
            processHandler.startNotify();
        } catch (ExecutionException e) {
            onProcessTerminated.call(installedLibs);
        }
    }
}
