package com.ocaml.sdk.utils.version;

import com.ocaml.OCamlBaseTest;
import com.ocaml.sdk.utils.OCamlSdkVersionManager;
import org.junit.Test;

@SuppressWarnings("JUnit4AnnotatedMethodInJUnit3TestCase")
public class VersionNewerThanTest extends OCamlBaseTest  {

    private void assertIsNewer(String base, String version) {
        assertTrue(OCamlSdkVersionManager.isNewerThan(base, version));
    }
    private void assertIsNotNewer(String base, String version) {
        assertFalse(OCamlSdkVersionManager.isNewerThan(base, version));
    }

    @Test
    public void testSameNoMinor() {
        assertIsNewer("4.08", "4.08");
    }

    @Test
    public void testSame() {
        assertIsNewer("4.08.0", "4.08.0");
    }

    @Test
    public void testNotNewer() {
        assertIsNotNewer("4.12.0", "4.08.0");
    }

    @Test
    public void testNewer() {
        assertIsNewer("4.08.0", "4.12.0");
    }

    @Test
    public void testNotNewerWithMinor() {
        assertIsNotNewer("4.08.1", "4.08.0");
    }

    @Test
    public void testNewerWithMinor() {
        assertIsNewer("4.08.0", "4.08.1");
    }

}
