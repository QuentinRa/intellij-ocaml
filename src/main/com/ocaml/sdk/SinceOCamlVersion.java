package com.ocaml.sdk;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Used to find the elements that are used/...
 * since a specific version of OCaml.
 */
@Retention(value = RetentionPolicy.SOURCE)
@Target({ElementType.FIELD, ElementType.METHOD})
public @interface SinceOCamlVersion {

    String since();
}
