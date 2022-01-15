package com.ocaml.ide.sdk;

import com.intellij.openapi.projectRoots.*;
import org.jetbrains.annotations.*;

/**
 * I'm still unsure of which kind of JDK we will use as a base for OCamlSdkType.
 * This class is the one handling this.
 */
abstract class LocalSdkType extends JavaSdk {
    public LocalSdkType(@NotNull String name) {
        super(name);
    }

    @Override public @NotNull Sdk createJdk(@NotNull String jdkName, @NotNull String home, boolean isJre) {
        throw new UnsupportedOperationException("OCamlSdkType#createJdk not supported");
    }

    @Override public @Nullable JavaSdkVersion getVersion(@NotNull Sdk sdk) {
        throw new UnsupportedOperationException("OCamlSdkType#getVersion not supported");
    }

    @Override public boolean isOfVersionOrHigher(@NotNull Sdk sdk, @NotNull JavaSdkVersion version) {
        throw new UnsupportedOperationException("OCamlSdkType#isOfVersionOrHigher not supported");
    }

    @Override public String getBinPath(@NotNull Sdk sdk) {
        throw new UnsupportedOperationException("OCamlSdkType#getBinPath not supported");
    }

    @Override public String getToolsPath(@NotNull Sdk sdk) {
        throw new UnsupportedOperationException("OCamlSdkType#getToolsPath not supported");
    }

    @Override public String getVMExecutablePath(@NotNull Sdk sdk) {
        throw new UnsupportedOperationException("OCamlSdkType#getVMExecutablePath not supported");
    }

    @SuppressWarnings({"UnstableApiUsage", "RedundantSuppression"})
    public JavaSdkVersion getVersion(@NotNull String versionString) {
        throw new UnsupportedOperationException("OCamlSdkType#getVersion not supported");
    }
}
