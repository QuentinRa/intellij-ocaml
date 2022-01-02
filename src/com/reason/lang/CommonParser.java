package com.reason.lang;

import com.intellij.lang.*;
import com.intellij.psi.tree.*;
import com.reason.lang.core.type.*;
import org.jetbrains.annotations.*;

import static com.intellij.lang.parser.GeneratedParserUtilBase.*;
import static com.reason.lang.ParserScopeEnum.*;

public abstract class CommonParser<T> implements PsiParser, LightPsiParser {

    protected final T m_types;

    protected CommonParser(T types) {
        m_types = types;
    }

    @Override
    @NotNull
    public ASTNode parse(@NotNull IElementType elementType, @NotNull PsiBuilder builder) {
        builder.setDebugMode(false);

        //long start = System.currentTimeMillis();

        ASTNode treeBuilt;

        //try {
            parseLight(elementType, builder);
            treeBuilt = builder.getTreeBuilt();
        //}
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

        if (m_types instanceof ORTypes) {
            state.mark(((ORTypes) m_types).C_FAKE_MODULE).popEnd();
        }

        exit_section_(builder, 0, m, elementType, true, true, TRUE_CONDITION);
    }

    protected abstract void parseFile(PsiBuilder builder, ParserState parserState);
}
