package com.ocaml.sdk.providers.cygwin;

import org.junit.Test;

@SuppressWarnings("JUnit4AnnotatedMethodInJUnit3TestCase")
public final class CygwinSuggestHomePathsTest extends CygwinBaseTest {

    @Test
    public void testCygwinOpamSdksAreSuggested() {
        assertInstallationFolderWasSuggested(CygwinFolders.OPAM_HOME);
    }
}
