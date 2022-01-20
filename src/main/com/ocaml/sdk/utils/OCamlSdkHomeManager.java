package com.ocaml.sdk.utils;

import com.ocaml.sdk.providers.OCamlSdkProvidersManager;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

/**
 * Find OCaml Homes
 **/
public final class OCamlSdkHomeManager {

    /**
     * Tries to find existing OCaml SDKs on this computer.
     */
    public static @NotNull List<String> suggestHomePaths() {
        // wrap
        return new ArrayList<>(OCamlSdkProvidersManager.INSTANCE.suggestHomePaths());
    }

    public static @Nullable String defaultOCamlLocation() {
        return null;
    }

    public static boolean isValid(@NotNull String homePath) {
        return isValid(Path.of(homePath));
    }

    public static boolean isValid(@NotNull Path homePath) {
        return Boolean.TRUE.equals(OCamlSdkProvidersManager.INSTANCE.isHomePathValid(homePath));
    }
}
