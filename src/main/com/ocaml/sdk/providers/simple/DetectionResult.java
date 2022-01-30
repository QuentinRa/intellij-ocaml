package com.ocaml.sdk.providers.simple;

import com.ocaml.OCamlBundle;
import com.ocaml.sdk.utils.OCamlSdkVersionManager;
import org.jetbrains.annotations.NotNull;

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
            OCamlBundle.message("project.wizard.sdk.ocaml.binary.invalid.short"), OCamlSdkVersionManager.UNKNOWN_VERSION,
            OCamlBundle.message("project.wizard.sdk.ocaml.binary.invalid.short"), true, false
    );

    @NotNull public final String ocaml;
    @NotNull public final String ocamlCompiler;
    @NotNull public final String version;
    @NotNull public final String sources;
    public final boolean isError;
    public final boolean sourcesMissing;

    DetectionResult(String ocaml, String ocamlCompiler, String version, String sources, boolean sourcesMissing) {
        this(ocaml, ocamlCompiler, version, sources, false, sourcesMissing);
    }

    DetectionResult(@NotNull String ocaml, @NotNull String ocamlCompiler, @NotNull String version,
                    @NotNull  String sources, boolean isError, boolean sourcesMissing) {
        this.ocaml = ocaml;
        this.ocamlCompiler = ocamlCompiler;
        this.version = version;
        this.sources = sources;
        this.isError = isError;
        this.sourcesMissing = sourcesMissing;
    }

    @Override public String toString() {
        return "DetectionResult{" +
                "ocaml='" + ocaml + '\'' +
                ", ocamlCompiler='" + ocamlCompiler + '\'' +
                ", version='" + version + '\'' +
                ", sources='" + sources + '\'' +
                ", sourcesMissing=" + sourcesMissing +
                ", isError=" + isError +
                '}';
    }
}
