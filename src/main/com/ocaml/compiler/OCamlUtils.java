package com.ocaml.compiler;

import com.intellij.openapi.util.*;
import com.intellij.openapi.util.io.*;
import com.ocaml.compiler.finder.*;
import org.jetbrains.annotations.*;

import java.nio.file.*;
import java.util.*;
import java.util.regex.*;

/**
 * Utilities for OCaml
 */
public final class OCamlUtils {

    public static final class Version {
        private static final String UNKNOWN_VERSION = "unknown version";

        /** Return the version of an SDK given its home.
         * The version is, by design, always in the path, so we are only
         * extracting it.
         * */
        public static String parse(String sdkHome) {
            // read the version in the name
            String serialized = sdkHome.replace("\\", "/");
            Matcher m1 = OCamlConstants.VERSION_PATH_REGEXP.matcher(serialized);
            if (m1.matches()) {
                return m1.group(1);
            }
            return UNKNOWN_VERSION;
        }

        /** Return true if a version is a valid SDK version */
        public static boolean isValid(String version) {
            return OCamlConstants.VERSION_REGEXP.matcher(version).matches();
        }
    }

    public static final class Home {
        /** Find OCaml Homes **/
        public static final class Finder {
            /** Tries to find existing OCaml SDKs on this computer. */
            public static @NotNull List<String> suggestHomePaths() {
                Set<String> existingSdks;

                if (SystemInfo.isWindows) {
                    existingSdks = new OCamlHomeFinderWindows().findExistingSdks();
                } else {
                    existingSdks = new BaseOCamlHomeFinder().findExistingSdks();
                }

                return new ArrayList<>(existingSdks);
            }

            public static @Nullable String defaultOCamlLocation() {
                if (SystemInfo.isLinux) {
                    return FileUtil.expandUserHome("~/.opam");
                }
                return null;
            }
        }

        public static boolean isValid(@NotNull String homePath) {
            return isValid(Path.of(homePath));
        }

        public static boolean isValid(@NotNull Path homePath) {
            // version
            boolean ok = OCamlUtils.Version.isValid(homePath.toFile().getName());
            // interactive toplevel
            ok = ok && (Files.exists(homePath.resolve("bin/ocaml")) || Files.exists(homePath.resolve("bin/ocaml.exe")));
            // compiler
            ok = ok && (
                    Files.exists(homePath.resolve("bin/ocamlc")) || Files.exists(homePath.resolve("bin/ocamlc.exe"))
                    || Files.exists(homePath.resolve("bin/ocamlc.opt")) || Files.exists(homePath.resolve("bin/ocamlc.opt.exe"))
            );
            // sources
            ok = ok && (Files.exists(homePath.resolve("lib/ocaml"))); // not usr/lib/ocaml
            return ok;
        }
    }
}
