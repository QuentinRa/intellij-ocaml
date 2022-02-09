package com.ocaml.utils.adaptor;

/**
 * Use this to mark a class/method/... that was removed/... in a release.
 * This is used so that we know, without testing every version,
 * why we are using this code, and it's supposed to help if there is a need
 * to update the code.
 */
public @interface UntilIdeVersion {
    String release();
}
