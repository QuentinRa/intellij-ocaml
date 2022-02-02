package com.ocaml.sdk.output;

import com.intellij.build.FilePosition;
import com.ocaml.sdk.SinceOCamlVersion;
import org.jetbrains.annotations.NotNull;

public class CompilerOutputMessage {
    /** position in the file **/
    public FilePosition filePosition;

    /** kind of message **/
    public enum Kind { ERROR, WARNING, ALERT, INFO }
    public @NotNull Kind kind = Kind.ERROR;

    /** The header of the message shown by the compiler.  **/
    public String header() {
        String header = content.substring(0, content.indexOf(':') + 2); // ": "
        int end = header.indexOf('.');
        header = header.substring(0, end == -1 ? header.length() : end);
        return header;
    }

    /**
     * The content of the message.
     * The header is included inside the content. This time, the header
     * is properly capitalized, end ending with a dot.
     */
    public String content;

    /** Code sample of the location of the error, with
     * a caret pointing the error **/
    @SinceOCamlVersion(since = "4.08.0")
    public String context = "";

    // utils

    @Override public String toString() {
        return "CompilerOutputMessage{" +
                "file=" + filePosition.getFile().getName() +
                ", at=(l:"+filePosition.getStartLine()+"-"+filePosition.getEndLine()
                +",c:" +filePosition.getStartColumn()+"-"+filePosition.getEndColumn() + ")"+
                ", kind=" + kind +
                ", header='" + header() + '\'' +
                '}';
    }
}
