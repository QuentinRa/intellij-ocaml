package com.ocaml.sdk.providers.ocaml64;

import com.intellij.util.SystemProperties;
import com.ocaml.sdk.providers.cygwin.CygwinBaseTest;
import org.junit.Test;

@SuppressWarnings("JUnit4AnnotatedMethodInJUnit3TestCase")
public class OCaml64HomePathTest extends CygwinBaseTest {

    @Test
    public void testFilesOkNoVersion() {
        // we may find every file, but the file name do not have the
        // version of ocaml (ex: 4.05.0), so it's not a valid homePath
        assertCygwinHomeInvalid("C:\\OCaml64\\");
    }

    @Test
    public void testInvalid() {
        assertCygwinHomeInvalid("C:\\OCaml64\\invalid");
    }

    @Test
    public void testOpamValid() {
        assertCygwinHomeValid(
                "C:\\OCaml64\\home\\"+ SystemProperties.getUserName() + "\\.opam\\4.13.1+mingw64c\\"
        );
    }

    @Test
    public void testOpamInvalid() {
        assertCygwinHomeInvalid(
                "C:\\OCaml64\\home\\"+ SystemProperties.getUserName() + "\\.opam\\0.00.0\\"
        );
    }

}
