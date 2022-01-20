package com.ocaml.ide.sdk.providers.windows;

import com.ocaml.ide.sdk.providers.BaseOCamlSdkProvider;
import org.jetbrains.annotations.NotNull;

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
public class CygwinSdkProvider extends BaseOCamlSdkProvider {

    protected static final String OCAML_EXE = "ocaml.exe";

    // Utils

    @Override
    protected boolean canUseProviderForOCamlBinary(@NotNull String path) {
        return path.contains("cygwin64") && path.endsWith(OCAML_EXE);
    }

    // Implementation

    @Override public @NotNull Set<String> getOCamlExecutablePathCommands() {
        return Set.of(OCAML_EXE);
    }

    @Override public @NotNull Set<String> getOCamlCompilerExecutablePathCommands() {
        return Set.of("ocamlc.exe", "ocamlc.opt.exe");
    }
}
