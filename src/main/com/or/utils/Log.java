package com.or.utils;

import com.intellij.openapi.diagnostic.Logger;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiNamedElement;
import com.intellij.psi.PsiQualifiedNamedElement;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;

public class Log {

    private static final String SEP = ": ";
    @NotNull
    private final Logger m_log;

    private Log(String name) {
        m_log = Logger.getInstance("ReasonML." + name);
    }

    @NotNull
    public static Log create(String name) {
        return new Log(name);
    }

    public boolean isDebugEnabled() {
        return m_log.isDebugEnabled();
    }

    public boolean isTraceEnabled() {
        return m_log.isTraceEnabled();
    }

    public void debug(String comment) {
        if (m_log.isDebugEnabled()) {
            m_log.debug(comment);
        }
    }

    public void debug(String comment, String t) {
        if (m_log.isDebugEnabled()) {
            m_log.debug(comment + SEP + t);
        }
    }

    public void debug(String comment, @Nullable Collection<?> t) {
        if (m_log.isDebugEnabled()) {
            m_log.debug(
                    comment + SEP + (t == null ? "" : t.size() + " ") + "[" + Joiner.join(", ", t) + "]");
        }
    }

    public void trace(String comment, @Nullable Collection<?> t) {
        if (m_log.isDebugEnabled()) {
            m_log.debug(
                    comment + SEP + (t == null ? "" : t.size() + " ") + "[" + Joiner.join(", ", t) + "]");
        }
    }

    public void debug(String comment, @NotNull PsiQualifiedNamedElement element) {
        if (m_log.isDebugEnabled()) {
            m_log.debug(
                    comment
                            + SEP
                            + element.getQualifiedName()
                            + " ("
                            + element.getContainingFile().getVirtualFile().getPath()
                            + ")");
        }
    }

    public void debug(String msg, PsiElement element) {
        if (m_log.isDebugEnabled()) {
            m_log.debug(msg + SEP + element + (element instanceof PsiNamedElement ? ", name=[" + ((PsiNamedElement) element).getName() + "]" : ""));
        }
    }

    public void info(String msg) {
        m_log.info(msg);
    }

    public void warn(String msg) {
        m_log.warn(msg);
    }

    public void trace(String msg) {
        if (m_log.isTraceEnabled()) {
            m_log.trace(msg);
        }
    }

    public void trace(String msg, String t) {
        if (m_log.isTraceEnabled()) {
            m_log.trace(msg + SEP + " " + t);
        }
    }

    public void trace(@NotNull String msg, @NotNull PsiElement element) {
        if (m_log.isTraceEnabled()) {
            m_log.trace(msg + SEP + element + (element instanceof PsiNamedElement ? ", name=[" + ((PsiNamedElement) element).getName() + "]" : ""));
        }
    }

    public void error(String message, Exception e) {
        m_log.error(message, e);
    }

}
