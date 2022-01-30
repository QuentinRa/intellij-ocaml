package com.ocaml.sdk.providers.ocaml64;

import org.junit.Test;

@SuppressWarnings("JUnit4AnnotatedMethodInJUnit3TestCase")
public final class OCaml64SuggestHomePathsTest extends OCaml64BaseTest {

    @Test
    public void testCygwinOpamSdksAreSuggested() {
        assertInstallationFolderWasSuggested(OCaml64Folders.OPAM_HOME);
    }
}
