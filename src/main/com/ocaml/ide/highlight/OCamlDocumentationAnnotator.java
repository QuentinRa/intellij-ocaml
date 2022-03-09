package com.ocaml.ide.highlight;

import com.intellij.openapi.editor.DefaultLanguageHighlighterColors;
import com.intellij.openapi.editor.colors.TextAttributesKey;

import com.intellij.lang.annotation.AnnotationHolder;
import com.intellij.lang.annotation.Annotator;
import com.intellij.lang.annotation.HighlightSeverity;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.PsiComment;
import com.intellij.psi.PsiElement;
import com.or.ide.docs.ORDocumentationProvider;
import org.jetbrains.annotations.NotNull;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Add some brown for "parameters" (values between [], regardeless of the content)
 * in the documentation.
 */
public class OCamlDocumentationAnnotator implements Annotator {

    // extracted from JavaHighlightingColors
    public static final TextAttributesKey DOC_COMMENT_TAG_VALUE =
            TextAttributesKey.createTextAttributesKey("DOC_COMMENT_TAG_VALUE",
                    DefaultLanguageHighlighterColors.DOC_COMMENT_TAG_VALUE);

    public static final Pattern PARAMETERS_MATCHER = Pattern.compile("(\\[[^]]*])");

    @Override public void annotate(@NotNull PsiElement element, @NotNull AnnotationHolder holder) {
        if (!(element instanceof PsiComment)) return;
        if (!ORDocumentationProvider.isDocumentation(element)) return;

        Matcher matcher = PARAMETERS_MATCHER.matcher(element.getText());
        TextRange range = element.getTextRange();
        while (matcher.find()) {
            holder.newSilentAnnotation(HighlightSeverity.INFORMATION)
                    .range(range.cutOut(new TextRange(matcher.start(), matcher.end())))
                    .textAttributes(DOC_COMMENT_TAG_VALUE).create();
        }
    }
}
