package com.ocaml.sdk.providers.linux;

import com.intellij.util.SystemProperties;
import com.ocaml.sdk.utils.SdkInfo;

public interface LinuxFolders {

    //
    // BIN
    //

    /** a valid binary candidate for an SDK **/
    SdkInfo BIN_VALID_SDK = new SdkInfo(
            "/",
            "/bin/ocaml",
            "",
            "4.08.1",
            "/lib/ocaml"
    );
    SdkInfo BIN_CREATE_SDK = new SdkInfo(
            null,
            "/bin/ocaml",
            "/bin/ocamlc",
            "4.08.1",
            "/usr/lib/ocaml"
    );

    /** a valid file that is not ocaml **/
    String BIN_VALID = "/bin/find";

    //
    // OPAM
    //

    /** path to the opam folder **/
    String OPAM_HOME =  "/home/"+ SystemProperties.getUserName() +"/.opam/";

    /** everything should be valid **/
    SdkInfo OPAM_VALID_SDK = new SdkInfo(
            "/home/" + SystemProperties.getUserName() + "/.opam/4.12.0",
            "/home/" + SystemProperties.getUserName() + "/.opam/4.12.0/bin/ocaml",
            "",
            "4.12.0",
            "/lib/ocaml" // relative
    );

    /** expected: properly formatted path, non-existing SDK version **/
    String OPAM_INVALID = "/home/" + SystemProperties.getUserName() + "/.opam/0.00.0";
    String OPAM_INVALID_BIN = "/home/" + SystemProperties.getUserName() + "/.opam/0.00.0/ocaml";

}
