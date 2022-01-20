package com.ocaml.ide.sdk.providers;

import com.intellij.execution.configurations.GeneralCommandLine;
import com.ocaml.ide.sdk.providers.utils.AssociatedBinaries;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Set;

public interface OCamlSdkProvider {

    /**
     * If a provider is made of multiples providers, you shall
     * return them using this method.
     * @return null or a list of providers
     */
    @NotNull List<OCamlSdkProvider> getNestedProviders();

    //
    // PATH
    //

    /** A list of commands that are starting the
     * ocaml interactive toplevel, if the command is in the path
     * ex: "ocaml" */
    @NotNull Set<String> getOCamlExecutablePathCommands();

    /** A list of commands that are used to compile
     * ocaml files, if the command is in the path.
     * <br>
     * Values must be sorted by what's the most likely to be a
     * valid value.
     * <br>
     * Ex: "ocamlc" */
    @NotNull List<String> getOCamlCompilerExecutablePathCommands();

    /**
     * The native folders in which sources may be stored.
     * The path is relative to the SDK root folder.
     * <br>
     * Values must be sorted by what's the most likely to be a
     * valid value.
     * <br>
     * Ex: for /bin/ocaml, the root folder is "/" meaning that if the
     * sources are in /lib/ocaml, then you should return "lib/ocaml"
     */
    @NotNull List<String> getOCamlSourcesFolders();

    //
    // Compiler
    //

    /**
     * The provider will try to return the associated compiler, if possible.
     * @param ocamlBinary the path to the ocaml binary, may be invalid
     * @return null of the path to the ocamlc binary
     */
    @Nullable AssociatedBinaries getAssociatedBinaries(@NotNull String ocamlBinary);

    //
    // SDK
    //

    /**
     * Usual installations folders
     * @return a list of installation folder.
     * Paths may be relatives or absolutes.
     */
    @NotNull Set<String> getInstallationFolders();

    /** Tries to find existing OCaml SDKs on this computer. */
    @NotNull Set<String> suggestHomePaths();

    //
    // Commands
    //

    /**
     * Return a command line that can be used to get the version
     * of the compiler
     * @param ocamlcCompilerPath path to the ocaml compiler
     * @return "ocamlc -version"
     *         or null if this provider cannot generate a command for this compiler
     */
    @Nullable GeneralCommandLine getCompilerVersionCLI(String ocamlcCompilerPath);
}
