package com.dune.lang.parser;

import com.dune.lang.core.psi.impl.DuneTypes;
import com.intellij.lang.ASTNode;
import com.intellij.lang.LightPsiParser;
import com.intellij.lang.PsiBuilder;
import com.intellij.lang.PsiParser;
import com.intellij.psi.tree.IElementType;
import org.jetbrains.annotations.NotNull;

import static com.intellij.lang.parser.GeneratedParserUtilBase.*;

public class DuneParser implements PsiParser, LightPsiParser {

    @Override public void parseLight(IElementType root, PsiBuilder builder) {
        builder = adapt_builder_(root, builder, this, null);
        PsiBuilder.Marker m = enter_section_(builder, 0, _COLLAPSE_, null);

        ParserScope fileScope = ParserScope.markRoot(builder);

        ParserState state = new ParserState(builder, fileScope);
        parseFile(builder, state);

        // if we have a scope at last position in a file, without SEMI, we need to handle it here
        if (!state.empty()) {
            state.clear();
        }

        fileScope.end();

        exit_section_(builder, 0, m, root, true, true, TRUE_CONDITION);
    }

    @Override public @NotNull ASTNode parse(@NotNull IElementType root, @NotNull PsiBuilder builder) {
        builder.setDebugMode(false);

        ASTNode treeBuilt;
        parseLight(root, builder);
        treeBuilt = builder.getTreeBuilt();

        return treeBuilt;
    }

    private void parseFile(@NotNull PsiBuilder builder, @NotNull ParserState state) {
        IElementType tokenType = null;

        while (true) {
            state.previousElementType1 = tokenType;
            tokenType = builder.getTokenType();
            if (tokenType == null) {
                break;
            }

            if (tokenType == DuneTypes.ATOM) {
                parseAtom(state);
            }
            // ( ... )
            else if (tokenType == DuneTypes.LPAREN) {
                parseLParen(state);
            } else if (tokenType == DuneTypes.RPAREN) {
                parseRParen(state);
            }
            // %{ ... }
            else if (tokenType == DuneTypes.VAR_START) {
                parseVarStart(state);
            } else if (tokenType == DuneTypes.VAR_END) {
                parseVarEnd(state);
            }

            if (state.dontMove) {
                state.dontMove = false;
            } else {
                builder.advanceLexer();
            }
        }
    }

    private void parseAtom(@NotNull ParserState state) {
        if (state.is(DuneTypes.STANZA)) {
            state.advance().mark(DuneTypes.FIELDS);
        }
    }

    private void parseLParen(@NotNull ParserState state) {
        if (state.isRoot()) {
            state.markScope(DuneTypes.STANZA, DuneTypes.LPAREN);
        } else if (state.is(DuneTypes.FIELDS)) {
            state.markScope(DuneTypes.FIELD, DuneTypes.LPAREN);
        } else {
            state.markScope(DuneTypes.S_EXPR, DuneTypes.LPAREN);
        }
    }

    private void parseRParen(@NotNull ParserState state) {
        if (state.is(DuneTypes.FIELDS)) {
            state.popEnd();
        }

        if (state.hasScopeToken()) {
            state.advance().popEnd();
        } else {
            state.error("Unbalanced parenthesis");
        }
    }

    private void parseVarStart(@NotNull ParserState state) {
        // |>%{<| ... }
        state.markScope(DuneTypes.VAR, DuneTypes.VAR_START);
    }

    private void parseVarEnd(@NotNull ParserState state) {
        if (state.is(DuneTypes.VAR)) {
            // %{ ... |>}<|
            state.advance().popEnd();
        }
    }
}
