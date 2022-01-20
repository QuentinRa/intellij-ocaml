package com.ocaml.sdk.ocaml64;

import com.intellij.util.SystemProperties;
import com.ocaml.sdk.cygwin.CygwinBaseTest;
import org.junit.Test;

@SuppressWarnings("JUnit4AnnotatedMethodInJUnit3TestCase")
public class OCaml64NativeDetectionTest extends CygwinBaseTest {
    // This test is not possible because
    // this one isn't in the PATH

    @Test
    public void testPath() {}

    //
    // Testing
    //

    @Test
    public void testEmpty() {
        assertCygwinInvalid("");
    }

    @Test
    public void testPathInvalid() {
        assertCygwinInvalid("C:\\OCaml64\\invalid\\bin\\ocaml.exe");
    }

    @Test
    public void testNoExe() {
        assertCygwinInvalid("C:\\OCaml64\\invalid\\bin\\ocaml");
    }

    @Test
    public void testNotOCaml() {
        assertCygwinInvalid("C:\\OCaml64\\bin\\find.exe");
    }

    @Test // not installed on OCaml64
    public void testBin() {}

    @Test
    public void testOpamBinValid() {
        assertCygwinValid(
                "C:\\OCaml64\\home\\"+ SystemProperties.getUserName() + "\\.opam\\4.13.1+mingw64c\\bin\\ocaml.exe",
                "ocamlc.exe",
                "4.13.1+mingw64c"
        );
    }

    @Test
    public void testOpamBinInvalid() {
        assertCygwinInvalid(
                "C:\\OCaml64\\home\\"+ SystemProperties.getUserName() + "\\.opam\\0.0.0\\bin\\ocaml.exe"
        );
    }
}
