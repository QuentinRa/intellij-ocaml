package com.ocaml.sdk.providers.cygwin;

import com.ocaml.sdk.providers.simple.DetectionResult;
import com.ocaml.sdk.providers.simple.OCamlNativeDetector;
import org.junit.Ignore;
import org.junit.Test;

@SuppressWarnings("JUnit4AnnotatedMethodInJUnit3TestCase")
public final class CygwinNativeDetectionTest extends CygwinBaseTest {

    // This test is only possible because
    // this one is in the PATH

    @Test @Ignore // todo: test https://stefanbirkner.github.io/system-rules/
    public void testPath() {
        DetectionResult detectionResult = OCamlNativeDetector.detectNativeSdk();
        assertEquals("C:\\cygwin64\\bin\\ocaml.exe", detectionResult.ocaml);
        assertEquals("C:\\cygwin64\\bin\\ocamlc.opt.exe", detectionResult.ocamlCompiler);
        assertEquals("C:\\cygwin64\\lib\\ocaml", detectionResult.sources);
        assertEquals("4.10.0", detectionResult.version);
        assertFalse(detectionResult.isError);
    }

    //
    // Testing
    //

    @Test
    public void testEmpty() {
        assertCygwinDetectionInvalid("");
    }

    @Test
    public void testPathInvalid() {
        assertCygwinDetectionInvalid("C:\\cygwin64\\invalid\\bin\\ocaml.exe");
    }

    @Test
    public void testNoExe() {
        assertCygwinDetectionInvalid("C:\\cygwin64\\invalid\\bin\\ocaml");
    }

    @Test
    public void testNotOCaml() {
        assertCygwinDetectionInvalid(CygwinFolders.BIN_VALID_EXE);
    }

    @Test
    public void testBin() {
        assertCygwinDetectionValid(CygwinFolders.BIN_VALID_SDK);
    }

    @Test
    public void testOpamBinValid() {
        assertCygwinDetectionValid(CygwinFolders.OPAM_VALID_SDK);
    }

    @Test
    public void testOpamBinInvalid() {
        assertCygwinDetectionInvalid(CygwinFolders.OPAM_INVALID_BIN);
    }
}
