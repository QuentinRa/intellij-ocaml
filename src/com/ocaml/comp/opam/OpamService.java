package com.ocaml.comp.opam;

import com.intellij.execution.*;
import com.intellij.execution.configurations.*;
import com.intellij.execution.process.*;
import com.intellij.execution.wsl.*;
import com.intellij.openapi.project.*;
import com.intellij.openapi.util.*;
import com.intellij.openapi.util.io.*;
import com.intellij.openapi.util.text.StringUtil;
import com.ocaml.comp.opam.process.*;
import com.ocaml.ide.settings.*;
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

    public GeneralCommandLine ocaml() {
        ORSettings service = myProject.getService(ORSettings.class);
        String opamLocation = service.getOpamLocation();
        String opamSwitch = service.getSwitchName();

        // assuming Linux-based directories
        GeneralCommandLine cli = new GeneralCommandLine("./bin/ocaml");
        cli.setWorkDirectory(opamLocation + "/" + opamSwitch);
        cli.setRedirectErrorStream(true);

        return OpamUtils.patchCommandLine(opamLocation, cli, myProject);
    }

    public GeneralCommandLine ocamlc(String file, String workingDirectory) {
        ORSettings service = myProject.getService(ORSettings.class);
        String opamLocation = service.getOpamLocation();
        String ocamlc = opamLocation+service.getSwitchName() + "/bin/ocamlc";

        WSLDistribution distribution = OpamUtils.getDistribution(opamLocation);
        if (distribution != null) {
            file = distribution.getWslPath(file);
            // to Unix Path
            ocamlc = StringUtil.trimStart(ocamlc, WSLDistribution.UNC_PREFIX);
            ocamlc = StringUtil.trimStart(ocamlc, distribution.getPresentableName());
            ocamlc = ocamlc.replace("\\", "/");

            workingDirectory = FileUtil.toSystemDependentName(workingDirectory);
        } else {
            // assuming that the user is on Linux
            file = FileUtil.toSystemDependentName(file);
            workingDirectory = FileUtil.toSystemDependentName(workingDirectory);
        }
        // todo: -color=never??
        GeneralCommandLine cli = new GeneralCommandLine(ocamlc, file, "-w", "+A");
        cli.setWorkDirectory(workingDirectory);
        cli.setRedirectErrorStream(true);

        // Update the command line if we are using a WSL
        return OpamUtils.patchCommandLine(distribution, cli, myProject);
    }
}
