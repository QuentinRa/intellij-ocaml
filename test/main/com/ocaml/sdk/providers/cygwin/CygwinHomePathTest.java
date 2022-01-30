package com.ocaml.sdk.providers.cygwin;

import org.junit.Test;

@SuppressWarnings("JUnit4AnnotatedMethodInJUnit3TestCase")
public final class CygwinHomePathTest extends CygwinBaseTest  {

    @Test
    public void testFilesOkNoVersion() {
        // we got every file, but the file name do not have the
        // version of ocaml (ex: 4.05.0), so it's not a valid homePath
        assertCygwinHomeInvalid(CygwinFolders.BIN_VALID_SDK.path);
    }

    @Test
    public void testInvalid() {
        assertCygwinHomeInvalid("C:\\cygwin64\\invalid");
    }

    @Test
    public void testOpamValid() {
        assertCygwinHomeValid(CygwinFolders.OPAM_VALID_SDK.path);
    }

    @Test
    public void testOpamInvalid() {
        assertCygwinHomeInvalid(CygwinFolders.OPAM_INVALID_BIN);
    }

}
