package com.ocaml.utils.files;

import com.intellij.util.*;
import org.jetbrains.annotations.*;

public final class OCamlPathUtils {

    /**
     * Check if a path ends with a value
     * @param path can be a Linux or a Windows Path
     * @param end must be a Linux Path
     * @return true if ends with value or value/, false else
     */
    public static boolean folderEndsWith(@NotNull String path, String end) {
        return PathUtil.toSystemIndependentName(path).endsWith(end)
                || PathUtil.toSystemIndependentName(path).endsWith(end+"/");
    }

    /**
     * Check if a path ends with another path
     * @param path can be a Linux or a Windows Path
     * @param end must be a Linux Path
     * @return true if ends with value, false else
     */
    public static boolean fileEndsWith(@NotNull String path, String end) {
        return fileEndsWith(path, end, null);
    }

    /**
     * Check if a path ends with another path
     * @param path can be a Linux or a Windows Path
     * @param end must be a Linux Path
     * @param extension an extension that may be added to end
     * @return true if ends with value, false else
     */
    public static boolean fileEndsWith(String path, String end, @Nullable String extension) {
        return PathUtil.toSystemIndependentName(path).endsWith(end)
                || (extension != null && PathUtil.toSystemIndependentName(path).endsWith(end+extension));
    }
}
