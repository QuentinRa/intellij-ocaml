package com.ocaml.ide.sdk.providers.windows;

import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * The following installer https://fdopen.github.io/opam-repository-mingw/installation/
 * is installing opam and ocaml mingw using cygwin.
 */
public class OCaml64SdkProvider extends CygwinSdkProvider  {

    // the installation folder is not the cygwin64
    @Override protected boolean canUseProviderForOCamlBinary(@NotNull String path) {
        return path.contains("OCaml64") && path.endsWith(OCAML_EXE);
    }

    @Override public @NotNull List<String> getOCamlCompilerExecutablePathCommands() {
        return List.of("ocamlc.exe", "ocamlc.opt.exe");
    }
}
