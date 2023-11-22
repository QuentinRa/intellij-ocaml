package com.odoc.lang;

import com.intellij.codeInsight.documentation.DocumentationManagerProtocol;
import com.intellij.codeInsight.documentation.DocumentationManagerUtil;
import com.intellij.lang.documentation.DocumentationMarkup;
import com.intellij.lexer.FlexLexer;
import com.intellij.openapi.util.text.HtmlBuilder;
import com.intellij.openapi.util.text.HtmlChunk;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.psi.TokenType;
import com.intellij.psi.tree.IElementType;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @see DocumentationManagerUtil
 * @see DocumentationMarkup
 * @see DocumentationManagerProtocol
 */
abstract class ORDocConverter {
    protected static final HtmlChunk SPACE_CHUNK = HtmlChunk.text(" ");

    protected static @NotNull List<HtmlChunk> trimEndChildren(@NotNull List<HtmlChunk> children) {
        if (!children.isEmpty()) {
            int lastIndex = children.size() - 1;
            HtmlChunk lastChunk = children.get(lastIndex);
            if (lastChunk == SPACE_CHUNK) {
                children.remove(lastIndex);
                return trimEndChildren(children);
            }
        }
        return children;
    }

    public abstract @NotNull HtmlBuilder convert(@NotNull String text);

    protected IElementType skipWhiteSpace(@NotNull FlexLexer lexer) throws IOException {
        IElementType elementType = lexer.advance();
        while (elementType != null && elementType == TokenType.WHITE_SPACE) {
            elementType = lexer.advance();
        }
        return elementType;
    }

    @NotNull
    protected String extractRaw(int startOffset, int endOffset, @NotNull CharSequence text) {
        return ((String) text).substring(startOffset, text.length() - endOffset);
    }

    @NotNull
    protected String extract(int startOffset, int endOffset, @NotNull CharSequence text) {
        return ((String) text).substring(startOffset, text.length() - endOffset).trim();
    }

    public static class ORDocHtmlBuilder {
        public final HtmlBuilder myBuilder = new HtmlBuilder();
        public final List<HtmlChunk> myChildren = new ArrayList<>();

        public void appendChildren(boolean wrap) {
            if (!myChildren.isEmpty()) {
                trimEndChildren(myChildren);
                if (!myChildren.isEmpty()) {
                    if (wrap) {
                        myBuilder.append(HtmlChunk.p().children(myChildren));
                    } else {
                        for (HtmlChunk chunk : myChildren) {
                            myBuilder.append(chunk);
                        }
                    }
                    myChildren.clear();
                }
            }
        }

        public void addSpace() {
            if (!myChildren.isEmpty()) {
                int lastIndex = myChildren.size() - 1;
                HtmlChunk lastChunk = myChildren.get(lastIndex);
                if (lastChunk != SPACE_CHUNK) {
                    myChildren.add(SPACE_CHUNK);
                }
            }
        }

        public void addChild(HtmlChunk.Element element) {
            myChildren.add(element);
        }
    }

    public static class ORDocSectionsBuilder extends ORDocHtmlBuilder {
        public String myTag = "";
        public HtmlChunk.Element myHeaderCell = null;

        public void addHeaderCell(@NotNull String tag) {
            myHeaderCell = DocumentationMarkup.SECTION_HEADER_CELL.child(HtmlChunk.text(StringUtil.toTitleCase(tag) + ":").wrapWith("p"));
            myChildren.add(HtmlChunk.raw("<p>"));
            myTag = tag;
        }

        public void addSection() {
            HtmlChunk contentCell = DocumentationMarkup.SECTION_CONTENT_CELL.children(trimEndChildren(myChildren));
            myBuilder.append(HtmlChunk.tag("tr").children(myHeaderCell, contentCell));
            myHeaderCell = null;
            myTag = "";
            myChildren.clear();
        }
    }

}
