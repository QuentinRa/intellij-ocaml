package com.ocaml.ide.highlight;

import com.ocaml.utils.adaptor.RequireJavaPlugin;

import com.intellij.ide.highlighter.JavaHighlightingColors;
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
@RequireJavaPlugin(what = "JavaHighlightingColors")
public class OCamlDocumentationAnnotator implements Annotator {

    public static final Pattern PARAMETERS_MATCHER = Pattern.compile("(\\[[^]]*])");

    @Override public void annotate(@NotNull PsiElement element, @NotNull AnnotationHolder holder) {
        if (!(element instanceof PsiComment)) return;
        if (!ORDocumentationProvider.isDocumentation(element)) return;

        Matcher matcher = PARAMETERS_MATCHER.matcher(element.getText());
        TextRange range = element.getTextRange();
        while (matcher.find()) {
            holder.newSilentAnnotation(HighlightSeverity.INFORMATION)
                    .range(range.cutOut(new TextRange(matcher.start(), matcher.end())))
                    .textAttributes(JavaHighlightingColors.DOC_COMMENT_TAG_VALUE).create();
        }
    }
}
