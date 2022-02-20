package com.ocaml.utils;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;

/**
 * Mark a class or a method as tested.
 * This is supposed to help keeping track of what I need to test.
 * I could also use some coverage tools, but I will do that when
 * I'm in the mood of writing tests, I'm keeping things simple for now :D.
 */
@Target({TYPE, FIELD, METHOD, PARAMETER, CONSTRUCTOR, LOCAL_VARIABLE, MODULE})
@Retention(RetentionPolicy.SOURCE)
public @interface Tested {

    // note
    String note() default "";

}
