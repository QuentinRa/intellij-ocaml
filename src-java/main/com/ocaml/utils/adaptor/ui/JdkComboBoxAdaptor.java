package com.ocaml.utils.adaptor.ui;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.projectRoots.Sdk;
import com.intellij.openapi.projectRoots.SdkTypeId;
import com.intellij.openapi.roots.ui.configuration.JdkComboBox;
import com.intellij.openapi.roots.ui.configuration.projectRoot.ProjectSdksModel;
import com.intellij.openapi.util.Condition;
import com.intellij.util.Consumer;
import com.ocaml.utils.adaptor.RequireJavaPlugin;
import com.ocaml.utils.adaptor.SinceIdeVersion;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * @see JdkComboBox
 */
@RequireJavaPlugin(what = "JdkComboBox, ProjectJdkComboBoxItem")
public class JdkComboBoxAdaptor extends JdkComboBox {

    public JdkComboBoxAdaptor(@Nullable Project project, @NotNull ProjectSdksModel sdkModel, @Nullable Condition<? super SdkTypeId> sdkTypeFilter, @Nullable Condition<? super Sdk> sdkFilter, @Nullable Condition<? super SdkTypeId> creationFilter, @Nullable Consumer<? super Sdk> onNewSdkAdded) {
        super(project, sdkModel, sdkTypeFilter, sdkFilter, creationFilter, onNewSdkAdded);
    }

    @SinceIdeVersion(release = "212")
    public boolean isProjectJdkSelected() {
        return getSelectedItem() instanceof ProjectJdkComboBoxItem;
    }
}
