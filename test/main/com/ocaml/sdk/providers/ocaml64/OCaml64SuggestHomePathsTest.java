package com.ocaml.sdk.providers.ocaml64;

import com.intellij.util.SystemProperties;
import com.ocaml.sdk.providers.cygwin.CygwinBaseTest;
import org.junit.Test;

@SuppressWarnings("JUnit4AnnotatedMethodInJUnit3TestCase")
public final class OCaml64SuggestHomePathsTest extends CygwinBaseTest {

    @Test
    public void testCygwinOpamSdksAreSuggested() {
        assertInstallationFolderWasSuggested(
                "C:\\cygwin64\\home\\"+ SystemProperties.getUserName() +"\\.opam\\"
        );
    }
}
