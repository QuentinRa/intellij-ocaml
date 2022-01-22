// Copyright 2000-2020 JetBrains s.r.o. Use of this source code is governed by the Apache 2.0 license that can be found in the LICENSE file.
package com.ocaml.ide.files;

import com.intellij.openapi.fileTypes.LanguageFileType;
import com.ocaml.OCamlBundle;
import com.ocaml.icons.OCamlIcons;
import com.ocaml.OCamlLanguage;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;

/**
 * OCaml source file
 */
public final class OCamlFileType extends LanguageFileType {
    // extension
    public static final String DEFAULT_EXTENSION = "ml";
    public static final String DOT_DEFAULT_EXTENSION = ".ml";

    // instance
    public static final OCamlFileType INSTANCE = new OCamlFileType();
    private OCamlFileType() {
        super(OCamlLanguage.INSTANCE);
    }

    // implementation

    @Override @NotNull public String getName() {
        return "OCAML";
    }

    @Override @NotNull public String getDescription() {
        return OCamlBundle.message("filetype.ml.description");
    }

    @Override @NotNull public String getDefaultExtension() {
        return DEFAULT_EXTENSION;
    }

    @Override public Icon getIcon() {
        return OCamlIcons.FileTypes.OCAML;
    }

    @Override public @Nls @NotNull String getDisplayName() {
        return getDescription();
    }
}
