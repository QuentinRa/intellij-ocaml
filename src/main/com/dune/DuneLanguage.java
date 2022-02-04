package com.dune;

import com.intellij.lang.Language;

public class DuneLanguage extends Language {
    public static final DuneLanguage INSTANCE = new DuneLanguage();
    public static final String NAME = "Dune";

    private DuneLanguage() {
        super(NAME.toUpperCase());
    }
}
