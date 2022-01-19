package com.ocaml.compiler.finder;

import com.intellij.openapi.diagnostic.*;
import com.intellij.openapi.util.*;
import com.intellij.openapi.util.text.*;
import com.intellij.util.*;
import com.intellij.util.containers.*;
import com.ocaml.compiler.*;
import org.jetbrains.annotations.*;

import java.io.*;
import java.nio.file.*;
import java.util.*;
import java.util.HashSet;
import java.util.function.*;

import static java.nio.file.Files.isDirectory;
import static java.util.Collections.emptySet;

/**
 * A cheap copy of JavaHomeFinderBasic
 * @see com.intellij.openapi.projectRoots.impl.JavaHomeFinderBasic
 */
public class BaseOCamlHomeFinder {
    private static final Logger LOG = Logger.getInstance(BaseOCamlHomeFinder.class);
    protected final List<Supplier<Set<String>>> myFinders = new ArrayList<>();

    public BaseOCamlHomeFinder(String ... paths) {
        myFinders.add(this::findInPATH);
        myFinders.add(() -> findInSpecifiedPaths(paths));
        myFinders.add(this::findJavaInstalledByOpam);
    }

    // Consume

    @NotNull
    public final Set<String> findExistingSdks() {
        Set<String> result = new TreeSet<>();
        for (Supplier<Set<String>> action : myFinders) {
            try {
                result.addAll(action.get());
            } catch (Exception e) {
                LOG.warn("Failed to find OCaml Home. " + e.getMessage(), e);
            }
        }
        return result;
    }

    // scan
    @SuppressWarnings("SameParameterValue") protected @NotNull Set<String> scanAll(@Nullable Path file, boolean includeNestDirs) {
        if (file == null) {
            return emptySet();
        }
        return scanAll(Collections.singleton(file), includeNestDirs);
    }

    protected @NotNull Set<String> scanAll(@NotNull Collection<? extends Path> files, boolean includeNestDirs) {
        Set<String> result = new HashSet<>();
        for (Path root : new HashSet<>(files)) {
            scanFolder(root, includeNestDirs, result);
        }
        return result;
    }

    protected void scanFolder(@NotNull Path folder, boolean includeNestDirs, @NotNull Collection<? super String> result) {
        // Check if the folder is valid
        if (OCamlUtils.Home.isValid(folder)) {
            result.add(folder.toAbsolutePath().toString());
            return;
        }

        // explore
        if (!includeNestDirs) return;
        File[] files = folder.toFile().listFiles();
        if (files == null) return;

        for (File candidate : files) {
            for (File adjusted : Collections.singletonList(candidate)) {
                scanFolder(adjusted.toPath(), false, result);
            }
        }
    }

    // utils

    @SuppressWarnings("SameParameterValue") protected @Nullable String getEnvironmentVariable(@NotNull String name) {
        return EnvironmentUtil.getValue(name);
    }

    // find

    private @NotNull Set<String> findInSpecifiedPaths(String[] paths) {
        return scanAll(ContainerUtil.map(paths, Paths::get), true);
    }

    private @NotNull Set<String> findInPATH() {
        try {
            String pathVarString = getEnvironmentVariable("PATH");
            if (pathVarString == null || pathVarString.isEmpty()) return emptySet();

            Set<Path> dirsToCheck = new HashSet<>();
            for (String p : pathVarString.split(File.pathSeparator)) {
                Path dir = Paths.get(p);
                // the path must ends with "bin"
                if (!StringUtilRt.equal(dir.getFileName().toString(), "bin", SystemInfoRt.isFileSystemCaseSensitive))
                    continue;
                // then the parent may be an ocaml SDK
                Path parentFile = dir.getParent();
                if (parentFile == null) continue;
                // we should check
                dirsToCheck.addAll(Collections.singletonList(parentFile));
            }

            return scanAll(dirsToCheck, false);
        } catch (Exception e) {
            LOG.warn("Failed to scan PATH for SDKs. " + e.getMessage(), e);
            return emptySet();
        }
    }

    private @NotNull Set<String> findJavaInstalledByOpam() {
        Path sdks = getPathInUserHome(".opam");
        return sdks != null && isDirectory(sdks) ? scanAll(sdks, true) : emptySet();
    }

    @SuppressWarnings("SameParameterValue") protected @Nullable Path getPathInUserHome(@NotNull String relativePath) {
        String homePath = System.getProperty("user.home");
        return homePath != null ? Path.of(homePath, relativePath) : null;
    }

}
