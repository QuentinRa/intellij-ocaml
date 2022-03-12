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
import com.intellij.openapi.projectRoots.SdkModel;
import com.intellij.openapi.projectRoots.impl.ProjectJdkImpl;
import com.intellij.openapi.roots.ProjectRootManager;
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
import com.intellij.util.containers.ContainerUtil;
import com.intellij.util.containers.MultiMap;
import com.ocaml.OCamlBundle;
import com.ocaml.ide.wizard.minor.settings.OCamlProjectConfigurable;
import com.ocaml.sdk.OCamlSdkType;
import com.ocaml.utils.adaptor.ConvertPredicate;
import com.ocaml.utils.adaptor.UntilIdeVersion;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.tree.TreePath;
import java.util.*;

public class SdkListConfigurable extends MasterDetailsComponent implements SearchableConfigurable, Disposable, Place.Navigator {
    private final ProjectSdksModel mySdksModel;
    private final Project myProject;

    protected boolean myUiDisposed = true;
    protected boolean myAutoScrollEnabled = true;
    private boolean myWasTreeInitialized;

    private boolean hasListenerRegistered = false;
    private final SdkModel.Listener myListener = new SdkModel.Listener() {
        @Override
        public void sdkAdded(@NotNull Sdk sdk) {
            addJdkNode(sdk, true);
        }

        @Override
        public void sdkChanged(@NotNull Sdk sdk, String previousName) {
            updateName();
        }

        @Override
        public void sdkHomeSelected(@NotNull Sdk sdk, @NotNull String newSdkHome) {
            updateName();
        }

        private void updateName() {
            final TreePath path = myTree.getSelectionPath();
            if (path != null) {
                final NamedConfigurable<?> configurable = ((MyNode)path.getLastPathComponent()).getConfigurable();
                if (configurable instanceof SdkConfigurable) {
                    configurable.updateName();
                }
            }
        }
    };

    public SdkListConfigurable(@NotNull OCamlProjectConfigurable ocamlProjectConfigurable) {
        super(new MasterDetailsState());
        myProject = ocamlProjectConfigurable.getProject();
        mySdksModel = ocamlProjectConfigurable.getSdksModel();
    }

    @Override protected @Nullable MasterDetailsStateService getStateService() {
        return MasterDetailsStateService.getInstance(myProject);
    }

