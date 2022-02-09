package com.ocaml.ide.editor;

import com.intellij.codeHighlighting.BackgroundEditorHighlighter;
import com.intellij.ide.structureView.StructureViewBuilder;
import com.intellij.openapi.actionSystem.*;
import com.ocaml.utils.adaptor.actions.SimpleDataContextBuilderAdaptor;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.fileEditor.*;
import com.intellij.openapi.fileEditor.impl.text.TextEditorImpl;
import com.intellij.openapi.fileEditor.impl.text.TextEditorProvider;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.UserDataHolderBase;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.pom.Navigatable;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiManager;
import com.ocaml.OCamlBundle;
import com.ocaml.ide.actions.editor.help.OCamlEditorApiAction;
import com.ocaml.ide.actions.editor.help.OCamlEditorHelpAction;
import com.ocaml.ide.actions.editor.run.OCamlRunFileREPLAction;
import com.ocaml.ide.actions.editor.run.OCamlRunSelection;
import com.ocaml.utils.adaptor.actions.ActionUtilsAdaptor;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.awt.*;
import java.beans.PropertyChangeListener;

public class OCamlFileEditor extends UserDataHolderBase implements TextEditor {

    private final JPanel myMainComponent;
    private final Project myProject;
    private final TextEditorImpl myTextEditor;
    private final VirtualFile myFile;

    public OCamlFileEditor(@NotNull Project project, @NotNull TextEditorImpl textEditor, @NotNull VirtualFile file) {
        myProject = project;
        myTextEditor = textEditor;
        myFile = file;
        myMainComponent = new JPanel(new BorderLayout());
        myMainComponent.add(textEditor.getComponent(), BorderLayout.CENTER);
        myMainComponent.add(createOCamlEditorToolbar().getComponent(), BorderLayout.NORTH);
    }

    @Override public @Nls(capitalization = Nls.Capitalization.Title) @NotNull String getName() {
        return OCamlBundle.message("editor.name");
    }

    private @NotNull ActionToolbar createOCamlEditorToolbar() {
        ActionManager instance = ActionManager.getInstance();
        var toolbar = instance.createActionToolbar(
                ActionPlaces.EDITOR_TOOLBAR,
                createActionGroup(),
                true
        );
        toolbar.setTargetComponent(myTextEditor.getEditor().getContentComponent());
        return toolbar;
    }

    private @NotNull ActionGroup createActionGroup() {
        DefaultActionGroup defaultActionGroup = new DefaultActionGroup();
        defaultActionGroup.add(new ToolbarAction(OCamlRunFileREPLAction.ACTION_ID));
        defaultActionGroup.add(new ToolbarAction(OCamlRunSelection.ACTION_ID));
        defaultActionGroup.add(new Separator());
        defaultActionGroup.add(new ToolbarAction(OCamlEditorApiAction.ACTION_ID));
        defaultActionGroup.add(new ToolbarAction(OCamlEditorHelpAction.ACTION_ID));
        return defaultActionGroup;
    }

    private class ToolbarAction extends AnAction {
        private final AnAction myAction;

        public ToolbarAction(String actionId) {
            // we will transfer actionPerformed and updates to this action
            myAction = ActionManager.getInstance().getAction(actionId);
            // Load icon/... from the original action
            copyFrom(myAction);
        }

        @Override public void actionPerformed(@NotNull AnActionEvent e) {
            ActionUtilsAdaptor.performActionDumbAwareWithCallbacks(myAction, createEvent(e));
        }

        @Override public void update(@NotNull AnActionEvent e) {
            myAction.update(createEvent(e));
        }

        private @NotNull AnActionEvent createEvent(AnActionEvent e) {
            var file = FileDocumentManager.getInstance().getFile(myTextEditor.getEditor().getDocument());
            PsiFile psiFile = null;
            if (file != null) psiFile = PsiManager.getInstance(myProject).findFile(file);

            return AnActionEvent.createFromInputEvent(
                    e.getInputEvent(), "", e.getPresentation(),
                    SimpleDataContextBuilderAdaptor.builder()
                            .add(CommonDataKeys.EDITOR, myTextEditor.getEditor())
                            .add(CommonDataKeys.VIRTUAL_FILE, file)
                            .add(CommonDataKeys.PSI_FILE, psiFile)
                            .setParent(e.getDataContext())
                            .build()
            );
        }
    }

    // Others
    @Override public void dispose() { TextEditorProvider.getInstance().disposeEditor(myTextEditor); }
    @Override public @NotNull JComponent getComponent() { return myMainComponent; }
    @Override public @NotNull FileEditorState getState(@NotNull FileEditorStateLevel level) { return myTextEditor.getState(level); }
    @Override public void setState(@NotNull FileEditorState state) { myTextEditor.setState(state); }
    @Override public boolean isModified() { return myTextEditor.isModified(); }
    @Override public boolean isValid() { return myTextEditor.isValid(); }
    @Override public @Nullable BackgroundEditorHighlighter getBackgroundHighlighter() { return myTextEditor.getBackgroundHighlighter(); }
    @Override public @Nullable FileEditorLocation getCurrentLocation() { return myTextEditor.getCurrentLocation(); }
    @Override public @Nullable JComponent getPreferredFocusedComponent() { return myTextEditor.getPreferredFocusedComponent(); }
    @Override public @Nullable StructureViewBuilder getStructureViewBuilder() { return myTextEditor.getStructureViewBuilder(); }
    @Override public @NotNull Editor getEditor() { return myTextEditor.getEditor(); }
    @Override public void navigateTo(@NotNull Navigatable navigatable) { myTextEditor.navigateTo(navigatable); }
    @Override public boolean canNavigateTo(@NotNull Navigatable navigatable) { return myTextEditor.canNavigateTo(navigatable); }
    @Nullable @Override public VirtualFile getFile() { return myFile; }
    @Override public void selectNotify() {}
    @Override public void deselectNotify() {}
    @Override public void addPropertyChangeListener(@NotNull PropertyChangeListener listener) { myTextEditor.addPropertyChangeListener(listener); }
    @Override public void removePropertyChangeListener(@NotNull PropertyChangeListener listener) { myTextEditor.removePropertyChangeListener(listener); }
}
