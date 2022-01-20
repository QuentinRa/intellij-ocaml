package com.ocaml.ide.sdk.providers;

import com.intellij.openapi.util.SystemInfo;
import com.ocaml.ide.sdk.providers.windows.WindowsOCamlSdkProvider;
import com.ocaml.utils.ComputeMethod;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * The only provider that should be used outside.
 * It's loading providers if they should be available, and calling
 * every one of them each time the user request something.
 */
public final class OCamlSdkProvidersManager implements OCamlSdkProvider {
    public static final OCamlSdkProvidersManager INSTANCE = new OCamlSdkProvidersManager();
    private final ArrayList<OCamlSdkProvider> myProviders = new ArrayList<>();

    private OCamlSdkProvidersManager() {
        if (SystemInfo.isWindows) {
            addProvider(new WindowsOCamlSdkProvider());
        } else {
            addProvider(new BaseOCamlSdkProvider());
        }
    }

    // providers

    private void addProvider(OCamlSdkProvider provider) {
        myProviders.add(provider);
        List<OCamlSdkProvider> nestedProviders = provider.getNestedProviders();
        myProviders.addAll(nestedProviders);
    }

    @Override public @NotNull List<OCamlSdkProvider> getNestedProviders() {
        return myProviders;
    }

    // implemented

    @Override public @NotNull Set<String> getOCamlExecutablePathCommands() {
        return callProviders(OCamlSdkProvider::getOCamlExecutablePathCommands);
    }

    @Override public @NotNull Set<String> getOCamlCompilerExecutablePathCommands() {
        return callProviders(OCamlSdkProvider::getOCamlCompilerExecutablePathCommands);
    }

    @Override public @NotNull Set<String> getOCamlSourcesFolders() {
        return callProviders(OCamlSdkProvider::getOCamlSourcesFolders);
    }

    // call providers

    /** arg is always a OCamlSdkProvider **/
    private interface ComputeProviders<R> extends ComputeMethod<R, OCamlSdkProvider> {
    }

    private <R> Set<R> callProviders(ComputeProviders<Set<R>> provider) {
        HashSet<R> values = new HashSet<>();
        for (OCamlSdkProvider p: myProviders)
            values.addAll(provider.call(p));
        return values;
    }
}
