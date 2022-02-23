package com.ocaml.ide.editor;

import com.intellij.application.options.colors.ColorAndFontOptions;
import com.intellij.icons.AllIcons;
import com.intellij.ide.highlighter.HighlighterFactory;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.EditorFactory;
import com.intellij.openapi.editor.EditorSettings;
import com.intellij.openapi.editor.colors.EditorColorsManager;
import com.intellij.openapi.editor.colors.EditorColorsScheme;
import com.intellij.openapi.editor.ex.EditorEx;
import com.intellij.openapi.editor.ex.EditorGutterComponentEx;
import com.intellij.openapi.editor.ex.EditorMarkupModel;
import com.intellij.openapi.editor.highlighter.EditorHighlighter;
import com.intellij.openapi.editor.markup.AnalyzerStatus;
import com.intellij.openapi.fileEditor.FileEditor;
import com.intellij.openapi.fileEditor.FileEditorLocation;
import com.intellij.openapi.fileEditor.FileEditorState;
import com.intellij.openapi.fileEditor.impl.FileDocumentManagerBase;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.Disposer;
import com.intellij.openapi.util.UserDataHolderBase;
import com.intellij.openapi.vfs.VfsUtil;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiManager;
import com.intellij.testFramework.LightVirtualFile;
import com.ocaml.ide.files.OCamlFileType;
import com.ocaml.ide.highlight.OCamlSyntaxHighlighter;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.beans.PropertyChangeListener;

/**
 * Add a tab preview, allowing the user
 * to preview the content of the file.
 * @see com.intellij.application.options.colors.SimpleEditorPreview
 * @see com.intellij.application.options.JavaCodeStyleMainPanel
 * @see com.intellij.ide.JavaLanguageCodeStyleSettingsProvider
 */
public class OCamlAnnotFileEditor extends UserDataHolderBase implements FileEditor {

    private final VirtualFile file;
    private final EditorEx myEditor;
    private JPanel myMainPanel;

    public OCamlAnnotFileEditor(@NotNull Project project, @NotNull VirtualFile file) {
        this.file = file;

        PsiFile psiFile = PsiManager.getInstance(project).findFile(file);
        if (psiFile == null) throw new IllegalStateException("PsiFile was null for "+file.getName());
        String annot = psiFile.getText();

        String nameWithoutExtension = file.getNameWithoutExtension();
        VirtualFile source = VfsUtil.findRelativeFile(file.getParent(), nameWithoutExtension + OCamlFileType.DOT_DEFAULT_EXTENSION);
        if (source == null) throw new IllegalStateException("Source not found");
        PsiFile sourcePsi = PsiManager.getInstance(project).findFile(source);
        if (sourcePsi == null) throw new IllegalStateException("Source not found (psi).");
        String text = sourcePsi.getText();

        EditorColorsScheme scheme = EditorColorsManager.getInstance().getGlobalScheme();
        ColorAndFontOptions options = new ColorAndFontOptions();
        options.reset();
        options.selectScheme(scheme.getName());

        EditorHighlighter highlighter = HighlighterFactory.createHighlighter(new OCamlSyntaxHighlighter(), scheme);

        // createPreviewEditor
        EditorFactory editorFactory = EditorFactory.getInstance();
        Document editorDocument = editorFactory.createDocument(text);
        FileDocumentManagerBase.registerDocument(editorDocument, new LightVirtualFile());
        myEditor = (EditorEx) editorFactory.createViewer(editorDocument); // read-only
        myEditor.setHighlighter(highlighter);
        EditorSettings settings = myEditor.getSettings();
        settings.setLineNumbersShown(false);
        settings.setWhitespacesShown(true);
        settings.setLineMarkerAreaShown(false);
        settings.setIndentGuidesShown(false);
        settings.setAdditionalColumnsCount(0);
        settings.setAdditionalLinesCount(0);
        settings.setRightMarginShown(true);
        settings.setRightMargin(60);
        settings.setGutterIconsShown(false);
        settings.setIndentGuidesShown(false);
        settings.setUseSoftWraps(true);
        ((EditorGutterComponentEx)myEditor.getGutter()).setPaintBackground(false);

        // installTrafficLights + DumbTrafficLightRenderer
        EditorMarkupModel markupModel = (EditorMarkupModel)myEditor.getMarkupModel();
        markupModel.setErrorStripeRenderer(() -> new AnalyzerStatus(AllIcons.General.InspectionsOK, "", "", AnalyzerStatus::getEmptyController));
        markupModel.setErrorStripeVisible(true);

        myMainPanel.add(myEditor.getComponent());
    }

    @Override public @NotNull JComponent getComponent() {
        return myMainPanel;
    }

    @Override public @Nullable JComponent getPreferredFocusedComponent() {
        return myMainPanel;
    }

    @Override public @Nls(capitalization = Nls.Capitalization.Title) @NotNull String getName() {
        return "Preview";
    }

    @Override public void setState(@NotNull FileEditorState state) {
    }

    @Override public boolean isModified() {
        return false;
    }

    @Override public boolean isValid() {
        return true;
    }

    @Override public @Nullable FileEditorLocation getCurrentLocation() {
        return null;
    }

    @Override public void dispose() {
        // editor
        EditorFactory editorFactory = EditorFactory.getInstance();
        editorFactory.releaseEditor(myEditor);

        Disposer.dispose(this);
    }

    @Override public void addPropertyChangeListener(@NotNull PropertyChangeListener listener) {
    }

    @Override public void removePropertyChangeListener(@NotNull PropertyChangeListener listener) {
    }

    // The default implementation of method 'getFile' is deprecated, you need to override it in
    // 'class com.ocaml.ide.files.editor.OCamlAnnotFileEditor'.
    // A proper @NotNull implementation required
    @Override public @Nullable VirtualFile getFile() {
        return file;
    }
}
