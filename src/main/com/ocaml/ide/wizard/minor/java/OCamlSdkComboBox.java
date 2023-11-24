// Copyright 2000-2020 JetBrains s.r.o. Use of this source code is governed by the Apache 2.0 license that can be found in the LICENSE file.
// based on JdkComboBox, extracted from the Java plugin to be used in Minor IDEs such as CLion
// some methods were removed, and a patch using "parentDisposable" was made
package com.ocaml.ide.wizard.minor.java;

import com.intellij.openapi.Disposable;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.projectRoots.Sdk;
import com.intellij.openapi.projectRoots.SdkTypeId;
import com.intellij.openapi.projectRoots.SimpleJavaSdkType;
import com.intellij.openapi.roots.ui.configuration.*;
import com.intellij.openapi.roots.ui.configuration.SdkListItem.SdkItem;
import com.intellij.openapi.roots.ui.configuration.projectRoot.ProjectSdksModel;
import com.intellij.openapi.ui.ComboBoxPopupState;
import com.intellij.openapi.util.Condition;
import com.intellij.util.Consumer;
import com.ocaml.OCamlBundle;
import com.ocaml.utils.Callback;
import com.ocaml.utils.adaptor.roots.SdkListModelBuilderAdaptor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.util.Objects;
import java.util.function.Supplier;

import static com.ocaml.ide.wizard.minor.java.OCamlSdkComboBox.OCamlSdkComboBoxItem;

/**
 * @author Eugene Zhuravlev
 */
public class OCamlSdkComboBox extends SdkComboBoxBase<OCamlSdkComboBoxItem> {
    @NotNull private final Consumer<Sdk> myOnNewSdkAdded;
    private Disposable parentDisposable;

    /**
     * Creates new Sdk selector combobox
     * @param project current project (if any)
     * @param sdkModel the sdks model
     * @param sdkTypeFilter sdk types filter predicate to show
     * @param sdkFilter filters Sdk instances that are listed, it implicitly includes the {@param sdkTypeFilter}
     * @param creationFilter a filter of SdkType that allowed to create a new Sdk with that control
     * @param onNewSdkAdded a callback that is executed once a new Sdk is added to the list
     */
    public OCamlSdkComboBox(@Nullable Project project,
                       @NotNull ProjectSdksModel sdkModel,
                       @Nullable Condition<? super SdkTypeId> sdkTypeFilter,
                       @Nullable Condition<? super Sdk> sdkFilter,
                       @Nullable Condition<? super SdkTypeId> creationFilter,
                       @Nullable Consumer<? super Sdk> onNewSdkAdded) {
        this(project, sdkModel, sdkTypeFilter, sdkFilter, null, creationFilter, onNewSdkAdded);
    }

    /**
     * Creates new Sdk selector combobox
     * @param project current project (if any)
     * @param sdkModel the sdks model
     * @param sdkTypeFilter sdk types filter predicate to show
     * @param sdkFilter filters Sdk instances that are listed, it implicitly includes the {@param sdkTypeFilter}
     * @param creationFilter a filter of SdkType that allowed to create a new Sdk with that control
     * @param onNewSdkAdded a callback that is executed once a new Sdk is added to the list
     */
    public OCamlSdkComboBox(@Nullable Project project,
                       @NotNull ProjectSdksModel sdkModel,
                       @Nullable Condition<? super SdkTypeId> sdkTypeFilter,
                       @Nullable Condition<? super Sdk> sdkFilter,
                       @Nullable Condition<? super SdkListItem.SuggestedItem> suggestedSdkFilter,
                       @Nullable Condition<? super SdkTypeId> creationFilter,
                       @Nullable Consumer<? super Sdk> onNewSdkAdded) {
        this(SdkListModelBuilderAdaptor.create(project, sdkModel, sdkTypeFilter, SimpleJavaSdkType.notSimpleJavaSdkType(creationFilter), sdkFilter, suggestedSdkFilter, null),
                onNewSdkAdded);
    }

    /**
     * Creates new Sdk selector combobox
     * @param onNewSdkAdded a callback that is executed once a new Sdk is added to the list
     */
    public OCamlSdkComboBox(@NotNull SdkListModelBuilder modelBuilder,
                            @Nullable Consumer<? super Sdk> onNewSdkAdded) {
        super(modelBuilder);
        myOnNewSdkAdded = sdk -> {
            if (onNewSdkAdded != null) {
                onNewSdkAdded.consume(sdk);
            }
        };
        // fixme:
        setRenderer(new SdkListPresenter(() -> ((OCamlSdkComboBoxModel)this.getModel()).myInnerModel)
                .forType(OCamlSdkComboBox::unwrapItem));
        reloadModel();
    }

    @Override
    protected void onModelUpdated(@NotNull SdkListModel model) {
        Object previousSelection = getSelectedItem();
        OCamlSdkComboBoxModel newModel = new OCamlSdkComboBoxModel(model);
        newModel.setSelectedItem(previousSelection);
        setModel(newModel);
    }

