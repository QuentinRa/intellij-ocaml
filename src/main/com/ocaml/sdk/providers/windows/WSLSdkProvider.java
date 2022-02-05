package com.ocaml.sdk.providers.windows;

import com.intellij.execution.ExecutionException;
import com.intellij.execution.configurations.GeneralCommandLine;
import com.intellij.execution.wsl.*;
import com.intellij.openapi.util.io.FileUtil;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.openapi.vfs.impl.wsl.WslConstants;
import com.ocaml.sdk.providers.simple.SimpleSdkData;
import com.ocaml.sdk.providers.utils.CompileWithCmtInfo;
import com.ocaml.sdk.utils.OCamlSdkVersionManager;
import com.ocaml.sdk.providers.utils.AssociatedBinaries;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * A WSL can be installed easily with "Windows Store > Debian" or
 * "Windows Store > Ubuntu". They are working like a "Linux on Windows".
 * Everything involving WSL is complex, because the paths must be converted
 * again and again, and we have to look for installed distributions sometimes (slow).
 */
public class WSLSdkProvider extends AbstractWindowsBaseProvider {

    @Override protected boolean canUseProviderForOCamlBinary(@NotNull String path) {
        return false;
    }

    @Override public @NotNull Set<String> getInstallationFolders() {
        Set<String> homePaths = new HashSet<>();
        // WSL
        for (WSLDistribution distro : WslDistributionManager.getInstance().getInstalledDistributions()) {
            String wslPath = distro.getUserHome();
            if (wslPath == null) continue;
            String windowsPath = distro.getWindowsPath(wslPath + "/.opam/");
            homePaths.add(windowsPath);
            // we may have Simple SDKs
            homePaths.add(distro.getWindowsPath(expandUserHome(distro, SimpleSdkData.SDK_FOLDER)));
        }
        return homePaths;
    }

    // move to another class?
    public static @NotNull String expandUserHome(WSLDistribution distro, @NotNull String folder) {
        if (!folder.contains("~")) return folder;
        String userHome = distro.getUserHome();
        if (userHome == null) return folder;
        return folder.replace("~", userHome);
    }

    @Override
    public @Nullable String createSdkFromBinaries(String ocaml, String compiler,
                                                  String version, String sources,
                                                  String sdkFolder, String sdkModifier) {
        // is wsl
        WslPath path = WslPath.parseWindowsUncPath(ocaml);
        if (path == null) return null;
        sdkFolder += "/"+version+sdkModifier;

        WSLDistribution distribution = path.getDistribution();
        //noinspection DuplicatedCode
        ocaml = distribution.getWslPath(ocaml);
        if (ocaml == null) return null;
        compiler = distribution.getWslPath(compiler);
        if (compiler == null) return null;
        sources = distribution.getWslPath(sources);
        if (sources == null) return null;

        // the order will be reversed, so we need to put the last commands first
        WSLCommandLineOptions wslCommandLineOptions = new WSLCommandLineOptions();
        // get absolute path to the created SDK folder (with the $i)
        wslCommandLineOptions.addInitCommand("find " + sdkFolder + "$i -maxdepth 0 2>/dev/null");
        // link to sources
        wslCommandLineOptions.addInitCommand("ln -s " + sources + " " + sdkFolder + "$i/lib/");
        // link to compiler
        wslCommandLineOptions.addInitCommand("ln -s " + compiler + " " + sdkFolder + "$i/bin/ocamlc");
        // link to ocaml
        wslCommandLineOptions.addInitCommand("ln -s " + ocaml + " " + sdkFolder + "$i/bin/ocaml");
        // create lib
        wslCommandLineOptions.addInitCommand("mkdir -p " + sdkFolder + "$i/lib/");
        // create both SDK folder and bin
        wslCommandLineOptions.addInitCommand("mkdir -p " + sdkFolder + "$i/bin");
        // find $i
        wslCommandLineOptions.addInitCommand("i=0; while true; do if [ ! -d " + sdkFolder + "$i ]; then break; else i=$((i+1)); fi done");

        try {
            GeneralCommandLine cli = distribution.patchCommandLine(new GeneralCommandLine("true"), null, wslCommandLineOptions);
            LOG.debug("The CLI was: " + cli.getCommandLineString());
            Process process = cli.createProcess();
            // parse the result of find
            String out = new String(process.getInputStream().readAllBytes());
            out = out.replace("\n", "");
            return distribution.getWindowsPath(out).trim();
        } catch (ExecutionException | IOException e) {
            LOG.error("Couldn't process command. Error:"+e.getMessage());
            return null;
        }
    }

