package com.ocaml.ide.sdk.providers;

import com.intellij.execution.configurations.GeneralCommandLine;
import com.intellij.openapi.util.SystemInfo;
import com.ocaml.ide.sdk.providers.utils.AssociatedBinaries;
import com.ocaml.ide.sdk.providers.windows.WindowsOCamlSdkProvider;
import com.ocaml.utils.ComputeMethod;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

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

    private final OCamlSdkProvider defaultProvider = new BaseOCamlSdkProvider();

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
        return callProvidersValuesS(OCamlSdkProvider::getOCamlExecutablePathCommands);
    }

    @Override public @NotNull List<String> getOCamlCompilerExecutablePathCommands() {
        return callProvidersValuesL(OCamlSdkProvider::getOCamlCompilerExecutablePathCommands);
    }

    @Override public @NotNull List<String> getOCamlSourcesFolders() {
        return callProvidersValuesL(OCamlSdkProvider::getOCamlSourcesFolders);
    }

    @Override public @Nullable AssociatedBinaries getAssociatedBinaries(@NotNull String ocamlBinary) {
        return callProvidersValue(provider -> provider.getAssociatedBinaries(ocamlBinary));
    }

    @Override public @Nullable GeneralCommandLine getCompilerVersionCLI(String ocamlcCompilerPath) {
        return callProvidersValue(provider -> provider.getCompilerVersionCLI(ocamlcCompilerPath));
    }

    // call providers

    /** arg is always a OCamlSdkProvider **/
    private interface ComputeProviders<R> extends ComputeMethod<R, OCamlSdkProvider> {
    }

    private <R> Set<R> callProvidersValuesS(ComputeProviders<Set<R>> computeValues) {
        HashSet<R> values = new HashSet<>();
        for (OCamlSdkProvider p: myProviders)
            values.addAll(computeValues.call(p));
        return values;
    }

    private <R> List<R> callProvidersValuesL(ComputeProviders<List<R>> computeValues) {
        List<R> values = new ArrayList<>();
        for (OCamlSdkProvider p: myProviders)
            values.addAll(computeValues.call(p));
        return values;
    }

    private <R> R callProvidersValue(ComputeProviders<R> computeValues) {
        for (OCamlSdkProvider p: myProviders) {
            R call = computeValues.call(p);
            if (call != null) return call;
        }
        return computeValues.call(defaultProvider);
    }
}
