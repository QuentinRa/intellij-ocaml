package com.ocaml.utils.files;

import com.intellij.execution.configurations.PathEnvironmentVariableUtil;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.util.SystemInfo;
import com.intellij.util.PathUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;

public final class OCamlPathUtils {

    /**
     * Check if a path ends with a value
     *
     * @param path can be a Linux or a Windows Path
     * @param end  must be a Linux Path
     * @return true if ends with value or value/, false else
     */
    public static boolean folderEndsWith(@NotNull String path, String end) {
        return PathUtil.toSystemIndependentName(path).endsWith(end)
                || PathUtil.toSystemIndependentName(path).endsWith(end + "/");
    }

    /**
     * Check if a path ends with another path
     *
     * @param path      can be a Linux or a Windows Path
     * @param end       must be a Linux Path
     * @param extension an extension that may be added to end
     * @return true if ends with value, false else
     */
    public static boolean fileEndsWith(String path, String end, @Nullable String extension) {
        return PathUtil.toSystemIndependentName(path).endsWith(end)
                || (extension != null && PathUtil.toSystemIndependentName(path).endsWith(end + extension));
    }

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

    /**
     * Find an executable in the PATH. On Windows, both
     * name and name.exe are checked.
     *
     * @param name the name of the executable
     * @return null or the absolute path to the executable
     */
    public static String findExecutableInPath(String name) {
        File f = findExecutableInPathAsFile(name);
        return f == null ? null : f.getAbsolutePath();
    }

    /**
     * Find an executable in the PATH. On Windows, both
     * name and name.exe are checked.
     *
     * @param name the name of the executable
     * @return null or the file leading to the executable
     */
    public static File findExecutableInPathAsFile(String name) {
        File f = PathEnvironmentVariableUtil.findInPath(name);
        if (f == null && SystemInfo.isWindows)
            f = PathEnvironmentVariableUtil.findInPath(name + ".exe");
        return f;
    }
}
