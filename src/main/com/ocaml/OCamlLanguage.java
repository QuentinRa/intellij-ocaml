package com.ocaml;

import com.intellij.lang.Language;
import com.or.lang.utils.ORLanguageProperties;
import org.jetbrains.annotations.NotNull;

/**
 * OCaml Language
 */
public final class OCamlLanguage extends Language implements ORLanguageProperties {

    @NotNull
    public static final OCamlLanguage INSTANCE = new OCamlLanguage();

    public static final String FUNCTION_SIGNATURE_SEPARATOR = " -> ";

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

    @Override
    public @NotNull String getParameterSeparator() {
        return FUNCTION_SIGNATURE_SEPARATOR;
    }

    @Override
    public @NotNull String getFunctionSeparator() {
        return FUNCTION_SIGNATURE_SEPARATOR;
    }

    @Override
    public @NotNull String getTemplateStart() {
        return "";
    }

    @Override
    public @NotNull String getTemplateEnd() {
        return "";
    }
}