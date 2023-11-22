package com.odoc;

import com.intellij.lang.Language;

public class OdocLanguage extends Language {
    public static final OdocLanguage INSTANCE = new OdocLanguage();

    private OdocLanguage() {
        super("odoc");
    }
}
