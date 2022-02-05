package com.or.lang.utils;

import com.intellij.lang.PsiBuilder;
import com.intellij.psi.tree.IElementType;
import com.intellij.util.ArrayUtil;
import com.or.lang.OCamlTypes;
import com.or.lang.core.type.ORCompositeType;
import com.or.lang.core.type.ORTokenElementType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.LinkedList;

public class ParserState {

    public final PsiBuilder m_builder;
    private final LinkedList<ParserScope> m_composites = new LinkedList<>();
    private final ParserScope m_rootComposite;
    @Nullable
    public IElementType previousElementType2;
    @Nullable
    public IElementType previousElementType1;
    public PsiBuilder.Marker m_latestMark;
    public boolean dontMove = false;
    private ParserScope m_currentScope;

    public ParserState(PsiBuilder builder, ParserScope rootCompositeMarker) {
        m_builder = builder;
        m_rootComposite = rootCompositeMarker;
        m_currentScope = rootCompositeMarker;
    }

    public void popEndUntilStart() {
        ParserScope latestKnownScope;

        if (!m_composites.isEmpty()) {
            latestKnownScope = m_composites.peek();
            ParserScope scope = latestKnownScope;
            while (scope != null && !scope.isStart()) {
                scope = pop();
                if (scope != null) {
                    if (scope.isEmpty()) {
                        scope.drop();
                    } else {
                        scope.end();
                    }
                }
                scope = getLatestScope();
            }
        }
    }

    public void popEndUntilScope() {
        ParserScope latestKnownScope;

        if (!m_composites.isEmpty()) {
            latestKnownScope = m_composites.peek();
            ParserScope marker = latestKnownScope;
            while (marker != null && !marker.hasScope()) {
                marker = pop();
                if (marker != null) {
                    if (marker.isEmpty()) {
                        marker.drop();
                    } else {
                        marker.end();
                    }
                }
                marker = getLatestScope();
            }
        }

    }

    @Nullable
    public ParserScope popEndUntilScopeToken(@NotNull ORTokenElementType scopeElementType) {
        ParserScope scope = null;

        if (!m_composites.isEmpty()) {
            scope = m_composites.peek();
            while (scope != null && scope.getScopeTokenElementType() != scopeElementType) {
                popEnd();
                scope = getLatestScope();
            }
        }

        return scope;
    }

    public @Nullable ParserScope getLatestScope() {
        return m_composites.isEmpty() ? null : m_composites.peek();
    }

    public boolean is(ORCompositeType composite) {
        return m_currentScope.isCompositeEqualTo(composite);
    }

    public boolean isPrevious(ORCompositeType composite) {
        if (m_composites.size() >= 2) {
            return m_composites.get(1).isCompositeType(composite);
        }
        return false;
    }

    public boolean in(ORCompositeType composite) {
        for (ParserScope scope : m_composites) {
            if (scope.isCompositeEqualTo(composite)) {
                return true;
            }
        }
        return false;
    }

    public boolean isOneOf(ORCompositeType @NotNull ... composites) {
        for (ORCompositeType composite : composites) {
            if (m_currentScope.isCompositeType(composite)) {
                return true;
            }
        }
        return false;
    }

    public boolean isCurrentResolution(ParserScopeEnum scope) {
        return m_currentScope.isResolution(scope);
    }

    public boolean isPreviousResolution(ParserScopeEnum scope) {
        if (m_composites.size() >= 2) {
            return m_composites.get(1).isResolution(scope);
        }
        return false;
    }

    public boolean isGrandPreviousResolution(ParserScopeEnum scope) {
        if (m_composites.size() >= 3) {
            return m_composites.get(2).isResolution(scope);
        }
        return false;
    }

    public boolean isGrandParent(ORCompositeType composite) {
        if (m_composites.size() >= 3) {
            return m_composites.get(2).isCompositeType(composite);
        }
        return false;
    }

    @NotNull
    public ParserState complete() {
        m_currentScope.complete();
        return this;
    }

    public void optional() {
        m_currentScope.optional();
    }

    public void add(@NotNull ParserScope scope) {
        m_composites.push(scope);
        m_currentScope = scope;
    }

    public @NotNull ParserState markDummy() {
        mark(OCamlTypes.C_DUMMY).dummy();
        return this;
    }

    public @NotNull ParserState mark(@NotNull ORCompositeType composite) {
        ParserScope scope = ParserScope.mark(m_builder, composite);
        add(scope);
        m_latestMark = scope.m_mark;
        return this;
    }

    public @NotNull ParserState markScope(@NotNull ORCompositeType composite, @NotNull ORTokenElementType scope) {
        ParserState state = mark(composite);
        state.updateScopeToken(scope);
        return this;
    }

