package com.ocaml.sdk.providers.cygwin;

import com.ocaml.sdk.providers.simple.DetectionResult;
import com.ocaml.sdk.providers.simple.OCamlNativeDetector;
import org.junit.Ignore;
import org.junit.Test;

public final class CygwinNativeDetectionTest extends CygwinBaseTest {
    public CygwinNativeDetectionTest(String folderName) {
        super(folderName);
    }

    // This test is only possible because
    // this one is in the PATH

    @Test @Ignore // todo: test https://stefanbirkner.github.io/system-rules/
    public void testPath() {
        if (true) return;
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
        if (folders.OCAML_BIN_INVALID == null) return;
        assertCygwinDetectionInvalid(folders.OCAML_BIN_INVALID);
    }

    @Test
    public void testNoExe() {
        if (folders.OCAML_BIN_INVALID_NO_EXE == null) return;
        assertCygwinDetectionInvalid(folders.OCAML_BIN_INVALID_NO_EXE);
    }

    @Test
    public void testNotOCaml() {
        if (folders.BIN_VALID_EXE == null) return;
        assertCygwinDetectionInvalid(folders.BIN_VALID_EXE);
    }

    @Test
    public void testBin() {
        if (folders.BIN_VALID_SDK == null) return;
        assertCygwinDetectionValid(folders.BIN_VALID_SDK);
    }

    @Test
    public void testOpamBinValid() {
        if (folders.OPAM_VALID_SDK == null) return;
        assertCygwinDetectionValid(folders.OPAM_VALID_SDK);
    }

    @Test
    public void testOpamBinInvalid() {
        if (folders.OPAM_INVALID_BIN == null) return;
        assertCygwinDetectionInvalid(folders.OPAM_INVALID_BIN);
    }
}
