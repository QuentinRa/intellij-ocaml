package com.ocaml.sdk.providers.utils;

import com.intellij.execution.configurations.GeneralCommandLine;
import org.jetbrains.annotations.NotNull;

public final class CompileWithCmtInfo {

    /**
     * ocamlc
     * -c $file
     * -o $outputDirectory/$executableName
     * -I $outputDirectory
     * -w +A
     * -color=never
     * -bin-annot
     */
    @NotNull public final GeneralCommandLine cli;
    /**
     * This root is used by {@link com.ocaml.ide.highlight.annotations.OCamlMessageAdaptor#temperPaths(String, String)}
     * to provide an OS-independent path in the messages.
     */
    @NotNull public final String rootFolderForTempering;

    public CompileWithCmtInfo(@NotNull GeneralCommandLine cli,
                              @NotNull String rootFolderForTempering) {
        this.cli = cli;

        // must ends with a trailing slash
        if (!rootFolderForTempering.endsWith("/") && !rootFolderForTempering.endsWith("\\"))
            rootFolderForTempering += rootFolderForTempering.contains("/") ? "/" : "\\";

        this.rootFolderForTempering = rootFolderForTempering;
    }

    /**
     * ocamlc
     * -c $file
     * -o $outputDirectory/$executableName
     * -I $outputDirectory
     * -w +A
     * -color=never
     * -bin-annot
     */
    public static @NotNull GeneralCommandLine createAnnotatorCommand(String compiler, @NotNull String file, String outputFile,
                                                                     String outputDirectory, String workingDirectory) {
        GeneralCommandLine cli = new GeneralCommandLine(compiler);
        if (file.endsWith(".mli")) cli.addParameter("-c");
        // compile everything else
        cli.addParameters(file, "-o", outputFile,
                "-I", outputDirectory,
                "-w", "+A", "-color=never", "-bin-annot");
        // fix issue -I is adding, so the current directory
        // is included, and this may lead to problems later (ex: a file.cmi may be
        // used instead of the one in the output directory, because we found one in the
        // current directory)
        cli.setWorkDirectory(workingDirectory);
        // needed otherwise the input stream ~~may be~~~ is empty
        cli.setRedirectErrorStream(true);
        return cli;
    }
}
