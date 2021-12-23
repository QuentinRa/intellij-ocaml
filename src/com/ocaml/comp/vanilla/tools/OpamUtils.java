package com.ocaml.comp.vanilla.tools;

import com.intellij.execution.wsl.*;
import com.intellij.openapi.util.*;
import com.intellij.openapi.util.io.*;
import com.ocaml.ide.sdk.*;
import org.jetbrains.annotations.*;

import java.io.*;
import java.nio.file.*;
import java.util.*;

/**
 * Utilities for .opam kind of SDK.
 * "extended" by PortableOpamUtils.
 */
public class OpamUtils {

    /** true if it is an opam SDK **/
    public static boolean isOpam(@NotNull File sdkHome) {
        return isOpamPath(sdkHome.getPath());
    }
    public static boolean isOpamPath(@NotNull String sdkHomePath) {
        return sdkHomePath.contains(".opam");
    }

    /** the folder in which sources are stored for opam SDKs,
     * @implNote we suppose that the sdk is an opam SDK */
    public static String getOpamSDKSourceFolder(@NotNull File sdkHome, @NotNull String version) {
        return new File(
                sdkHome,
                ".opam-switch/sources/ocaml-base-compiler." + version
        ).getAbsolutePath();
    }

    /** Check if we have sdk in the usual opam folder */
    public static void lookForSDK(HashSet<Path> roots) {
        if (SystemInfo.isWindows) {
            // look for WSL
            WslDistributionManager instance = WslDistributionManager.getInstance();
            for (WSLDistribution wslDistribution : instance.getInstalledDistributions()) {
                // check if there is an opam folder
                String home = wslDistribution.getUserHome();
                String opamFolderPath = wslDistribution.getWindowsPath(home+"\\.opam");
                File opamFolder = new File(opamFolderPath);
                if (!opamFolder.exists()) continue;
                // iterate the folder, find ocaml versions
                exploreOpamFolder(opamFolder, roots);
            }
        } else {
            // look for opam in home
            File opamFolder = new File(FileUtil.expandUserHome("~/.opam/"));
            if (opamFolder.exists() && opamFolder.isDirectory()) {
                exploreOpamFolder(opamFolder, roots);
            }
        }
    }

    private static void exploreOpamFolder(File folder, HashSet<Path> roots) {
        for (File f : Objects.requireNonNull(folder.listFiles())) {
            if (!f.canRead() || f.isFile()) {
                continue;
            }
            // todo: should not do this, and check ORSettingsConfigurable, same problem
            if (OCamlSdkType.VERSION_REGEXP.matcher(f.getPath().replace("\\", "/")).matches()) {
                roots.add(Path.of(f.getPath()));
            }
        }
    }
}
