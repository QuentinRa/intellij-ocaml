package com.ocaml.sdk;

import com.intellij.openapi.projectRoots.*;
import com.intellij.openapi.roots.OrderRootType;
import com.intellij.openapi.roots.ui.configuration.projectRoot.SdkDownload;
import com.intellij.openapi.roots.ui.configuration.projectRoot.SdkDownloadTask;
import com.intellij.openapi.util.io.FileUtil;
import com.intellij.openapi.vfs.LocalFileSystem;
import com.intellij.openapi.vfs.VirtualFile;
import com.ocaml.sdk.utils.OCamlSdkHomeManager;
import com.ocaml.sdk.utils.OCamlSdkRootsManager;
import com.ocaml.sdk.utils.OCamlSdkVersionManager;
import com.ocaml.icons.OCamlIcons;
import com.ocaml.utils.adaptor.SinceIdeVersion;
import org.jdom.Element;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.io.File;
import java.util.Collection;
import java.util.List;
import java.util.function.Consumer;

/**
 * OCaml SDK
 * <ul>
 *     <li>.opam-switch/sources/*</li>
 *     <li>bin/ocaml (.exe allowed)</li>
 *     <li>bin/ocamlc (.exe allowed)</li>
 *     <li>lib/*</li>
 * </ul>
 */
public class OCamlSdkType extends LocalSdkType implements SdkDownload {

    private static final String OCAML_SDK = "OCaml SDK";

    public OCamlSdkType() {
        super(OCAML_SDK);
    }

    public static OCamlSdkType getInstance() {
        return SdkType.EP_NAME.findExtension(OCamlSdkType.class);
    }

    //
    // Sources
    //

    public static void addSources(@NotNull File sdkHomeFile, @NotNull SdkModificator sdkModificator) {
        List<String> sources = OCamlSdkRootsManager.getSourcesFolders(sdkHomeFile.getPath());
        for (String source : sources) {
            addSources(source, sdkHomeFile, sdkModificator);
        }
    }

    private static void addSources(String sourceName, File sdkHomeFile, SdkModificator sdkModificator) {
        File rootFolder = new File(sdkHomeFile, sourceName);
        if (!rootFolder.exists()) return;

        File[] files = rootFolder.listFiles();
        if (files == null) return;
        for (File file : files) {
            VirtualFile rootCandidate = LocalFileSystem.getInstance()
                    .findFileByPath(FileUtil.toSystemIndependentName(file.getAbsolutePath()));
            if (rootCandidate == null) continue;
            sdkModificator.addRoot(rootCandidate, OrderRootType.CLASSES);
        }
    }

    //
    // Name + Icon
    //

    @NotNull @Override public String getPresentableName() {
        return "OCaml";
    }

    @NotNull @Override public Icon getIcon() {
        return OCamlIcons.Nodes.OCAML_SDK;
    }

    //
    // Home path
    //

    @Override public @NotNull Collection<String> suggestHomePaths() {
        return OCamlSdkHomeManager.suggestHomePaths();
    }

    @Nullable @Override public String suggestHomePath() {
        return OCamlSdkHomeManager.defaultOCamlLocation();
    }

    //
    // suggestSdkName, getVersionString
    //

    @NotNull
    @Override public final String suggestSdkName(String currentSdkName, @NotNull String sdkHome) {
        return "OCaml-" + getVersionString(sdkHome);
    }

    @NotNull @Override public String getVersionString(@NotNull String sdkHome) {
        return OCamlSdkVersionManager.parse(sdkHome);
    }

    //
    // Valid
    //

    @Override public boolean isValidSdkHome(@NotNull String sdkHome) {
        return OCamlSdkHomeManager.isValid(sdkHome);
    }

    //
    // Data
    //

    @Override @Nullable
    public AdditionalDataConfigurable createAdditionalDataConfigurable(@NotNull SdkModel sdkModel,
                                                                       @NotNull SdkModificator sdkModificator) {
        return null;
    }

    @Override public void saveAdditionalData(@NotNull SdkAdditionalData additionalData,
                                             @NotNull Element additional) {
    }

    //
    // Setup
    //

    @Override public void setupSdkPaths(@NotNull Sdk sdk) {
        String homePath = sdk.getHomePath();
        assert homePath != null : sdk;
        SdkModificator sdkModificator = sdk.getSdkModificator();
        sdkModificator.removeRoots(OrderRootType.CLASSES);
        addSources(new File(homePath), sdkModificator);
        sdkModificator.commitChanges();
    }

    //
    // Documentation
    //

    @Override public @Nullable String getDefaultDocumentationUrl(@NotNull Sdk sdk) {
        return null; // https://www.ocaml.org/api/index.html for 4.13, but before?
    }

    //
    // Download
    //

    @Override public boolean supportsDownload(@NotNull SdkTypeId sdkTypeId) {
        return false;
    }

    @Override public void showDownloadUI(@NotNull SdkTypeId sdkTypeId, @NotNull SdkModel sdkModel,
                                         @NotNull JComponent parentComponent, @Nullable Sdk selectedSdk,
                                         @NotNull Consumer<SdkDownloadTask> sdkCreatedCallback) {
    }

    //
    // WSL
    //

    @SinceIdeVersion(release = "213")
    @SuppressWarnings("unused")
    public boolean allowWslSdkForLocalProject() {
        return true;
    }
}
