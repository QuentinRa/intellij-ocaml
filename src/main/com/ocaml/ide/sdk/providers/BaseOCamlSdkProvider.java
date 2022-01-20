package com.ocaml.ide.sdk.providers;

import org.jetbrains.annotations.NotNull;

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

    @Override public @NotNull List<OCamlSdkProvider> getNestedProviders() {
        return new ArrayList<>();
    }

    // methods

    @Override public @NotNull Set<String> getOCamlExecutablePathCommands() {
        return Set.of("ocaml");
    }

    @Override public @NotNull Set<String> getOCamlCompilerExecutablePathCommands() {
        return Set.of("ocamlc");
    }

    @Override public @NotNull Set<String> getOCamlSourcesFolders() {
        return Set.of("lib/ocaml", "usr/lib/ocaml", "usr/local/lib/ocaml");
    }
}
