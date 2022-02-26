package com.ocaml.utils.adaptor;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;

/**
 * Use this to mark a class/method/... that was added in a release.
 * This is used so that we know, without testing every version,
 * why we are using this adaptor, and it's supposed to help if there is a need
 * to update the adaptor.
 */
@Target({TYPE, FIELD, METHOD, PARAMETER, CONSTRUCTOR, LOCAL_VARIABLE, MODULE})
@Retention(RetentionPolicy.SOURCE)
public @interface SinceIdeVersion {
    String release();
    String note() default "";
}
