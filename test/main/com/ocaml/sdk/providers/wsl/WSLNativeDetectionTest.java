package com.ocaml.sdk.providers.wsl;

import com.ocaml.OCamlBaseTest;
import com.ocaml.sdk.providers.simple.DetectionResult;
import com.ocaml.sdk.providers.simple.OCamlNativeDetector;
import org.junit.Test;

@SuppressWarnings("JUnit4AnnotatedMethodInJUnit3TestCase")
public final class WSLNativeDetectionTest extends OCamlBaseTest {

    public void assertWSLValid(String ocamlBin, String expectedVersion) {
        assertWSLValid(ocamlBin, expectedVersion, "\\lib\\ocaml");
    }

    public void assertWSLValid(String ocamlBin, String expectedVersion, String expectedLib) {
        String root = ocamlBin.replace("\\bin\\ocaml", "");
        DetectionResult detectionResult = OCamlNativeDetector.detectNativeSdk(ocamlBin);
        assertEquals(ocamlBin, detectionResult.ocaml);
        assertEquals(ocamlBin+"c", detectionResult.ocamlCompiler);
        assertEquals(root + expectedLib, detectionResult.sources);
        assertEquals(expectedVersion, detectionResult.version);
        assertFalse(detectionResult.isError);
    }

    public void assertWSLInvalid(String ocamlBin) {
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
        assertWSLInvalid("\\\\wsl$\\Fedora\\home\\calistro\\.opam\\4.07.0\\bin\\ocaml");
    }

    @Test
    public void testNotOCaml() {
        assertWSLInvalid("\\\\wsl$\\Debian\\bin\\find");
    }

    @Test
    public void testBin() {
        assertWSLValid(
                "\\\\wsl$\\Debian\\bin\\ocaml",
                "4.12.0",
                "\\usr\\lib\\ocaml"
        );
    }

    @Test
    public void testOpamBinValid() {
        assertWSLValid(
                "\\\\wsl$\\Debian\\home\\calistro\\.opam\\4.07.0\\bin\\ocaml",
                "4.07.0"
        );
    }

    @Test
    public void testOpamBinInvalid() {
        assertWSLInvalid("\\\\wsl$\\Debian\\home\\calistro\\.opam\\0.00.0\\bin\\ocaml");
    }
}
