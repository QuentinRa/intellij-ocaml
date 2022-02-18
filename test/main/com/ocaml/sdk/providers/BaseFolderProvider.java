package com.ocaml.sdk.providers;

public interface BaseFolderProvider {

    String getName();
    boolean isOpamAvailable();
    boolean isBinAvailable();

}
