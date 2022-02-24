package com.ocaml.ide.editor;

import com.intellij.application.options.colors.ColorAndFontOptions;
import com.intellij.icons.AllIcons;
import com.intellij.ide.highlighter.HighlighterFactory;
import com.intellij.openapi.editor.*;
import com.intellij.openapi.editor.colors.EditorColors;
import com.intellij.openapi.editor.colors.EditorColorsManager;
import com.intellij.openapi.editor.colors.EditorColorsScheme;
import com.intellij.openapi.editor.ex.EditorEx;
import com.intellij.openapi.editor.ex.EditorGutterComponentEx;
import com.intellij.openapi.editor.ex.EditorMarkupModel;
import com.intellij.openapi.editor.ex.util.EditorUtil;
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
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import javax.swing.border.AbstractBorder;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeCellRenderer;
import javax.swing.tree.TreePath;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.EventListener;
import java.util.HashMap;

/**
 * Add a tab preview, allowing the user
 * to preview the content of the file.
 * <br>
 * The code was mostly inspired by classes such as
 * @see com.intellij.application.options.colors.SimpleEditorPreview
 * @see com.intellij.application.options.JavaDocFormattingPanel
 * @see com.intellij.application.options.colors.FontEditorPreview
 */
public class OCamlAnnotFileEditor extends UserDataHolderBase implements FileEditor, EventListener {

    private final VirtualFile file;
    private final EditorEx myEditor;
    private final JBSplitter myMainPanel;
    private final PsiFile mySourcePsi;

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
        mySourcePsi = PsiManager.getInstance(project).findFile(source);
        if (mySourcePsi == null) throw new IllegalStateException("Source not found (psi).");
        String sourceText = mySourcePsi.getText();
        // source may be empty
        if (sourceText.isBlank()) sourceText = "(* "+OCamlBundle.message("editor.annot.source.empty")+" *)\n" + sourceText;

        // init editor
        EditorColorsScheme scheme = EditorColorsManager.getInstance().getGlobalScheme();
        ColorAndFontOptions options = new ColorAndFontOptions();
        options.reset();
        options.selectScheme(scheme.getName());

        SyntaxHighlighter myHighlighter = new OCamlSyntaxHighlighter();
        EditorHighlighter highlighter = HighlighterFactory.createHighlighter(myHighlighter, scheme);

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
        // signatures are not sorted by line number
        // we need to create/get the group
        HashMap<String, DefaultMutableTreeNode> groups = new HashMap<>();
        // fill the tree
        for (OCamlInferredSignature signature : signatures) {
            String newGroupName = "Line "+signature.position.getStartLine();
            DefaultMutableTreeNode groupNode = groups.get(newGroupName);
            if (!groups.containsKey(newGroupName)) {
                groupNode = new DefaultMutableTreeNode(newGroupName);
                groups.put(newGroupName, groupNode);
                rootNode.add(groupNode);
            }
            groupNode.add(new MySignatureNode(signature));
        }
        groups.clear();

        if (errorMessage != null) {
            rootNode.removeAllChildren();
            rootNode.add(new ErrorMessageNode(errorMessage));
            rootNode.add(new DefaultMutableTreeNode(OCamlBundle.message("please.submit.an.issue")));
        }

