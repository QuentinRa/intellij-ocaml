package com.ocaml.utils;

public final class OCamlPlatformUtils {

    private static Boolean isJavaAvailable;

    public static boolean isJavaPluginAvailable() {
        if (isJavaAvailable == null) {
            try {
                Class.forName("com.intellij.ide.highlighter.JavaFileType");
                isJavaAvailable = true;
            } catch (ClassNotFoundException e) {
                isJavaAvailable = false;
            }
        }
        return isJavaAvailable;
    }

}
