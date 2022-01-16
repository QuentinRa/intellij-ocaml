package com.ocaml.compiler.opam;

import org.jetbrains.annotations.*;

public final class OpamUtils {

    public static boolean isOpamBinary(@NotNull String path) {
        return path.contains(".opam");
    }

}
