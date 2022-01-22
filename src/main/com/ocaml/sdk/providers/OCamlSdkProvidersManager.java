package com.ocaml.sdk.providers;

import com.intellij.execution.configurations.GeneralCommandLine;
import com.intellij.openapi.util.SystemInfo;
import com.ocaml.sdk.providers.utils.AssociatedBinaries;
import com.ocaml.sdk.providers.windows.WindowsOCamlSdkProvider;
import com.ocaml.utils.ComputeMethod;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.nio.file.Path;
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

    @Override public @NotNull Set<String> getOCamlTopLevelCommands() {
        return callProvidersValuesS(OCamlSdkProvider::getOCamlTopLevelCommands);
    }

    @Override public @NotNull List<String> getOCamlCompilerCommands() {
        return callProvidersValuesL(OCamlSdkProvider::getOCamlCompilerCommands);
    }

    @Override public @NotNull List<String> getOCamlSourcesFolders() {
        return callProvidersValuesL(OCamlSdkProvider::getOCamlSourcesFolders);
    }

    @Override public @Nullable Boolean isOpamBinary(@NotNull String ocamlBinary) {
        return callProvidersValue(provider -> provider.isOpamBinary(ocamlBinary));
    }

    @Override
    public @Nullable String createSdkFromBinaries(String ocaml, String compiler, String version,
                                                  String sources, String sdkFolder, String sdkModifier) {
        return callProvidersValue(provider ->
                provider.createSdkFromBinaries(ocaml, compiler, version, sources, sdkFolder, sdkModifier)
        );
    }

    @Override public @Nullable AssociatedBinaries getAssociatedBinaries(@NotNull String ocamlBinary) {
        return callProvidersValue(provider -> provider.getAssociatedBinaries(ocamlBinary));
    }

    @Override public @NotNull Set<String> getAssociatedSourcesFolders(@NotNull String sdkHome) {
        return callProvidersValue(provider -> provider.getAssociatedSourcesFolders(sdkHome));
    }

    @Override public @Nullable GeneralCommandLine getCompilerVersionCLI(String ocamlcCompilerPath) {
        return callProvidersValue(provider -> provider.getCompilerVersionCLI(ocamlcCompilerPath));
    }

    @Override public @NotNull Set<String> getInstallationFolders() {
        return callProvidersValuesS(OCamlSdkProvider::getInstallationFolders);
    }

    @Override public @NotNull Set<String> suggestHomePaths() {
        return callProvidersValue(OCamlSdkProvider::suggestHomePaths);
    }

    @Override public Boolean isHomePathValid(@NotNull Path homePath) {
        return callProvidersValue(provider -> provider.isHomePathValid(homePath));
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
