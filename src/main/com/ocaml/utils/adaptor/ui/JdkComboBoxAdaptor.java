package com.ocaml.utils.adaptor.ui;

import com.intellij.openapi.project.*;
import com.intellij.openapi.projectRoots.*;
import com.intellij.openapi.roots.ui.configuration.*;
import com.intellij.openapi.roots.ui.configuration.projectRoot.*;
import com.intellij.openapi.util.*;
import com.intellij.util.*;
import com.ocaml.utils.adaptor.*;
import org.jetbrains.annotations.*;

/**
 * @see JdkComboBox
 */
public class JdkComboBoxAdaptor extends JdkComboBox {

    public JdkComboBoxAdaptor(@Nullable Project project, @NotNull ProjectSdksModel sdkModel, @Nullable Condition<? super SdkTypeId> sdkTypeFilter, @Nullable Condition<? super Sdk> sdkFilter, @Nullable Condition<? super SdkTypeId> creationFilter, @Nullable Consumer<? super Sdk> onNewSdkAdded) {
        super(project, sdkModel, sdkTypeFilter, sdkFilter, creationFilter, onNewSdkAdded);
    }

    @SinceIdeVersion(release = "212")
    public boolean isProjectJdkSelected() {
        return getSelectedItem() instanceof ProjectJdkComboBoxItem;
    }
}
