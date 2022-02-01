package com.or.lang.parser;

import com.intellij.lang.ASTNode;
import com.intellij.lang.ParserDefinition;
import com.intellij.lang.PsiParser;
import com.intellij.lexer.Lexer;
import com.intellij.openapi.project.Project;
import com.intellij.psi.FileViewProvider;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.TokenType;
import com.intellij.psi.tree.IElementType;
import com.intellij.psi.tree.IFileElementType;
import com.intellij.psi.tree.TokenSet;
import com.ocaml.ide.files.OCamlInterfaceFileType;
import com.ocaml.lang.lexer.OCamlLexerAdapter;
import com.or.ide.files.OclFile;
import com.or.ide.files.OclInterfaceFile;
import com.or.lang.OCamlTypes;
import com.or.lang.core.stub.type.ORStubElementType;
import com.or.lang.core.stub.type.OclFileStubElementType;
import org.jetbrains.annotations.NotNull;

public class OclParserDefinition implements ParserDefinition {
    private static final TokenSet WHITE_SPACES = TokenSet.create(TokenType.WHITE_SPACE);
    private static final TokenSet COMMENTS = TokenSet.create(OCamlTypes.MULTI_COMMENT);
    private static final TokenSet STRINGS = TokenSet.create(OCamlTypes.STRING_VALUE);

    @NotNull
    @Override
    public Lexer createLexer(Project project) {
        return new OCamlLexerAdapter();
    }

    @NotNull
    public TokenSet getWhitespaceTokens() {
        return WHITE_SPACES;
    }

    @NotNull
    public TokenSet getCommentTokens() {
        return COMMENTS;
    }

    @NotNull
    public TokenSet getStringLiteralElements() {
        return STRINGS;
    }

    @NotNull
    public PsiParser createParser(final Project project) {
        return new OclParser();
    }

    @NotNull
    @Override
    public IFileElementType getFileNodeType() {
        return OclFileStubElementType.INSTANCE;
    }

    @NotNull
    public PsiFile createFile(@NotNull FileViewProvider viewProvider) {
        return viewProvider.getFileType() instanceof OCamlInterfaceFileType
                ? new OclInterfaceFile(viewProvider)
                : new OclFile(viewProvider);
    }

    @NotNull
    public SpaceRequirements spaceExistenceTypeBetweenTokens(ASTNode left, ASTNode right) {
        return SpaceRequirements.MAY;
    }

    @NotNull
    public PsiElement createElement(@NotNull ASTNode node) {
        IElementType type = node.getElementType();
        if (type instanceof ORStubElementType) {
            //noinspection rawtypes
            return ((ORStubElementType) type).createPsi(node);
        }

        throw new IllegalArgumentException("Not an OCaml stub node: " + node + " (" + type + ", " + type.getLanguage() + "): " + node.getText());
    }
}
