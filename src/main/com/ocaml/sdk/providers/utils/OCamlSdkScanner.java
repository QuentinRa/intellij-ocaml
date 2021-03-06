package com.ocaml.sdk.providers.utils;

import com.ocaml.sdk.utils.OCamlSdkHomeManager;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.nio.file.Path;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class OCamlSdkScanner {

    public static @NotNull Set<String> scanAll(@NotNull Collection<? extends Path> files, boolean includeNestDirs) {
        Set<String> result = new HashSet<>();
        for (Path root : new HashSet<>(files)) {
            scanFolder(root, includeNestDirs, result);
        }
        return result;
    }

    public static void scanFolder(@NotNull Path folder, boolean includeNestDirs, @NotNull Collection<? super String> result) {
        // Check if the folder is valid
        if (OCamlSdkHomeManager.isValid(folder)) {
            result.add(folder.toAbsolutePath().toString());
            return;
        }

        // explore
        if (!includeNestDirs) return;
        File[] files = folder.toFile().listFiles();
        if (files == null) return;

        for (File candidate : files) {
            for (File adjusted : Collections.singletonList(candidate)) {
                scanFolder(adjusted.toPath(), false, result);
            }
        }
    }
}
