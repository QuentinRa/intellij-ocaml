package com.ocaml.sdk.providers.windows;

import com.intellij.execution.configurations.GeneralCommandLine;
import com.intellij.openapi.util.io.FileUtil;
import com.intellij.util.SystemProperties;
import com.ocaml.sdk.providers.utils.CompileWithCmtInfo;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Set;

/**
 * Cygwin
 * <ul>
 *     <li>files are ending with ".exe"</li>
 *     <li>
 *         sometimes "ocamlc.exe" is not valid (if the user isn't using mingw), so we need
 *         to use "ocamlc.opt.exe"
 *      </li>
 * </ul>
 */
public class CygwinSdkProvider extends AbstractWindowsBaseProvider {

    protected static final String OCAML_EXE = "ocaml.exe";

    // Utils

    @Override
    protected boolean canUseProviderForOCamlBinary(@NotNull String path) {
        path = FileUtil.toSystemIndependentName(path);
        if (!path.endsWith(OCAML_EXE)) return false;
        for (String folder : getInstallationFolders()) {
            if (path.contains(folder)) return true;
        }
        return false;
    }

    @Override protected boolean canUseProviderForHome(@NotNull Path homePath) {
        return canUseProviderForHome(homePath.toFile().getAbsolutePath());
    }

    protected boolean canUseProviderForHome(@NotNull String homePath) {
        String homePathString = FileUtil.toSystemIndependentName(homePath);
        for (String folder : getInstallationFolders()) {
            if (homePathString.contains(folder)) return true;
        }
        return false;
    }

    protected @NotNull String getCygwinOpamFolder(@NotNull String baseFolderName) {
        return baseFolderName + "\\home\\" + SystemProperties.getUserName() + "\\.opam\\";
    }

    // Implementation

    @Override public @NotNull Set<String> getOCamlTopLevelCommands() {
        return Set.of(OCAML_EXE);
    }

    @Override public @NotNull List<String> getOCamlCompilerCommands() {
        return List.of("ocamlc.opt.exe", "ocamlc.exe");
    }

    @Override public @NotNull Set<String> getInstallationFolders() {
        return Set.of("cygwin64", "cygwin", getCygwinOpamFolder("cygwin64"));
    }

    // commands

    @Override public @Nullable GeneralCommandLine getREPLCommand(String sdkHomePath) {
        if (!canUseProviderForHome(sdkHomePath)) return null;
        GeneralCommandLine cli = new GeneralCommandLine(sdkHomePath + "\\bin\\" + OCAML_EXE, "-no-version");
        cli.withEnvironment("OCAMLLIB", sdkHomePath + "\\lib\\ocaml");
        return cli;
    }

    @Override
    public @Nullable CompileWithCmtInfo getCompileCommandWithCmt(String sdkHomePath, String rootFolderForTempering, String file, String outputDirectory, String executableName) {
        if (!canUseProviderForHome(sdkHomePath)) return null;
        Path homePath = Path.of(sdkHomePath);
        // look for a compiler
        for (String compilerName : getOCamlCompilerCommands()) {
            if (!Files.exists(homePath.resolve("bin/" + compilerName))) continue;
            // use this compiler
            GeneralCommandLine cli = CompileWithCmtInfo.createAnnotatorCommand(
                    sdkHomePath + "\\bin\\" + compilerName,
                    file, outputDirectory + "\\" + executableName,
                    outputDirectory, outputDirectory
            );
            cli.withEnvironment("OCAMLLIB", sdkHomePath + "\\lib\\ocaml");
            return new CompileWithCmtInfo(cli, rootFolderForTempering);
        }

        LOG.warn("No compiler found for cygwin in " + sdkHomePath + ".");
        return null;
    }

    @Override protected @NotNull String getDuneExecutable(String sdkHomePath) {
        return sdkHomePath + "\\bin\\dune.exe";
    }
}
