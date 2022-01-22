package com.ocaml.lang;

import com.intellij.lang.Language;
import org.jetbrains.annotations.NotNull;

/**
 * OCaml Language
 */
public final class OCamlLanguage extends Language {

    @NotNull
    public static final OCamlLanguage INSTANCE = new OCamlLanguage();

    private OCamlLanguage() {
        super("OCAML");
    }

    @NotNull
    @Override
    public String getDisplayName() {
        return "OCaml";
    }

    @Override
    public boolean isCaseSensitive() {
        return true;
    }
}
