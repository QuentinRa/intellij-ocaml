package com.ocaml.ide.sdk.providers.utils;

public final class AssociatedBinaries {

    public final String compilerPath;
    public final String sourcesPath;
    public final String compilerVersion;

    public AssociatedBinaries(String compilerPath, String sourcesPath, String compilerVersion) {
        this.compilerPath = compilerPath;
        this.sourcesPath = sourcesPath;
        this.compilerVersion = compilerVersion;
    }

    @Override public String toString() {
        return "AssociatedBinaries{" +
                "compilerPath='" + compilerPath + '\'' +
                ", sourcesPath='" + sourcesPath + '\'' +
                ", compilerVersion='" + compilerVersion + '\'' +
                '}';
    }
}
