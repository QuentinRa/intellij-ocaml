package com.ocaml.utils.adaptor;

import java.lang.annotation.*;

/**
 * Mark a class/element that is only available if the java plugin is available
 */
@Retention(RetentionPolicy.CLASS)
@Target({ElementType.METHOD, ElementType.FIELD, ElementType.PARAMETER, ElementType.LOCAL_VARIABLE, ElementType.TYPE_USE})
public @interface RequireJavaPlugin {
    String what() default "";
}
