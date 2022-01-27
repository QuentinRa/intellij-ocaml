package com.ocaml.sdk.output;

import com.intellij.build.FilePosition;
import com.ocaml.sdk.SinceOCamlVersion;
import org.jetbrains.annotations.NotNull;

public class CompilerOutputMessage {
    /** position in the file **/
    public FilePosition filePosition;

    /** kind of message **/
    enum Kind { ERROR, WARNING, ALERT, INFO }
    public @NotNull Kind kind = Kind.ERROR;

    /** We are storing the header of the message shown by the compiler.
     * It's usually on the same line as the warning, and sometimes, it may be on multiple lines.
     * The header is always ending with a ".", unless it's an error.
     * This field is the header, without the final ".". The first letter may be a lower case. **/
    public String header;

    /**
     * The content of the message.
     * The header is included inside the content.
     */
    public String content;

    /** Code sample of the location of the error, with
     * a caret pointing the error **/
    @SinceOCamlVersion(since = "4.08.0")
    public String context = "";

    // utils
    public boolean isError() { return kind == Kind.ERROR; }
    public boolean isWarning() { return kind == Kind.WARNING; }
    public boolean isAlert() { return kind == Kind.ALERT; }
}
