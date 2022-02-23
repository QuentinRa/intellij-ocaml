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
import com.intellij.openapi.ui.OnePixelDivider;
import com.intellij.openapi.util.Disposer;
import com.intellij.openapi.util.UserDataHolderBase;
import com.intellij.openapi.vfs.VfsUtil;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiManager;
import com.intellij.testFramework.LightVirtualFile;
import com.intellij.ui.JBSplitter;
import com.intellij.ui.components.JBScrollPane;
import com.intellij.ui.treeStructure.Tree;
import com.intellij.util.ui.JBUI;
import com.intellij.util.ui.UIUtil;
import com.ocaml.OCamlBundle;
import com.ocaml.ide.files.OCamlFileType;
import com.ocaml.ide.highlight.OCamlSyntaxHighlighter;
import com.ocaml.sdk.annot.OCamlAnnotParser;
import com.ocaml.sdk.annot.OCamlInferredSignature;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import javax.swing.border.AbstractBorder;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import java.awt.*;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;

// todo: CellRenderer -> icons?
//  Click -> blink in editor
//  Click from editor?

/**
 * Add a tab preview, allowing the user
 * to preview the content of the file.
 * <br>
 * The code was mostly inspired by classes such as
 * @see com.intellij.application.options.colors.SimpleEditorPreview
 * @see com.intellij.application.options.JavaDocFormattingPanel
 * @see com.intellij.application.options.colors.FontEditorPreview
 */
public class OCamlAnnotFileEditor extends UserDataHolderBase implements FileEditor {

    private final VirtualFile file;
    private final EditorEx myEditor;
    private final JBSplitter myMainPanel;

    public OCamlAnnotFileEditor(@NotNull Project project, @NotNull VirtualFile file) {
        this.file = file;

        PsiFile psiFile = PsiManager.getInstance(project).findFile(file);
        if (psiFile == null) throw new IllegalStateException("PsiFile was null for "+file.getName());
        String annotText = psiFile.getText();
        ArrayList<OCamlInferredSignature> signatures;
        String errorMessage = null;
        try {
            OCamlAnnotParser annotParser = new OCamlAnnotParser(annotText);
            signatures = annotParser.get();
        } catch (Exception e) {
            errorMessage = e.getLocalizedMessage();
            signatures = new ArrayList<>();
        }

        String nameWithoutExtension = file.getNameWithoutExtension();
        VirtualFile source = VfsUtil.findRelativeFile(file.getParent(), nameWithoutExtension + OCamlFileType.DOT_DEFAULT_EXTENSION);
        if (source == null) throw new IllegalStateException("Source not found");
        PsiFile sourcePsi = PsiManager.getInstance(project).findFile(source);
        if (sourcePsi == null) throw new IllegalStateException("Source not found (psi).");
        String sourceText = sourcePsi.getText();
        // source may be empty
        if (sourceText.isBlank()) sourceText = "(* "+OCamlBundle.message("editor.annot.source.empty")+" *)\n" + sourceText;

        // init editor
        EditorColorsScheme scheme = EditorColorsManager.getInstance().getGlobalScheme();
        ColorAndFontOptions options = new ColorAndFontOptions();
        options.reset();
        options.selectScheme(scheme.getName());

        EditorHighlighter highlighter = HighlighterFactory.createHighlighter(new OCamlSyntaxHighlighter(), scheme);

        // createPreviewEditor
        EditorFactory editorFactory = EditorFactory.getInstance();
        Document editorDocument = editorFactory.createDocument(sourceText);
        FileDocumentManagerBase.registerDocument(editorDocument, new LightVirtualFile());
        myEditor = (EditorEx) editorFactory.createViewer(editorDocument); // read-only
        myEditor.setHighlighter(highlighter);
        EditorSettings settings = myEditor.getSettings();
        settings.setLineNumbersShown(true); // better 'cause we are indexing results by line number
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

        DefaultMutableTreeNode rootNode = new DefaultMutableTreeNode();
        String groupName = "";
        DefaultMutableTreeNode groupNode = null;

        for (OCamlInferredSignature signature : signatures) {
            String newGroupName = "Line "+signature.position.getStartLine();
            if (!newGroupName.equals(groupName)) {
                groupName = newGroupName;
                groupNode = new DefaultMutableTreeNode(newGroupName);
                rootNode.add(groupNode);
            }
            groupNode.add(new MySignatureNode(signature));
        }

        if (errorMessage != null)
            rootNode.add(new DefaultMutableTreeNode(errorMessage));

        DefaultTreeModel model = new DefaultTreeModel(rootNode);
        final Tree optionsTree = new Tree(model);
        optionsTree.setRootVisible(false);
        optionsTree.setShowsRootHandles(true);
        optionsTree.getEmptyText().setText(OCamlBundle.message("editor.annot.annot.empty"));
        optionsTree.setBackground(UIUtil.getPanelBackground());
        // padding
        optionsTree.setBorder(JBUI.Borders.empty(10));

        // init mainPanel
        JScrollPane scrollPane = new JBScrollPane(optionsTree);
        JPanel previewPanel = createPreviewPanel();

        myMainPanel = new JBSplitter(false, 0.5f);
        myMainPanel.setFirstComponent(scrollPane);
        myMainPanel.setSecondComponent(previewPanel);
        myMainPanel.setShowDividerControls(false);
        myMainPanel.setHonorComponentsMinimumSize(false);
    }

    private @NotNull JPanel createPreviewPanel() {
        JPanel previewPanel = new JPanel(new BorderLayout());

        previewPanel.setLayout(new BorderLayout());
        previewPanel.add(myEditor.getComponent(), BorderLayout.CENTER);
        previewPanel.setBorder(new AbstractBorder() {
            private static final int LEFT_WHITE_SPACE = 2;

            @Override
            public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
                g.setColor(myEditor.getBackgroundColor());
                g.fillRect(x + 1, y, LEFT_WHITE_SPACE, height);
                g.setColor(OnePixelDivider.BACKGROUND);
                g.fillRect(x, y, 1, height);
            }

            @Override
            public Insets getBorderInsets(Component c, Insets insets) {
                insets.set(0, 1 + LEFT_WHITE_SPACE, 0, 0);
                return insets;
            }
        });

        return previewPanel;
    }

    @Override public @NotNull JComponent getComponent() {
        return myMainPanel;
    }

    @Override public @Nullable JComponent getPreferredFocusedComponent() {
        return myMainPanel;
    }

    @Override public @Nls(capitalization = Nls.Capitalization.Title) @NotNull String getName() {
        return OCamlBundle.message("editor.annot.name");
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

    private static class MySignatureNode extends DefaultMutableTreeNode {
        private final OCamlInferredSignature signature;

        public MySignatureNode(OCamlInferredSignature signature) {
            this.signature = signature;
        }

        // todo: bundle
        @Override public String toString() {
            // Already grouped by line
            int startColumn = signature.position.getStartColumn();
            int endColumn = signature.position.getEndColumn();
            // line
            String result = " Column";
            if (startColumn != endColumn) result += "s";
            result += " "+startColumn;
            if (startColumn != endColumn) result += "-"+endColumn;

            // name, type, etc.
            switch (signature.kind) {
                case UNKNOWN: result = "<unknown>"; break;
                case VALUE: result += " '"+signature.type+"'"; break;
                case VARIABLE:
                case MODULE:
                    result += " "+signature.name+" ('"+signature.type+"')"; break;
            }
            return result;
        }
    }
}
