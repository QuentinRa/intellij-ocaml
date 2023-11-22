// Copyright 2000-2020 JetBrains s.r.o. Use of this source code is governed by the Apache 2.0 license that can be found in the LICENSE file.
// http://www.apache.org/licenses/LICENSE-2.0
package com.ocaml.ide.files;

import com.intellij.openapi.fileTypes.LanguageFileType;
import com.ocaml.OCamlBundle;
import com.ocaml.OCamlLanguage;
import com.ocaml.icons.OCamlIcons;
import org.jetbrains.annotations.Contract;
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

    /**
     * Given a path to a source file, return the path to the interface
     *
     * @param path path to the source file, must be properly formatted
     * @return the path to the interface
     * @throws IllegalArgumentException if the path is valid using {@link OCamlFileType#isFile(String)}
     */
    public static @NotNull String fromSource(@NotNull String path) {
        if (OCamlFileType.isFile(path))
            return path.replace(OCamlFileType.DEFAULT_EXTENSION, DEFAULT_EXTENSION);
        throw new IllegalArgumentException("Not a valid source file '" + path + "'");
    }

    @Contract(pure = true) public static boolean isFile(@NotNull String name) {
        return name.endsWith(DOT_DEFAULT_EXTENSION);
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
