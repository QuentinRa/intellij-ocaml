package com.ocaml.sdk.providers.cygwin;

import com.intellij.openapi.util.SystemInfo;
import com.ocaml.sdk.providers.BaseSdkProviderTest;
import com.ocaml.sdk.providers.simple.DetectionResult;
import com.ocaml.sdk.providers.simple.OCamlNativeDetector;
import com.ocaml.sdk.utils.OCamlSdkHomeManager;
import com.ocaml.sdk.utils.SdkInfo;

import java.io.File;
import java.nio.file.Path;

public class CygwinBaseTest extends BaseSdkProviderTest {

    protected boolean passCygwinTest() {
        if(!SystemInfo.isWin10OrNewer) return true;
        // ensure cygwin is available
        boolean canPass = new File(CygwinFolders.BIN_VALID_SDK.path).exists()
                && new File(CygwinFolders.BIN_VALID_SDK.toplevel).exists()
                && new File(CygwinFolders.BIN_VALID_EXE).exists()
                && new File(CygwinFolders.OPAM_HOME).exists()
                && new File(CygwinFolders.OPAM_VALID_SDK.path).exists()
                && new File(CygwinFolders.OPAM_VALID_SDK.toplevel).exists()
                && !(new File(CygwinFolders.OPAM_INVALID_BIN).exists());
        if (!canPass) System.out.println("Cygwin: test ignored");
        return !canPass;
    }

    protected void assertCygwinDetectionValid(SdkInfo info) {
        assertCygwinDetectionValid(info.toplevel, info.comp, info.version);
    }

    protected void assertCygwinDetectionValid(String ocamlBinary, String ocamlcName, String expectedVersion) {
        if (passCygwinTest()) return;

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
        if (passCygwinTest()) return;
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
        if (passCygwinTest()) return;
        assertTrue(OCamlSdkHomeManager.isValid(homePath));
    }

    protected void assertCygwinHomeInvalid(String homePath) {
        if (passCygwinTest()) return;
        assertFalse(OCamlSdkHomeManager.isValid(homePath));
    }

    @Override protected void assertInstallationFolderWasSuggested(String installationFolder) {
        if (passCygwinTest()) return;
        super.assertInstallationFolderWasSuggested(installationFolder);
    }
}
