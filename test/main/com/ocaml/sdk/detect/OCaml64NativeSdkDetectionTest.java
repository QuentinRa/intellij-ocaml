package com.ocaml.sdk.detect;

import com.intellij.util.SystemProperties;
import org.junit.Test;

//@Ignore
@SuppressWarnings("JUnit4AnnotatedMethodInJUnit3TestCase")
public class OCaml64NativeSdkDetectionTest extends CygwinNativeSdkDetectionTest {

    // We are testing the detection.
    // - The path given to detectNativeSdk(ocaml) must ends with a valid executable name
    // - Paths given to detectNativeSdk(ocaml, ocamlc) must ends with a valid executables' name
    // This is because they are internal methods,
    // and the previous calls are ensuring that.

    // We are testing the detection.
    // - The path given to detectNativeSdk(ocaml) must ends with a valid executable name
    // - Paths given to detectNativeSdk(ocaml, ocamlc) must ends with a valid executables' name
    // This is because they are internal methods,
    // and the previous calls are ensuring that.

    // we can't test the path because we can't set the PATH for now
    @Test public void testPath() {}
    // we can't test if the bin is valid because
    // ocaml is not installed
    @Test public void testBinValid() {}

    @Test
    public void testOpamBinValid() {
        String sdkFolder = "C:\\OCaml64\\home\\"+ SystemProperties.getUserName() + "\\.opam\\4.13.1+mingw64c\\";
        assertCygwinValid(sdkFolder, "4.13.1+mingw64c", false);
    }

    @Test
    public void testOpamBinInvalid() {
        String sdkFolder = "C:\\OCaml64\\home\\"+ SystemProperties.getUserName() + "\\.opam\\4.57.0\\";
        assertCygwinInvalid(sdkFolder, false);
    }
}
