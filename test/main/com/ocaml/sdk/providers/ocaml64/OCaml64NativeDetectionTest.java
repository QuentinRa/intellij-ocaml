package com.ocaml.sdk.providers.ocaml64;

import org.junit.Test;

@SuppressWarnings("JUnit4AnnotatedMethodInJUnit3TestCase")
public final class OCaml64NativeDetectionTest extends OCaml64BaseTest {

    //
    // Testing
    //

    @Test
    public void testEmpty() {
        assertCygwinDetectionInvalid("");
    }

    @Test
    public void testPathInvalid() {
        assertCygwinDetectionInvalid("C:\\OCaml64\\invalid\\bin\\ocaml.exe");
    }

    @Test
    public void testNoExe() {
        assertCygwinDetectionInvalid("C:\\OCaml64\\invalid\\bin\\ocaml");
    }

    @Test
    public void testOpamBinValid() {
        assertCygwinDetectionValid(OCaml64Folders.OPAM_VALID_SDK);
    }

    @Test
    public void testOpamBinInvalid() {
        assertCygwinDetectionInvalid(OCaml64Folders.OPAM_INVALID_BIN);
    }
}
