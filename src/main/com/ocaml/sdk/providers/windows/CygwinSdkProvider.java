package com.ocaml.sdk.providers.windows;

import org.jetbrains.annotations.NotNull;

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
        return getInstallationFolders().contains(path) && path.endsWith(OCAML_EXE);
    }

    // Implementation

    @Override public @NotNull Set<String> getOCamlExecutablePathCommands() {
        return Set.of(OCAML_EXE);
    }

    @Override public @NotNull List<String> getOCamlCompilerExecutablePathCommands() {
        return List.of("ocamlc.opt.exe", "ocamlc.exe");
    }

    @Override public @NotNull Set<String> getInstallationFolders() {
        return Set.of("cygwin64");
    }
}
