package com.ocaml.lang;

import com.intellij.lang.*;
import com.intellij.lexer.*;
import com.intellij.openapi.project.*;
import com.intellij.psi.*;
import com.intellij.psi.tree.*;
import com.ocaml.lang.core.*;
import com.ocaml.lang.core.psi.*;
import com.ocaml.lang.ocaml.*;
import org.jetbrains.annotations.*;

public class OclParserDefinition implements ParserDefinition {
    public static final TokenSet WHITE_SPACES = TokenSet.create(TokenType.WHITE_SPACE);
    public static final TokenSet COMMENTS = TokenSet.create(OclTypes.MULTI_COMMENT);
    public static final IFileElementType FILE = new IFileElementType(OclLanguage.INSTANCE);

    // Tokens
    @NotNull @Override public PsiElement createElement(ASTNode node) {
        // return OclTypes.Factory.createElement(node);
        IElementType type = node.getElementType();
        throw new AssertionError("Unknown element type: " + type);
    }
    @NotNull @Override public TokenSet getWhitespaceTokens() {
        return WHITE_SPACES;
    }
    @NotNull @Override public TokenSet getCommentTokens() {
        return COMMENTS;
    }
    @Override public @NotNull IFileElementType getFileNodeType() {
        return FILE;
    }
    @NotNull @Override public TokenSet getStringLiteralElements() {
        return TokenSet.EMPTY;
    }

    // Implementation
    @Override public @NotNull Lexer createLexer(Project project) {
        return new OclLexerAdapter();
    }
    @NotNull @Override public PsiParser createParser(final Project project) {
        return new OclParser();
    }
    @Override public @NotNull PsiFile createFile(@NotNull FileViewProvider viewProvider) {
        return new OclFileType(viewProvider);
    }
    @Override public @NotNull SpaceRequirements spaceExistenceTypeBetweenTokens(ASTNode left,
                                                                                ASTNode right) {
        return SpaceRequirements.MAY;
    }
}
