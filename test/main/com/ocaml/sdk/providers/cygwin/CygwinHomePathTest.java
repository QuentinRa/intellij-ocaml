package com.ocaml.sdk.providers.cygwin;

import com.intellij.util.SystemProperties;
import org.junit.Test;

@SuppressWarnings("JUnit4AnnotatedMethodInJUnit3TestCase")
public class CygwinHomePathTest extends CygwinBaseTest  {

    @Test
    public void testFilesOkNoVersion() {
        // we got every file, but the file name do not have the
        // version of ocaml (ex: 4.05.0), so it's not a valid homePath
        assertCygwinHomeInvalid("C:\\cygwin64\\");
    }

    @Test
    public void testInvalid() {
        assertCygwinHomeInvalid("C:\\cygwin64\\invalid");
    }

    @Test
    public void testOpamValid() {
        assertCygwinHomeValid(
                "C:\\cygwin64\\home\\"+ SystemProperties.getUserName() + "\\.opam\\4.08.0\\"
        );
    }

    @Test
    public void testOpamInvalid() {
        assertCygwinHomeInvalid(
                "C:\\cygwin64\\home\\"+ SystemProperties.getUserName() + "\\.opam\\0.00.0\\"
        );
    }

}
