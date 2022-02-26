package com.ocaml.utils.files;

import com.intellij.openapi.diagnostic.Logger;
import org.jetbrains.annotations.Nullable;

import java.nio.file.Files;
import java.nio.file.Path;

public final class OCamlPathUtils {

    /**
     * Create a symbolicLink
     *
     * @param src    the link that will be created
     * @param dest   the destination of the link
     * @param logger log an error that occurred when creating a symbolic link
     * @param args   parts of the path leading to the destination
     */
    public static boolean createSymbolicLink(String src, String dest, @Nullable Logger logger, String... args) {
        try {
            Path link = Path.of(dest, args);
            Path target = Path.of(src);
            Files.createSymbolicLink(link, target);
            return true;
        } catch (Exception e) {
            if (logger != null)
                logger.error("Could not create link from '" + src + "' to '" + dest + "'.", e);
            return false;
        }
    }
}
