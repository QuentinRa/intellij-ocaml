package com.ocaml.sdk.utils.version;

import com.ocaml.OCamlBaseTest;
import com.ocaml.sdk.utils.OCamlSdkVersionManager;
import org.junit.Test;

@SuppressWarnings("JUnit4AnnotatedMethodInJUnit3TestCase")
public final class VersionCompareTest extends OCamlBaseTest {

    private void assertPath(String path1, String path2, int expected) {
        assertEquals(expected, OCamlSdkVersionManager.comparePaths(path1, path2));
    }

    @Test
    public void testEquals1() {
        assertPath("~/4.08", "~/4.08", 0);
    }

    @Test
    public void testEquals2() {
        assertPath("~/4.08.0", "~/4.08.0", 0);
    }

    @Test
    public void testGreater() {
        assertPath("~/4.12.0", "~/4.08.0", 1);
    }

    @Test
    public void testLesser() {
        assertPath("~/4.08.0", "~/4.12.0", -1);
    }

    @Test
    public void testGreaterMingw() {
        assertPath("~/4.13.1+mingw64c", "~/4.12.0", 1);
    }

    @Test
    public void testGreaterPatchVersion() {
        assertPath("~/4.13.1", "~/4.13.0", 1);
    }
}