        DefaultTreeModel model = new DefaultTreeModel(rootNode);
        final Tree optionsTree = new Tree(model);
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
    }
    private void updatePreview(TreePath treePath) {
        if (treePath == null) {
            return;
        }
        Object o = treePath.getLastPathComponent();
        if (o instanceof MySignatureNode) {
            MySignatureNode node = (MySignatureNode) o;
            blink(node.signature);
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

        public @NotNull ArrayList<FragmentData> getFragments() {
            ArrayList<FragmentData> fragments = new ArrayList<>();

            // column
            int startColumn = signature.position.getStartColumn();
            int endColumn = signature.position.getEndColumn();
            String result = OCamlBundle.message("editor.annot.indicator.columns");
            result += " "+startColumn + "-" +endColumn;
            // add
            fragments.add(new FragmentData(result+" "));

            // name, type, etc.
            switch (signature.kind) {
                default: case UNKNOWN: fragments.add(new FragmentData(OCamlBundle.message("editor.annot.indicator.unknown"))); break;
                case VALUE: fragments.add(new FragmentData("'"+signature.type+"'")); break;
                case VARIABLE:
                case MODULE:
                    fragments.add(new FragmentData(signature.name+" ('"+signature.type+"')")); break;
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
                MySignatureNode treeNode = (MySignatureNode)value;
                myLabel.setIcon(treeNode.getIcon());
                for (FragmentData fragment : treeNode.getFragments()) {
                    if (fragment.isHTML) {
                        myLabel.appendHTML(fragment.text, fragment.attributes);
                    } else {
                        myLabel.append(fragment.text, fragment.attributes);
                    }
                }
            } else if (value instanceof ErrorMessageNode) {
                myLabel.appendHTML(value.toString(), SimpleTextAttributes.REGULAR_ATTRIBUTES);
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

    // Blink

    private void blink(OCamlInferredSignature signature) {
        MarkupModel markupModel = myEditor.getMarkupModel();
        markupModel.removeAllHighlighters(); // reset
        Rectangle visibleArea = myEditor.getScrollingModel().getVisibleArea();
        VisualPosition visualStart = myEditor.xyToVisualPosition(visibleArea.getLocation());
        VisualPosition visualEnd = myEditor.xyToVisualPosition(new Point(visibleArea.x + visibleArea.width, visibleArea.y + visibleArea.height));

        // There is a possible case that viewport is located at its most bottom position and last document symbol
        // is located at the start of the line, hence, resulting visual end column has a small value and doesn't actually
        // indicate target visible rectangle. Hence, we need to correct that if necessary.
        int endColumnCandidate = visibleArea.width / EditorUtil.getSpaceWidth(Font.PLAIN, myEditor) + visualStart.column;
        if (endColumnCandidate > visualEnd.column) {
            visualEnd = new VisualPosition(visualEnd.line, endColumnCandidate);
        }

        int offset = myEditor.logicalPositionToOffset(new LogicalPosition(
                signature.position.getStartLine() - 1,
                signature.position.getStartColumn()
        ));

        int endOffset = myEditor.logicalPositionToOffset(new LogicalPosition(
                signature.position.getEndLine() - 1,
                signature.position.getEndColumn() - 1
        ));

        int offsetToScroll = -1;

        PsiElement start = mySourcePsi.findElementAt(offset);
        PsiElement end = mySourcePsi.findElementAt(endOffset);
        if (start == null || end == null) return;
        String text = mySourcePsi.getText();
        TextRange range = new TextRange(start.getTextRange().getStartOffset(), end.getTextRange().getEndOffset());

        boolean rangeVisible = isWithinBounds(myEditor.offsetToVisualPosition(range.getStartOffset()),
                visualStart, visualEnd) || isWithinBounds(myEditor.offsetToVisualPosition(range.getEndOffset()), visualStart, visualEnd);
        if (text.charAt(range.getStartOffset()) != '\n') {
            offsetToScroll = range.getStartOffset();
        } else if (range.getEndOffset() > 0 && text.charAt(range.getEndOffset() - 1) != '\n') {
            offsetToScroll = range.getEndOffset() - 1;
        }

        TextAttributes backgroundAttributes = myEditor.getColorsScheme().getAttributes(EditorColors.SEARCH_RESULT_ATTRIBUTES);
        TextAttributes borderAttributes = new TextAttributes(
                null, null, backgroundAttributes.getBackgroundColor(), EffectType.BOXED, Font.PLAIN
        );
        TextAttributes attributesToUse = range.getLength() > 0 ? backgroundAttributes : borderAttributes;
        markupModel.addRangeHighlighter(
                range.getStartOffset(), range.getEndOffset(), HighlighterLayer.SELECTION, attributesToUse, HighlighterTargetArea.EXACT_RANGE
        );

        if (!rangeVisible) {
            if (offsetToScroll >= 0 && offsetToScroll < text.length() - 1 && text.charAt(offsetToScroll) != '\n') {
                // There is a possible case that target offset is located too close to the right edge. However, our point is to show
                // highlighted region at target offset, hence, we need to scroll to the visual symbol end. Hence, we're trying to ensure
                // that by scrolling to the symbol's end over than its start.
                offsetToScroll++;
            }

            if (offsetToScroll >= 0 && offsetToScroll < myEditor.getDocument().getTextLength()) {
                myEditor.getScrollingModel().scrollTo(
                        myEditor.offsetToLogicalPosition(offsetToScroll), ScrollType.RELATIVE
                );
            }
        }
    }

    /**
     * Allows us to answer if a particular visual position belongs to visual rectangle
     * identified by the given visual position of its top-left and bottom-right corners.
     *
     * @param targetPosition    position which belonging to target visual rectangle should be checked
     * @param startPosition     visual position of top-left corner of the target visual rectangle
     * @param endPosition       visual position of bottom-right corner of the target visual rectangle
     * @return                  {@code true} if given visual position belongs to the target visual rectangle;
     *                          {@code false} otherwise
     */
    @Contract(pure = true)
    private static boolean isWithinBounds(@NotNull VisualPosition targetPosition, @NotNull VisualPosition startPosition, VisualPosition endPosition) {
        return targetPosition.line >= startPosition.line && targetPosition.line <= endPosition.line
                && targetPosition.column >= startPosition.column && targetPosition.column <= endPosition.column;
    }

}
