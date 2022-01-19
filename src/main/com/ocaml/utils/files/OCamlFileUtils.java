package com.ocaml.utils.files;

import com.intellij.openapi.vfs.*;
import org.jetbrains.annotations.*;

import java.io.*;
import java.nio.file.*;
import java.util.*;

public final class OCamlFileUtils {

    /**
     * Load a file stored in resources.
     * The path must starts with a /.
     * The separators for the lines in the returned file will be \n.
     */
    public static @NotNull String loadFileContent(@NotNull String filePath) {
        try {
            var url = OCamlFileUtils.class.getClassLoader().getResource(filePath.replaceFirst("/", ""));
            if (url == null) throw new IOException("Couldn't get URL for "+filePath);
            VirtualFile virtualFile = VfsUtil.findFileByURL(url);
            if (virtualFile == null) throw new IOException("Couldn't find file by URL for "+filePath);
            String text = VfsUtil.loadText(virtualFile);
            String[] split = text.split("\r\n");
            return String.join("\n", split);
        } catch (IOException e) {
            // log: log this exception
            return "";
        }
    }

    /** Create a file with some text. */
    public static void createFile(@NotNull File sourceRootFile,
                                  @NotNull String fileName, @NotNull String text) {
        try {
            File mainFile = new File(sourceRootFile, fileName);
            Files.write(mainFile.toPath(), new ArrayList<>(Arrays.asList(text.split("\n"))));
        } catch (IOException e) {
            // log: log this exception?
        }
    }
}
