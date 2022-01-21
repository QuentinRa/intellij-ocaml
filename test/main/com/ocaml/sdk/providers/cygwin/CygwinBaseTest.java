package com.ocaml.sdk.providers.cygwin;

import com.ocaml.OCamlBaseTest;
import com.ocaml.sdk.providers.simple.DetectionResult;
import com.ocaml.sdk.providers.simple.OCamlNativeDetector;
import com.ocaml.sdk.utils.OCamlSdkHomeManager;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class CygwinBaseTest extends OCamlBaseTest  {
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

    protected void assertInstallationFolderWasSuggested(String installationFolder) {
        List<String> homePaths = OCamlSdkHomeManager.suggestHomePaths();
        Path p = Path.of(installationFolder);
        // cannot test anything
        if (!Files.exists(p)) {
            assertTrue(true);
            return;
        }
        File[] files = p.toFile().listFiles();
        if (files == null) { // no files, done
            assertTrue(true);
            return;
        }
        for (File file : files) {
            String path = file.getAbsolutePath();
            if (!OCamlSdkHomeManager.isValid(path)) continue;
            assertTrue(homePaths.remove(path));
        }
    }
}
