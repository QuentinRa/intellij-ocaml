package com.ocaml.sdk.utils.version;

import com.ocaml.OCamlBaseTest;
import com.ocaml.sdk.utils.OCamlSdkVersionManager;
import org.junit.Test;

@SuppressWarnings("JUnit4AnnotatedMethodInJUnit3TestCase")
public final class VersionRegexTest extends OCamlBaseTest {

    // invalid

    @Test
    public void test1d() {
        assertFalse(OCamlSdkVersionManager.isValid("4"));
    }

    @Test
    public void test1dEmpty() {
        assertFalse(OCamlSdkVersionManager.isValid("4."));
    }

    @Test
    public void test1d1d() {
        assertFalse(OCamlSdkVersionManager.isValid("4.0"));
    }

    @Test
    public void test1d1dEmpty() {
        assertFalse(OCamlSdkVersionManager.isValid("4.0."));
    }

    @Test
    public void test1d1d1d() {
        assertFalse(OCamlSdkVersionManager.isValid("4.0.0"));
    }

    @Test
    public void testInvalidStart() {
        assertFalse(OCamlSdkVersionManager.isValid("ocaml-4.05.0"));
    }

    // valid

    @Test
    public void test1d2d() {
        assertTrue(OCamlSdkVersionManager.isValid("4.05"));
    }

    @Test
    public void test1d2dKind() {
        assertTrue(OCamlSdkVersionManager.isValid("4.05+mingw64c"));
    }

    @Test
    public void test1d2d1d() {
        assertTrue(OCamlSdkVersionManager.isValid("4.05.0"));
    }

    @Test
    public void test1d2d1dKind() {
        assertTrue(OCamlSdkVersionManager.isValid("4.05.0+mingw64c"));
    }

    @Test
    public void test1d2d1dSpecial() {
        assertTrue(OCamlSdkVersionManager.isValid("4.05.0-v"));
        assertTrue(OCamlSdkVersionManager.isValid("4.05.0-v2"));
        // invalid inside "valid", because they are altogether special
        assertFalse(OCamlSdkVersionManager.isValid("4.05.0-"));
    }
}
