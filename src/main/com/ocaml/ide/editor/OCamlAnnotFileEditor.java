// Copyright 2000-2021 JetBrains s.r.o. Use of this source code is governed by the Apache 2.0 license that can be found in the LICENSE file.
// http://www.apache.org/licenses/LICENSE-2.0
package com.ocaml.ide.editor;

import com.intellij.application.options.colors.ColorAndFontOptions;
import com.intellij.icons.AllIcons;
import com.intellij.ide.highlighter.HighlighterFactory;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.EditorFactory;
import com.intellij.openapi.editor.EditorSettings;
import com.intellij.openapi.editor.LogicalPosition;
import com.intellij.openapi.editor.colors.EditorColors;
import com.intellij.openapi.editor.colors.EditorColorsManager;
import com.intellij.openapi.editor.colors.EditorColorsScheme;
import com.intellij.openapi.editor.event.EditorMouseEvent;
import com.intellij.openapi.editor.event.EditorMouseListener;
import com.intellij.openapi.editor.ex.EditorEx;
import com.intellij.openapi.editor.ex.EditorGutterComponentEx;
import com.intellij.openapi.editor.ex.EditorMarkupModel;
import com.intellij.openapi.editor.highlighter.EditorHighlighter;
import com.intellij.openapi.editor.markup.*;
import com.intellij.openapi.fileEditor.FileEditor;
import com.intellij.openapi.fileEditor.FileEditorLocation;
import com.intellij.openapi.fileEditor.FileEditorState;
import com.intellij.openapi.fileEditor.impl.FileDocumentManagerBase;
import com.intellij.openapi.fileTypes.SyntaxHighlighter;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.OnePixelDivider;
import com.intellij.openapi.util.Disposer;
import com.intellij.openapi.util.Pair;
import com.intellij.openapi.util.TextRange;
import com.intellij.openapi.util.UserDataHolderBase;
import com.intellij.openapi.vfs.VfsUtil;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiManager;
import com.intellij.testFramework.LightVirtualFile;
import com.intellij.ui.ClickListener;
import com.intellij.ui.JBSplitter;
import com.intellij.ui.SimpleColoredComponent;
import com.intellij.ui.SimpleTextAttributes;
import com.intellij.ui.components.JBScrollPane;
import com.intellij.ui.render.RenderingUtil;
import com.intellij.ui.treeStructure.Tree;
import com.intellij.util.Alarm;
import com.intellij.util.ui.JBUI;
import com.intellij.util.ui.UIUtil;
import com.intellij.util.ui.tree.TreeUtil;
import com.ocaml.OCamlBundle;
import com.ocaml.OCamlLanguage;
import com.ocaml.icons.OCamlIcons;
import com.ocaml.ide.files.OCamlFileType;
import com.ocaml.ide.highlight.OCamlSyntaxHighlighter;
import com.ocaml.sdk.annot.OCamlAnnotParser;
import com.ocaml.sdk.annot.OCamlInferredSignature;
import com.ocaml.utils.adaptor.AlarmAdaptor;
import com.ocaml.utils.adaptor.ui.SimpleColoredComponentAdaptor;
import com.ocaml.utils.editor.ExtendedEditorUtil;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import javax.swing.border.AbstractBorder;
import javax.swing.tree.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.EventListener;
import java.util.HashMap;

/**
 * Add a tab preview, allowing the user
 * to preview the content of the file.
 * <br>
 * The code was mostly inspired by classes such as
 *
 * @see com.intellij.application.options.colors.SimpleEditorPreview
 * @see com.intellij.application.options.JavaDocFormattingPanel
 * @see com.intellij.application.options.colors.FontEditorPreview
 * @see com.intellij.application.options.CodeStyleAbstractPanel
 */
public class OCamlAnnotFileEditor extends UserDataHolderBase implements FileEditor, EventListener {

    private final VirtualFile file;
    private final EditorEx myEditor;
    private final JBSplitter myMainPanel;
    private final @Nullable PsiFile mySourcePsi;
    private final Alarm myUpdateAlarm;
    private final @NotNull PsiFile myAnnotPsiFile;

