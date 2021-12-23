package com.ocaml.ide.sdk;

import com.ocaml.comp.vanilla.tools.*;
import org.jetbrains.annotations.*;

import java.io.*;
import java.nio.file.*;
import java.util.*;

/**
 * Look for "ocaml" in
 *
 * 1. Linux
 *  - /bin/ocaml?
 * 2. Windows
 *  - C:/ocaml/bin/ocaml
 *  - C:/Ocaml64/bin/ocaml
 *  - ~/.opam/usr/portable/{ocaml_folder}/bin/ocaml
 * 3. Both
 *  - ~/.opam/home/{username}/{ocaml_folder}/bin/ocaml
 */
public class OCamlSDKFinder {

    public static Collection<String> findExistingSDKs() {
        HashSet<Path> roots = new HashSet<>();
        // check opam
        OpamUtils.lookForSDK(roots);
        // scan
        return scanAll(roots);
    }

    private static @NotNull Set<String> scanAll(@NotNull Collection<Path> files) {
        Set<String> result = new HashSet<>();
        for (Path root : new HashSet<>(files)) {
            scanFolder(root.toFile(), true, result);
        }
        return result;
    }

    private static void scanFolder(@NotNull File folder, boolean includeNestDirs, @NotNull Collection<? super String> result) {
        if (checkForJdk(folder)) {
            result.add(folder.getAbsolutePath());
            return;
        }

        if (!includeNestDirs) {
            return;
        }
        File[] files = folder.listFiles();
        if (files == null) {
            return;
        }

        for (File candidate : files) {
            scanFolder(candidate, false, result);
        }
    }

    private static boolean checkForJdk(File homePath) {
        return new File(homePath, "bin/ocaml").isFile();
    }
}
