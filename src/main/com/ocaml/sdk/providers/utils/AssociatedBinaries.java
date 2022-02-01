package com.ocaml.sdk.providers.utils;

import org.jetbrains.annotations.NotNull;

public final class AssociatedBinaries {

    @NotNull public final String ocamlBin;
    @NotNull public final String compilerPath;
    @NotNull public final String sourcesPath;
    @NotNull public final String compilerVersion;

    public AssociatedBinaries(@NotNull String ocamlBin, @NotNull String compilerPath,
                              @NotNull String sourcesPath, @NotNull String compilerVersion) {
        this.ocamlBin = ocamlBin;
        this.compilerPath = compilerPath;
        this.sourcesPath = sourcesPath;
        this.compilerVersion = compilerVersion;
    }

    @Override public String toString() {
        return "AssociatedBinaries{" +
                "ocamlBin='" + ocamlBin + '\'' +
                ", compilerPath='" + compilerPath + '\'' +
                ", sourcesPath='" + sourcesPath + '\'' +
                ", compilerVersion='" + compilerVersion + '\'' +
                '}';
    }
}
