package com.ocaml.sdk.providers.cygwin;

import com.intellij.openapi.options.ConfigurationException;
import com.ocaml.sdk.providers.simple.SimpleSdkData;
import com.ocaml.utils.files.OCamlFileUtils;
import org.junit.Test;

import java.util.ArrayList;

@SuppressWarnings("JUnit4AnnotatedMethodInJUnit3TestCase")
public class CygwinCreateSimpleSdkTest extends CygwinBaseTest {

    private void assertCreate(int i) {
        ArrayList<String> homes = new ArrayList<>();
        try {
            try {
                for (; i > 0 ; i--) {
                    SimpleSdkData simpleSdkData = new SimpleSdkData(
                            "C:\\cygwin64\\bin\\ocaml.exe",
                            "C:\\cygwin64\\bin\\ocamlc.opt.exe",
                            "4.10.0",
                            "C:\\cygwin64\\lib\\ocaml"
                    );
                    homes.add(simpleSdkData.homePath);
                    assertCygwinHomeValid(simpleSdkData.homePath);
                }
            } catch (ConfigurationException e) {
                fail(e.getMessage());
            }
        } finally {
//            homes.forEach(OCamlFileUtils::deleteDirectory);
            OCamlFileUtils.deleteDirectory(homes.get(0));
        }
    }

    @Test
    public void testCreate1Sdk() {
        assertCreate(1);
    }

    @Test
    public void testCreate2Sdks() {
        assertCreate(2);
    }

    @Test
    public void testCreate3Sdks() {
        assertCreate(3);
    }

}
