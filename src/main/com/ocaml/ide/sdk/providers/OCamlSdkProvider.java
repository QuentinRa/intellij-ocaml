package com.ocaml.ide.sdk.providers;

import org.jetbrains.annotations.NotNull;

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
     * ocaml files, if the command is in the path
     * ex: "ocamlc" */
    @NotNull Set<String> getOCamlCompilerExecutablePathCommands();

    /**
     * The native folders in which sources may be stored.
     * The path is relative to the SDK root folder.
     * <br>
     * Ex: for /bin/ocaml, the root folder is "/" meaning that if the
     * sources are in /lib/ocaml, then you should return "lib/ocaml"
     */
    @NotNull Set<String> getOCamlSourcesFolders();
}
