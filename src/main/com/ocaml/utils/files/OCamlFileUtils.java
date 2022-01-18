package com.ocaml.utils.files;

import org.jetbrains.annotations.*;

import java.io.*;
import java.nio.file.*;
import java.util.*;
import java.util.function.*;

public final class OCamlFileUtils {

    /**
     * Same as this method, but this time the
     * first file is ending with a number too.
     * @see com.intellij.openapi.util.io.FileUtil#findSequentFile
     */
    @Deprecated(since = "0.0.3", forRemoval = true)
    @NotNull public static File findSequentFile(@NotNull File parentFolder,
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

    /**
     * Create a file, with some text.
     * todo: deprecate, copy of file from a template would be better I think
     */
    @Nullable public static File createFile(@NotNull File sourceRootFile,
                                            @NotNull String fileName, @NotNull String text) {
        try {
            File mainFile = new File(sourceRootFile, fileName);
            Files.write(mainFile.toPath(), new ArrayList<>(Arrays.asList(text.split("\n"))));
            return mainFile;
        } catch (IOException e) {
            return null;
        }
    }
}
