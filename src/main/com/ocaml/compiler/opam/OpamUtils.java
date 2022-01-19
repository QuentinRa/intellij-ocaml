package com.ocaml.compiler.opam;

import org.jetbrains.annotations.NotNull;

public final class OpamUtils {

    public static boolean isOpamBinary(@NotNull String path) {
        return path.contains(".opam");
    }

}
