package com.ocaml.sdk.providers.windows;

import com.intellij.openapi.util.io.FileUtil;
import com.ocaml.sdk.providers.BaseOCamlSdkProvider;
import com.ocaml.sdk.providers.OCamlSdkProvider;
import com.ocaml.sdk.providers.simple.SimpleSdkData;
import com.ocaml.sdk.providers.utils.OCamlSdkScanner;
import org.jetbrains.annotations.NotNull;

import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

/**
 * Windows is not natively supporting ocaml.
 * We are calling providers such as, WSL, Cygwin, Ocaml64, etc.
 */
public class WindowsOCamlSdkProvider extends BaseOCamlSdkProvider {
    private final CygwinSdkProvider cygwinSdkProvider;
    private final OCaml64SdkProvider oCaml64SdkProvider;
    private final WSLSdkProvider wslSdkProvider;
    private final OCamlSdkProvider[] myProviders;

    public WindowsOCamlSdkProvider() {
        cygwinSdkProvider = new CygwinSdkProvider();
        oCaml64SdkProvider = new OCaml64SdkProvider();
        wslSdkProvider = new WSLSdkProvider();
        myProviders = new OCamlSdkProvider[]{cygwinSdkProvider, oCaml64SdkProvider, wslSdkProvider};
    }

    // There is no such thing on Windows

    @Override protected boolean canUseProviderForOCamlBinary(@NotNull String path) {
        return false;
    }

    @Override protected boolean canUseProviderForHome(@NotNull Path homePath) {
        return false;
    }

    @Override protected boolean canUseProviderForHome(@NotNull String homePath) {
        return false;
    }

    @Override public @NotNull List<String> getOCamlCompilerCommands() {
        return List.of();
    }

    @Override public @NotNull Set<String> getOCamlTopLevelCommands() {
        return Set.of();
    }

    @Override public @NotNull Set<String> getInstallationFolders() {
        return Set.of();
    }

    // but, we can install these

    @Override public @NotNull List<OCamlSdkProvider> getNestedProviders() {
        List<OCamlSdkProvider> nestedProviders = super.getNestedProviders();
        nestedProviders.addAll(Arrays.asList(myProviders));
        return nestedProviders;
    }

    // We are the ones

    @Override public @NotNull Set<String> suggestHomePaths() {
        var fsRoots = FileSystems.getDefault().getRootDirectories();
        if (fsRoots == null) return Collections.emptySet();

        HashSet<Path> installationDirectories = new HashSet<>();
        HashSet<Path> roots = new HashSet<>();
        for (OCamlSdkProvider p : new OCamlSdkProvider[]{cygwinSdkProvider, oCaml64SdkProvider, wslSdkProvider}) {
            for (String installationDirectory : p.getInstallationFolders()) {
                Path installationDirectoryPath = Path.of(installationDirectory);
                boolean absolute = installationDirectoryPath.isAbsolute() || installationDirectory.startsWith("\\\\"); // UNC path
                if (absolute) roots.add(installationDirectoryPath);
                else installationDirectories.add(installationDirectoryPath);
            }
        }
        LOG.debug("Roots found (1/2):" + roots);
        LOG.debug("Installation directories to explore:" + installationDirectories);

        for (Path root : fsRoots) {
            if (!Files.exists(root)) continue;
            for (Path dir : installationDirectories) {
                roots.add(root.resolve(dir));
            }
        }

        // we may have created simple SDKs
        roots.add(Path.of(FileUtil.expandUserHome(SimpleSdkData.SDK_FOLDER)));

        LOG.debug("Roots found (2/2):" + roots);

        return OCamlSdkScanner.scanAll(roots, true);
    }
}
