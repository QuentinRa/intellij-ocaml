package com.ocaml.sdk.providers.windows;

import com.intellij.openapi.util.io.FileUtil;
import org.jetbrains.annotations.NotNull;

import java.nio.file.Path;
import java.util.List;
import java.util.Set;

/**
 * Cygwin
 * <ul>
 *     <li>files are ending with ".exe"</li>
 *     <li>
 *         sometimes "ocamlc.exe" is not valid (if the user isn't using mingw), so we need
 *         to use "ocamlc.opt.exe"
 *      </li>
 * </ul>
 */
public class CygwinSdkProvider extends AbstractWindowsBaseProvider {

    protected static final String OCAML_EXE = "ocaml.exe";

    // Utils

    @Override
    protected boolean canUseProviderForOCamlBinary(@NotNull String path) {
        path = FileUtil.toSystemIndependentName(path);
        if (!path.endsWith(OCAML_EXE)) return false;
        for (String folder:getInstallationFolders()) {
            if (path.contains(folder)) return true;
        }
        return false;
    }

    @Override protected boolean canUseProviderForHome(@NotNull Path homePath) {
        String homePathString = FileUtil.toSystemIndependentName(homePath.toFile().getAbsolutePath());
        for (String folder:getInstallationFolders()) {
            if (homePathString.contains(folder)) return true;
        }
        return false;
    }

    // Implementation

    @Override public @NotNull Set<String> getOCamlTopLevelCommands() {
        return Set.of(OCAML_EXE);
    }

    @Override public @NotNull List<String> getOCamlCompilerCommands() {
        return List.of("ocamlc.opt.exe", "ocamlc.exe");
    }

    @Override public @NotNull Set<String> getInstallationFolders() {
        return Set.of("cygwin64");
    }
}
