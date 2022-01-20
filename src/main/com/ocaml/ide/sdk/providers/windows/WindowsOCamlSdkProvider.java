package com.ocaml.ide.sdk.providers.windows;

import com.ocaml.ide.sdk.providers.BaseOCamlSdkProvider;
import com.ocaml.ide.sdk.providers.OCamlSdkProvider;
import com.ocaml.ide.sdk.providers.utils.AssociatedBinaries;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Set;

/**
 * Windows is not natively supporting ocaml.
 * We are calling providers such as, WSL, Cygwin, Ocaml64, etc.
 */
public class WindowsOCamlSdkProvider extends BaseOCamlSdkProvider {

    // There is no such thing on Windows

    @Override public @NotNull List<String> getOCamlCompilerExecutablePathCommands() {
        return List.of();
    }

    @Override public @NotNull Set<String> getOCamlExecutablePathCommands() {
        return Set.of();
    }

    @Override public @Nullable AssociatedBinaries getAssociatedBinaries(@NotNull String ocamlBinary) {
        return null;
    }

    // but, we can install these

    @Override public @NotNull List<OCamlSdkProvider> getNestedProviders() {
        List<OCamlSdkProvider> nestedProviders = super.getNestedProviders();
        nestedProviders.add(new CygwinSdkProvider());
        nestedProviders.add(new OCaml64SdkProvider());
        return nestedProviders;
    }
}
