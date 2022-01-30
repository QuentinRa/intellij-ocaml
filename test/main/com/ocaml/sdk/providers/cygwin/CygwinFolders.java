package com.ocaml.sdk.providers.cygwin;

import com.intellij.util.SystemProperties;
import com.ocaml.sdk.providers.simple.SimpleSdkData;
import com.ocaml.sdk.utils.SdkInfo;

public interface CygwinFolders {

    //
    // BIN
    //
    /** a valid binary candidate for an SDK **/
    SdkInfo BIN_VALID_SDK = new SdkInfo(
            "C:\\cygwin64\\",
            "C:\\cygwin64\\bin\\ocaml.exe",
            "4.10.0",
            "ocamlc.opt.exe"
    );
    SdkInfo BIN_CREATE_SDK = new SdkInfo(
            null,
            "C:\\cygwin64\\bin\\ocaml.exe",
            "C:\\cygwin64\\bin\\ocamlc.opt.exe",
            "4.10.0",
            "C:\\cygwin64\\lib\\ocaml"
    );

    /** a valid file that is not ocaml **/
    String BIN_VALID_EXE = "C:\\cygwin64\\bin\\find.exe";

    //
    // OPAM
    //

    /** path to the opam folder **/
    String OPAM_HOME =  "C:\\cygwin64\\home\\"+ SystemProperties.getUserName() +"\\.opam\\";

    /** everything should be valid **/
    SdkInfo OPAM_VALID_SDK = new SdkInfo(
            "C:\\cygwin64\\home\\"+ SystemProperties.getUserName() + "\\.opam\\4.08.0\\",
            "C:\\cygwin64\\home\\"+ SystemProperties.getUserName() + "\\.opam\\4.08.0\\bin\\ocaml.exe",
            "4.08.0",
            "ocamlc.opt.exe"
    );

    /** expected: properly formatted path, non-existing SDK version **/
    String OPAM_INVALID_BIN = "C:\\cygwin64\\home\\"+ SystemProperties.getUserName() + "\\.opam\\0.0.0\\bin\\ocaml.exe";

}