    private final Document myEditorDocument; // update .ml
    private final DefaultMutableTreeNode myRootNode; // update nodes
    private final String mySourceFileName; // show error
    private final DefaultTreeModel myTreeModel; // reload model
    private String myAnnotText; // check changed
    private HashMap<String, LineNumberNode> myGroups; // used by listeners

    public OCamlAnnotFileEditor(@NotNull Project project, @NotNull VirtualFile file) {
        this.file = file;

        PsiFile psiFile = PsiManager.getInstance(project).findFile(file);
        // this is an internal error
        if (psiFile == null) throw new IllegalStateException("PsiFile was null for " + file.getName());
        myAnnotPsiFile = psiFile;
        Pair<ArrayList<OCamlInferredSignature>, String> loadedContent = loadAnnotFile();

        // Usually, the .annot is in the same directory
        // At least, I'm doing this when compiling
        // So, we are checking this location first
        String nameWithoutExtension = file.getNameWithoutExtension();
        String fileName = nameWithoutExtension + OCamlFileType.DOT_DEFAULT_EXTENSION;
        VirtualFile source = VfsUtil.findRelativeFile(file.getParent(), fileName);
        // TOO BAD, maybe we should check the source in the signatures then
        if (source == null && loadedContent.first.size() > 0) {
            File f = loadedContent.first.get(0).position.getFile();
            fileName = f.getAbsolutePath(); // if used in messages
            source = VfsUtil.findFile(f.toPath(), false);
        }

        if (source != null) {
            mySourcePsi = PsiManager.getInstance(project).findFile(source);
            if (mySourcePsi == null) // again, this is an internal error
                throw new IllegalStateException("Source (psi) not found for '" + fileName + "'.");
        } else {
            mySourcePsi = null;
            // take precedence over the previous error message (if any)
            loadedContent = new Pair<>(loadedContent.first, OCamlBundle.message("source.psi.not.found", fileName));
        }

        mySourceFileName = fileName;

        // source may be empty
        String sourceText = mySourcePsi == null ? "" : mySourcePsi.getText();
        if (sourceText.isBlank())
            sourceText = "(* " + OCamlBundle.message("editor.annot.source.empty") + " *)\n" + sourceText;

        // init editor
        EditorColorsScheme scheme = EditorColorsManager.getInstance().getGlobalScheme();
        ColorAndFontOptions options = new ColorAndFontOptions();
        options.reset();
        options.selectScheme(scheme.getName());

        SyntaxHighlighter myHighlighter = new OCamlSyntaxHighlighter();
        EditorHighlighter highlighter = HighlighterFactory.createHighlighter(myHighlighter, scheme);

        // createPreviewEditor
        EditorFactory editorFactory = EditorFactory.getInstance();
        myEditorDocument = editorFactory.createDocument(sourceText);
        FileDocumentManagerBase.registerDocument(myEditorDocument, new LightVirtualFile());
        myEditor = (EditorEx) editorFactory.createViewer(myEditorDocument); // read-only
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
        ((EditorGutterComponentEx) myEditor.getGutter()).setPaintBackground(false);

        // installTrafficLights + DumbTrafficLightRenderer
        EditorMarkupModel markupModel = (EditorMarkupModel) myEditor.getMarkupModel();
        markupModel.setErrorStripeRenderer(() -> new AnalyzerStatus(AllIcons.General.InspectionsOK, "", "", AnalyzerStatus::getEmptyController));
        markupModel.setErrorStripeVisible(true);

        myRootNode = new DefaultMutableTreeNode();
        fillRootNode(loadedContent);

        myTreeModel = new DefaultTreeModel(myRootNode);
        final Tree optionsTree = new Tree(myTreeModel);
        // icons
        optionsTree.setCellRenderer(new MyTreeCellRenderer());
        optionsTree.setRootVisible(false);
        optionsTree.setShowsRootHandles(true);
        optionsTree.getEmptyText().setText(OCamlBundle.message("editor.annot.annot.empty"));
        optionsTree.setBackground(UIUtil.getPanelBackground());
        // padding
        optionsTree.setBorder(JBUI.Borders.empty(10));
        // Add actions
        TreeUtil.installActions(optionsTree);

        optionsTree.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (!optionsTree.isEnabled()) return;
                if (e.getKeyCode() == KeyEvent.VK_SPACE) {
                    TreePath treePath = optionsTree.getLeadSelectionPath();
                    updatePreview(treePath);
                    e.consume();
                }
            }
        });
        new ClickListener() {
            @Override
            public boolean onClick(@NotNull MouseEvent e, int clickCount) {
                if (!optionsTree.isEnabled()) return false;
                TreePath treePath = optionsTree.getPathForLocation(e.getX(), e.getY());
                updatePreview(treePath);
                return true;
            }
        }.installOn(optionsTree);

        // init mainPanel
        JScrollPane scrollPane = new JBScrollPane(optionsTree);
        JPanel previewPanel = createPreviewPanel();

        myMainPanel = new JBSplitter(false, 0.5f);
        myMainPanel.setFirstComponent(scrollPane);
        myMainPanel.setSecondComponent(previewPanel);
        myMainPanel.setShowDividerControls(false);
        myMainPanel.setHonorComponentsMinimumSize(false);

        myUpdateAlarm = AlarmAdaptor.createAlarm(myEditor.getComponent(), this);

        myEditor.addEditorMouseListener(new EditorMouseListener() {
            @Override
            public void mouseClicked(@NotNull EditorMouseEvent event) {
                if (mySourcePsi == null) return;
                // find element
                Point point = event.getMouseEvent().getPoint();
                LogicalPosition logicalPosition = myEditor.xyToLogicalPosition(point);
                int pos = logicalPosition.column;

                // find group
                LineNumberNode node = myGroups.get(LineNumberNode.makeMessage(logicalPosition.line + 1));
                if (node == null) return;

                // iterate the group, to find if we got something
                Enumeration<TreeNode> children = node.children();
                MySignatureNode focusNode = null;
                while (children.hasMoreElements()) {
                    TreeNode child = children.nextElement();
                    if (!(child instanceof MySignatureNode)) continue;
                    MySignatureNode treeNode = (MySignatureNode) child;
                    OCamlInferredSignature signature = treeNode.signature;
                    int cStart = signature.position.getStartColumn();
                    int cEnd = signature.position.getEndColumn();
                    // if before or after
                    // we can't use inRange, dunno why?
                    // well, testing if the cursor is in the range works too
                    if (pos < cStart || pos > cEnd) continue;
                    focusNode = treeNode;
                    break;
                }

                if (focusNode != null) {
                    TreePath path = new TreePath(focusNode.getPath());
                    optionsTree.setSelectionPath(path);
                    optionsTree.scrollPathToVisible(path);
                }
            }
        });
    }

    private void fillRootNode(@NotNull Pair<ArrayList<OCamlInferredSignature>, String> loadedContent) {
        // signatures are not sorted by line number
        // we need to create/get the group
        myGroups = new HashMap<>();
        // fill the tree
        for (OCamlInferredSignature signature : loadedContent.first) {
            int line = signature.position.getStartLine();
            String newGroupName = LineNumberNode.makeMessage(line);
            LineNumberNode groupNode = myGroups.get(newGroupName);
            if (!myGroups.containsKey(newGroupName)) {
                groupNode = new LineNumberNode(line, newGroupName);
                myGroups.put(newGroupName, groupNode);
                myRootNode.add(groupNode);
            }
            groupNode.add(new MySignatureNode(signature));
        }

        if (loadedContent.second != null) {
            myRootNode.removeAllChildren();
            myRootNode.add(new ErrorMessageNode(loadedContent.second));
            myRootNode.add(new DefaultMutableTreeNode(OCamlBundle.message("please.submit.an.issue")));
        }
    }

    @Contract(" -> new")
    private @NotNull Pair<ArrayList<OCamlInferredSignature>, String> loadAnnotFile() {
        myAnnotText = myAnnotPsiFile.getText();
        ArrayList<OCamlInferredSignature> signatures;
        String errorMessage = null;
        try {
            OCamlAnnotParser annotParser = new OCamlAnnotParser(myAnnotText);
            signatures = annotParser.get();
        } catch (Exception e) {
            errorMessage = e.getLocalizedMessage();
            signatures = new ArrayList<>();
        }
        return new Pair<>(signatures, errorMessage);
    }

    private void updatePreview(TreePath treePath) {
        if (treePath == null) {
            return;
        }
        Object o = treePath.getLastPathComponent();
        if (o instanceof MySignatureNode) {
            MySignatureNode node = (MySignatureNode) o;
            startBlinking(node);
        } else if (o instanceof LineNumberNode) {
            goToLine(((LineNumberNode) o).line);
        }
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
        // was modified if content is different
        return !myAnnotPsiFile.getText().equals(myAnnotText);
    }

    @Override public boolean isValid() {
        return myAnnotPsiFile.isValid();
    }

    @Override public void selectNotify() {
        if (!isModified()) return;

        // just in case, the file was changed during blinking
        myUpdateAlarm.cancelAllRequests();

        Pair<ArrayList<OCamlInferredSignature>, String> loadedContent = loadAnnotFile();
        if (mySourcePsi == null) {
            // take precedence over the previous error message (if any)
            loadedContent = new Pair<>(loadedContent.first, OCamlBundle.message("source.psi.not.found", mySourceFileName));
        }

        myRootNode.removeAllChildren();
        fillRootNode(loadedContent);
        myTreeModel.reload();

        // update the .ml shown in the editor
        if (mySourcePsi != null) {
            ApplicationManager.getApplication()
                    .runWriteAction(() -> myEditorDocument.setText(mySourcePsi.getText()));
        }
    }

    @Override public @Nullable FileEditorLocation getCurrentLocation() {
        return null;
    }

    @Override public void dispose() {
        myUpdateAlarm.cancelAllRequests();

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

        public @NotNull ArrayList<FragmentData> getFragments() {
            ArrayList<FragmentData> fragments = new ArrayList<>();

            // column
            int startColumn = signature.position.getStartColumn();
            int endColumn = signature.position.getEndColumn();
            String result = OCamlBundle.message("editor.annot.indicator.columns");
            result += " " + startColumn + "-" + endColumn;
            // add
            fragments.add(new FragmentData(result + " "));

            // name, type, etc.
            switch (signature.kind) {
                default: case UNKNOWN: fragments.add(new FragmentData(OCamlBundle.message("editor.annot.indicator.unknown"))); break;
                case VALUE: fragments.add(new FragmentData(signature.type)); break;
                case VARIABLE:
                case MODULE:
                    fragments.add(new FragmentData(signature.name + " (" + signature.type + ")"));
                    break;
            }

            return fragments;
        }

        @Contract(pure = true) public @Nullable Icon getIcon() {
            switch (signature.kind) {
                default: case UNKNOWN: return null;
                case VALUE: return OCamlIcons.Nodes.OBJECT;
                case VARIABLE:
                    return signature.type.contains(OCamlLanguage.FUNCTION_SIGNATURE_SEPARATOR) ?
                            OCamlIcons.Nodes.FUNCTION : OCamlIcons.Nodes.VARIABLE;
                case MODULE: return OCamlIcons.Nodes.INNER_MODULE;
            }
        }
    }

    private static class LineNumberNode extends DefaultMutableTreeNode {
        public final int line;

        public LineNumberNode(int line, String message) {
            super(message);
            this.line = line;
        }

        @Contract(pure = true) public static @NotNull String makeMessage(int line) {
            return OCamlBundle.message("editor.annot.indicator.line") + " " + line;
        }
    }

    private static class ErrorMessageNode extends DefaultMutableTreeNode {
        public ErrorMessageNode(String message) {
            super(message);
        }
    }

    private static class FragmentData {
        public String text;
        public SimpleTextAttributes attributes = SimpleTextAttributes.REGULAR_ATTRIBUTES;
        public boolean isHTML = false;

        public FragmentData(String text) {
            this.text = text;
        }
    }

    private static class MyTreeCellRenderer implements TreeCellRenderer {
        @Override
        public @NotNull Component getTreeCellRendererComponent(JTree tree, Object value, boolean isSelected, boolean expanded,
                                                               boolean leaf, int row, boolean hasFocus) {
            Color background = RenderingUtil.getBackground(tree, isSelected);
            Color foreground = RenderingUtil.getForeground(tree, isSelected);
            SimpleColoredComponent myLabel = new SimpleColoredComponent();
            if (value instanceof MySignatureNode) {
                MySignatureNode treeNode = (MySignatureNode) value;
                myLabel.setIcon(treeNode.getIcon());
                for (FragmentData fragment : treeNode.getFragments()) {
                    if (fragment.isHTML) {
                        SimpleColoredComponentAdaptor.appendHTML(myLabel, fragment.text, fragment.attributes);
                    } else {
                        myLabel.append(fragment.text, fragment.attributes);
                    }
                }
            } else if (value instanceof ErrorMessageNode) {
                SimpleColoredComponentAdaptor.appendHTML(myLabel, value.toString(), SimpleTextAttributes.REGULAR_ATTRIBUTES);
                myLabel.setIcon(AllIcons.General.Error);
            } else if (value != null) {
                // ex: group names
                myLabel.append(value.toString());
            } else {
                myLabel.append("null");
            }
            myLabel.setEnabled(tree.isEnabled());
            myLabel.setForeground(foreground);
            myLabel.setBackground(background);
            myLabel.setOpaque(true);
            return myLabel;
        }
    }

    //
    // Blink
    //

    private void startBlinking(@NotNull MySignatureNode node) {
        long endHighlightPreviewChangesTimeMillis = System.currentTimeMillis() + 5000;

        // reset
        myEditor.getMarkupModel().removeAllHighlighters();
        // stop
        myUpdateAlarm.cancelAllRequests();

        myUpdateAlarm.addComponentRequest(new Runnable() {
            private boolean canBeEnabled = true;

            @Override
            public void run() {
                Project project = myEditor.getProject();
                if (myEditor.isDisposed() || project != null && project.isDisposed()) return;
                // Show
                if (System.currentTimeMillis() <= endHighlightPreviewChangesTimeMillis && canBeEnabled) {
                    blink(node.signature);
                    myUpdateAlarm.addComponentRequest(this, 400);
                } else {
                    // Hide
                    myEditor.getMarkupModel().removeAllHighlighters();
                    myUpdateAlarm.addComponentRequest(this, 400);
                }
                canBeEnabled = !canBeEnabled;
            }
        }, 300);
    }

    private void blink(OCamlInferredSignature signature) {
        if (mySourcePsi == null) return;

        MarkupModel markupModel = myEditor.getMarkupModel();

        int offset = ExtendedEditorUtil.positionStartToOffset(myEditor, signature.position);
        int endOffset = ExtendedEditorUtil.positionEndToOffset(myEditor, signature.position);

        PsiElement start = mySourcePsi.findElementAt(offset);
        PsiElement end = mySourcePsi.findElementAt(endOffset);
        if (start == null || end == null) return;
        TextRange range = new TextRange(start.getTextRange().getStartOffset(), end.getTextRange().getEndOffset());

        TextAttributes backgroundAttributes = myEditor.getColorsScheme().getAttributes(EditorColors.SEARCH_RESULT_ATTRIBUTES);
        TextAttributes borderAttributes = new TextAttributes(
                null, null, backgroundAttributes.getBackgroundColor(), EffectType.BOXED, Font.PLAIN
        );
        TextAttributes attributesToUse = range.getLength() > 0 ? backgroundAttributes : borderAttributes;
        markupModel.addRangeHighlighter(
                range.getStartOffset(), range.getEndOffset(), HighlighterLayer.SELECTION, attributesToUse, HighlighterTargetArea.EXACT_RANGE
        );

        // Scroll to
        ExtendedEditorUtil.scrollToIfNotVisible(myEditor, range, mySourcePsi);
    }

    private void goToLine(int line) {
        if (mySourcePsi == null) return;

        int offset = ExtendedEditorUtil.positionToOffset(myEditor, line, 0);
        PsiElement start = mySourcePsi.findElementAt(offset);
        if (start == null) return;

        ExtendedEditorUtil.scrollToIfNotVisible(myEditor, start.getTextRange(), mySourcePsi);
    }

}
