package com.dune.ide.highlight;

import com.dune.lang.core.psi.impl.DuneTypes;
import com.dune.lang.lexer.DuneLexer;
import com.intellij.lexer.FlexAdapter;
import com.intellij.lexer.Lexer;
import com.intellij.openapi.editor.DefaultLanguageHighlighterColors;
import com.intellij.openapi.editor.HighlighterColors;
import com.intellij.openapi.editor.colors.TextAttributesKey;
import com.intellij.openapi.fileTypes.SyntaxHighlighterBase;
import com.intellij.psi.TokenType;
import com.intellij.psi.tree.IElementType;
import org.jetbrains.annotations.NotNull;

public class DuneSyntaxHighlighter extends SyntaxHighlighterBase {

    public static final TextAttributesKey PARENS_ = createTA("PAREN", DefaultLanguageHighlighterColors.PARENTHESES);
    public static final TextAttributesKey COMMENT_ = createTA("COMMENT", DefaultLanguageHighlighterColors.BLOCK_COMMENT);
    public static final TextAttributesKey STANZAS_ = createTA("STANZA", DefaultLanguageHighlighterColors.FUNCTION_DECLARATION);
    public static final TextAttributesKey FIELDS_ = createTA("FIELD", DefaultLanguageHighlighterColors.KEYWORD);
    public static final TextAttributesKey OPTIONS_ = createTA("OPTION", DefaultLanguageHighlighterColors.LABEL);
    public static final TextAttributesKey STRING_ = createTA("STRING", DefaultLanguageHighlighterColors.STRING);
    public static final TextAttributesKey ATOM_ = createTA("ATOM", DefaultLanguageHighlighterColors.CONSTANT);
    public static final TextAttributesKey VAR_ = createTA("VAR", DefaultLanguageHighlighterColors.LOCAL_VARIABLE);
    private static final TextAttributesKey BAD_CHAR_ = createTA("BAD_CHARACTER", HighlighterColors.BAD_CHARACTER);
    private static final TextAttributesKey[] COMMENT_KEYS = new TextAttributesKey[]{COMMENT_};
    private static final TextAttributesKey[] PAREN_KEYS = new TextAttributesKey[]{PARENS_};
    private static final TextAttributesKey[] STRING_KEYS = new TextAttributesKey[]{STRING_};
    private static final TextAttributesKey[] ATOM_KEYS = new TextAttributesKey[]{ATOM_};
    private static final TextAttributesKey[] BAD_CHAR_KEYS = new TextAttributesKey[]{BAD_CHAR_};
    private static final TextAttributesKey[] EMPTY_KEYS = new TextAttributesKey[0];

    /**
     * create Text attributes
     **/
    private static TextAttributesKey createTA(String name, TextAttributesKey fallbackAttributeKey) {
        return TextAttributesKey.createTextAttributesKey("DUNE_" + name, fallbackAttributeKey);
    }

    @Override public @NotNull Lexer getHighlightingLexer() {
        return new FlexAdapter(new DuneLexer(null));
    }

    @Override
    public @NotNull TextAttributesKey @NotNull [] getTokenHighlights(@NotNull IElementType tokenType) {
        if (tokenType.equals(DuneTypes.LPAREN) || tokenType.equals(DuneTypes.RPAREN)) {
            return PAREN_KEYS;
        } else if (tokenType.equals(DuneTypes.COMMENT)) {
            return COMMENT_KEYS;
        } else if (tokenType.equals(DuneTypes.STRING)) {
            return STRING_KEYS;
        } else if (tokenType.equals(DuneTypes.ATOM)) {
            return ATOM_KEYS;
        } else if (TokenType.BAD_CHARACTER.equals(tokenType)) {
            return BAD_CHAR_KEYS;
        }

        return EMPTY_KEYS;
    }
}
