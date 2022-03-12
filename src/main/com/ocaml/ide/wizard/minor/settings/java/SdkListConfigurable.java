// Copyright 2000-2021 JetBrains s.r.o. Use of this source code is governed by the Apache 2.0 license that can be found in the LICENSE file.
package com.ocaml.ide.wizard.minor.settings.java;

import com.intellij.openapi.Disposable;
import com.intellij.openapi.actionSystem.*;
import com.intellij.openapi.keymap.Keymap;
import com.intellij.openapi.keymap.KeymapManager;
import com.intellij.openapi.options.ConfigurationException;
import com.intellij.openapi.options.SearchableConfigurable;
import com.intellij.openapi.project.DumbAware;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.projectRoots.Sdk;
import com.intellij.openapi.projectRoots.impl.ProjectJdkImpl;
import com.intellij.openapi.roots.ui.configuration.SdkPopupFactory;
import com.intellij.openapi.roots.ui.configuration.projectRoot.ProjectSdksModel;
import com.intellij.openapi.ui.MasterDetailsComponent;
import com.intellij.openapi.ui.MasterDetailsState;
import com.intellij.openapi.ui.MasterDetailsStateService;
import com.intellij.openapi.ui.NamedConfigurable;
import com.intellij.openapi.util.ActionCallback;
import com.intellij.openapi.util.Disposer;
import com.intellij.ui.navigation.Place;
import com.intellij.util.IconUtil;
import com.ocaml.OCamlBundle;
import com.ocaml.ide.wizard.minor.settings.OCamlProjectConfigurable;
import com.ocaml.sdk.OCamlSdkType;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Predicate;

public class SdkListConfigurable extends MasterDetailsComponent implements SearchableConfigurable, Disposable, Place.Navigator {
    private final OCamlProjectConfigurable myOCamlProjectConfigurable;
    private final Project myProject;

    protected boolean myUiDisposed = true;
    private boolean myWasTreeInitialized;
    protected boolean myAutoScrollEnabled = true;

    public SdkListConfigurable(@NotNull OCamlProjectConfigurable ocamlProjectConfigurable) {
        super(new MasterDetailsState());
        myOCamlProjectConfigurable = ocamlProjectConfigurable;
        myProject = ocamlProjectConfigurable.getProject();
    }

    @Override protected @Nullable MasterDetailsStateService getStateService() {
        return MasterDetailsStateService.getInstance(myProject);
    }

    @Override public @Nullable ActionCallback navigateTo(@Nullable Place place, boolean requestFocus) {
        if (place == null) return ActionCallback.DONE;

        final Object object = place.getPath(TREE_OBJECT);
        final String byName = (String)place.getPath(TREE_NAME);

        if (object == null && byName == null) return ActionCallback.DONE;

        final MyNode node = object == null ? null : findNodeByObject(myRoot, object);
        final MyNode nodeByName = byName == null ? null : findNodeByName(myRoot, byName);

        if (node == null && nodeByName == null) return ActionCallback.DONE;

        NamedConfigurable<?> config = Objects.requireNonNullElse(node, nodeByName).getConfigurable();
        ActionCallback result = new ActionCallback().doWhenDone(() -> myAutoScrollEnabled = true);
        myAutoScrollEnabled = false;
        myAutoScrollHandler.cancelAllRequests();
        final MyNode nodeToSelect = node != null ? node : nodeByName;
        selectNodeInTree(nodeToSelect, requestFocus).doWhenDone(() -> {
            setSelectedNode(nodeToSelect);
            Place.goFurther(config, place, requestFocus).notifyWhenDone(result);
        });

        return result;
    }

    @Override public void queryPlace(@NotNull Place place) {
        if (myCurrentConfigurable != null) {
            place.putPath(TREE_OBJECT, myCurrentConfigurable.getEditableObject());
            Place.queryFurther(myCurrentConfigurable, place);
        }
    }

    // tree
    @Override protected void initTree() {
        if (myWasTreeInitialized) return;
        myWasTreeInitialized = true;
        super.initTree();
    }

    // dispose
    @Override public void dispose() {
    }

    @Override public void disposeUIResources() {
        if (myUiDisposed) return;

        super.disposeUIResources();

        myUiDisposed = true;

        myAutoScrollHandler.cancelAllRequests();

        Disposer.dispose(this);
    }

    @Override public @Nullable SdkConfigurable getSelectedConfigurable() {
        return (SdkConfigurable) super.getSelectedConfigurable();
    }

    // actions

    @Override protected @Nullable List<AnAction> createActions(boolean fromPopup) {
        final ArrayList<AnAction> result = new ArrayList<>();
        result.add(new AddSdkAction());
        result.add(new MyRemoveAction());
        result.add(Separator.getInstance());
        return result;
    }

    // todo: remove sdk
    final class MyRemoveAction extends MyDeleteAction {
        MyRemoveAction() {
            super((Predicate<Object[]>) objects -> {
                return false;
            });
        }
    }

    private class AddSdkAction extends AnAction implements DumbAware {
        @SuppressWarnings("RedundantSuppression") AddSdkAction() {
            super(OCamlBundle.message("add.new.jdk.text"), null, IconUtil.getAddIcon());

            KeymapManager keymapManager = KeymapManager.getInstance();
            if (keymapManager != null) {
                final Keymap active = keymapManager.getActiveKeymap();
                final Shortcut[] shortcuts = active.getShortcuts("NewElement");
                setShortcutSet(new CustomShortcutSet(shortcuts));
            }
        }

        @Override
        public void update(@NotNull AnActionEvent e) {
            e.getPresentation().setEnabledAndVisible(true);
        }

        @Override
        public void actionPerformed(@NotNull AnActionEvent e) {
            SdkPopupFactory.newBuilder().withProject(myProject)
                    .withProjectSdksModel(myOCamlProjectConfigurable.getSdksModel())
                    .withSdkTypeFilter(c -> c  instanceof OCamlSdkType)
                    .buildPopup()
                    .showPopup(e);
        }
    }

    // usual

    @Override public void apply() throws ConfigurationException {
        super.apply();
    }

    @Override public void reset() {
        myUiDisposed = false;

        if (!myWasTreeInitialized) {
            initTree();
            myTree.setShowsRootHandles(false);
            loadTreeNodes();
        } else {
            reloadTreeNodes();
        }

        super.reset();
    }

    private void loadTreeNodes() {
        ProjectSdksModel sdksModel = myOCamlProjectConfigurable.getSdksModel();
        final Map<Sdk,Sdk> sdks = sdksModel.getProjectSdks();
        for (Sdk sdk : sdks.keySet()) {
            final SdkConfigurable configurable = new SdkConfigurable((ProjectJdkImpl)sdks.get(sdk), sdksModel, TREE_UPDATER, myHistory);
            addNode(new MyNode(configurable), myRoot);
        }
    }

    protected final void reloadTreeNodes() {
        super.disposeUIResources();
        myTree.setShowsRootHandles(false);
        loadTreeNodes();
    }

    // data

    @Override public @NotNull @NonNls String getId() {
        return getDisplayName();
    }

    @Override public String getDisplayName() {
        return "OCamlProjectSdkConfigurable";
    }
}
