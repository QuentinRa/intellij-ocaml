package com.ocaml.sdk.output;

import com.intellij.build.FilePosition;
import com.ocaml.utils.strings.StringsUtil;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Parse the compiler output,
 * it should work for any versions after 4.06.0.
 */
public abstract class CompilerOutputParser {

    private static final class CompilerState {
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
    /** if we are reading data, then this is not null. **/
    private @Nullable CompilerState currentState = null;

    public CompilerOutputParser() {}

    protected void messageReady() {
        if (currentState == null) return;

        CompilerOutputMessage message = new CompilerOutputMessage();
        // kind
        if (currentState.kind == null)
            throw new IllegalStateException("Current state invalid, no 'kind' for "+currentState);
        message.kind = currentState.kind;
        // header
        int i = currentState.messageRaw.indexOf('.');
        if (i == -1) i = currentState.messageRaw.length();
        // we may have \in inside this header
        message.header = currentState.messageRaw.substring(0, i); // with the '.'
        // the whole message
        message.content = StringsUtil.capitalize(currentState.messageRaw);
        // file position
        message.filePosition = new FilePosition(
                createFile(currentState.filePath),
                currentState.startLine,
                currentState.startColumn,
                currentState.endLine,
                currentState.endColumn
        );
        // context
        message.context = currentState.context;

        onMessageReady(message);

        currentState = null;
    }

    @Contract("_ -> new")
    protected @NotNull File createFile(String filePath) {
        return new File(filePath);
    }

    /**
     * Callback when a message was parsed
     * @param message parsed message
     */
    protected abstract void onMessageReady(@NotNull CompilerOutputMessage message);

    /**
     * DO NOT FORGET TO CALL {@link #inputDone()} after you submitted the
     * last line.
     * @param line a line of the output of the compiler
     */
    public void parseLine(@NotNull String line) {
        if (line.startsWith("File")) // we are done with the previous message
            messageReady();

        // parse the row
        if (currentState == null) {
            parseLocation(line);
            return;
        }

        // find the type, if needed
        if (currentState.kind == null) {
            if (line.startsWith("Warning")) currentState.kind = CompilerOutputMessage.Kind.WARNING;
            else if (line.startsWith("Error")) currentState.kind = CompilerOutputMessage.Kind.ERROR;
            else if (line.startsWith("Alert")) currentState.kind = CompilerOutputMessage.Kind.ALERT;
            else {
                currentState.context += line + "\n";
                return;
            }
            int i = line.indexOf(':');
            assert i != -1;
            line = line.substring(i+1); // we got ": ", we are skipping both
        }

        currentState.messageRaw += line.trim() + "\n";
    }

    public static final Pattern FILE_LOCATION = Pattern.compile("File \"([^\"]+)\", lines? ([^,]+), characters ([^-]+)-([0-9]+):(.*)");
    public static final Pattern FILE_LOCATION_LINE = Pattern.compile("File \"([^\"]+)\", line ([^,]+):(.*)");

    /**
     * The first line. Usually something like
     * "<code>File "file.ml", line 1, characters 0-18:</code>".<br>
     *
     * We got some special cases
     * <ul>
     *     <li>"line" may be "lines", and the "1" will becomes an interval</li>
     *     <li>the "characters" part is optional</li>
     * </ul>
     *
     * @param line we are expecting a location.
     * @throws IllegalStateException if this line couldn't be parsed
     */
    private void parseLocation(String line) {
        boolean wholeLine = false;
        Matcher matcher = FILE_LOCATION.matcher(line);

        if (!matcher.matches()) { // there is no "characters"
            matcher = FILE_LOCATION_LINE.matcher(line);
            wholeLine = true;
        }
        if (!matcher.matches())
            throw new IllegalStateException("Was expecting a location, got '"+line+"'.");
        currentState = new CompilerState(matcher.group(1));

        // line
        String lineInterval = matcher.group(2);
        int startLine, endLine;
        int dash = lineInterval.indexOf('-');
        if (dash == -1) endLine = (startLine = Integer.parseInt(lineInterval));
        else {
            startLine = Integer.parseInt(lineInterval.substring(0, dash));
            endLine = Integer.parseInt(lineInterval.substring(dash+1));
        }
        currentState.startLine = startLine;
        currentState.endLine = endLine;

        if (!wholeLine) { // add the columns
            currentState.startColumn = Integer.parseInt(matcher.group(3));
            currentState.endColumn = Integer.parseInt(matcher.group(4));
        }
    }

    /** must be called after every call to parseLocation **/
    public void inputDone() {
        messageReady();
    }
}
