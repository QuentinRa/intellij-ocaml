package com.dune;

import com.intellij.DynamicBundle;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.PropertyKey;

/**
 * Localization
 */
public class DuneBundle extends DynamicBundle {

    @NonNls
    public static final String BUNDLE = "messages.DuneBundle";
    private static final DuneBundle INSTANCE = new DuneBundle();

    private DuneBundle() {
        super(BUNDLE);
    }

    @NotNull
    public static @Nls String message(@NotNull @PropertyKey(resourceBundle = BUNDLE) String key,
                                      Object ... params) {
        return INSTANCE.getMessage(key, params);
    }
}
