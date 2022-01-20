package com.ocaml.sdk.detect;

import com.intellij.util.SystemProperties;
import com.ocaml.OCamlBaseTest;
import com.ocaml.compiler.simple.DetectionResult;
import com.ocaml.compiler.simple.OCamlNativePathDetector;
import org.junit.Test;

//@Ignore
@SuppressWarnings("JUnit4AnnotatedMethodInJUnit3TestCase")
public class CygwinNativeSdkDetectionTest extends OCamlBaseTest {

    protected void assertCygwinValid(String sdkFolder, String version) {
        assertCygwinValid(sdkFolder, version, true);
    }

    protected void assertCygwinValid(String sdkFolder, String version, boolean opt) {
        String ocaml = sdkFolder + "bin\\ocaml.exe";
        System.out.println("ocaml:"+ocaml);
        String ocamlc = sdkFolder + (opt ? "bin\\ocamlc.opt.exe" : "bin\\ocamlc.exe");
        DetectionResult detectionResult = OCamlNativePathDetector.detectNativeSdk(ocaml, ocamlc);
        assertEquals(ocaml, detectionResult.ocaml);
        assertEquals(ocamlc, detectionResult.ocamlCompiler);
        assertEquals(sdkFolder + "lib\\ocaml", detectionResult.sources);
        assertEquals(version, detectionResult.version);
        assertFalse(detectionResult.isError);
    }

    protected void assertCygwinInvalid(String sdkFolder) {
        assertCygwinInvalid(sdkFolder, true);
    }

    protected void assertCygwinInvalid(String sdkFolder, boolean opt) {
        String ocaml = sdkFolder + "bin\\ocaml.exe";
        String ocamlc = sdkFolder + (opt ? "\\bin\\ocamlc.opt.exe" : "bin\\ocamlc.exe");
        try {
            OCamlNativePathDetector.detectNativeSdk(ocaml, ocamlc);
            fail("Supposed unreachable code");
        } catch (AssertionError e) {
            assertTrue(true);
        }
    }

    // We are testing the detection.
    // - The path given to detectNativeSdk(ocaml) must ends with a valid executable name
    // - Paths given to detectNativeSdk(ocaml, ocamlc) must ends with a valid executables' name
    // This is because they are internal methods,
    // and the previous calls are ensuring that.

    @Test
    public void testPath() {
        DetectionResult detectionResult = OCamlNativePathDetector.detectNativeSdk();
        assertEquals("C:\\cygwin64\\bin\\ocaml.exe", detectionResult.ocaml);
        assertEquals("C:\\cygwin64\\bin\\ocamlc.opt.exe", detectionResult.ocamlCompiler);
        assertEquals("C:\\cygwin64\\lib\\ocaml", detectionResult.sources);
        assertEquals("4.10.0", detectionResult.version);
        assertFalse(detectionResult.isError);
    }

    // we can't test if the bin is valid because
    // ocaml is not installed
    @Test public void testBinValid() {
        String sdkFolder = "C:\\cygwin64\\";
        assertCygwinValid(sdkFolder, "4.10.0");
    }
    @Test
    public void testOpamBinValid() {
        String sdkFolder = "C:\\cygwin64\\home\\"+ SystemProperties.getUserName() + "\\.opam\\4.08.0\\";
        assertCygwinValid(sdkFolder, "4.08.0");
    }

    @Test
    public void testOpamBinInvalid() {
        String sdkFolder = "C:\\cygwin64\\home\\"+ SystemProperties.getUserName() + "\\.opam\\4.57.0\\";
        assertCygwinInvalid(sdkFolder);
    }
}
