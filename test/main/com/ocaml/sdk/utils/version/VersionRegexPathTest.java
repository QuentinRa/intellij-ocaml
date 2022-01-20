package com.ocaml.sdk.utils.version;

import com.ocaml.OCamlBaseTest;
import com.ocaml.sdk.utils.OCamlSdkVersionManager;
import org.junit.Test;

@SuppressWarnings("JUnit4AnnotatedMethodInJUnit3TestCase")
public final class VersionRegexPathTest extends OCamlBaseTest {

    private void assertIsUnknownVersion(String path) {
        assertTrue(OCamlSdkVersionManager.isUnknownVersion(OCamlSdkVersionManager.parse(path)));
    }

    private void assertIsVersion(String path, String expectedVersion) {
        assertEquals(expectedVersion, OCamlSdkVersionManager.parse(path));
    }

    @Test
    public void testEmpty() {
        assertIsUnknownVersion("");
    }

    @Test
    public void testNotAPath() {
        assertIsUnknownVersion("4.05.0");
    }

    @Test
    public void test2d() {
        assertIsUnknownVersion("/40.5");
        assertIsUnknownVersion("/40.50");
    }

    @Test
    public void test1dThen2d() {
        assertIsVersion("~/.opam/4.05", "4.05");
        assertIsVersion("~/.opam/4.05/", "4.05");
        assertIsVersion("~\\.opam\\4.05", "4.05");
        assertIsVersion("~\\.opam\\4.05/", "4.05");
    }

    @Test
    public void test1dThen2dThenEmpty() {
        assertIsUnknownVersion("~/.opam/4.05.");
        assertIsUnknownVersion("~\\.opam\\4.05.");
    }

    @Test
    public void test1dThen2dThen1d() {
        assertIsVersion("~/.opam/4.05.2", "4.05.2");
        assertIsVersion("~/.opam/4.05.2/", "4.05.2");
        assertIsVersion("~\\.opam\\4.05.2", "4.05.2");
        assertIsVersion("~\\.opam\\4.05.2\\", "4.05.2");
    }

    @Test
    public void test1dThen2dThen2d() {
        assertIsUnknownVersion("~/.opam/4.05.02");
        assertIsUnknownVersion("~/.opam/4.05.02/");
        assertIsUnknownVersion("~\\.opam\\4.05.02");
        assertIsUnknownVersion("~\\.opam\\4.05.02\\");
    }

    @Test
    public void test1dThen2dThen2dThenMingw() {
        assertIsVersion("~/.opam/4.13.1+mingw64c", "4.13.1+mingw64c");
        assertIsVersion("~/.opam/4.13.1+mingw64c/", "4.13.1+mingw64c");
        assertIsVersion("~\\.opam\\4.13.1+mingw64c", "4.13.1+mingw64c");
        assertIsVersion("~\\.opam\\4.13.1+mingw64c\\", "4.13.1+mingw64c");
    }

    @Test
    public void test1dThen2dThen2dThenFake() {
        assertIsVersion("~/.opam/4.12.0-v", "4.12.0");
        assertIsVersion("~/.opam/4.12.0-v2", "4.12.0");
        assertIsVersion("~/.opam/4.12.0-v/", "4.12.0");
        assertIsVersion("~/.opam/4.12.0-v2/", "4.12.0");
        assertIsVersion("~\\.opam\\4.12.0-v", "4.12.0");
        assertIsVersion("~\\.opam\\4.12.0-v2", "4.12.0");
        assertIsVersion("~\\.opam\\4.12.0-v\\", "4.12.0");
        assertIsVersion("~\\.opam\\4.12.0-v2\\", "4.12.0");
    }
}
