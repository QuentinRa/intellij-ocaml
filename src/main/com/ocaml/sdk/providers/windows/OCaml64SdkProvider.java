package com.ocaml.sdk.providers.windows;

import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Set;

/**
 * The following installer https://fdopen.github.io/opam-repository-mingw/installation/
 * is installing opam and ocaml mingw using cygwin.
 */
public class OCaml64SdkProvider extends CygwinSdkProvider {

    @Override public @NotNull List<String> getOCamlCompilerCommands() {
        return List.of("ocamlc.exe", "ocamlc.opt.exe");
    }

    // the installation folder is not the cygwin64 :)
    @Override public @NotNull Set<String> getInstallationFolders() {
        return Set.of("OCaml64", getCygwinOpamFolder("OCaml64"));
    }
}
