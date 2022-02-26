package com.ocaml.sdk.utils;

import com.intellij.openapi.util.io.FileUtil;
import com.ocaml.OCamlBundle;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class OCamlSdkVersionManager {

    private static final Pattern VERSION_PATH_REGEXP =
            Pattern.compile(".*/[a-z-.]*(\\d\\.\\d\\d(\\.\\d)?([+~][0-9a-z-]+)*)/.*");

    private static final Pattern VERSION_ONLY_REGEXP =
            Pattern.compile(".*/[a-z-.]*(\\d\\.\\d\\d(\\.\\d)?)([+~][0-9a-z-]+)*/.*");

    // This is the first regex above, the part between the two slashes
    private static final Pattern VERSION_REGEXP = Pattern.compile("[a-z-.]*(\\d\\.\\d\\d(\\.\\d)?([+~][0-9a-z-]+)*)");

    /** unknown version **/
    public static final String UNKNOWN_VERSION = OCamlBundle.message("sdk.version.unknown");

    public static boolean isUnknownVersion(String version) {
        return UNKNOWN_VERSION.equals(version);
    }

    /**
     * Return the version of an SDK given its home.
     * The version is, by design, always in the path,
     * so we are only extracting it.
     *
     * @param sdkHome a path (expected to be absolute, but it should work
     *                for most relative paths as long as there is at least one /),
     *                using \\ or / as file separator.
     * @return either UNKNOWN_VERSION or the version
     * @see #isUnknownVersion(String)
     * @see #UNKNOWN_VERSION
     */
    public static String parse(@NotNull String sdkHome) {
        return parse(sdkHome, VERSION_PATH_REGEXP);
    }

    private static String parse(@NotNull String sdkHome, Pattern regexp) {
        if (sdkHome.isBlank()) return UNKNOWN_VERSION;
        // use Linux paths, as we are using these in the regex
        sdkHome = FileUtil.toSystemIndependentName(sdkHome);
        if (!sdkHome.endsWith("/")) sdkHome += "/";
        // try to find the first group
        Matcher matcher = regexp.matcher(sdkHome);
        if (matcher.matches()) return matcher.group(1);
        // no match
        return UNKNOWN_VERSION;
    }

    /**
     * Unlike parse, only returns the version, without any modifier (the +followed by some stuff)
     *
     * @param sdkHome a path (expected to be absolute, but it should work
     *                for most relative paths as long as there is at least one /),
     *                using \\ or / as file separator.
     * @return the version (ex: 4.12.0, even if the real version is 4.12.0+mingw64)
     */
    // not tested as both regex are the same,
    // the other one is tested. THe only difference is the group
    // that will be recuperated
    public static String parseWithoutModifier(@NotNull String sdkHome) {
        return parse(sdkHome, VERSION_ONLY_REGEXP);
    }

    /**
     * @param base return true if version is newer than the base version
     * @param version the version tested with the base
     * @return true if version is newer (or equals) than the base
     */
    @Contract(pure = true)
    public static boolean isNewerThan(@NotNull String base, @NotNull String version) {
        return compareVersions(version, base) >= 0;
    }

    /**
     * Return true if a version is a valid SDK version
     */
    public static boolean isValid(String version) {
        return VERSION_REGEXP.matcher(version).matches();
    }

    /**
     * Compare two paths and returns
     * <ul>
     *     <li><b>0</b>: they have the same version</li>
     *     <li><b>-1</b>: the second one has a newer version</li>
     *     <li><b>1</b>: the first one has a newer version</li>
     * </ul>
     */
    public static int comparePaths(String p1, String p2) {
        String v1 = parseWithoutModifier(p1);
        String v2 = parseWithoutModifier(p2);
        return compareVersions(v1, v2);
    }

    private static int compareVersions(@NotNull String v1, @NotNull String v2) {
        // missing one '.'
        int i1 = v1.lastIndexOf('.');
        int i2 = v2.lastIndexOf('.');
        if (i2 > i1) v1 += ".0";
        if (i1 > i2) v2 += ".0";

        // clamp between -1 and 1 (integers)
        int i = v1.compareTo(v2);
        return i == 0 ? 0 : i >= 1 ? 1 : -1;
    }
}
