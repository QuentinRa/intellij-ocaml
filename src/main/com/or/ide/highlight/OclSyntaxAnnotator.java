package com.or.ide.highlight;

import com.intellij.lang.annotation.AnnotationHolder;
import com.intellij.lang.annotation.Annotator;
import com.intellij.openapi.editor.colors.TextAttributesKey;
import com.intellij.openapi.editor.markup.TextAttributes;
import com.intellij.psi.PsiElement;
import com.intellij.psi.tree.IElementType;
import com.or.lang.OCamlTypes;
import com.or.lang.core.psi.PsiInclude;
import com.or.lang.core.psi.PsiModule;
import com.or.lang.core.psi.PsiOpen;
import com.or.lang.core.psi.impl.PsiInterpolationReference;
import org.jetbrains.annotations.NotNull;

import static com.intellij.lang.annotation.HighlightSeverity.INFORMATION;
import static com.ocaml.ide.highlight.OCamlSyntaxHighlighter.*;

public class OclSyntaxAnnotator implements Annotator {
    public OclSyntaxAnnotator() {
    }

    public void annotate(@NotNull PsiElement element, @NotNull AnnotationHolder holder) {
        IElementType elementType = element.getNode().getElementType();

        if (elementType == OCamlTypes.C_UPPER_IDENTIFIER) {
            PsiElement parent = element.getParent();
            if (parent instanceof PsiModule) {
                color(holder, element.getNavigationElement(), MODULE_NAME_);
            }
        } else if (elementType == OCamlTypes.C_UPPER_SYMBOL) {
            PsiElement parent = element.getParent();
            PsiElement nextElement = element.getNextSibling();
            IElementType nextElementType =
                    nextElement == null ? null : nextElement.getNode().getElementType();
            boolean mightBeVariant =
                    (nextElementType != OCamlTypes.DOT)
                            && !(parent instanceof PsiOpen)
                            && !(parent instanceof PsiInclude)
                            && !(parent instanceof PsiModule && ((PsiModule) parent).getAlias() != null);
            color(holder, element, mightBeVariant ? VARIANT_NAME_ : MODULE_NAME_);
        } else if (elementType == OCamlTypes.C_VARIANT_DECLARATION) {
            PsiElement identifier = element.getNavigationElement();
            color(holder, identifier, VARIANT_NAME_);
        } else if (elementType == OCamlTypes.VARIANT_NAME) {
            color(holder, element, VARIANT_NAME_);
        } else if (elementType == OCamlTypes.C_MACRO_NAME) {
            color(holder, element, ANNOTATION_);
        } else if (elementType == OCamlTypes.TAG_NAME
                || elementType == OCamlTypes.TAG_LT
                || elementType == OCamlTypes.TAG_LT_SLASH
                || elementType == OCamlTypes.TAG_GT
                || elementType == OCamlTypes.TAG_AUTO_CLOSE) {
            color(holder, element, MARKUP_TAG_);
        } else if (elementType == OCamlTypes.OPTION) {
            color(holder, element, OPTION_);
        } else if (elementType == OCamlTypes.PROPERTY_NAME) {
            color(holder, element, MARKUP_ATTRIBUTE_);
        } else if (elementType == OCamlTypes.C_INTERPOLATION_PART) {
            color(holder, element, STRING_);
        } else if (element instanceof PsiInterpolationReference) {
            color(holder, element, INTERPOLATED_REF_);
        }
    }

    private void color(@NotNull AnnotationHolder holder, @NotNull PsiElement element, @NotNull TextAttributesKey key) {
        holder.newSilentAnnotation(INFORMATION)
                .range(element)
                .textAttributes(key)
                .create();
    }
}
