package com.ocaml.ide.files;

import com.intellij.openapi.fileTypes.FileType;
import org.jetbrains.annotations.Nullable;

public final class FileHelper {

    private FileHelper() {
    }

    public static boolean isOCaml(@Nullable FileType fileType) {
        return fileType instanceof OCamlFileType || fileType instanceof OCamlInterfaceFileType;
    }

}
