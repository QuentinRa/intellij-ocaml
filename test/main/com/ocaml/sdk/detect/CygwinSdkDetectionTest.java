package com.ocaml.sdk.detect;

import com.intellij.openapi.util.SystemInfo;
import com.intellij.util.SystemProperties;
import com.ocaml.OCamlBaseTest;
import com.ocaml.compiler.simple.DetectionResult;
import com.ocaml.compiler.simple.OCamlBinariesManager;
import org.junit.Test;

//@Ignore
@SuppressWarnings("JUnit4AnnotatedMethodInJUnit3TestCase")
public class CygwinSdkDetectionTest extends OCamlBaseTest {

    @Test
    public void testBin() {
        DetectionResult detectionResult = OCamlBinariesManager.detectNativeSdk();
        assertEquals("C:\\cygwin64\\bin\\ocaml.exe", detectionResult.ocaml);
        assertEquals("C:\\cygwin64\\bin\\ocamlc.opt.exe", detectionResult.ocamlCompiler);
        assertEquals("C:\\cygwin64\\lib\\ocaml", detectionResult.sources);
        assertFalse(detectionResult.isError);
    }

    @Test
    public void testOpamBin() {
        String sdkFolder = "C:\\cygwin64\\home\\"+ SystemProperties.getUserName() + "\\.opam\\4.08.0\\";
        String ocaml = sdkFolder + "bin\\ocaml.exe";
        String ocamlc = sdkFolder + "bin\\ocamlc.opt.exe";
        DetectionResult detectionResult = OCamlBinariesManager.detectNativeSdk(ocaml, ocamlc);
        assertEquals(ocaml, detectionResult.ocaml);
        assertEquals(ocamlc, detectionResult.ocamlCompiler);
        assertEquals(sdkFolder + "lib\\ocaml", detectionResult.sources);
        assertFalse(detectionResult.isError);
    }
}
