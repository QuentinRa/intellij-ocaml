package com.ocaml.compiler;

import com.ocaml.*;
import org.junit.*;

@SuppressWarnings("JUnit4AnnotatedMethodInJUnit3TestCase")
public final class SdkHomeCheckTest extends OCamlBaseTest {

    //
    // Windows
    //

    @Test
    public void testValidWin(){
        assertTrue(OCamlUtils.Home.isValid(V_OCAML_HOME_WIN));
    }

    @Test
    public void testInvalidWin(){
        assertFalse(OCamlUtils.Home.isValid(I_OCAML_HOME_WIN));
    }

    @Test
    public void testInvalidWinVersion(){
        assertFalse(OCamlUtils.Home.isValid("C:\\cygwin64\\"));
    }

    //
    // Windows
    //

    @Test
    public void testValidWSL(){
        assertTrue(OCamlUtils.Home.isValid(V_OCAML_HOME_WSL));
    }

    @Test
    public void testInvalidWSL(){
        assertFalse(OCamlUtils.Home.isValid(I_OCAML_HOME_WSL));
    }

    @Test
    public void testInvalidWSLVersion(){
        assertFalse(OCamlUtils.Home.isValid("\\\\wsl$\\Debian\\"));
    }
}
