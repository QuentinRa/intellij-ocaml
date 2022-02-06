package com.ocaml.sdk.providers;

import com.intellij.execution.configurations.GeneralCommandLine;
import com.ocaml.sdk.providers.utils.AssociatedBinaries;
import com.ocaml.sdk.providers.utils.CompileWithCmtInfo;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.nio.file.Path;
import java.util.List;
import java.util.Set;

public interface OCamlSdkProvider {

    /**
     * If a provider is made of multiples providers, you shall
     * return them using this method.
     *
     * @return null or a list of providers
     */
    @NotNull List<OCamlSdkProvider> getNestedProviders();

    //
    // PATH
    //

    /**
     * @return A list of commands that are starting the
     * ocaml interactive toplevel (ex: "ocaml")
     */
    @NotNull Set<String> getOCamlTopLevelCommands();

    /**
     * @return A list of commands that are used to compile
     * ocaml files, if the command is in the path.
     * <br>
     * Values must be sorted by what's the most likely to be a
     * valid value.
     * <br>
     * Ex: "ocamlc"
     */
    @NotNull List<String> getOCamlCompilerCommands();

    /**
     * @return The folders in which sources may be stored.
     * Values must be sorted by what's the most likely to be a
     * valid value. The path is relative to the SDK root folder.
     */
    @NotNull List<String> getOCamlSourcesFolders();

    /**
     * @param ocamlBinary a possible path to an opam SDK
     * @return true if this ocamlBinary is an opam binary
     */
    @Nullable Boolean isOpamBinary(@NotNull String ocamlBinary);

    /**
     * Create an SDK using binaries. They should be linked, not copied.
     *
     * @param ocaml       location to the ocaml binary
     * @param compiler    location of the compiler
     * @param version     version of the compiler
     * @param sources     location of the sources folder
     * @param sdkFolder   the folder, that may not exist, in which the SDK should be stored.
     * @param sdkModifier the name of the SDK should be sdkFolder/version followed by sdkModifier
     * @return a path to the created SDK, or null
     */
    @Nullable String createSdkFromBinaries(String ocaml, String compiler, String version,
                                           String sources, String sdkFolder, String sdkModifier);

    /**
     * The provider will try to return the associated compiler, if possible.
     *
     * @param ocamlBinary the path to the ocaml binary, may be invalid
     * @return null of the path to the ocamlc binary
     */
    @Nullable AssociatedBinaries getAssociatedBinaries(@NotNull String ocamlBinary);

    /**
     * @param sdkHome a valid sdk home
     * @return the path to the sources folders,
     * relatives to the sdk home. Usually, sources are only stored in "lib/".
     */
    @NotNull Set<String> getAssociatedSourcesFolders(@NotNull String sdkHome);

    /**
     * Usual installations folders
     *
     * @return a list of installation folder.
     * Paths may be relatives or absolutes.
     */
    @NotNull Set<String> getInstallationFolders();

    /**
     * @return tries to find existing OCaml SDKs on this computer.
     */
    @NotNull Set<String> suggestHomePaths();

    /**
     * Check if an homePath is valid, the method should be fast
     * if possible, or avoid heavy operations. You should ensure that
     * an SDK is stored inside a folder with a version ({@link com.ocaml.sdk.utils.OCamlSdkVersionManager#parse(String)})
     *
     * @param homePath an homePath
     * @return true if the homePath is valid for at least one provider
     * @see com.ocaml.sdk.utils.OCamlSdkVersionManager#parse(String)
     */
    @Nullable Boolean isHomePathValid(@NotNull Path homePath);

    //
    // Commands
    //

    /**
     * Return a command line that can be used to get the version
     * of the compiler
     *
     * @param ocamlcCompilerPath path to the ocaml compiler
     * @return "ocamlc -version"
     * or null if this provider cannot generate a command for this compiler
     */
    @Nullable GeneralCommandLine getCompilerVersionCLI(String ocamlcCompilerPath);

    /**
     * @param sdkHomePath path to the SDK home
     * @return "ocaml -no-version"
     */
    @Nullable GeneralCommandLine getREPLCommand(String sdkHomePath);

    /**
     * @param sdkHomePath            path to the SDK home
     * @param rootFolderForTempering Most of the time, the root is returned unchanged.
     *                               If the paths used are tempered, then this path too, should be tempered
     *                               (ex: WSL).
     * @param file                   the file we are compiling
     * @param outputDirectory        the output directory
     * @param executableName         the name of the generated executable
     * @return the command line with the root for tempering paths, or null
     */
    @Nullable CompileWithCmtInfo getCompileCommandWithCmt(String sdkHomePath,
                                                          String rootFolderForTempering,
                                                          String file,
                                                          String outputDirectory,
                                                          String executableName);
}
