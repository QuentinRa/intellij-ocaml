package com.or.lang.utils;

import com.intellij.lang.PsiBuilder;
import com.intellij.psi.tree.IElementType;
import com.or.lang.core.type.ORCompositeType;
import com.or.lang.core.type.ORTokenElementType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ParserScope {

    @NotNull private final PsiBuilder m_builder;
    private final int m_offset;
    public PsiBuilder.Marker m_mark;
    private ParserScopeEnum m_resolution;
    private ORCompositeType m_compositeElementType;
    private ORTokenElementType m_scopeTokenElementType;
    private boolean m_isComplete = true;
    private boolean m_isDummy = false; // Always drop
    private boolean m_isStart = false;

    private ParserScope(@NotNull PsiBuilder builder, @NotNull PsiBuilder.Marker mark, @Nullable ORCompositeType compositeElementType, @Nullable ORTokenElementType scopeTokenElementType) {
        m_builder = builder;
        m_mark = mark;
        m_offset = builder.getCurrentOffset();
        m_compositeElementType = compositeElementType;
        m_scopeTokenElementType = scopeTokenElementType;
    }

    public static @NotNull ParserScope mark(@NotNull PsiBuilder builder, @NotNull ORCompositeType compositeElementType) {
        return new ParserScope(builder, builder.mark(), compositeElementType, null);
    }

    public static @NotNull ParserScope precedeScope(@NotNull ParserScope scope, @NotNull ORCompositeType compositeType) {
        PsiBuilder.Marker precede = scope.m_mark.precede();
        return new ParserScope(scope.m_builder, precede, compositeType, null);
    }

    static @NotNull ParserScope markRoot(@NotNull PsiBuilder builder) {
        return new ParserScope(builder, builder.mark(), null, null);
    }

    public int getLength() {
        return m_builder.getCurrentOffset() - m_offset;
    }

    public boolean isEmpty() {
        return getLength() == 0;
    }

    public void end() {
        if (m_isDummy) {
            drop();
        } else if (m_isComplete) {
            done();
        } else {
            drop();
        }
    }

    private void done() {
        if (m_mark != null) {
            if (m_compositeElementType instanceof IElementType) {
                m_mark.done((IElementType) m_compositeElementType);
            } else {
                m_mark.drop();
            }
            m_mark = null;
        }
    }

    void drop() {
        if (m_mark != null) {
            m_mark.drop();
            m_mark = null;
        }
    }

    public void complete() {
        m_isComplete = true;
    }

    public void optional() {
        m_isComplete = false;
    }

    public void dummy() {
        m_isComplete = false;
        m_isDummy = true;
    }

    public boolean isResolution(ParserScopeEnum resolution) {
        return m_resolution == resolution;
    }

    public void resolution(@Nullable ParserScopeEnum resolution) {
        m_resolution = resolution;
    }

    boolean isCompositeEqualTo(ORCompositeType compositeType) {
        return m_compositeElementType == compositeType;
    }

    public boolean isScopeToken(ORTokenElementType tokenElementType) {
        return m_scopeTokenElementType == tokenElementType;
    }

    void setScopeTokenType(@NotNull ORTokenElementType tokenElementType) {
        m_scopeTokenElementType = tokenElementType;
    }

    public void updateCompositeElementType(@NotNull ORCompositeType compositeType) {
        m_compositeElementType = compositeType;
        m_isDummy = false;
    }

    public boolean isStart() {
        return m_isStart;
    }

    public void setIsStart(boolean isStart) {
        m_isStart = isStart;
    }

    public @Nullable ParserScopeEnum getResolution() {
        return m_resolution;
    }

    public boolean hasScope() {
        return m_scopeTokenElementType != null;
    }

    @Nullable ORTokenElementType getScopeTokenElementType() {
        return m_scopeTokenElementType;
    }

    public void rollbackTo() {
        if (m_mark != null) {
            m_mark.rollbackTo();
        }
    }

    public boolean isCompositeType(ORCompositeType elementType) {
        return m_compositeElementType == elementType;
    }

    public boolean isDummy() {
        return m_isDummy;
    }
}
