package com.ocaml.ide.sdk.providers.windows;

import com.ocaml.ide.sdk.providers.BaseOCamlSdkProvider;
import com.ocaml.ide.sdk.providers.OCamlSdkProvider;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Set;

/**
 * Windows is not natively supporting ocaml.
 * We are calling providers such as, WSL, Cygwin, Ocaml64, etc.
 */
public class WindowsOCamlSdkProvider extends BaseOCamlSdkProvider {

    // There is no such thing on Windows

    @Override public @NotNull Set<String> getOCamlCompilerExecutablePathCommands() {
        return Set.of();
    }

    @Override public @NotNull Set<String> getOCamlExecutablePathCommands() {
        return Set.of();
    }

    // but, we can install these

    @Override public @NotNull List<OCamlSdkProvider> getNestedProviders() {
        List<OCamlSdkProvider> nestedProviders = super.getNestedProviders();
        nestedProviders.add(new CygwinSdkProvider());
        return nestedProviders;
    }
}
