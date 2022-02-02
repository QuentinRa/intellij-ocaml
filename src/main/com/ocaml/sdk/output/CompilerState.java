package com.ocaml.sdk.output;

public final class CompilerState {
    public int startLine, endLine;
    public int startColumn = -1, endColumn = -1;
    public String filePath;
    public String messageRaw = "";
    public String context = "";
    public CompilerOutputMessage.Kind kind;

    public CompilerState(String filePath) {
        this.filePath = filePath;
    }

    @Override public String toString() {
        return "CompilerState{" +
                "startLine=" + startLine +
                ", endLine=" + endLine +
                ", startColumn=" + startColumn +
                ", endColumn=" + endColumn +
                ", filePath='" + filePath + '\'' +
                ", messageRaw='" + messageRaw + '\'' +
                ", context='" + context + '\'' +
                ", kind=" + kind +
                '}';
    }
}
