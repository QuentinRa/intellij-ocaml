package com.ocaml.sdk.providers.linux;

import org.junit.Test;

@SuppressWarnings("JUnit4AnnotatedMethodInJUnit3TestCase")
public final class LinuxSuggestHomePathsTest extends LinuxBaseTest {

    @Test
    public void testCygwinOpamSdksAreSuggested() {
        if (passLinuxTest()) return;
        assertInstallationFolderWasSuggested(LinuxFolders.OPAM_HOME);
    }
}
