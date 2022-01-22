package com.ocaml.sdk.providers.wsl;

import org.junit.Test;

@SuppressWarnings("JUnit4AnnotatedMethodInJUnit3TestCase")
public final class WSLSuggestHomePathsTest extends WSLBaseTest {

    @Test
    public void testCygwinOpamSdksAreSuggested() {
        assertInstallationFolderWasSuggested("\\\\wsl$\\Debian\\home\\calistro\\.opam\\");
    }
}
