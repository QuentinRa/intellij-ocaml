package com.ocaml.sdk.providers.utils;

import com.intellij.execution.configurations.GeneralCommandLine;
import com.ocaml.ide.insight.annotations.OCamlMessageAdaptor;
import com.ocaml.utils.ImplementationNote;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

@ImplementationNote(
        since = "0.0.8",
        note = "-annot is deprecated since 4.13. An alternative is to use" +
                "./ocamlcmt -annot file.cmt. You can generate a .cmt with -bin-annot." +
                "As this still available in 4.14, I won't update :D.")
public final class CompileWithCmtInfo {

    public static final String OUTPUT_EXTENSION = ".out";

    /**
     * ocamlc
     * -c $file
     * -o $outputDirectory/$executableName
     * -I $outputDirectory
     * -w +A
     * -color=never
     * -annot
     */
    @NotNull public final GeneralCommandLine cli;

    /**
     * This root is used by {@link OCamlMessageAdaptor#temperPaths(String, String)}
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
     * @return the extension of the annotation file, without the dot (".").
     */
    @Contract(pure = true) public @NotNull String getAnnotationFileExtension() {
        return "annot";
    }

    /**
     * ocamlc
     * -c $file
     * -o $outputDirectory/$executableName+OUTPUT_EXTENSION
     * -I $outputDirectory
     * -w +A
     * -color=never
     * -annot
     */
    public static @NotNull GeneralCommandLine createAnnotatorCommand(String compiler, @NotNull String file, String outputFile,
                                                                     String outputDirectory, String workingDirectory) {
        GeneralCommandLine cli = new GeneralCommandLine(compiler);
        if (file.endsWith(".mli")) cli.addParameter("-c");
        // compile everything else
        // fix #71: adding extension
        cli.addParameters(file, "-o", outputFile + OUTPUT_EXTENSION,
                "-I", outputDirectory,
                "-w", "+A", "-color=never", "-annot");
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
