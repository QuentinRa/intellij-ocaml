package com.ocaml.ide.sdk;

import com.intellij.execution.*;
import com.intellij.execution.configurations.*;
import com.intellij.execution.wsl.*;
import com.intellij.openapi.options.*;
import com.intellij.openapi.projectRoots.*;
import com.intellij.openapi.roots.ui.configuration.projectRoot.*;
import com.intellij.openapi.util.io.*;
import com.ocaml.*;
import com.ocaml.utils.files.*;
import org.jetbrains.annotations.*;

import java.io.*;
import java.nio.file.*;

public final class OCamlSdkUtils {

    public static final String JDK_FOLDER = "~/.jdks/ocaml";

    /**
     * Create an SDK or raise an exception with some information
     * as to why the creation failed.
     *
     * @param ocamlBinary must ends with <code>/bin/ocaml</code> (.exe allowed)
     * @param ocamlCompilerBinary must ends with <code>/bin/ocamlc</code> (.exe allowed)
     * @param ocamlSourcesFolder ex: <code>/usr/lib/ocaml</code> or <code>/lib/ocaml</code> for opam.
     *                           Must ends with <code>/lib/ocaml</code>
     * @return an SDK, null if there is no data to create an SDK
     * @throws ConfigurationException if the data is invalid
     */
    public static @NotNull Sdk createSdk(@NotNull String ocamlBinary,
                                         @NotNull String version,
                                         @NotNull String ocamlCompilerBinary,
                                         @NotNull String ocamlSourcesFolder,
                                         @NotNull ProjectSdksModel sdksModel) throws ConfigurationException {
        if (version.isEmpty())
            throw new ConfigurationException(OCamlBundle.message("sdk.ocaml.version.empty"));

        WslPath wslPath = WslPath.parseWindowsUncPath(ocamlBinary);
        WSLDistribution distribution = null;
        if (wslPath != null) {
            distribution = wslPath.getDistribution();
            if (distribution.getVersion() == -1)
                throw new ConfigurationException(OCamlBundle.message("sdk.path.binary.wsl.invalid", distribution.getPresentableName()));
            ocamlBinary = distribution.getWslPath(ocamlBinary);
            if (ocamlBinary == null) ocamlBinary = "";
            ocamlCompilerBinary = distribution.getWslPath(ocamlCompilerBinary);
            if (ocamlCompilerBinary == null) ocamlCompilerBinary = "";
            ocamlSourcesFolder = distribution.getWslPath(ocamlSourcesFolder);
            if (ocamlSourcesFolder == null) ocamlSourcesFolder = "";
        }

        // Check paths valid
        if (!OCamlPathUtils.fileEndsWith(ocamlBinary, "/bin/ocaml", ".exe"))
            throw new ConfigurationException(OCamlBundle.message("sdk.ocaml.binary.invalid"));
        if (!OCamlPathUtils.folderEndsWith(ocamlSourcesFolder,"/lib/ocaml")) // folder, can end with a /
            throw new ConfigurationException(OCamlBundle.message("sdk.ocaml.sources.folder.invalid"));

        // Check file exists
        int exitCode = -4;
        if (distribution != null) {
            try {
                GeneralCommandLine cli = new GeneralCommandLine("true");
                WSLCommandLineOptions wslCommandLineOptions = new WSLCommandLineOptions();
                wslCommandLineOptions.addInitCommand(
                        "(if [ -f "+ocamlBinary+" ]; then" +
                        " if [ -f "+ocamlCompilerBinary+" ]; then" +
                        " if [ -d "+ocamlSourcesFolder+" ]; then echo 0; else echo -3; fi;" +
                        " else echo -2; fi;" +
                        " else echo -1; fi;)"
                );
                cli = distribution.patchCommandLine(cli, null, wslCommandLineOptions);
                Process process = cli.createProcess();
                process.waitFor();
                exitCode = Integer.parseInt(
                        new String(process.getInputStream().readAllBytes()).replace("\n", "")
                );
            } catch (ExecutionException | InterruptedException | IOException e) {
                throw new ConfigurationException("Unexpected error:"+e.getMessage());
            }
        } else {
            try {
                File file = Path.of(ocamlBinary).toFile();
                if (!file.exists() || !file.isFile()) exitCode = -1;
                else {
                    file = Path.of(ocamlCompilerBinary).toFile();
                    if (!file.exists() || !file.isFile()) {
                        if (ocamlCompilerBinary.endsWith(".exe")) {
                            // try again without .exe
                            ocamlCompilerBinary = ocamlCompilerBinary.replace("ocamlc.exe", "ocamlc");
                            file = Path.of(ocamlCompilerBinary).toFile();
                            if (!file.exists()) { // issue: no extension = not a file?
                                exitCode = -2;
                            }
                        }
                    }

                    if (exitCode == -4) {
                        file = Path.of(ocamlSourcesFolder).toFile();
                        if (!file.exists() || !file.isDirectory()) exitCode = -3;
                        else {
                            exitCode = 0; // OK
                        }
                    }
                }
            } catch (InvalidPathException e) {
                throw new ConfigurationException("Unexpected error:"+e.getMessage());
            }
        }

        switch (exitCode) {
            case -1:
                throw new ConfigurationException(OCamlBundle.message("sdk.ocaml.binary.not.found"));
            case -2:
                throw new ConfigurationException(OCamlBundle.message("sdk.ocaml.compiler.not.found"));
            case -3:
                throw new ConfigurationException(OCamlBundle.message("sdk.ocaml.sources.not.found"));
            case -4:
                throw new IllegalStateException("Couldn't check that files exist. Please report this error.");
        }

        File jdksFolder = new File(FileUtil.expandUserHome(JDK_FOLDER));
        String homePath = null;

        // create the "opam-like" SDK
        if (distribution != null) {
            String sdkFolder = JDK_FOLDER+"/"+version;
            try {
                WSLCommandLineOptions wslCommandLineOptions = new WSLCommandLineOptions();
                // the order will be reversed, so we need to put the last commands first
                wslCommandLineOptions.addInitCommand("echo -n "+sdkFolder+"-v$i");
                wslCommandLineOptions.addInitCommand("ln -s "+ocamlSourcesFolder+" "+sdkFolder+"-v$i/lib/");
                wslCommandLineOptions.addInitCommand("ln -s "+ocamlCompilerBinary+" "+sdkFolder+"-v$i/bin/ocamlc");
                wslCommandLineOptions.addInitCommand("ln -s "+ocamlBinary+" "+sdkFolder+"-v$i/bin/ocaml");
                wslCommandLineOptions.addInitCommand("mkdir -p "+sdkFolder+"-v$i/lib/");
                wslCommandLineOptions.addInitCommand("mkdir -p "+sdkFolder+"-v$i/bin");
                wslCommandLineOptions.addInitCommand("i=0; while true; do if [ ! -d "+sdkFolder+"-v$i ]; then break; else i=$((i+1)); fi done");
                GeneralCommandLine cli = distribution.patchCommandLine(new GeneralCommandLine("true"), null, wslCommandLineOptions);
                Process process = cli.createProcess();
                // wait, then parse the result of "echo", or fail
                if (process.waitFor() == 0) {
                    String out = new String(process.getInputStream().readAllBytes());
                    if (!out.isEmpty() && distribution.getUserHome() != null) {
                        homePath = out.replace("~", distribution.getUserHome());
                        homePath = distribution.getWindowsPath(homePath).trim();
                    } else
                        throw new ExecutionException("Invalid home path.");
                } else {
                    throw new ExecutionException(new String(process.getErrorStream().readAllBytes()));
                }
            } catch (ExecutionException | InterruptedException | IOException e) {
                throw new ConfigurationException("Unknown error (create SDK):"+e.getMessage());
            }
        } else {
            File sdkFolder = FileUtil.findSequentNonexistentFile(jdksFolder, version+"-v", "");
            boolean ok = sdkFolder.mkdirs();
            ok = ok && new File(sdkFolder, "bin").mkdir();
            ok = ok && new File(sdkFolder, "lib").mkdir();
            ok = ok && OCamlPathUtils.createSymbolicLink(ocamlBinary, sdkFolder.getPath(), "bin", "ocaml");
            ok = ok && OCamlPathUtils.createSymbolicLink(ocamlCompilerBinary, sdkFolder.getPath(), "bin", "ocamlc");
            ok = ok && OCamlPathUtils.createSymbolicLink(ocamlSourcesFolder, sdkFolder.getPath(), "lib", "ocaml");
            if (ok) homePath = sdkFolder.getAbsolutePath();
        }

        if (homePath == null)
            throw new ConfigurationException(OCamlBundle.message("sdk.create.failed", JDK_FOLDER));

        // Finally, we are creating the SDK
        return sdksModel.createSdk(OCamlSdkType.getInstance(), OCamlSdkType.suggestSdkName(version), homePath);
    }

}
