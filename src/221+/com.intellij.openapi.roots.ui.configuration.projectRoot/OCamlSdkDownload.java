package com.intellij.openapi.roots.ui.configuration.projectRoot;

import com.intellij.openapi.projectRoots.Sdk;
import com.intellij.openapi.projectRoots.SdkModel;
import com.intellij.openapi.projectRoots.SdkTypeId;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.util.function.Consumer;
public interface OCamlSdkDownload extends SdkDownload {
    default void showDownloadUI(@NotNull SdkTypeId sdkTypeId,
                                @NotNull SdkModel sdkModel,
                                @NotNull JComponent parentComponent,
                                @Nullable Sdk selectedSdk,
                                @NotNull Consumer<? super SdkDownloadTask> sdkCreatedCallback) {
    }
}