    /**
     * It's worth noting that we could use the one in the BaseProvider, but
     * "Files.exists" is failing for every symbolic link, and I don't want that.
     */
    @Override public @Nullable AssociatedBinaries getAssociatedBinaries(@NotNull String ocamlBinary) {
        // is ocaml
        if (!ocamlBinary.endsWith("ocaml")) return null;
        // is wsl
        WslPath path = WslPath.parseWindowsUncPath(ocamlBinary);
        if (path == null) return null;
        // OK let's start
        LOG.debug("Detected WSL "+path.getDistribution()+" for "+ocamlBinary);
        WSLDistribution distribution = path.getDistribution();
        // get path to ocamlc
        String ocamlc = distribution.getWslPath(ocamlBinary + "c");
        if (ocamlc == null) {
            LOG.debug("ocamlc not found for "+ocamlBinary);
            return null;
        }
        // get sources
        String root = ocamlc.replace("bin/ocamlc", "");
        String sourcesFolder = null;
        for (String s:getOCamlSourcesFolders()) {
            String sourcePath = root + s;
            // try to convert to WSL path
            String sourceCandidate = distribution.getWindowsPath(sourcePath);
            // Exists?
            if (!Files.exists(Path.of(sourceCandidate))) continue;
            // OK
            sourcesFolder = sourceCandidate;
            break;
        }
        if (sourcesFolder == null) {
            LOG.debug("No sources folder");
            return null;
        }

        // ocamlc -version
        try {
            GeneralCommandLine cli = new GeneralCommandLine(ocamlc, "-version");
            cli = distribution.patchCommandLine(cli, null, new WSLCommandLineOptions());
            LOG.debug("CLI is: "+cli.getCommandLineString());
            Process process = cli.createProcess();
            InputStream inputStream = process.getInputStream();
            String version = new String(inputStream.readAllBytes()).trim();
            LOG.debug("Version of "+ocamlc+" is '"+version+"'.");
            // if we got something better (ex: 4.05.0+mingw64, or 4.05.0+local)
            String alt = OCamlSdkVersionManager.parse(ocamlBinary);
            if (!OCamlSdkVersionManager.isUnknownVersion(alt)) version = alt;
            return new AssociatedBinaries(ocamlBinary, ocamlBinary + "c", sourcesFolder, version);
        } catch (ExecutionException | IOException e) {
            LOG.debug(e.getMessage());
        }
        return null;
    }

    @Override public @Nullable GeneralCommandLine getCompilerVersionCLI(String ocamlcCompilerPath) {
        return null;
    }

    @Override public @Nullable GeneralCommandLine getREPLCommand(String sdkHomePath) {
        // is wsl
        WslPath path = WslPath.parseWindowsUncPath(sdkHomePath);
        if (path == null) return null;
        try {
            String ocaml = path.getLinuxPath()+"/bin/ocaml";
            GeneralCommandLine cli = new GeneralCommandLine(ocaml, "-noprompt", "-no-version");
            return path.getDistribution().patchCommandLine(cli, null, new WSLCommandLineOptions());
        } catch (ExecutionException e) {
            LOG.error("Error creating REPL command", e);
            return null;
        }
    }

