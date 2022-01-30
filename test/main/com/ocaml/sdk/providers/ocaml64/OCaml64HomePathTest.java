package com.ocaml.sdk.providers.ocaml64;

import org.junit.Test;

@SuppressWarnings("JUnit4AnnotatedMethodInJUnit3TestCase")
public final class OCaml64HomePathTest extends OCaml64BaseTest {

    @Test
    public void testInvalid() {
        assertCygwinHomeInvalid("C:\\OCaml64\\invalid");
    }

    @Test
    public void testOpamValid() {
        assertCygwinHomeValid(OCaml64Folders.OPAM_VALID_SDK.path);
    }

    @Test
    public void testOpamInvalid() {
        assertCygwinHomeInvalid(OCaml64Folders.OPAM_INVALID_BIN);
    }

}
