package com.ocaml.sdk.providers;

import com.intellij.execution.ExecutionException;
import com.intellij.execution.configurations.GeneralCommandLine;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.util.SystemProperties;
import com.ocaml.sdk.providers.utils.OCamlSdkScanner;
import com.ocaml.sdk.utils.OCamlSdkVersionManager;
import com.ocaml.sdk.providers.utils.AssociatedBinaries;
import com.ocaml.utils.logs.OCamlLogger;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

/**
 * Provide the most common values for a method. Subclasses may override
 * these if they are not valid for them.
 */
public class BaseOCamlSdkProvider implements OCamlSdkProvider {

    protected static final Logger LOG = OCamlLogger.getSdkProviderInstance();

    public BaseOCamlSdkProvider() {
    }

    // providers

    protected boolean canUseProviderForOCamlBinary(@NotNull String path) {
        return path.endsWith("ocaml");
    }

    protected boolean canUseProviderForHome(@NotNull Path homePath) {
        return true;
    }

    @Override public @NotNull List<OCamlSdkProvider> getNestedProviders() {
        return new ArrayList<>();
    }

    // path

    @Override public @NotNull Set<String> getOCamlTopLevelCommands() {
        return Set.of("ocaml");
    }

    @Override public @NotNull List<String> getOCamlCompilerCommands() {
        return List.of("ocamlc");
    }

    @Override public @NotNull List<String> getOCamlSourcesFolders() {
        return List.of("lib/ocaml", "usr/lib/ocaml", "usr/local/lib/ocaml");
    }

    // compiler

    @Override public @Nullable AssociatedBinaries getAssociatedBinaries(@NotNull String ocamlBinary) {
        if (!canUseProviderForOCamlBinary(ocamlBinary)) return null;
        // check files exists
        Path ocamlBinPath = Path.of(ocamlBinary);
        if(!Files.exists(ocamlBinPath)) {
            LOG.debug("binary not found: "+ocamlBinary);
            return null;
        }
        Path binPath = ocamlBinPath.getParent();
        if(!Files.exists(binPath)) { // useless?
            LOG.debug("bin folder not found for "+ocamlBinary);
            return null;
        }
        Path root = binPath.getParent();
        if(!Files.exists(root)) {  // useless?
            LOG.debug("root folder not found for "+ocamlBinary);
            return null;
        }
        String sourceFolder = null;

        // find a valid source folder
        for (String source : getOCamlSourcesFolders()) {
            Path sourcePath = root.resolve(source);
            if (!Files.exists(sourcePath)) continue;
            sourceFolder = sourcePath.toFile().getAbsolutePath();
            break;
        }
        if (sourceFolder == null) {
            LOG.debug("Sources' folder not found for "+ocamlBinary);
            return null;
        }

        // testing compilers
        for (String compilerName: getOCamlCompilerCommands()) {
            LOG.debug("testing "+compilerName);
            Path compilerPath = binPath.resolve(compilerName);
            if(!Files.exists(binPath)) continue;
            String compiler = compilerPath.toFile().getAbsolutePath();

            String version;

            // try to find the version using the compiler
            GeneralCommandLine cli = OCamlSdkProvidersManager.INSTANCE.getCompilerVersionCLI(compiler);
            if (cli == null) {
                LOG.debug("No cli for "+compiler);
                continue;
            }
            LOG.debug("CLI for "+compiler+" is "+cli.getCommandLineString());
            try {
                Process process = cli.createProcess();
                InputStream inputStream = process.getInputStream();
                version = new String(inputStream.readAllBytes()).trim();
                LOG.info("Version of "+compiler+" is '"+version+"'.");
                // if we got something better
                String alt = OCamlSdkVersionManager.parse(ocamlBinary);
                if (!OCamlSdkVersionManager.isUnknownVersion(alt)) version = alt;
            } catch (ExecutionException | IOException e) {
                LOG.debug("Command failed:"+e.getMessage());
                continue;
            }

            return new AssociatedBinaries(ocamlBinary, compiler, sourceFolder, version);
        }

        LOG.warn("No compiler found for "+ocamlBinary);
        return null;
    }

    @Override public @NotNull Set<String> getAssociatedSourcesFolders(@NotNull String sdkHome) {
        return Set.of(
                "lib",
                ".opam-switch/sources/" // we may have this one in opam SDKs
        );
    }

    // commands

    @Override public @NotNull Set<String> getInstallationFolders() {
        Set<String> installationFolders = new HashSet<>();
        // we know that we may find opam
        installationFolders.add(SystemProperties.getUserHome()+".opam");
        // is there any other places in which we may find ocaml SDKs?
        // ...
        return installationFolders;
    }

    @Override public @NotNull Set<String> suggestHomePaths() {
        Set<Path> roots = new HashSet<>();
        for (String folder:getInstallationFolders()) {
            roots.add(Path.of(folder));
        }
        return OCamlSdkScanner.scanAll(roots, true);
    }

    @Override public @Nullable Boolean isHomePathValid(@NotNull Path homePath) {
        if (!canUseProviderForHome(homePath)) return null;
        // version
        boolean ok = OCamlSdkVersionManager.isValid(homePath.toFile().getName());
        if (!ok) {
            LOG.debug("Not a valid home name: "+homePath);
            return false;
        }
        // interactive toplevel
        boolean hasTopLevel = false;
        for (String exeName: getOCamlTopLevelCommands()) {
            hasTopLevel = Files.exists(homePath.resolve("bin/"+exeName));
            if (hasTopLevel) break;
        }
        if (!hasTopLevel) {
            LOG.debug("Not top level found for "+homePath);
            return false;
        }
        // compiler
        boolean hasCompiler = false;
        for (String compilerName: getOCamlCompilerCommands()) {
            hasCompiler = Files.exists(homePath.resolve("bin/"+compilerName));
            if (hasCompiler) break;
        }
        if (!hasCompiler) {
            LOG.debug("Not compiler found for "+homePath);
            return false;
        }
        // sources
        boolean hasSources = false;
        for (String sourceFolder: getOCamlSourcesFolders()) {
            hasSources = Files.exists(homePath.resolve(sourceFolder));
            if (hasSources) break;
        }
        if (!hasSources) LOG.debug("Not sources found for "+homePath);
        return hasSources;
    }

    // sdk

    @Override public @Nullable GeneralCommandLine getCompilerVersionCLI(String ocamlcCompilerPath) {
        return new GeneralCommandLine(ocamlcCompilerPath, "-version");
    }
}
