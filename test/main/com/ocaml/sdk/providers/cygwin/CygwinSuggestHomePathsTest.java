package com.ocaml.sdk.providers.cygwin;

import com.intellij.util.SystemProperties;
import org.junit.Test;

@SuppressWarnings("JUnit4AnnotatedMethodInJUnit3TestCase")
public class CygwinSuggestHomePathsTest extends CygwinBaseTest {

    @Test
    public void testCygwinOpamSdksAreSuggested() {
        assertInstallationFolderWasSuggested(
                "C:\\OCaml64\\home\\"+ SystemProperties.getUserName() +"\\.opam\\"
        );
    }
}
