// Copyright 2000-2020 JetBrains s.r.o. Use of this source code is governed by the Apache 2.0 license that can be found in the LICENSE file.
// http://www.apache.org/licenses/LICENSE-2.0
package com.ocaml.ide.files;

import com.intellij.openapi.fileTypes.FileType;
import com.intellij.openapi.vfs.VirtualFile;
import com.ocaml.OCamlBundle;
import com.ocaml.icons.OCamlIcons;
import com.ocaml.utils.adaptor.UntilIdeVersion;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

/**
 * OCaml Annotation (annot) file (non-binary, consider moving to .cmt)
 */
public final class OCamlAnnotFileType implements FileType {

    // instance
    public static final OCamlAnnotFileType INSTANCE = new OCamlAnnotFileType();

    private OCamlAnnotFileType() {
        super();
    }

    // implementation
    @Override @NotNull public String getName() {
        return "OCAML_ANNOT";
    }

    @Override @NotNull public String getDescription() {
        return OCamlBundle.message("filetype.annot.description");
    }

    @Override @NotNull public String getDefaultExtension() {
        return "annot";
    }

    @Override public Icon getIcon() {
        return OCamlIcons.FileTypes.OCAML_ANNOT;
    }

    @Override public boolean isBinary() {
        return false;
    }

    @Override public @Nls @NotNull String getDisplayName() {
        return getDescription();
    }

    @Override @UntilIdeVersion(release = "203")
    public @Nullable String getCharset(@NotNull VirtualFile file, byte @NotNull [] content) {
        return null;
    }
}
