package com.ocaml.sdk.utils;

import com.ocaml.sdk.providers.OCamlSdkProvidersManager;
import com.ocaml.sdk.providers.utils.InvalidHomeError;
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
     * They are sorted by version, from newest to oldest.
     */
    public static @NotNull List<String> suggestHomePaths() {
        // wrap
        ArrayList<String> homes = new ArrayList<>(OCamlSdkProvidersManager.INSTANCE.suggestHomePaths());
        // reverse order (newer -> older)
        homes.sort((o1, o2) -> OCamlSdkVersionManager.comparePaths(o2, o1));
        return homes;
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

    public static @Nullable InvalidHomeError invalidHomeErrorMessage(@NotNull Path homePath) {
        return OCamlSdkProvidersManager.INSTANCE.isHomePathValidErrorMessage(homePath);
    }
}
