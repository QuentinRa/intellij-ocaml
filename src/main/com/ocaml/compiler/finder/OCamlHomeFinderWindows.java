package com.ocaml.compiler.finder;

import com.intellij.execution.wsl.WSLDistribution;
import com.intellij.execution.wsl.WslDistributionManager;
import com.intellij.util.ArrayUtil;
import com.intellij.util.SystemProperties;
import com.ocaml.compiler.cygwin.CygwinConstants;
import com.ocaml.ide.sdk.OCamlSdkUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * - handle WSL
 * - handle [root]/cygwin64
 * - handle [root]/ocaml
 * - handle [root]/OCaml
 * - handle [root]/OCaml64
 */
public class OCamlHomeFinderWindows extends BaseOCamlHomeFinder {

    public OCamlHomeFinderWindows(String... paths) {
        super(paths);
        myFinders.add(this::guessPossibleLocations);
        // WSL
        for (WSLDistribution distro : WslDistributionManager.getInstance().getInstalledDistributions()) {
            var wslFinder = new OCamlHomeFinderWsl(distro);
            myFinders.add(wslFinder::findExistingSdks);
        }
    }

    private Set<String> guessPossibleLocations() {
        var fsRoots = FileSystems.getDefault().getRootDirectories();
        if (fsRoots == null) return Collections.emptySet();
        HashSet<Path> roots = new HashSet<>();
        for (Path root : fsRoots) {
            if (!Files.exists(root)) continue;
            // I know that my teacher gave us an "ocaml" folder
            // so why not?
            roots.add(root.resolve("ocaml"));
            roots.add(root.resolve("OCaml"));
            // cygwin
            roots.add(root.resolve(CygwinConstants.getOpamFolder()));
            // there is also the deprecated installer "OCaml64" using cygwin
            roots.add(root.resolve("OCaml64"));
        }
        roots.add(Path.of(SystemProperties.getUserHome(), OCamlSdkUtils.JDK_FOLDER));
        return scanAll(roots, true);
    }

    private static class OCamlHomeFinderWsl extends BaseOCamlHomeFinder {
        private static final Set<String> DEFAULT_PATHS = Set.of("/bin", "/usr/bin", "/usr/local/bin");
        private final WSLDistribution myDistro;

        public OCamlHomeFinderWsl(WSLDistribution distro) {
            super(lookupPaths(distro));
            myDistro = distro;
        }

        private static String[] lookupPaths(WSLDistribution distro) {
            List<String> list = new ArrayList<>();
            String home = distro.getUserHome();
            if (home != null) list.add(distro.getWindowsPath(home + OCamlSdkUtils.JDK_FOLDER));
            return ArrayUtil.toStringArray(list);
        }

        // utils

        @Override
        protected @Nullable String getEnvironmentVariable(@NotNull String name) {
            String value = myDistro.getEnvironmentVariable(name);
            if (value == null) return null;
            else if (value.indexOf(':') < 0) return myDistro.getWindowsPath(value);

            String mntRoot = myDistro.getMntRoot();
            String converted = Stream.of(value.split(":"))
                    .filter(p -> !DEFAULT_PATHS.contains(p) && !p.startsWith(mntRoot))
                    .map(myDistro::getWindowsPath)
                    .collect(Collectors.joining(File.pathSeparator));
            return converted.isEmpty() ? null : converted;
        }

        @Override
        protected @Nullable Path getPathInUserHome(@NotNull String relativePath) {
            String wslPath = myDistro.getUserHome();
            return wslPath == null ? null : Path.of(myDistro.getWindowsPath(wslPath), relativePath);
        }

    }
}
