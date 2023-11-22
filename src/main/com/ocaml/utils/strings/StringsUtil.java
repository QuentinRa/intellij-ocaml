package com.ocaml.utils.strings;

import org.jetbrains.annotations.NotNull;

public final class StringsUtil {

    public static @NotNull String capitalize(@NotNull String str) {
        if (str.isBlank() || str.length() == 1) return str.toUpperCase();
        return str.substring(0, 1).toUpperCase() + str.substring(1);
    }
}
