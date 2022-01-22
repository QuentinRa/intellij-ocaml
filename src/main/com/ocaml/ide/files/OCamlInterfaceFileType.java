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
 * OCaml interface
 */
public final class OCamlInterfaceFileType extends LanguageFileType {
    // extension
    public static final String DEFAULT_EXTENSION = "mli";
    public static final String DOT_DEFAULT_EXTENSION = ".mli";

    // instance
    public static final OCamlInterfaceFileType INSTANCE = new OCamlInterfaceFileType();
    private OCamlInterfaceFileType() {
        super(OCamlLanguage.INSTANCE);
    }

    // implementation
    @Override @NotNull public String getName() {
        return "OCAML_INTERFACE";
    }

    @Override @NotNull public String getDescription() {
        return OCamlBundle.message("filetype.mli.description");
    }

    @Override @NotNull public String getDefaultExtension() {
        return DEFAULT_EXTENSION;
    }

    @Override public Icon getIcon() {
        return OCamlIcons.FileTypes.OCAML_INTERFACE;
    }

    @Override public @Nls @NotNull String getDisplayName() {
        return getDescription();
    }
}