    @Override
    public @Nullable CompileWithCmtInfo getCompileCommandWithCmt(String sdkHomePath, String rootFolderForTempering, String file, String outputDirectory, String executableName) {
        // is wsl
        WslPath path = WslPath.parseWindowsUncPath(sdkHomePath);
        if (path == null) return null;
        try {
            WSLDistribution distribution = path.getDistribution();
            String wslOutputDirectory = distribution.getWslPath(outputDirectory);
            if (wslOutputDirectory == null)
                throw new ExecutionException("Could not parse output directory:"+outputDirectory);
            String wslFile = distribution.getWslPath(file);
            if (wslFile == null)
                throw new ExecutionException("Could not parse file:"+file);

            // create cli
            GeneralCommandLine cli = CompileWithCmtInfo.createAnnotatorCommand(
                    path.getLinuxPath()+"/bin/ocamlc",
                    wslFile, wslOutputDirectory + "/" + executableName,
                    wslOutputDirectory, outputDirectory /* use OS working directory */
            );
            cli = distribution.patchCommandLine(cli, null, new WSLCommandLineOptions());
            String wslRootFolderForTempering = distribution.getWslPath(rootFolderForTempering);
            if (wslRootFolderForTempering == null)
                throw new ExecutionException("Could not parse rootFolder:"+rootFolderForTempering);
            return new CompileWithCmtInfo(cli, wslRootFolderForTempering);
        } catch (ExecutionException e) {
            LOG.error("Error creating Compiler command", e);
            return null;
        }
    }

    //
    // SDK
    //

    @Override public @NotNull List<String> getOCamlCompilerCommands() {
        return List.of("ocamlc", "ocamlc.opt"); // because ocamlc is not valid on Windows
    }

    @Override protected boolean canUseProviderForHome(@NotNull Path homePath) {
        if (!WSLUtil.isSystemCompatible()) return false;
        String windowsUncPath = homePath.toFile().getAbsolutePath();
        String path = FileUtil.toSystemDependentName(windowsUncPath);
        if (!path.startsWith(WslConstants.UNC_PREFIX)) return false;
        path = StringUtil.trimStart(path, WslConstants.UNC_PREFIX);
        int index = path.indexOf('\\');
        return index > 0;
    }

    @Override protected @Nullable Boolean handleSymlinkHomePath(Path homePath) {
        WslPath path = WslPath.parseWindowsUncPath(homePath.toFile().getAbsolutePath());
        if (path == null) return null;
        WSLDistribution distribution = path.getDistribution();

        // they are the ONLY path allowed in an SDK, by definition
        // other paths that were allowed in other places, are not directly
        // used with SDK, they will be renamed, etc., so that they match
        // the SDK expected file structure
        String ocaml = homePath.resolve("bin/ocaml").toFile().getAbsolutePath();
        String compiler = homePath.resolve("bin/ocamlc").toFile().getAbsolutePath();
        String sources = homePath.resolve("lib/ocaml/").toFile().getAbsolutePath();

        //noinspection DuplicatedCode
        ocaml = distribution.getWslPath(ocaml);
        if (ocaml == null) return null;
        compiler = distribution.getWslPath(compiler);
        if (compiler == null) return null;
        sources = distribution.getWslPath(sources);
        if (sources == null) return null;

        GeneralCommandLine cli = new GeneralCommandLine("true");
        WSLCommandLineOptions wslCommandLineOptions = new WSLCommandLineOptions();
        // -L -> symlink
        wslCommandLineOptions.addInitCommand(
                "(if [ -L " + ocaml + " ]; then" +
                        " if [ -L " + compiler + " ]; then" +
                        " if [ -L " + sources + " ]; then echo 0; else echo -3; fi;" +
                        " else echo -2; fi;" +
                        " else echo -1; fi;)"
        );
        try {
            cli = distribution.patchCommandLine(cli, null, wslCommandLineOptions);
            LOG.debug("The CLI was: " + cli.getCommandLineString());
            Process process = cli.createProcess();
            process.waitFor();
            int exitCode = Integer.parseInt(
                    new String(process.getInputStream().readAllBytes()).replace("\n", "")
            );
            LOG.debug("code:"+exitCode);
            return exitCode == 0;
        } catch (ExecutionException | InterruptedException | IOException | NumberFormatException e) {
            return null;
        }
    }
}
