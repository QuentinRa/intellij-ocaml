package com.ocaml.utils.logs;

import com.intellij.openapi.diagnostic.*;
import org.jetbrains.annotations.*;

/**
 * Create a logger for the plugin<br>
 * See https://plugins.jetbrains.com/docs/intellij/ide-infrastructure.html#error-reporting
 */
public class OCamlLogger {

    /** should not be used, create a custom group **/
    public static @NotNull Logger getInstance(String s) {
        return Logger.getInstance("ocaml."+s);
    }

    /** Group for templates */
    public static @NotNull Logger getTemplateInstance(String name) {
        return getInstance("template."+name);
    }

    public static @NotNull Logger getSdkInstance(String name) {
        return getInstance("sdk."+name);
    }
}
