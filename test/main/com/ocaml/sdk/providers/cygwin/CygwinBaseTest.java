package com.ocaml.sdk.providers.cygwin;

import com.ocaml.sdk.providers.BaseSdkProviderTest;
import com.ocaml.sdk.providers.simple.DetectionResult;
import com.ocaml.sdk.providers.simple.OCamlNativeDetector;
import com.ocaml.sdk.utils.OCamlSdkHomeManager;
import com.ocaml.sdk.utils.SdkInfo;

import java.nio.file.Path;

public class CygwinBaseTest extends BaseSdkProviderTest {

    protected CygwinFolders folders;

    @Override protected void setUp() throws Exception {
        super.setUp();
        folders = new CygwinFolders("cygwin64");
    }

    @Override protected void tearDown() throws Exception {
        super.tearDown();
        folders = null;
    }

    protected void assertCygwinDetectionValid(SdkInfo info) {
        assertCygwinDetectionValid(info.toplevel, info.comp, info.version);
    }

    protected void assertCygwinDetectionValid(String ocamlBinary, String ocamlcName, String expectedVersion) {
        Path bin = Path.of(ocamlBinary).getParent();
        String root = bin.getParent().toFile().getAbsolutePath();
        String ocamlc = bin.resolve(ocamlcName).toFile().getAbsolutePath();
        DetectionResult detectionResult = OCamlNativeDetector.detectNativeSdk(ocamlBinary);
        assertEquals(ocamlBinary, detectionResult.ocaml);
        assertEquals(ocamlc, detectionResult.ocamlCompiler);
        assertEquals(root + "\\lib\\ocaml", detectionResult.sources);
        assertEquals(expectedVersion, detectionResult.version);
        assertFalse(detectionResult.isError);
    }

    protected void assertCygwinDetectionInvalid(String ocamlBin) {
        try {
            DetectionResult detectionResult = OCamlNativeDetector.detectNativeSdk(ocamlBin);
            if (detectionResult.isError) throw new AssertionError("OK");
        } catch (AssertionError e) {
            assertTrue(true);
            return;
        }
        fail("Supposed unreachable code");
    }

    protected void assertCygwinHomeValid(String homePath) {
        assertTrue(OCamlSdkHomeManager.isValid(homePath));
    }

    protected void assertCygwinHomeInvalid(String homePath) {
        assertFalse(OCamlSdkHomeManager.isValid(homePath));
    }
}
