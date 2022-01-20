package com.ocaml.ide.sdk.providers;

import com.esotericsoftware.minlog.Log;
import com.intellij.execution.ExecutionException;
import com.intellij.execution.configurations.GeneralCommandLine;
import com.intellij.openapi.diagnostic.Logger;
import com.ocaml.compiler.OCamlSdkVersionManager;
import com.ocaml.ide.sdk.providers.utils.AssociatedBinaries;
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

    private static final Logger LOG = OCamlLogger.getSdkProviderInstance();

    public BaseOCamlSdkProvider() {
    }

    // providers
    protected boolean canUseProviderForOCamlBinary(@NotNull String path) {
        return path.endsWith("ocaml");
    }

    @Override public @NotNull List<OCamlSdkProvider> getNestedProviders() {
        return new ArrayList<>();
    }

    // path

    @Override public @NotNull Set<String> getOCamlExecutablePathCommands() {
        return Set.of("ocaml");
    }

    @Override public @NotNull List<String> getOCamlCompilerExecutablePathCommands() {
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
        for (String compilerName: getOCamlCompilerExecutablePathCommands()) {
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
                Log.debug("Command failed:"+e.getMessage());
                continue;
            }

            return new AssociatedBinaries(compiler, sourceFolder, version);
        }

        Log.warn("No compiler found for "+ocamlBinary);
        return null;
    }

    // commands

    @Override public @Nullable GeneralCommandLine getCompilerVersionCLI(String ocamlcCompilerPath) {
        return new GeneralCommandLine(ocamlcCompilerPath, "-version");
    }
}
