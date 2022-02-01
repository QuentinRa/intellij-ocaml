package com.ocaml.sdk.providers.wsl;

import com.ocaml.sdk.utils.SdkInfo;

public interface WSLFolders {

    //
    // BIN
    //
    /** a valid binary candidate for an SDK **/
    SdkInfo BIN_VALID_SDK = new SdkInfo(
            "\\\\wsl$\\Debian\\",
            "\\\\wsl$\\Debian\\bin\\ocaml",
            "",
            "4.12.0",
            "\\usr\\lib\\ocaml"
    );
    SdkInfo BIN_CREATE_SDK = new SdkInfo(
            null,
            "\\\\wsl$\\Debian\\bin\\ocaml",
            "\\\\wsl$\\Debian\\bin\\ocamlc",
            "4.12.0",
            "\\\\wsl$\\Debian\\usr\\lib\\ocaml"
    );

    /** a valid file that is not ocaml **/
    String BIN_VALID = "\\\\wsl$\\Debian\\bin\\find";

    //
    // OPAM
    //

    /** path to the opam folder **/
    String OPAM_HOME =  "\\\\wsl$\\Debian\\home\\calistro\\.opam\\";

    /** everything should be valid **/
    SdkInfo OPAM_VALID_SDK = new SdkInfo(
            "\\\\wsl$\\Debian\\home\\calistro\\.opam\\4.07.0",
            "\\\\wsl$\\Debian\\home\\calistro\\.opam\\4.07.0\\bin\\ocaml",
            "",
            "4.07.0",
            "\\lib\\ocaml"
    );

    SdkInfo OPAM_INVALID_DIST = new SdkInfo(
            "\\\\wsl$\\Fedora\\home\\calistro\\.opam\\4.07.0",
            "\\\\wsl$\\Fedora\\home\\calistro\\.opam\\4.07.0\\bin\\ocaml",
            "",
            ""
    );

    /** expected: properly formatted path, non-existing SDK version **/
    String OPAM_INVALID = "\\\\wsl$\\Debian\\home\\calistro\\.opam\\0.00.0";
    String OPAM_INVALID_BIN = "\\\\wsl$\\Debian\\home\\calistro\\.opam\\0.00.0\\bin\\ocaml";

}
