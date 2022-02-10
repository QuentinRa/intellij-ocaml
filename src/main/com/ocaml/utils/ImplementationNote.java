package com.ocaml.utils;

import java.lang.annotation.*;

/**
 * Note that should be read when changing a class
 */
@Retention(RetentionPolicy.SOURCE)
@Target(ElementType.TYPE)
public @interface ImplementationNote {

    // since plugin release
    String since();

    // note
    String note();
}
