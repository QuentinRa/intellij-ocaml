package com.ocaml.sdk.providers.wsl;

import com.ocaml.sdk.utils.OCamlSdkHomeManager;
import org.junit.Test;

@SuppressWarnings("JUnit4AnnotatedMethodInJUnit3TestCase")
public final class WSLHomePathTest extends WSLBaseTest {
    private void assertWSLHomeValid(String homePath) {
        assertTrue(OCamlSdkHomeManager.isValid(homePath));
    }

    private void assertWSLHomeInvalid(String homePath) {
        assertFalse(OCamlSdkHomeManager.isValid(homePath));
    }

    @Test
    public void testFilesOkNoVersion() {
        // we may find every file, but the file name do not have the
        // version of ocaml (ex: 4.05.0), so it's not a valid homePath
        assertWSLHomeInvalid("\\\\wsl$\\Debian\\");
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
        assertWSLHomeInvalid("\\\\wsl$\\Fedora\\home\\calistro\\.opam\\4.07.0");
    }

    @Test
    public void testInvalidWSLDistributionTS() {
        assertWSLHomeInvalid("\\\\wsl$\\Fedora\\home\\calistro\\.opam\\4.07.0\\");
    }

    @Test
    public void testOpamValid() {
        assertWSLHomeValid(
                "\\\\wsl$\\Debian\\home\\calistro\\.opam\\4.07.0"
        );
    }

    @Test
    public void testOpamValidTS() {
        assertWSLHomeValid(
                "\\\\wsl$\\Debian\\home\\calistro\\.opam\\4.07.0\\"
        );
    }

    @Test
    public void testOpamInvalid() {
        assertWSLHomeInvalid(
                "\\\\wsl$\\Debian\\home\\calistro\\.opam\\0.00.0"
        );
    }

    @Test
    public void testOpamInvalidTS() {
        assertWSLHomeInvalid(
                "\\\\wsl$\\Debian\\home\\calistro\\.opam\\0.00.0\\"
        );
    }

}
