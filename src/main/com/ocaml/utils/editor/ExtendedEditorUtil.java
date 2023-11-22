package com.ocaml.utils.editor;

import com.intellij.build.FilePosition;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.LogicalPosition;
import com.intellij.openapi.editor.ScrollType;
import com.intellij.openapi.editor.VisualPosition;
import com.intellij.openapi.editor.ex.util.EditorUtil;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.PsiElement;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.awt.*;

public class ExtendedEditorUtil {

    // Position to Offset

    public static int positionToOffset(@NotNull Editor editor, int line, int column) {
        return editor.logicalPositionToOffset(new LogicalPosition(
                line - 1,
                column
        ));
    }

    public static int positionStartToOffset(@NotNull Editor editor, @NotNull FilePosition position) {
        return positionToOffset(editor, position.getStartLine(), position.getStartColumn());
    }

    public static int positionEndToOffset(@NotNull Editor editor, @NotNull FilePosition position) {
        return positionToOffset(editor, position.getEndLine(), position.getEndColumn() - 1);
    }

    // Scroll

    public static void scrollToIfNotVisible(@NotNull Editor myEditor, TextRange range, PsiElement e) {
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

        String text = e.getText();
        int offsetToScroll = -1;

        boolean rangeVisible = isWithinBounds(myEditor.offsetToVisualPosition(range.getStartOffset()),
                visualStart, visualEnd) || isWithinBounds(myEditor.offsetToVisualPosition(range.getEndOffset()), visualStart, visualEnd);
        if (text.charAt(range.getStartOffset()) != '\n') {
            offsetToScroll = range.getStartOffset();
        } else if (range.getEndOffset() > 0 && text.charAt(range.getEndOffset() - 1) != '\n') {
            offsetToScroll = range.getEndOffset() - 1;
        }

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
     * @param targetPosition position which belonging to target visual rectangle should be checked
     * @param startPosition  visual position of top-left corner of the target visual rectangle
     * @param endPosition    visual position of bottom-right corner of the target visual rectangle
     * @return {@code true} if given visual position belongs to the target visual rectangle;
     * {@code false} otherwise
     */
    @Contract(pure = true)
    public static boolean isWithinBounds(@NotNull VisualPosition targetPosition, @NotNull VisualPosition startPosition, VisualPosition endPosition) {
        return targetPosition.line >= startPosition.line && targetPosition.line <= endPosition.line
                && targetPosition.column >= startPosition.column && targetPosition.column <= endPosition.column;
    }
}
