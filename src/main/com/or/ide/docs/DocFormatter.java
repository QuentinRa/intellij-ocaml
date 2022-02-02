package com.or.ide.docs;

import com.intellij.lang.documentation.DocumentationMarkup;
import com.intellij.openapi.util.text.HtmlBuilder;
import com.intellij.openapi.util.text.HtmlChunk;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiNamedElement;
import com.odoc.lang.OdocConverter;
import com.or.ide.files.FileBase;
import com.or.lang.core.psi.PsiQualifiedPathElement;
import com.or.lang.core.psi.PsiSignature;
import com.or.lang.core.psi.PsiSignatureElement;
import com.or.lang.utils.ORLanguageProperties;
import com.or.utils.Joiner;
import com.or.utils.Log;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

class DocFormatter {
    private static final Log LOG = Log.create("doc.formatter");

    private DocFormatter() {
    }

    static @NotNull String format(@NotNull PsiFile file, @NotNull PsiElement element, @Nullable ORLanguageProperties lang, @NotNull String text) {
        if (file instanceof FileBase) {
            FileBase source = (FileBase) file;

            // Definition

            HtmlBuilder definitionBuilder = new HtmlBuilder();

            String path = source.getModuleName();
            if (element instanceof PsiQualifiedPathElement) {
                path = Joiner.join(".", ((PsiQualifiedPathElement) element).getPath());
            }
            definitionBuilder.append(HtmlChunk.text(path).bold());

            if (element instanceof PsiNamedElement) {
                String className = element.getClass().getSimpleName().substring(3).replace("Impl", "").toLowerCase();
                String name = ((PsiNamedElement) element).getName();
                if (name != null) {
                    definitionBuilder.append(HtmlChunk.raw("<p><i>"));
                    definitionBuilder.append(HtmlChunk.text(className + " " + name));

                    if (element instanceof PsiSignatureElement) {
                        PsiSignature signature = ((PsiSignatureElement) element).getSignature();
                        if (signature != null) {
                            definitionBuilder.append(HtmlChunk.text(" : ")).append(HtmlChunk.text(signature.asText(lang)).wrapWith("code"));
                        }
                    }
                }
                definitionBuilder.append(HtmlChunk.raw("</i></p>"));
            }

            // Content

            HtmlBuilder contentBuilder = new HtmlBuilder();
            /* if (isDeprecated()) {
                contentBuilder
                      .append(HtmlChunk.text(xxx).bold().wrapWith(HtmlChunk.font("#" + ColorUtil.toHex(JBColor.RED))))
                      .append(HtmlChunk.br());
            } */

            OdocConverter converter = new OdocConverter();
            contentBuilder.append(converter.convert(text));

            // final render

            HtmlBuilder builder = new HtmlBuilder();
            builder.append(definitionBuilder.wrapWith(DocumentationMarkup.DEFINITION_ELEMENT));
            builder.append(contentBuilder.wrapWith(DocumentationMarkup.CONTENT_ELEMENT));

            if (LOG.isDebugEnabled()) {
                LOG.debug(builder.toString());
            }

            return builder.toString();
        }
        return text;
    }


    static @NotNull String escapeCodeForHtml(@Nullable PsiElement code) {
        if (code == null) {
            return "";
        }

        return escapeCodeForHtml(code.getText());
    }

    @Nullable
    public static String escapeCodeForHtml(@Nullable String code) {
        return code == null ? null : code.
                replaceAll("<", "&lt;").
                replaceAll(">", "&gt;");
    }
}
