package com.ocaml.utils.strings;

import com.ocaml.utils.Tested;
import org.jetbrains.annotations.NotNull;

@Tested
public final class StringsUtil {

    public static @NotNull String capitalize(@NotNull String str) {
        if (str.isBlank() || str.length() == 1) return str.toUpperCase();
        return str.substring(0, 1).toUpperCase() + str.substring(1);
    }
}
