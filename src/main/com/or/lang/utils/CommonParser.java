package com.or.lang.utils;

import com.intellij.lang.ASTNode;
import com.intellij.lang.LightPsiParser;
import com.intellij.lang.PsiBuilder;
import com.intellij.lang.PsiParser;
import com.intellij.psi.tree.IElementType;
import com.or.lang.OCamlTypes;
import org.jetbrains.annotations.NotNull;

import static com.intellij.lang.parser.GeneratedParserUtilBase.*;

public abstract class CommonParser implements PsiParser, LightPsiParser {

    @Override
    @NotNull
    public ASTNode parse(@NotNull IElementType elementType, @NotNull PsiBuilder builder) {
        builder.setDebugMode(false);

        // System.out.println("start parsing ");
        // long start = System.currentTimeMillis();

        ASTNode treeBuilt;

        // try {
        parseLight(elementType, builder);
        treeBuilt = builder.getTreeBuilt();
        // }
        // finally {
        //    long end = System.currentTimeMillis();
        //    System.out.println("end parsing in " + (end - start) + "ms");
        // }

        return treeBuilt;
    }

    @Override
    public void parseLight(IElementType elementType, PsiBuilder builder) {
        builder = adapt_builder_(elementType, builder, this, null);
        PsiBuilder.Marker m = enter_section_(builder, 0, _COLLAPSE_, null);

        ParserScope fileScope = ParserScope.markRoot(builder);

        ParserState state = new ParserState(builder, fileScope);
        parseFile(builder, state);

        // if we have a scope at last position in a file, without SEMI, we need to handle it here
        if (!state.empty()) {
            state.clear();
        }

        fileScope.end();

        state.mark(OCamlTypes.C_FAKE_MODULE).popEnd();

        exit_section_(builder, 0, m, elementType, true, true, TRUE_CONDITION);
    }

    protected abstract void parseFile(PsiBuilder builder, ParserState parserState);
}
