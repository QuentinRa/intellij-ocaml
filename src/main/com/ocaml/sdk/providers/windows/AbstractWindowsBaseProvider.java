package com.ocaml.sdk.providers.windows;

import com.ocaml.sdk.providers.BaseOCamlSdkProvider;
import org.jetbrains.annotations.NotNull;

import java.util.Set;

public class AbstractWindowsBaseProvider extends BaseOCamlSdkProvider {

    /**
     * On Windows, the WindowsSDKProvider will be the one that will call
     * this method.
     * @return an empty set
     * @see WindowsOCamlSdkProvider
     */
    @Override public final @NotNull Set<String> suggestHomePaths() {
        return Set.of();
    }
}
