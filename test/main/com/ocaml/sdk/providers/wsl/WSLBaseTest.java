package com.ocaml.sdk.providers.wsl;

import com.ocaml.sdk.providers.BaseSdkProviderTest;
import com.ocaml.sdk.utils.OCamlSdkHomeManager;

public class WSLBaseTest extends BaseSdkProviderTest {

    protected void assertWSLHomeValid(String homePath) {
        assertTrue(OCamlSdkHomeManager.isValid(homePath));
    }

    protected void assertWSLHomeInvalid(String homePath) {
        assertFalse(OCamlSdkHomeManager.isValid(homePath));
    }

}
