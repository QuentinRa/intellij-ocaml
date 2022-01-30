package com.ocaml.sdk.providers.wsl;

import org.junit.Test;

@SuppressWarnings("JUnit4AnnotatedMethodInJUnit3TestCase")
public final class WSLHomePathTest extends WSLBaseTest {

    @Test
    public void testFilesOkNoVersion() {
        // we may find every file, but the file name do not have the
        // version of ocaml (ex: 4.05.0), so it's not a valid homePath
        assertWSLHomeInvalid(WSLFolders.BIN_VALID_SDK.path);
    }

    @Test
    public void testInvalid() {
        assertWSLHomeInvalid("\\\\wsl$\\Debian\\invalid");
    }

    @Test
    public void testInvalidTS() {
        assertWSLHomeInvalid("\\\\wsl$\\Debian\\invalid\\");
    }

    @Test
    public void testInvalidWSLDistribution() {
        assertWSLHomeInvalid(WSLFolders.OPAM_INVALID_DIST.path);
    }

    @Test
    public void testInvalidWSLDistributionTS() {
        assertWSLHomeInvalid(WSLFolders.OPAM_INVALID_DIST.path+"\\");
    }

    @Test
    public void testOpamValid() {
        assertWSLHomeValid(WSLFolders.OPAM_VALID_SDK.path);
    }

    @Test
    public void testOpamValidTS() {
        assertWSLHomeValid(WSLFolders.OPAM_VALID_SDK.path+"\\");
    }

    @Test
    public void testOpamInvalid() {
        assertWSLHomeInvalid(WSLFolders.OPAM_INVALID);
    }

    @Test
    public void testOpamInvalidTS() {
        assertWSLHomeInvalid(WSLFolders.OPAM_INVALID +"\\");
    }

}
