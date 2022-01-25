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

    /** the header of the message. It's usually on the same
     * line as the warning, but sometimes, it may be on multiple lines.
     * The header is always ending with a "." **/
    public String header;

    /** Code sample of the location of the error, with
     * a caret pointing the error **/
    @SinceOCamlVersion(since = "4.08.0")
    public String context = "";
}
