package com.ocaml.sdk.providers.ocaml64;

import com.intellij.util.SystemProperties;
import com.ocaml.sdk.utils.SdkInfo;

public interface OCaml64Folders {

    //
    // OPAM
    //

    /** path to the opam folder **/
    String OPAM_HOME =  "C:\\OCaml64\\home\\"+ SystemProperties.getUserName() +"\\.opam\\";

    /** everything should be valid **/
    SdkInfo OPAM_VALID_SDK = new SdkInfo(
            "C:\\OCaml64\\home\\"+ SystemProperties.getUserName() + "\\.opam\\4.13.1+mingw64c\\",
            "C:\\OCaml64\\home\\"+ SystemProperties.getUserName() + "\\.opam\\4.13.1+mingw64c\\bin\\ocaml.exe",
            "ocamlc.exe",
            "4.13.1+mingw64c"
    );

    /** expected: properly formatted path, non-existing SDK version **/
    String OPAM_INVALID_BIN = "C:\\OCaml64\\home\\"+ SystemProperties.getUserName() + "\\.opam\\0.00.0\\";

}
