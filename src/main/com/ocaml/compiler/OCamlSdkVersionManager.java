package com.ocaml.compiler;

import com.intellij.openapi.util.io.FileUtil;
import com.ocaml.OCamlBundle;
import org.jetbrains.annotations.NotNull;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class OCamlSdkVersionManager {

    /**
     * - something
     * - then a slash
     * - 1# then the major version
     * - 1# the two digits of the minor version
     * - 1# an optional patch version
     * - 1# an optional kind "+kind"
     * - we are allowing some text, after all of this, if the text is starting with "-v"
     * - then we got another slash
     * - something
     */
    private static final Pattern VERSION_PATH_REGEXP =
            Pattern.compile(".*/(\\d\\.\\d\\d(\\.\\d)?([+][^/]+)?)(-v[^/]*)?/.*");

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
        if (sdkHome.isBlank()) return UNKNOWN_VERSION;
        // use Linux paths, as we are using these in the regex
        sdkHome = FileUtil.toSystemIndependentName(sdkHome);
        if (!sdkHome.endsWith("/")) sdkHome += "/";
        // try to find the first group
        Matcher matcher = VERSION_PATH_REGEXP.matcher(sdkHome);
        if (matcher.matches()) return matcher.group(1);
        // no match
        return UNKNOWN_VERSION;
    }

    /**
     * Return true if a version is a valid SDK version
     */
    public static boolean isValid(String version) {
        return VERSION_REGEXP.matcher(version).matches();
    }
}
