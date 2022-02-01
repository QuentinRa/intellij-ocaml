package com.ocaml.sdk.providers.linux;

import org.junit.Test;

@SuppressWarnings("JUnit4AnnotatedMethodInJUnit3TestCase")
public final class LinuxHomePathTest extends LinuxBaseTest {

    @Test
    public void testFilesOkNoVersion() {
        // we may find every file, but the file name do not have the
        // version of ocaml (ex: 4.05.0), so it's not a valid homePath
        assertWSLHomeInvalid(LinuxFolders.BIN_VALID_SDK.path);
    }

    @Test
    public void testInvalid() {
        assertWSLHomeInvalid("/bin/invalid");
    }

    @Test
    public void testInvalidTS() {
        assertWSLHomeInvalid("/bin/invalid/");
    }

    @Test
    public void testOpamValid() {
        assertWSLHomeValid(LinuxFolders.OPAM_VALID_SDK.path);
    }

    @Test
    public void testOpamValidTS() {
        assertWSLHomeValid(LinuxFolders.OPAM_VALID_SDK.path+"/");
    }

    @Test
    public void testOpamInvalid() {
        assertWSLHomeInvalid(LinuxFolders.OPAM_INVALID);
    }

    @Test
    public void testOpamInvalidTS() {
        assertWSLHomeInvalid(LinuxFolders.OPAM_INVALID +"/");
    }

}
