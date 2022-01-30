package com.dune.ide.folding;

import com.dune.lang.core.psi.PsiDuneFields;
import com.dune.lang.core.psi.PsiStanza;
import com.dune.lang.core.psi.DuneTypes;
import com.dune.utils.psi.DunePsiTreeUtil;
import com.intellij.lang.ASTNode;
import com.intellij.lang.folding.FoldingBuilderEx;
import com.intellij.lang.folding.FoldingDescriptor;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.PsiElement;
import com.intellij.psi.util.PsiTreeUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class DuneFoldingBuilder extends FoldingBuilderEx {

    private static boolean isMultiline(@NotNull TextRange range, @NotNull Document document) {
        return document.getLineNumber(range.getStartOffset())
                < document.getLineNumber(range.getEndOffset());
    }

    @NotNull @Override public FoldingDescriptor @NotNull [] buildFoldRegions(@NotNull PsiElement root,
                                                                             @NotNull Document document,
                                                                             boolean quick) {
        List<FoldingDescriptor> descriptors = new ArrayList<>();

        PsiTreeUtil.processElements(
                root,
                element -> {
                    if (isMultiline(element.getTextRange(), document)) {
                        FoldingDescriptor fold = null;
                        if (element instanceof PsiStanza) {
                            fold = foldStanza((PsiStanza) element);
                        } else if (DuneTypes.S_EXPR == element.getNode().getElementType()) {
                            fold = fold(element);
                        }
                        if (fold != null) {
                            descriptors.add(fold);
                        }
                    }

                    return true;
                });

        return descriptors.toArray(new FoldingDescriptor[0]);
    }

    @Nullable private FoldingDescriptor foldStanza(@NotNull PsiStanza root) {
        PsiDuneFields fields = DunePsiTreeUtil.findImmediateFirstChildOfClass(root, PsiDuneFields.class);
        return fields == null ? null : new FoldingDescriptor(root, fields.getTextRange());
    }

    @Nullable private FoldingDescriptor fold(@Nullable PsiElement root) {
        if (root == null) {
            return null;
        }

        // find next element
        ASTNode element = root.getFirstChild().getNode();
        ASTNode nextElement = element == null ? null : DunePsiTreeUtil.nextSiblingNode(element);
        ASTNode nextNextElement = nextElement == null ? null : DunePsiTreeUtil.nextSiblingNode(nextElement);

        if (nextNextElement != null) {
            TextRange rootRange = root.getTextRange();
            TextRange nextRange = nextElement.getTextRange();
            return new FoldingDescriptor(
                    root, TextRange.create(nextRange.getEndOffset(), rootRange.getEndOffset() - 1));
        }

        return null;
    }

    @Nullable @Override public String getPlaceholderText(@NotNull ASTNode node) {
        return "...";
    }

    @Override public boolean isCollapsedByDefault(@NotNull ASTNode node) {
        return false;
    }
}
