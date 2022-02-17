package com.ocaml.sdk.providers.ocaml64;

import com.intellij.openapi.util.SystemInfo;
import com.ocaml.sdk.providers.cygwin.CygwinBaseTest;

import java.io.File;

public class OCaml64BaseTest extends CygwinBaseTest {

    protected boolean passCygwinTest() {
        if(!SystemInfo.isWin10OrNewer) return true;
        boolean canPass = new File(OCaml64Folders.OPAM_HOME).exists()
                && new File(OCaml64Folders.OPAM_VALID_SDK.path).exists()
                && new File(OCaml64Folders.OPAM_VALID_SDK.toplevel).exists()
                && !(new File(OCaml64Folders.OPAM_INVALID_BIN).exists());
        if (!canPass) System.out.println("OCaml64: test ignored");
        return !canPass;
    }
}
