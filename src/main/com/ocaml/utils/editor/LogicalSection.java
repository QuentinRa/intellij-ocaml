package com.ocaml.utils.editor;

import com.intellij.build.FilePosition;
import com.intellij.openapi.editor.LogicalPosition;
import org.jetbrains.annotations.NotNull;

/**
 * I had a lot of duplicates with LogicalPositions as some sections started
 * at the same position but weren't ending at the same position. This class
 * is the same almost the same as FilePosition but without the file.
 */
public class LogicalSection implements Comparable<LogicalSection> {

    public final int startLine;
    public final int startColumn;
    public final int endLine;
    public final int endColumn;

    public LogicalSection(int startLine, int startColumn, int endLine, int endColumn) {
        this.startLine = startLine;
        this.startColumn = startColumn;
        this.endLine = endLine;
        this.endColumn = endColumn;
    }

    public LogicalSection(@NotNull FilePosition position) {
        this(position.getStartLine(), position.getStartColumn(),
                position.getEndLine(), position.getEndColumn());
    }

    public LogicalSection(@NotNull LogicalPosition start, @NotNull LogicalPosition end) {
        this(start.line + 1, start.column, end.line + 1, end.column);
    }

    @Override public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof LogicalSection)) return false;
        LogicalSection that = (LogicalSection) o;
        if (startLine != that.startLine) return false;
        if (startColumn != that.startColumn) return false;
        if (endLine != that.endLine) return false;
        return endColumn == that.endColumn;
    }

    @Override public int hashCode() {
        int result = startLine;
        result = 31 * result + startColumn;
        result = 31 * result + endLine;
        result = 31 * result + endColumn;
        return result;
    }

    @Override public int compareTo(@NotNull LogicalSection o) {
        if (startLine != o.startLine) return startLine - o.startLine;
        if (endLine != o.endLine) return endLine - o.endLine;
        if (startColumn != o.startColumn) return startColumn - o.startColumn;
        return endColumn - o.endColumn;
    }

    @Override public String toString() {
        return "LogicalSection{" +
                "startLine=" + startLine +
                ", startColumn=" + startColumn +
                ", endLine=" + endLine +
                ", endColumn=" + endColumn +
                '}';
    }
}
