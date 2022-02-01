package com.ocaml.sdk.providers.linux;

import com.intellij.openapi.options.ConfigurationException;
import com.ocaml.sdk.providers.simple.SimpleSdkData;
import com.ocaml.utils.files.OCamlFileUtils;
import org.junit.Test;

import java.util.ArrayList;

@SuppressWarnings("JUnit4AnnotatedMethodInJUnit3TestCase")
public class LinuxCreateSimpleSdkTest extends LinuxBaseTest {

    private void assertCreate(int i) {
        if (passLinuxTest()) return;
        ArrayList<String> homes = new ArrayList<>();
        try {
            try {
                for (; i > 0 ; i--) {
                    SimpleSdkData simpleSdkData = new SimpleSdkData(
                            LinuxFolders.BIN_CREATE_SDK.toplevel,
                            LinuxFolders.BIN_CREATE_SDK.comp,
                            LinuxFolders.BIN_CREATE_SDK.version,
                            LinuxFolders.BIN_CREATE_SDK.sources
                    );
                    homes.add(simpleSdkData.homePath);
                    assertWSLHomeValid(simpleSdkData.homePath);
                }
            } catch (ConfigurationException e) {
                fail(e.getMessage());
            }
        } finally {
            homes.forEach(OCamlFileUtils::deleteDirectory);
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
