package com.ocaml.sdk.providers.wsl;

import com.intellij.execution.ExecutionException;
import com.intellij.execution.configurations.GeneralCommandLine;
import com.intellij.execution.wsl.WSLCommandLineOptions;
import com.intellij.execution.wsl.WSLDistribution;
import com.intellij.execution.wsl.WslDistributionManager;
import com.intellij.openapi.util.SystemInfo;
import com.ocaml.sdk.utils.SdkInfo;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.util.List;

public class WSLFolders {

    public @Nullable SdkInfo BIN_VALID_SDK;
    public @Nullable SdkInfo BIN_CREATE_SDK;
    public @Nullable String BIN_VALID;
    public @Nullable String OPAM_HOME;
    public @Nullable SdkInfo OPAM_VALID_SDK;
    public @Nullable SdkInfo OPAM_INVALID_DIST;
    public @Nullable String OPAM_INVALID;
    public @Nullable String OPAM_INVALID_BIN;

    public @Nullable String HOME_INVALID;
    public @Nullable String HOME_INVALID_TS;
    public @Nullable String OCAML_BIN_INVALID;

    public WSLFolders() {
        // not available
        if (!isWSLCompatible()) return;

        // get distributions
        List<WSLDistribution> list = WslDistributionManager.getInstance().getInstalledDistributions();
        if (list.size() <= 0) return;

        String version = null;

        // find
        for (WSLDistribution distribution : list) {
            try {
                // opam
                GeneralCommandLine cli = new GeneralCommandLine("opam", "switch", "show");
                cli = distribution.patchCommandLine(cli, null, new WSLCommandLineOptions());
                Process process = cli.createProcess();
                String s = new String(process.getInputStream().readAllBytes());
                if (s.isEmpty()) throw new ExecutionException("Empty version");
                System.out.println("found:"+s);
                // todo: fill variables
            } catch (ExecutionException | IOException ignore) {}
        }

        if (version == null) return;

        HOME_INVALID = "\\\\wsl$\\Debian\\invalid";
        HOME_INVALID_TS = "\\\\wsl$\\Debian\\invalid\\";
        OCAML_BIN_INVALID = "\\\\wsl$\\Debian\\invalid\\ocaml";
    }

    private boolean isWSLCompatible () {
        return SystemInfo.isWin10OrNewer;
    }

    //    //
//    // BIN
//    //
//    /** a valid binary candidate for an SDK **/
//    SdkInfo BIN_VALID_SDK = new SdkInfo(
//            "\\\\wsl$\\Debian\\",
//            "\\\\wsl$\\Debian\\bin\\ocaml",
//            "",
//            "4.12.0",
//            "\\usr\\lib\\ocaml"
//    );
//    SdkInfo BIN_CREATE_SDK = new SdkInfo(
//            null,
//            "\\\\wsl$\\Debian\\bin\\ocaml",
//            "\\\\wsl$\\Debian\\bin\\ocamlc",
//            "4.12.0",
//            "\\\\wsl$\\Debian\\usr\\lib\\ocaml"
//    );
//
//    /** a valid file that is not ocaml **/
//    String BIN_VALID = "\\\\wsl$\\Debian\\bin\\find";
//
//    //
//    // OPAM
//    //
//
//    /** path to the opam folder **/
//    String OPAM_HOME =  "\\\\wsl$\\Debian\\home\\calistro\\.opam\\";
//
//    /** everything should be valid **/
//    SdkInfo OPAM_VALID_SDK = new SdkInfo(
//            "\\\\wsl$\\Debian\\home\\calistro\\.opam\\4.07.0",
//            "\\\\wsl$\\Debian\\home\\calistro\\.opam\\4.07.0\\bin\\ocaml",
//            "",
//            "4.07.0",
//            "\\lib\\ocaml"
//    );
//
//    SdkInfo OPAM_INVALID_DIST = new SdkInfo(
//            "\\\\wsl$\\Fedora\\home\\calistro\\.opam\\4.07.0",
//            "\\\\wsl$\\Fedora\\home\\calistro\\.opam\\4.07.0\\bin\\ocaml",
//            "",
//            ""
//    );
//
//    /** expected: properly formatted path, non-existing SDK version **/
//    String OPAM_INVALID = "\\\\wsl$\\Debian\\home\\calistro\\.opam\\0.00.0";
//    String OPAM_INVALID_BIN = "\\\\wsl$\\Debian\\home\\calistro\\.opam\\0.00.0\\bin\\ocaml";
}
