package com.ocaml.ide.sdk.providers.utils;

public final class AssociatedBinaries {

    public final String ocamlBin;
    public final String compilerPath;
    public final String sourcesPath;
    public final String compilerVersion;

    public AssociatedBinaries(String ocamlBin, String compilerPath, String sourcesPath, String compilerVersion) {
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
