package com.ocaml.utils.adaptor;

/**
 * Use this to mark a class/method/... that was added in a release.
 * This is used so that we know, without testing every version,
 * why we are using this adaptor, and it's supposed to help if there is a need
 * to update the adaptor.
 */
public @interface SinceIdeVersion {
    String release();
}