    public @NotNull ParserState markOptional(@NotNull ORCompositeType composite) {
        ParserState state = mark(composite);
        state.optional();
        return this;
    }

    public void precedeScope(@NotNull ORCompositeType composite) {
        ParserScope latest = pop();
        if (latest != null) {
            add(ParserScope.precedeScope(latest, composite));
            add(latest);
        }
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

    public @NotNull ParserState popEnd() {
        ParserScope scope = pop();
        if (scope != null) {
            scope.end();
        }
        return this;
    }

    public @NotNull ParserState popCancel() {
        ParserScope scope = pop();
        if (scope != null) {
            scope.drop();
        }
        return this;
    }

    @Nullable
    public ParserScope popEndUntilOneOfElementType(@NotNull ORTokenElementType... scopeElementTypes) {
        ParserScope scope = null;

        if (!m_composites.isEmpty()) {
            scope = m_composites.peek();
            while (scope != null
                    && !ArrayUtil.contains(scope.getScopeTokenElementType(), scopeElementTypes)) {
                popEnd();
                scope = getLatestScope();
            }
        }

        return scope;
    }

    public void popEndUntilOneOfResolution(@NotNull ParserScopeEnum... resolutions) {
        if (!m_composites.isEmpty()) {
            ParserScope scope = m_composites.peek();
            while (scope != null && !ArrayUtil.contains(scope.getResolution(), resolutions)) {
                popEnd();
                scope = getLatestScope();
            }
        }
    }

    @NotNull
    public ParserState popEndUntil(@NotNull ORCompositeType composite) {
        if (!m_composites.isEmpty()) {
            ParserScope scope = m_composites.peek();
            while (scope != null && !scope.isCompositeType(composite)) {
                popEnd();
                scope = getLatestScope();
            }
        }

        return this;
    }

    @NotNull
    public ParserState resolution(@Nullable ParserScopeEnum resolution) {
        m_currentScope.resolution(resolution);
        return this;
    }

    public boolean hasScopeToken() {
        return m_currentScope.hasScope();
    }

    public @NotNull ParserState updateCurrentCompositeElementType(@NotNull ORCompositeType compositeElementType) {
        m_currentScope.updateCompositeElementType(compositeElementType);
        return this;
    }

    @NotNull
    public ParserState advance() {
        previousElementType2 = previousElementType1;
        previousElementType1 = m_builder.getTokenType();
        m_builder.advanceLexer();
        dontMove = true;
        return this;
    }

    public void updateScopeToken(@Nullable ORTokenElementType token) {
        if (token != null) {
            m_currentScope.setScopeTokenType(token);
        }
    }

    public void wrapWith(@NotNull ORCompositeType compositeType) {
        if (compositeType instanceof IElementType) {
            IElementType elementType = (IElementType) compositeType;
            m_latestMark = m_builder.mark();
            advance();
            m_latestMark.done(elementType);
        }
    }

    public void setStart() {
        m_currentScope.setIsStart(true);
    }

    public @NotNull ParserState remapCurrentToken(ORTokenElementType elementType) {
        m_builder.remapCurrentToken(elementType);
        return this;
    }

    public @Nullable String getTokenText() {
        return m_builder.getTokenText();
    }

    public @Nullable IElementType rawLookup(int steps) {
        return m_builder.rawLookup(steps);
    }

    public @Nullable IElementType lookAhead(int steps) {
        return m_builder.lookAhead(steps);
    }

    public @Nullable IElementType getTokenType() {
        return m_builder.getTokenType();
    }

    public @NotNull ParserState dummy() {
        m_currentScope.dummy();
        return this;
    }

    public @Nullable ParserScope findScopeContext() {
        int level = 1;
        ParserScope parserScope = m_composites.size() < 2 ? null : m_composites.get(level);
        while (parserScope != null && parserScope.isCompositeType(OCamlTypes.C_DUMMY)) {
            level++;
            parserScope = m_composites.get(level);
        }

        return parserScope;
    }

    public boolean isRoot() {
        return m_currentScope == m_rootComposite;
    }

    public void markOptionalParenDummyScope(@NotNull ORCompositeType scopeType) {
        if (getTokenType() == OCamlTypes.LPAREN) {
            markScope(scopeType, OCamlTypes.LPAREN).dummy().advance();
        }
    }

    public void markOptionalParenDummyScope() {
        markOptionalParenDummyScope(OCamlTypes.C_DUMMY);
    }

    public boolean isDummy() {
        return m_currentScope.isDummy();
    }

    @NotNull public ParserState popDummy() {
        if (isDummy()) {
            popEnd();
        }
        return this;
    }

}
