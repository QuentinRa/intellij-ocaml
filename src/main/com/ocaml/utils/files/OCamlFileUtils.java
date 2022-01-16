package com.ocaml.utils.files;

import org.jetbrains.annotations.*;

import java.io.*;
import java.util.function.*;

public final class OCamlFileUtils {

    /**
     * Same as this method, but this time the
     * first file is ending with a number too.
     * @see com.intellij.openapi.util.io.FileUtil#findSequentFile
     */
    @NotNull
    public static File findSequentFile(@NotNull File parentFolder,
                                       @NotNull String filePrefix,
                                       @NotNull String extension,
                                       @NotNull Predicate<? super File> condition) {
        int postfix = 0;
        String ext = extension.isEmpty() ? "" : '.' + extension;
        File candidate = new File(parentFolder, filePrefix + postfix + ext);
        while (!condition.test(candidate)) {
            postfix++;
            candidate = new File(parentFolder, filePrefix + postfix + ext);
        }
        return candidate;
    }
}
