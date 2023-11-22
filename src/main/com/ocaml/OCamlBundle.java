package com.ocaml;

import com.intellij.DynamicBundle;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.PropertyKey;

/**
 * Localization
 */
public class OCamlBundle extends DynamicBundle {

    @NonNls
    public static final String BUNDLE = "messages.OCamlBundle";
    private static final OCamlBundle INSTANCE = new OCamlBundle();

    private OCamlBundle() {
        super(BUNDLE);
    }

    @NotNull
    public static @Nls String message(@NotNull @PropertyKey(resourceBundle = BUNDLE) String key,
                                      Object @NotNull ... params) {
        return INSTANCE.getMessage(key, params);
    }
}
