package com.ocaml.sdk.providers.linux;

import com.intellij.openapi.util.SystemInfo;
import com.ocaml.sdk.providers.BaseSdkProviderTest;
import com.ocaml.sdk.utils.OCamlSdkHomeManager;

public class LinuxBaseTest extends BaseSdkProviderTest {

    protected boolean passLinuxTest() {
        return !SystemInfo.isLinux;
    }

    protected void assertWSLHomeValid(String homePath) {
        if (passLinuxTest()) return;
        assertTrue(OCamlSdkHomeManager.isValid(homePath));
    }

    protected void assertWSLHomeInvalid(String homePath) {
        if (passLinuxTest()) return;
        assertFalse(OCamlSdkHomeManager.isValid(homePath));
    }

}
