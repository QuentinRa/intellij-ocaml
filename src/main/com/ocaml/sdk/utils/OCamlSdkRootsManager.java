package com.ocaml.sdk.utils;

import com.ocaml.sdk.providers.OCamlSdkProvidersManager;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public final class OCamlSdkRootsManager {

    /**
     * @see com.ocaml.sdk.providers.OCamlSdkProvider#getAssociatedSourcesFolders(String)
     */
    public static @NotNull List<String> getSourcesFolders(String sdkHome) {
        return new ArrayList<>(OCamlSdkProvidersManager.INSTANCE.getAssociatedSourcesFolders(sdkHome));
    }
}