    @NotNull
    private static SdkListItem unwrapItem(@Nullable OCamlSdkComboBoxItem item) {
        if (item == null) item = new ProjectOCamlSdkComboBoxItem();

        if (item instanceof InnerComboBoxItem) {
            return ((InnerComboBoxItem)item).getItem();
        }
        throw new RuntimeException("Failed to unwrap " + item.getClass().getName() + ": " + item);
    }

    @NotNull
    private static OCamlSdkComboBoxItem wrapItem(@NotNull SdkListItem item) {
        if (item instanceof SdkListItem.SdkItem) {
            return new ActualJdkInnerItem((SdkListItem.SdkItem)item);
        }

        if (item instanceof SdkListItem.NoneSdkItem) {
            return new NoneOCamlSdkComboBoxItem();
        }

        if (item instanceof SdkListItem.ProjectSdkItem) {
            return new ProjectOCamlSdkComboBoxItem();
        }

        return new InnerOCamlSdkComboBoxItem(item);
    }

    public void setEditButton(@NotNull JButton editButton,
                              @NotNull Supplier<? extends Sdk> retrieveJDK,
                              @NotNull Callback<Sdk> onEdit) {
        editButton.addActionListener(e -> {
            final Sdk projectJdk = retrieveJDK.get();
            if (projectJdk != null) {
                onEdit.call(projectJdk);
            }
        });
        addActionListener(e -> {
            final OCamlSdkComboBoxItem selectedItem = getSelectedItem();
            editButton.setEnabled(selectedItem != null && selectedItem.getJdk() != null);
        });
    }

    @Nullable
    @Override
    public OCamlSdkComboBoxItem getSelectedItem() {
        return (OCamlSdkComboBoxItem)super.getSelectedItem();
    }

    /**
     * Returns selected JDK or null if there is no selection or Project JDK inherited.
     */
    public @Nullable Sdk getSelectedSdk() {
        OCamlSdkComboBoxItem selectedItem = getSelectedItem();
        return selectedItem != null ? selectedItem.getJdk() : null;
    }

    public void setSelectedJdk(@Nullable Sdk jdk) {
        setSelectedItem(jdk);
    }

    private void processFirstItem(@Nullable OCamlSdkComboBoxItem firstItem) {
        if (firstItem instanceof ProjectOCamlSdkComboBoxItem) {
            myModel.showProjectSdkItem();
        } else if (firstItem instanceof NoneOCamlSdkComboBoxItem) {
            myModel.showNoneSdkItem();
        } else if (firstItem instanceof ActualOCamlSdkComboBoxItem) {
            setSelectedJdk(((ActualOCamlSdkComboBoxItem)firstItem).myJdk);
        }
    }

    @Override
    public void firePopupMenuWillBecomeVisible() {
        resolveSuggestionsIfNeeded();
        super.firePopupMenuWillBecomeVisible();
    }

    private void resolveSuggestionsIfNeeded() {
        myModel.reloadActions();
        myModel.detectItems(this, parentDisposable);
    }

    @Override
    public void setSelectedItem(@Nullable Object anObject) {
        if (anObject instanceof SdkListItem) {
            setSelectedItem(wrapItem((SdkListItem)anObject));
            return;
        }

        if (anObject == null) {
            SdkListModel innerModel = ((OCamlSdkComboBoxModel)getModel()).myInnerModel;
            SdkListItem candidate = innerModel.findProjectSdkItem();
            if (candidate == null) {
                candidate = innerModel.findNoneSdkItem();
            }
            if (candidate == null) {
                candidate = myModel.showProjectSdkItem();
            }

            setSelectedItem(candidate);
            return;
        }

        if (anObject instanceof Sdk) {
            // it is a chance we have a cloned SDK instance from the model here, or an original one
            // reload model is needed to make sure we see all instances
            myModel.reloadSdks();
            ((OCamlSdkComboBoxModel)getModel()).trySelectSdk((Sdk)anObject);
            return;
        }

        if (anObject instanceof InnerComboBoxItem) {
            SdkListItem item = ((InnerComboBoxItem)anObject).getItem();
            if (myModel.executeAction(this, item, newItem -> {
                setSelectedItem(newItem);
                if (newItem instanceof SdkItem) {
                    myOnNewSdkAdded.consume(((SdkItem)newItem).sdk);
                }
            })) return;
        }

        if (anObject instanceof SelectableComboBoxItem) {
            super.setSelectedItem(anObject);
        }
    }

    public void setParentDisposable(Disposable parentDisposable) {
        this.parentDisposable = parentDisposable;
    }

