package com.dune.ide.highlight;

import com.dune.lang.core.psi.DuneTypes;
import com.dune.lang.core.psi.PsiDuneField;
import com.dune.lang.core.psi.PsiStanza;
import com.intellij.lang.annotation.AnnotationHolder;
import com.intellij.lang.annotation.Annotator;
import com.intellij.openapi.editor.colors.TextAttributesKey;
import com.intellij.psi.PsiElement;
import com.intellij.psi.tree.IElementType;
import org.jetbrains.annotations.NotNull;

import static com.intellij.lang.annotation.HighlightSeverity.INFORMATION;

public class DuneSyntaxAnnotator implements Annotator {

    @Override public void annotate(@NotNull PsiElement element, @NotNull AnnotationHolder holder) {
        IElementType elementType = element.getNode().getElementType();

        if (element instanceof PsiStanza) {
            PsiElement identifier = ((PsiStanza) element).getNameIdentifier();
            if (identifier != null) {
                color(holder, identifier, DuneSyntaxHighlighter.STANZAS_);
            }
        }

        if (element instanceof PsiDuneField) {
            PsiElement identifier = ((PsiDuneField) element).getNameIdentifier();
            if (identifier != null) {
                color(holder, identifier, DuneSyntaxHighlighter.FIELDS_);
            }
        } else if (elementType == DuneTypes.VAR) {
            color(holder, element, DuneSyntaxHighlighter.VAR_);
        }
    }

    private void color(@NotNull AnnotationHolder holder, @NotNull PsiElement element, @NotNull TextAttributesKey key) {
        holder.newSilentAnnotation(INFORMATION).range(element).textAttributes(key).create();
    }
}