    @Override public @Nullable ActionCallback navigateTo(@Nullable Place place, boolean requestFocus) {
        if (place == null) return ActionCallback.DONE;

        final Object object = place.getPath(TREE_OBJECT);
        final String byName = (String) place.getPath(TREE_NAME);

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
        mySdksModel.removeListener(myListener);
        hasListenerRegistered = false;
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

    // usual

    @Override public void apply() throws ConfigurationException {
        boolean modifiedJdks = false;
        for (int i = 0; i < myRoot.getChildCount(); i++) {
            final NamedConfigurable<?> configurable = ((MyNode)myRoot.getChildAt(i)).getConfigurable();
            if (configurable.isModified()) {
                configurable.apply();
                modifiedJdks = true;
            }
        }

        if (mySdksModel.isModified() || modifiedJdks) mySdksModel.apply(this);

        mySdksModel.setProjectSdk(ProjectRootManager.getInstance(myProject).getProjectSdk());
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

        if (!hasListenerRegistered) {
            hasListenerRegistered = true;
            mySdksModel.addListener(myListener);
        }
        myTree.setRootVisible(false);
    }

    @Override public boolean isModified() {
        return super.isModified() || mySdksModel.isModified();
    }

    private void loadTreeNodes() {
        final Map<Sdk, Sdk> sdks = mySdksModel.getProjectSdks();
        for (Sdk sdk : sdks.keySet()) {
            final SdkConfigurable configurable = new SdkConfigurable((ProjectJdkImpl) sdks.get(sdk), mySdksModel, TREE_UPDATER, myHistory);
            addNode(new MyNode(configurable), myRoot);
        }
    }

    protected final void reloadTreeNodes() {
        super.disposeUIResources();
        myTree.setShowsRootHandles(false);
        loadTreeNodes();
    }

    public void addJdkNode(final Sdk jdk, final boolean selectInTree) {
        if (!myUiDisposed) {
            addNode(new MyNode(new SdkConfigurable((ProjectJdkImpl)jdk, mySdksModel, TREE_UPDATER, myHistory)), myRoot);
            if (selectInTree) {
                selectNodeInTree(MasterDetailsComponent.findNodeByObject(myRoot, jdk));
            }
        }
    }

    final class MyRemoveAction extends MyDeleteAction {
        @UntilIdeVersion(release = "203", note = "Condition was deprecated over Predicate.")
        MyRemoveAction() {
            super(ConvertPredicate.cast(
                    objects -> {
                        List<MyNode> nodes = new ArrayList<>();
                        for (Object object : objects) {
                            if (!(object instanceof MyNode)) return false;
                            nodes.add((MyNode)object);
                        }
                        //noinspection rawtypes
                        MultiMap<RemoveConfigurableHandler, MyNode> map = groupNodes(nodes);
                        //noinspection rawtypes
                        for (Map.Entry<RemoveConfigurableHandler, Collection<MyNode>> entry : map.entrySet()) {
                            //noinspection unchecked
                            if (!entry.getKey().canBeRemoved(getEditableObjects(entry.getValue()))) {
                                return false;
                            }
                        }
                        return true;
                    }
            ));
        }

        @Override
        public void actionPerformed(@NotNull AnActionEvent e) {
            final TreePath[] paths = myTree.getSelectionPaths();
            if (paths == null) return;

            List<MyNode> removedNodes = removeFromModel(paths);
            removeNodes(removedNodes);
        }

        private @NotNull List<MyNode> removeFromModel(final TreePath[] paths) {
            List<MyNode> nodes = ContainerUtil.mapNotNull(paths, path -> {
                Object node = path.getLastPathComponent();
                return node instanceof MyNode ? (MyNode)node : null;
            });
            //noinspection rawtypes
            MultiMap<RemoveConfigurableHandler, MyNode> grouped = groupNodes(nodes);

            List<MyNode> removedNodes = new ArrayList<>();
            //noinspection rawtypes
            for (Map.Entry<RemoveConfigurableHandler, Collection<MyNode>> entry : grouped.entrySet()) {
                //noinspection unchecked
                boolean removed = entry.getKey().remove(getEditableObjects(entry.getValue()));
                if (removed) {
                    removedNodes.addAll(entry.getValue());
                }
            }
            return removedNodes;
        }
    }

    private static @NotNull List<?> getEditableObjects(@NotNull Collection<? extends MyNode> value) {
        List<Object> objects = new ArrayList<>();
        for (MyNode node : value) {
            objects.add(node.getConfigurable().getEditableObject());
        }
        return objects;
    }

    private class SdkRemoveHandler extends RemoveConfigurableHandler<Sdk> {
        SdkRemoveHandler() {
            super(SdkConfigurable.class);
        }
        @Override
        public boolean remove(@NotNull Collection<? extends Sdk> sdks) {
            for (Sdk sdk : sdks) {
                mySdksModel.removeSdk(sdk);
            }
            return true;
        }
    }

    @SuppressWarnings("rawtypes") @NotNull
    private MultiMap<RemoveConfigurableHandler, MyNode> groupNodes(@NotNull List<? extends MyNode> nodes) {
        List<? extends RemoveConfigurableHandler<?>> handlers = List.of(new SdkRemoveHandler());
        MultiMap<RemoveConfigurableHandler, MyNode> grouped = MultiMap.createLinked();
        for (MyNode node : nodes) {
            final NamedConfigurable<?> configurable = node.getConfigurable();
            if (configurable == null) continue;
            RemoveConfigurableHandler handler = findHandler(handlers, configurable.getClass());
            if (handler == null) continue;

            grouped.putValue(handler, node);
        }
        return grouped;
    }

    private static @Nullable RemoveConfigurableHandler<?> findHandler(@NotNull List<? extends RemoveConfigurableHandler<?>> handlers,
                                                                      @SuppressWarnings("rawtypes") Class<? extends NamedConfigurable> configurableClass) {
        for (RemoveConfigurableHandler<?> handler : handlers) {
            if (handler.getConfigurableClass().isAssignableFrom(configurableClass)) {
                return handler;
            }
        }
        return null;
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
                    .withProjectSdksModel(mySdksModel)
                    .withSdkTypeFilter(c -> c instanceof OCamlSdkType)
                    .buildPopup()
                    .showPopup(e);
        }
    }

    // data

    @Override public @NotNull @NonNls String getId() {
        return getDisplayName();
    }

    @Override public String getDisplayName() {
        return "OCamlProjectSdkConfigurable";
    }

    @Override protected @Nullable String getEmptySelectionString() {
        return OCamlBundle.message("project.jdks.configurable.empty.selection.string");
    }

    @Override public String getHelpTopic() {
        return myCurrentConfigurable != null ? myCurrentConfigurable.getHelpTopic() : "reference.settingsdialog.project.structure.jdk";
    }

    @Override protected @Nullable @NonNls String getComponentStateKey() {
        return "OCamlProjectSdkConfigurable.UI";
    }
}
