// Copyright 2000-2020 JetBrains s.r.o. Use of this source code is governed by the Apache 2.0 license that can be found in the LICENSE file.
package com.ocaml.ide.wizard.minor.settings.java;

import com.intellij.openapi.options.ConfigurationException;
import com.intellij.openapi.projectRoots.Sdk;
import com.intellij.openapi.projectRoots.SdkType;
import com.intellij.openapi.projectRoots.impl.ProjectJdkImpl;
import com.intellij.openapi.roots.ui.configuration.projectRoot.ProjectSdksModel;
import com.intellij.openapi.ui.NamedConfigurable;
import com.intellij.openapi.util.ActionCallback;
import com.intellij.ui.navigation.History;
import com.intellij.ui.navigation.Place;
import com.ocaml.OCamlBundle;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

/**
 * Simplified version of JdkConfigurable.
 */
public class SdkConfigurable extends NamedConfigurable<Sdk> implements Place.Navigator {

    private final ProjectJdkImpl myProjectJdk;
    private final SdkEditor mySdkEditor;

    public SdkConfigurable(@NotNull ProjectJdkImpl projectJdk,
                           @NotNull ProjectSdksModel sdksModel,
                           @NotNull Runnable updateTree,
                           @NotNull History history) {
        super(true, updateTree);
        myProjectJdk = projectJdk;
        mySdkEditor = createSdkEditor(sdksModel, history, myProjectJdk);
    }

    @NotNull
    protected SdkEditor createSdkEditor(@NotNull ProjectSdksModel sdksModel,
                                        @NotNull History history,
                                        @NotNull ProjectJdkImpl projectJdk) {
        return new SdkEditor(sdksModel, history, projectJdk);
    }

    @Override
    public Sdk getEditableObject() {
        return myProjectJdk;
    }

    @Override
    public String getBannerSlogan() {
        return OCamlBundle.message("project.roots.jdk.banner.text", myProjectJdk.getName());
    }

    @Override
    public String getDisplayName() {
        return myProjectJdk.getName();
    }

    @Override
    public void setDisplayName(final String name) {
        myProjectJdk.setName(name);
    }

    @Override
    public Icon getIcon(boolean open) {
        return ((SdkType) myProjectJdk.getSdkType()).getIcon();
    }

    @Override
    @Nullable
    @NonNls
    public String getHelpTopic() {
        return ((SdkType) myProjectJdk.getSdkType()).getHelpTopic();
    }


    @Override
    public JComponent createOptionsPanel() {
        return mySdkEditor.createComponent();
    }

    @Override
    public boolean isModified() {
        return mySdkEditor.isModified();
    }

    @Override
    public void apply() throws ConfigurationException {
        mySdkEditor.apply();
    }

    @Override
    public void reset() {
        mySdkEditor.reset();
    }

    @Override
    public void disposeUIResources() {
        mySdkEditor.disposeUIResources();
    }

    @Override
    public ActionCallback navigateTo(@Nullable final Place place, final boolean requestFocus) {
        return mySdkEditor.navigateTo(place, requestFocus);
    }

    @Override
    public void queryPlace(@NotNull final Place place) {
        mySdkEditor.queryPlace(place);
    }

}
