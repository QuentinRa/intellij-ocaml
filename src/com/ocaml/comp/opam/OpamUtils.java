package com.ocaml.comp.opam;

import com.intellij.execution.ExecutionException;
import com.intellij.execution.configurations.*;
import com.intellij.execution.wsl.*;
import com.intellij.openapi.project.*;
import com.intellij.openapi.util.*;
import com.intellij.openapi.util.text.*;
import com.ocaml.comp.opam.process.*;
import org.jetbrains.annotations.*;

import java.io.*;
import java.util.*;
import java.util.concurrent.*;

public class OpamUtils {

    /**
     * Look for OCaml versions installed using Opam
     */
    public static List<FindSwitchProcess.OpamSwitch> findExistingSDKs() {
        List<FindSwitchProcess.OpamSwitch> res = new ArrayList<>();
        CountDownLatch latch;

        if (SystemInfo.isWindows) {
            List<WSLDistribution> distributions = WslDistributionManager.getInstance().getInstalledDistributions();
            latch = new CountDownLatch(distributions.size());

            for (WSLDistribution distribution : distributions) {
                GeneralCommandLine cli = FindSwitchProcess.getCommand();
                try {
                    FindSwitchProcess.findSwitch(
                            distribution.getWindowsPath(distribution.getUserHome()+"/.opam/"),
                            distribution.patchCommandLine(cli, null, new WSLCommandLineOptions()),
                            o -> {
                                res.addAll(o);
                                latch.countDown();
                            }
                    );
                } catch (ExecutionException e) {
                    latch.countDown();
                }
            }
        } else {
            // Linux
            latch = new CountDownLatch(1);
            FindSwitchProcess.findSwitch(
                    "~/.opam/",
                    FindSwitchProcess.getCommand(),
                    o -> {
                        res.addAll(o);
                        latch.countDown();
                    }
            );
        }

        // wait for async calls
        try {
            latch.await();
        } catch (InterruptedException ignored) {}
        return res;
    }

    /** the folder in which sources are stored for opam SDKs,
     * @implNote we suppose that the sdk is an opam SDK */
    public static String getOpamSDKSourceFolder(@NotNull File sdkHome, @NotNull String version) {
        return new File(
                sdkHome,
                ".opam-switch/sources/ocaml-base-compiler." + version
        ).getAbsolutePath();
    }

    public static GeneralCommandLine patchCommandLine(@NotNull String opamRoot,
                                                      @NotNull GeneralCommandLine cli,
                                                      @Nullable Project project) {
        if (SystemInfo.isWindows) {
            if (opamRoot.startsWith(WSLDistribution.UNC_PREFIX)) {
                String path = StringUtil.trimStart(opamRoot, WSLDistribution.UNC_PREFIX);
                int index = path.indexOf('\\');
                if (index != -1) {
                    String distName = path.substring(0, index);
                    WSLDistribution distribution = WslDistributionManager.getInstance()
                            .getOrCreateDistributionByMsId(distName);
                    try {
                        cli = distribution.patchCommandLine(cli, project, new WSLCommandLineOptions());
                    } catch (ExecutionException ignore) {
                    }
                }
            }
        }
        return cli;
    }
}
