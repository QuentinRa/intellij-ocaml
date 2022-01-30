package com.ocaml.sdk.providers.linux;

import com.ocaml.sdk.providers.simple.DetectionResult;
import com.ocaml.sdk.providers.simple.OCamlNativeDetector;
import com.ocaml.sdk.utils.SdkInfo;
import org.jetbrains.annotations.NotNull;
import org.junit.Test;

@SuppressWarnings("JUnit4AnnotatedMethodInJUnit3TestCase")
public final class LinuxNativeDetectionTest extends LinuxBaseTest {

    public void assertLinuxValid(@NotNull SdkInfo info) {
        if (passLinuxTest()) return;
        assertLinuxValid(info.toplevel, info.version, info.sources);
    }

    public void assertLinuxValid(String ocamlBin, String expectedVersion, String expectedLib) {
        if (passLinuxTest()) return;
        String root = ocamlBin.replace("/bin/ocaml", "");
        DetectionResult detectionResult = OCamlNativeDetector.detectNativeSdk(ocamlBin);
        assertEquals(ocamlBin, detectionResult.ocaml);
        assertEquals(ocamlBin+"c", detectionResult.ocamlCompiler);
        assertEquals(root + expectedLib, detectionResult.sources);
        assertEquals(expectedVersion, detectionResult.version);
        assertFalse(detectionResult.isError);
    }

    public void assertLinuxValid(String ocamlBin) {
        if (passLinuxTest()) return;
        try {
            DetectionResult detectionResult = OCamlNativeDetector.detectNativeSdk(ocamlBin);
            if (detectionResult.isError) throw new AssertionError("OK");
        } catch (AssertionError e) {
            assertTrue(true);
            return;
        }
        fail("Supposed unreachable code");
    }

    @Test
    public void testEmpty() {
        assertLinuxValid("");
    }

    @Test
    public void testPathInvalid() {
        assertLinuxValid("/invalid/ocaml");
    }

    @Test
    public void testNotOCaml() {
        assertLinuxValid(LinuxFolders.BIN_VALID);
    }

    @Test
    public void testBin() {
        assertLinuxValid(LinuxFolders.BIN_VALID_SDK);
    }

    @Test
    public void testOpamBinValid() {
        assertLinuxValid(LinuxFolders.OPAM_VALID_SDK);
    }

    @Test
    public void testOpamBinInvalid() {
        assertLinuxValid(LinuxFolders.OPAM_INVALID_BIN);
    }
}