    @SuppressWarnings("UnstableApiUsage")
    private static class OCamlSdkComboBoxModel extends AbstractListModel<OCamlSdkComboBoxItem>
            implements ComboBoxPopupState<OCamlSdkComboBoxItem>, ComboBoxModel<OCamlSdkComboBoxItem> {
        private final SdkListModel myInnerModel;
        private OCamlSdkComboBoxItem mySelectedItem;

        OCamlSdkComboBoxModel(@NotNull SdkListModel innerModel) {
            myInnerModel = innerModel;
        }

        @Override
        public int getSize() {
            return myInnerModel.getItems().size();
        }

        @Override
        public OCamlSdkComboBoxItem getElementAt(int index) {
            return wrapItem(myInnerModel.getItems().get(index));
        }

        @Nullable
        @Override
        public ListModel<OCamlSdkComboBoxItem> onChosen(OCamlSdkComboBoxItem selectedValue) {
            if (selectedValue instanceof InnerComboBoxItem) {
                SdkListModel inner = myInnerModel.onChosen(((InnerComboBoxItem)selectedValue).getItem());
                return inner == null ? null : new OCamlSdkComboBoxModel(inner);
            }
            return null;
        }

        @Override
        public boolean hasSubstep(OCamlSdkComboBoxItem selectedValue) {
            if (selectedValue instanceof InnerComboBoxItem) {
                return myInnerModel.hasSubstep(((InnerComboBoxItem)selectedValue).getItem());
            }
            return false;
        }

        @Override
        public void setSelectedItem(Object anObject) {
            if (!(anObject instanceof OCamlSdkComboBoxItem)) return;
            if (!(anObject instanceof InnerComboBoxItem)) return;
            SdkListItem innerItem = ((InnerComboBoxItem)anObject).getItem();
            if (!myInnerModel.getItems().contains(innerItem)) return;
            mySelectedItem = (OCamlSdkComboBoxItem)anObject;
            fireContentsChanged(this, -1, -1);
        }

        @Override
        public Object getSelectedItem() {
            return mySelectedItem;
        }

        void trySelectSdk(@NotNull Sdk sdk) {
            SdkItem item = myInnerModel.findSdkItem(sdk);
            if (item == null) return;
            setSelectedItem(wrapItem(item));
        }
    }

    private interface InnerComboBoxItem {
        @NotNull SdkListItem getItem();
    }

    private interface SelectableComboBoxItem { }

    public abstract static class OCamlSdkComboBoxItem {
        @Nullable
        public Sdk getJdk() {
            return null;
        }
    }

    private static final class InnerOCamlSdkComboBoxItem extends OCamlSdkComboBoxItem implements InnerComboBoxItem {
        private final SdkListItem myItem;

        private InnerOCamlSdkComboBoxItem(@NotNull SdkListItem item) {
            myItem = item;
        }

        @NotNull
        @Override
        public SdkListItem getItem() {
            return myItem;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            InnerOCamlSdkComboBoxItem item = (InnerOCamlSdkComboBoxItem)o;
            return myItem.equals(item.myItem);
        }

        @Override
        public int hashCode() {
            return Objects.hash(myItem);
        }
    }

    private static final class ActualJdkInnerItem extends ActualOCamlSdkComboBoxItem implements InnerComboBoxItem {
        private final SdkItem myItem;

        private ActualJdkInnerItem(@NotNull SdkItem item) {
            super(item.sdk);
            myItem = item;
        }

        @NotNull
        @Override
        public SdkListItem getItem() {
            return myItem;
        }
    }

    public static class ActualOCamlSdkComboBoxItem extends OCamlSdkComboBoxItem implements SelectableComboBoxItem {
        private final Sdk myJdk;

        public ActualOCamlSdkComboBoxItem(@NotNull Sdk jdk) {
            myJdk = jdk;
        }

        @Override
        public String toString() {
            return myJdk.getName();
        }

        @NotNull
        @Override
        public Sdk getJdk() {
            return myJdk;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            ActualOCamlSdkComboBoxItem item = (ActualOCamlSdkComboBoxItem)o;
            return myJdk.equals(item.myJdk);
        }

        @Override
        public int hashCode() {
            return Objects.hash(myJdk);
        }
    }

    public static class ProjectOCamlSdkComboBoxItem extends OCamlSdkComboBoxItem implements InnerComboBoxItem, SelectableComboBoxItem {
        @NotNull
        @Override
        public SdkListItem getItem() {
            return new SdkListItem.ProjectSdkItem();
        }

        @Override
        public int hashCode() {
            return 42;
        }

        @Override
        public boolean equals(Object obj) {
            return obj instanceof ProjectOCamlSdkComboBoxItem;
        }
    }

    public static class NoneOCamlSdkComboBoxItem extends OCamlSdkComboBoxItem implements InnerComboBoxItem, SelectableComboBoxItem {
        @NotNull
        @Override
        public SdkListItem getItem() {
            return new SdkListItem.NoneSdkItem();
        }

        public String toString() {
            return OCamlBundle.message("jdk.combo.box.none.item");
        }

        @Override
        public int hashCode() {
            return 42;
        }

        @Override
        public boolean equals(Object obj) {
            return obj instanceof NoneOCamlSdkComboBoxItem;
        }
    }

    /**
     * @deprecated Use the {@link OCamlSdkComboBox} API to manage shown items,
     * this call is ignored
     */
    @Override
    @Deprecated
    @SuppressWarnings("deprecation")
    public void insertItemAt(OCamlSdkComboBoxItem item, int index) {
        super.insertItemAt(item, index);
        processFirstItem(item);
    }
}

