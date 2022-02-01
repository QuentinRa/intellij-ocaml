package com.ocaml.sdk.providers.wsl;

import com.ocaml.sdk.providers.simple.DetectionResult;
import com.ocaml.sdk.providers.simple.OCamlNativeDetector;
import com.ocaml.sdk.utils.SdkInfo;
import org.jetbrains.annotations.NotNull;
import org.junit.Test;

@SuppressWarnings("JUnit4AnnotatedMethodInJUnit3TestCase")
public final class WSLNativeDetectionTest extends WSLBaseTest {

    public void assertWSLValid(@NotNull SdkInfo info) {
        if (passWSLTest()) return;
        assertWSLValid(info.toplevel, info.version, info.sources);
    }

    public void assertWSLValid(String ocamlBin, String expectedVersion, String expectedLib) {
        if (passWSLTest()) return;
        String root = ocamlBin.replace("\\bin\\ocaml", "");
        DetectionResult detectionResult = OCamlNativeDetector.detectNativeSdk(ocamlBin);
        assertEquals(ocamlBin, detectionResult.ocaml);
        assertEquals(ocamlBin+"c", detectionResult.ocamlCompiler);
        assertEquals(root + expectedLib, detectionResult.sources);
        assertEquals(expectedVersion, detectionResult.version);
        assertFalse(detectionResult.isError);
    }

    public void assertWSLInvalid(String ocamlBin) {
        if (passWSLTest()) return;
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
        assertWSLInvalid("");
    }

    @Test
    public void testPathInvalid() {
        assertWSLInvalid("\\\\wsl$\\Debian\\invalid\\ocaml");
    }

    @Test
    public void testInvalidWSLDistribution() {
        assertWSLInvalid(WSLFolders.OPAM_INVALID_DIST.toplevel);
    }

    @Test
    public void testNotOCaml() {
        assertWSLInvalid(WSLFolders.BIN_VALID);
    }

    @Test
    public void testBin() {
        assertWSLValid(WSLFolders.BIN_VALID_SDK);
    }

    @Test
    public void testOpamBinValid() {
        assertWSLValid(WSLFolders.OPAM_VALID_SDK);
    }

    @Test
    public void testOpamBinInvalid() {
        assertWSLInvalid(WSLFolders.OPAM_INVALID_BIN);
    }
}
