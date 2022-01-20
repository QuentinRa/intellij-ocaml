package com.ocaml.compiler.simple;

import com.esotericsoftware.minlog.Log;
import com.intellij.execution.ExecutionException;
import com.intellij.execution.configurations.GeneralCommandLine;
import com.intellij.execution.configurations.PathEnvironmentVariableUtil;
import com.intellij.openapi.diagnostic.Logger;
import com.ocaml.ide.sdk.providers.OCamlSdkProvider;
import com.ocaml.ide.sdk.providers.OCamlSdkProvidersManager;
import com.ocaml.utils.logs.OCamlLogger;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Set;

public final class OCamlNativePathDetector {

    private static final Logger LOG = OCamlLogger.getSdkProviderInstance();

    /**
     * Look for ocaml and ocamlc in the path.<br>
     * Check the version of ocaml using the compiler.<br>
     * Look for the sources.<br>
     * If we found everything, then we return the DetectionResult,
     * otherwise, we return null.
     * @see OCamlSdkProvider#getOCamlExecutablePathCommands()
     * @see OCamlSdkProvider#getOCamlCompilerExecutablePathCommands()
     * @see OCamlSdkProvider#getOCamlSourcesFolders()
     */
    public static @NotNull DetectionResult detectNativeSdk() {
        Set<String> ocamlExecutablesNames = OCamlSdkProvidersManager.INSTANCE.getOCamlExecutablePathCommands();
        // check every binary
        for (String executableName: ocamlExecutablesNames) {
            File f = PathEnvironmentVariableUtil.findInPath(executableName);
            if (f == null || !f.exists()) continue;
            Log.debug("found binary: " + executableName);
            // look for the compiler
            DetectionResult res = detectNativeSdk(f.getAbsolutePath());
            Log.debug("Result binary:"+res);
            if (!res.isError) return res;
        }
        return DetectionResult.NO_ASSOCIATED_BINARIES;
    }

    /**
     * At this point, ocamlBinary is valid
     */
    @ApiStatus.Internal
    public static DetectionResult detectNativeSdk(String ocamlBinary) {
        Set<String> compilerNames = OCamlSdkProvidersManager.INSTANCE.getOCamlCompilerExecutablePathCommands();

        // check every compiler
        for (String compilerName: compilerNames) {
            File f = PathEnvironmentVariableUtil.findInPath(compilerName);
            if (f == null || !f.exists()) continue;
            Log.debug("found compiler: "+compilerName);
            // look for the sources' folder
            DetectionResult res = detectNativeSdk(ocamlBinary, f.getAbsolutePath());
            Log.debug("Result compiler:"+res);
            if (!res.isError) return res;
        }

        return DetectionResult.NO_ASSOCIATED_BINARIES;
    }

    /**
     * At this point, both ocamlBinary, and ocamlcCompiler are valid
     */
    @ApiStatus.Internal
    public static DetectionResult detectNativeSdk(String ocamlBinary, String ocamlcCompiler) {
        try {
            Path binOCaml = Path.of(ocamlBinary);
            if(!Files.exists(binOCaml)) throw new IllegalArgumentException(ocamlBinary+" not found");
            Path bin = binOCaml.getParent();
            if(!Files.exists(bin)) throw new IllegalArgumentException("bin folder not found");
            Path root = bin.getParent();
            if(!Files.exists(root)) throw new IllegalArgumentException("root folder not found");

            Set<String> sources = OCamlSdkProvidersManager.INSTANCE.getOCamlSourcesFolders();
            // check every sources' folder
            for (String source: sources) {
                Path lib = root.resolve(source);
                if(!Files.exists(lib)) continue;
                Log.debug("Found sources:"+source);
                // check the version
                DetectionResult res = detectNativeSdk(ocamlBinary, ocamlcCompiler, lib.toFile().getAbsolutePath());
                Log.debug("Result sources:"+res);
                if (!res.isError) return res;
            }
        } catch (Throwable e) {
            LOG.error(e.getMessage(), e);
        }

        return DetectionResult.NO_ASSOCIATED_BINARIES;
    }

    /**
     * At this point, both ocamlBinary,  ocamlcCompiler, and sourcesFolder are valid
     */
    @ApiStatus.Internal
    public static DetectionResult detectNativeSdk(String ocamlBinary,
                                                  String ocamlcCompiler,
                                                  String sourcesFolder) {
        GeneralCommandLine cli = OCamlSdkProvidersManager.INSTANCE.getCompilerVersionCLI(ocamlcCompiler);
        if (cli == null) {
            Log.error("Cli was null for "+ocamlcCompiler);
            return DetectionResult.NO_ASSOCIATED_BINARIES;
        }

        Log.debug("CLI:"+cli.getCommandLineString());

        try {
            Process process = cli.createProcess();
            InputStream inputStream = process.getInputStream();
            String version = new String(inputStream.readAllBytes()).trim();

            Log.debug("Found version: '"+version+"'");

            return new DetectionResult(ocamlBinary, ocamlcCompiler, version, sourcesFolder);
        } catch (ExecutionException | IOException e) {
            Log.error("Couldn't find compiler version: "+e.getMessage());
            return DetectionResult.NO_ASSOCIATED_BINARIES;
        }
    }

}
