package com.dune.lang.parser;

import com.dune.lang.core.psi.DuneElementType;
import com.intellij.lang.PsiBuilder;
import com.intellij.psi.tree.IElementType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ParserScope {

    @NotNull private final PsiBuilder m_builder;
    private final int m_offset;

    private final DuneElementType m_compositeElementType;
    public PsiBuilder.Marker m_mark;
    private IElementType m_scopeTokenElementType;
    private boolean m_isComplete = true;
    private boolean m_isDummy = false; // Always drop
    private boolean m_isStart = false;

    private ParserScope(@NotNull PsiBuilder builder, @NotNull PsiBuilder.Marker mark, @Nullable DuneElementType compositeElementType, @Nullable IElementType scopeTokenElementType) {
        m_builder = builder;
        m_mark = mark;
        m_offset = builder.getCurrentOffset();
        m_compositeElementType = compositeElementType;
        m_scopeTokenElementType = scopeTokenElementType;
    }

    public static @NotNull ParserScope mark(@NotNull PsiBuilder builder, @NotNull DuneElementType compositeElementType) {
        return new ParserScope(builder, builder.mark(), compositeElementType, null);
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

    public boolean isOptional() {
        return !m_isComplete;
    }

    public void dummy() {
        m_isComplete = false;
        m_isDummy = true;
    }

    boolean isCompositeEqualTo(DuneElementType compositeType) {
        return m_compositeElementType == compositeType;
    }

    void setScopeTokenType(@NotNull IElementType tokenElementType) {
        m_scopeTokenElementType = tokenElementType;
    }

    public boolean isStart() {
        return m_isStart;
    }

    public void setIsStart(boolean isStart) {
        m_isStart = isStart;
    }

    public boolean hasScope() {
        return m_scopeTokenElementType != null;
    }

    public boolean isDummy() {
        return m_isDummy;
    }
}
