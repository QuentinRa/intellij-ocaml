package com.ocaml.sdk.utils;

import com.intellij.openapi.util.io.FileUtil;
import com.ocaml.OCamlBundle;
import org.jetbrains.annotations.NotNull;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class OCamlSdkVersionManager {

    /**
     * <ul>
     * <li>something</li>
     * <li>then a slash</li>
     * <li>1# then the major version</li>
     * <li>1# the two digits of the minor version</li>
     * <li>1# an optional patch version</li>
     * <li>1# an optional kind "+kind"</li>
     * <li>we are allowing some text, after all of this, if the text is starting with "-v"</li>
     * <li>then we got another slash</li>
     * <li>something</li>
     * </ul>
     */
    private static final Pattern VERSION_PATH_REGEXP =
            Pattern.compile(".*/(\\d\\.\\d\\d(\\.\\d)?([+][^/]+)?)(-v[^/]*)?/.*");

    private static final Pattern VERSION_ONLY_REGEXP =
            Pattern.compile(".*/(\\d\\.\\d\\d(\\.\\d)?)([+][^/]+)?(-v[^/]*)?/.*");

    // This is the regex above, the part between the two slashes
    private static final Pattern VERSION_REGEXP = Pattern.compile("(\\d\\.\\d\\d(\\.\\d)?([+][^/]+)?)(-v[^/]*)?");

    /** unknown version  **/
    public static final String UNKNOWN_VERSION = OCamlBundle.message("sdk.version.unknown");

    public static boolean isUnknownVersion(String version) {
        return UNKNOWN_VERSION.equals(version);
    }

    /**
     * Return the version of an SDK given its home.
     * The version is, by design, always in the path,
     * so we are only extracting it.
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
     * @param sdkHome a path (expected to be absolute, but it should work
     *                for most relative paths as long as there is at least one /),
     *                using \\ or / as file separator.
     * @return the version (ex: 4.12.0, even if the real version is 4.12.0+mingw64)
     */
    // todo: test
    public static String parseVersionOnly(@NotNull String sdkHome) {
        return parse(sdkHome, VERSION_ONLY_REGEXP);
    }

    // todo: tests with folder 4.12
    public static boolean isNewerThan(String base, String version) {
        return version.compareTo(base) > 0;
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
        String v1 = parse(p1);
        String v2 = parse(p2);
        return v1.compareTo(v2);
    }
}
