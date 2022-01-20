package com.ocaml.compiler.simple;

import com.ocaml.OCamlBundle;
import com.ocaml.compiler.OCamlSdkVersionManager;

/**
 * The result of a detection.
 * We may have found, if {@link #isError} is false,
 * - a path to the ocaml executable
 * - a path to the ocaml compiler
 * - the version of ocaml
 * - a path to the sources' folder.
 *
 * Note that if we got an error, then {@link #NO_ASSOCIATED_BINARIES} is returned.
 */
public class DetectionResult {

    public static final DetectionResult NO_ASSOCIATED_BINARIES = new DetectionResult(
            "",
            OCamlBundle.message("sdk.ocaml.binary.invalid.short"), OCamlSdkVersionManager.UNKNOWN_VERSION,
            OCamlBundle.message("sdk.ocaml.binary.invalid.short"), true
    );

    public final String ocaml;
    public final String ocamlCompiler;
    public final String version;
    public final String sources;
    public final boolean isError;

    DetectionResult(String ocaml, String ocamlCompiler, String version, String sources) {
        this(ocaml, ocamlCompiler, version, sources, false);
    }

    DetectionResult(String ocaml, String ocamlCompiler, String version,
                    String sources, boolean isError) {
        this.ocaml = ocaml;
        this.ocamlCompiler = ocamlCompiler;
        this.version = version;
        this.sources = sources;
        this.isError = isError;
    }

    @Override public String toString() {
        return "DetectionResult{" +
                "ocaml='" + ocaml + '\'' +
                ", ocamlCompiler='" + ocamlCompiler + '\'' +
                ", version='" + version + '\'' +
                ", sources='" + sources + '\'' +
                ", isError=" + isError +
                '}';
    }
}
