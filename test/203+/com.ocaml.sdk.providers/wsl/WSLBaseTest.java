package com.ocaml.sdk.providers.wsl;

import com.intellij.openapi.util.SystemInfo;
import com.ocaml.sdk.providers.BaseSdkProviderTest;
import com.ocaml.sdk.utils.OCamlSdkHomeManager;

public class WSLBaseTest extends BaseSdkProviderTest {

    protected boolean passWSLTest() {
        return !SystemInfo.isWin10OrNewer;
    }

    protected void assertWSLHomeValid(String homePath) {
        if (passWSLTest()) return;
        assertTrue(OCamlSdkHomeManager.isValid(homePath));
    }

    protected void assertWSLHomeInvalid(String homePath) {
        if (passWSLTest()) return;
        assertFalse(OCamlSdkHomeManager.isValid(homePath));
    }

}
