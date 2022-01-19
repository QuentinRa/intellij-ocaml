package com.ocaml.ide.sdk;

import com.esotericsoftware.minlog.Log;
import com.intellij.execution.ExecutionException;
import com.intellij.execution.configurations.GeneralCommandLine;
import com.intellij.execution.wsl.WSLCommandLineOptions;
import com.intellij.execution.wsl.WSLDistribution;
import com.intellij.execution.wsl.WslPath;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.options.ConfigurationException;
import com.intellij.openapi.util.io.FileUtil;
import com.ocaml.OCamlBundle;
import com.ocaml.utils.files.OCamlPathUtils;
import com.ocaml.utils.logs.OCamlLogger;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;

public final class OCamlSdkUtils {

    public static final String JDK_FOLDER = "~/.jdks/ocaml";
    private static final Logger LOG = OCamlLogger.getSdkInstance("create");

    /**
     * Create an SDK or raise an exception with some information
     * as to why the creation failed.
     *
     * @param ocamlBinary         must ends with <code>/bin/ocaml</code> (.exe allowed)
     * @param ocamlCompilerBinary must ends with <code>/bin/ocamlc</code> (.exe allowed)
     * @param ocamlSourcesFolder  ex: <code>/usr/lib/ocaml</code> or <code>/lib/ocaml</code> for opam.
     *                            Must ends with <code>/lib/ocaml</code>
     * @return the data that will be used to create an SDK, null if there is no data to create an SDK
     * @throws ConfigurationException if the data is invalid
     */
    public static @NotNull CustomOCamlSdkData createSdk(@NotNull String ocamlBinary,
                                                        @NotNull String version,
                                                        @NotNull String ocamlCompilerBinary,
                                                        @NotNull String ocamlSourcesFolder) throws ConfigurationException {
        if (version.isEmpty())
            throw new ConfigurationException(OCamlBundle.message("sdk.ocaml.version.empty"));

        WslPath wslPath = WslPath.parseWindowsUncPath(ocamlBinary);
        WSLDistribution distribution = null;
        if (wslPath != null) {
            distribution = wslPath.getDistribution();
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
        if (!OCamlPathUtils.folderEndsWith(ocamlSourcesFolder, "/lib/ocaml")) // folder, can end with a /
            throw new ConfigurationException(OCamlBundle.message("sdk.ocaml.sources.folder.invalid"));

        LOG.info(
                "The binary is " + ocamlBinary + ", the compiler is " + ocamlCompilerBinary
                        + ", the sources' folder is " + ocamlSourcesFolder + ", and the version is " + version + "." +
                        "The WSL distribution is :" + distribution
        );

        // Check file exists
        int exitCode = -4;
        if (distribution != null) {
            try {
                GeneralCommandLine cli = new GeneralCommandLine("true");
                WSLCommandLineOptions wslCommandLineOptions = new WSLCommandLineOptions();
                wslCommandLineOptions.addInitCommand(
                        "(if [ -f " + ocamlBinary + " ]; then" +
                                " if [ -f " + ocamlCompilerBinary + " ]; then" +
                                " if [ -d " + ocamlSourcesFolder + " ]; then echo 0; else echo -3; fi;" +
                                " else echo -2; fi;" +
                                " else echo -1; fi;)"
                );
                cli = distribution.patchCommandLine(cli, null, wslCommandLineOptions);
                LOG.debug("The CLI was: " + cli.getCommandLineString());
                Process process = cli.createProcess();
                process.waitFor();
                exitCode = Integer.parseInt(
                        new String(process.getInputStream().readAllBytes()).replace("\n", "")
                );
            } catch (Throwable e) {
                throw new ConfigurationException(
                        OCamlBundle.message("sdk.path.binary.wsl.invalid", distribution.getPresentableName())
                                + "\n Unexpected error: " + e.getMessage()
                );
            }
        } else {
            try {
                File file = Path.of(ocamlBinary).toFile();
                if (!file.exists() || !file.isFile()) exitCode = -1;
                else {
                    Log.info("OCaml binary was found.");
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
                        Log.info("OCaml compiler was found.");
                        file = Path.of(ocamlSourcesFolder).toFile();
                        if (!file.exists() || !file.isDirectory()) exitCode = -3;
                        else {
                            exitCode = 0; // OK
                            Log.info("OCaml sources folder was found.");
                        }
                    }
                }
            } catch (InvalidPathException e) {
                throw new ConfigurationException("Unexpected error:" + e.getMessage());
            }
        }
        LOG.info("The command exited with " + exitCode);

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
            String sdkFolder = JDK_FOLDER + "/" + version;
            try {
                WSLCommandLineOptions wslCommandLineOptions = new WSLCommandLineOptions();
                // the order will be reversed, so we need to put the last commands first
                wslCommandLineOptions.addInitCommand("find " + sdkFolder + "-v$i -maxdepth 0 2>/dev/null");
                wslCommandLineOptions.addInitCommand("ln -s " + ocamlSourcesFolder + " " + sdkFolder + "-v$i/lib/");
                wslCommandLineOptions.addInitCommand("ln -s " + ocamlCompilerBinary + " " + sdkFolder + "-v$i/bin/ocamlc");
                wslCommandLineOptions.addInitCommand("ln -s " + ocamlBinary + " " + sdkFolder + "-v$i/bin/ocaml");
                wslCommandLineOptions.addInitCommand("mkdir -p " + sdkFolder + "-v$i/lib/");
                wslCommandLineOptions.addInitCommand("mkdir -p " + sdkFolder + "-v$i/bin");
                wslCommandLineOptions.addInitCommand("i=0; while true; do if [ ! -d " + sdkFolder + "-v$i ]; then break; else i=$((i+1)); fi done");
                GeneralCommandLine cli = distribution.patchCommandLine(new GeneralCommandLine("true"), null, wslCommandLineOptions);
                LOG.debug("The CLI was: " + cli.getCommandLineString());
                Process process = cli.createProcess();
                // wait, then parse the result of "echo", or fail
                if (process.waitFor() == 0) {
                    String out = new String(process.getInputStream().readAllBytes());
                    out = out.replace("\n", "");
                    if (!out.isEmpty()) {
                        homePath = distribution.getWindowsPath(out).trim();
                    } else {
                        LOG.error("Invalid home path (WSL): " + out);
                        throw new ExecutionException("Invalid home path.");
                    }
                } else {
                    throw new ExecutionException(new String(process.getErrorStream().readAllBytes()));
                }
            } catch (ExecutionException | InterruptedException | IOException e) {
                throw new ConfigurationException("Unknown error (create SDK):" + e.getMessage());
            }
        } else {
            File sdkFolder = FileUtil.findSequentNonexistentFile(jdksFolder, version + "-v", "");
            boolean ok = sdkFolder.mkdirs();
            ok = ok && new File(sdkFolder, "bin").mkdir();
            if (!ok) LOG.debug("create 'bin' failed");
            ok = ok && new File(sdkFolder, "lib").mkdir();
            if (!ok) LOG.debug("create 'lib' failed");
            ok = ok && OCamlPathUtils.createSymbolicLink(ocamlBinary, sdkFolder.getPath(), LOG, "bin", "ocaml");
            ok = ok && OCamlPathUtils.createSymbolicLink(ocamlCompilerBinary, sdkFolder.getPath(), LOG, "bin", "ocamlc");
            ok = ok && OCamlPathUtils.createSymbolicLink(ocamlSourcesFolder, sdkFolder.getPath(), LOG, "lib", "ocaml");
            if (ok) homePath = sdkFolder.getAbsolutePath();
        }

        LOG.debug("The homePath is " + homePath);

        if (homePath == null)
            throw new ConfigurationException(OCamlBundle.message("sdk.create.failed", JDK_FOLDER));

        return new CustomOCamlSdkData(OCamlSdkType.suggestSdkName(version), homePath);
    }

    public static final class CustomOCamlSdkData {
        public final String name;
        public final String homePath;
        public final OCamlSdkType type;

        public CustomOCamlSdkData(String name, String homePath) {
            this.name = name;
            this.homePath = homePath;
            this.type = OCamlSdkType.getInstance();
        }
    }

}
