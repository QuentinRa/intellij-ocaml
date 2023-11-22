package com.dune.lang.parser;

import com.dune.lang.core.psi.DuneElementType;
import com.intellij.lang.PsiBuilder;
import com.intellij.psi.tree.IElementType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.LinkedList;

public class ParserState {

    public final PsiBuilder m_builder;
    private final LinkedList<ParserScope> m_composites = new LinkedList<>();
    private final ParserScope m_rootComposite;
    @Nullable public IElementType previousElementType2;
    @Nullable public IElementType previousElementType1;
    public PsiBuilder.Marker m_latestMark;
    public boolean dontMove = false;
    private ParserScope m_currentScope;

    public ParserState(PsiBuilder builder, ParserScope rootCompositeMarker) {
        m_builder = builder;
        m_rootComposite = rootCompositeMarker;
        m_currentScope = rootCompositeMarker;
    }

    public boolean is(DuneElementType composite) {
        return m_currentScope.isCompositeEqualTo(composite);
    }

    public boolean in(DuneElementType composite) {
        for (ParserScope scope : m_composites) {
            if (scope.isCompositeEqualTo(composite)) {
                return true;
            }
        }
        return false;
    }

    public void add(@NotNull ParserScope scope) {
        m_composites.push(scope);
        m_currentScope = scope;
    }

    public @NotNull ParserState mark(@NotNull DuneElementType composite) {
        ParserScope scope = ParserScope.mark(m_builder, composite);
        add(scope);
        m_latestMark = scope.m_mark;
        return this;
    }

    public void markScope(@NotNull DuneElementType composite, @NotNull IElementType scope) {
        ParserState state = mark(composite);
        state.updateScopeToken(scope);
    }

    boolean empty() {
        return m_composites.isEmpty();
    }

    public @Nullable ParserScope tryPop(@NotNull LinkedList<ParserScope> scopes) {
        return empty() ? null : scopes.pop();
    }

    void clear() {
        ParserScope scope = tryPop(m_composites);
        while (scope != null) {
            scope.end();
            scope = tryPop(m_composites);
        }
        m_currentScope = m_rootComposite;
    }

    private void updateCurrentScope() {
        m_currentScope = m_composites.isEmpty() ? m_rootComposite : m_composites.peek();
    }

    @Nullable
    public ParserScope pop() {
        ParserScope scope = tryPop(m_composites);
        updateCurrentScope();
        return scope;
    }

    public void popEnd() {
        ParserScope scope = pop();
        if (scope != null) {
            scope.end();
        }
    }

    public boolean hasScopeToken() {
        return m_currentScope.hasScope();
    }

    @NotNull
    public ParserState advance() {
        previousElementType2 = previousElementType1;
        previousElementType1 = m_builder.getTokenType();
        m_builder.advanceLexer();
        dontMove = true;
        return this;
    }

    public void updateScopeToken(@Nullable IElementType token) {
        if (token != null) {
            m_currentScope.setScopeTokenType(token);
        }
    }

    @NotNull
    public ParserState setStart(boolean isStart) {
        m_currentScope.setIsStart(isStart);
        return this;
    }

    public void error(@NotNull String message) {
        m_builder.error(message);
    }

    public boolean isRoot() {
        return m_currentScope == m_rootComposite;
    }

}
