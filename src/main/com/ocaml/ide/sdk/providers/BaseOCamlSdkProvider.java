package com.ocaml.ide.sdk.providers;

import com.intellij.execution.ExecutionException;
import com.intellij.execution.configurations.GeneralCommandLine;
import com.ocaml.compiler.OCamlSdkVersionManager;
import com.ocaml.ide.sdk.providers.utils.AssociatedBinaries;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Provide the most common values for a method. Subclasses may override
 * these if they are not valid for them.
 */
public class BaseOCamlSdkProvider implements OCamlSdkProvider {

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

    @Override public @NotNull Set<String> getOCamlCompilerExecutablePathCommands() {
        return Set.of("ocamlc");
    }

    @Override public @NotNull Set<String> getOCamlSourcesFolders() {
        return Set.of("lib/ocaml", "usr/lib/ocaml", "usr/local/lib/ocaml");
    }

    // compiler

    @Override public @Nullable AssociatedBinaries getAssociatedBinaries(@NotNull String ocamlBinary) {
        if (!canUseProviderForOCamlBinary(ocamlBinary)) return null;
        // check file exists
        Path ocamlBinPath = Path.of(ocamlBinary);
        System.out.println("ici:"+ocamlBinary);
        if(!Files.exists(ocamlBinPath)) return null;
        System.out.println("found1");
        Path binPath = ocamlBinPath.getParent();
        if(!Files.exists(binPath)) return null;
        System.out.println("found2");
        Path root = binPath.getParent();
        if(!Files.exists(root)) return null;
        System.out.println("found3");
        String sourceFolder = null;

        // find a valid source folder
        for (String source : getOCamlSourcesFolders()) {
            Path sourcePath = root.resolve(source);
            System.out.println("sp:"+sourcePath);
            if (!Files.exists(sourcePath)) continue;
            sourceFolder = sourcePath.toFile().getAbsolutePath();
            break;
        }
        System.out.println("source="+sourceFolder);
        if (sourceFolder == null) return null;

        // testing compilers
        for (String compilerName: getOCamlCompilerExecutablePathCommands()) {
            System.out.println("with:"+compilerName);
            Path compilerPath = binPath.resolve(compilerName);
            if(!Files.exists(binPath)) continue;
            String compiler = compilerPath.toFile().getAbsolutePath();

            String version;

            // try to find the version using the compiler
            GeneralCommandLine cli = OCamlSdkProvidersManager.INSTANCE.getCompilerVersionCLI(compiler);
            System.out.println("cli1:"+cli);
            if (cli == null) continue;
            System.out.println("cli2:"+cli.getCommandLineString());
            try {
                Process process = cli.createProcess();
                InputStream inputStream = process.getInputStream();
                version = new String(inputStream.readAllBytes()).trim();
                // if we got something better
                String alt = OCamlSdkVersionManager.parse(ocamlBinary);
                if (!OCamlSdkVersionManager.isUnknownVersion(alt)) version = alt;
            } catch (ExecutionException | IOException e) {
                continue;
            }

            return new AssociatedBinaries(compiler, sourceFolder, version);
        }
        return null;
    }

    // commands

    @Override public @Nullable GeneralCommandLine getCompilerVersionCLI(String ocamlcCompilerPath) {
        return new GeneralCommandLine(ocamlcCompilerPath, "-version");
    }
}
