package com.ocaml.ide.sdk.buildTool;

import com.intellij.build.*;
import com.intellij.build.events.*;
import com.intellij.build.events.impl.*;
import com.intellij.build.output.*;
import com.intellij.openapi.util.*;
import com.ocaml.comp.opam.*;
import com.ocaml.ide.settings.*;

import java.io.*;
import java.util.*;
import java.util.function.*;
import java.util.regex.*;

/**
 * =========================================================
 * The output is always (4.05, 4.06, 4.07, 4.08, 4.10, 4.11) as follows
 * (aside from a "blank" line before Warning: on 4.05 and 4.08+)
 * =========================================================
 * File "test.ml", line 1, characters 7-7:
 * Error: Syntax error
 *
 * or
 *
 * File "test.ml", line 1, characters 8-9:
 * Warning 39: unused rec flag.
 * File "test.ml", line 1, characters 10-11:
 * Warning 27: unused variable i.
 *
 * or
 *
 * File "test.ml", line 3, characters 7-8:
 * Alert deprecated: t
 *
 * ======= NOTES =======
 * 1) Compilers 4.05 and 4.08-4.11 are showing the context between "File"
 * and the Warning/Error/Alert
 * File "test.ml", line 3, characters 4-5:
 * 3 | let x t = match t with
 *         ^
 * Warning 32: unused value x.
 *
 * 2) Messages may be on multiples lines.
 *
 * File "test.ml", lines 3-5, characters 10-8:
 * 3 | ..........match t with
 * 4 | | A(x) -> x
 * 5 | | E -> 0
 * Warning 8: this pattern-matching is not exhaustive.
 * Here is an example of a case that is not matched:
 * (C|D|B _)
 *
 * ========> https://github.com/Chris00/tuareg/blob/master/compilation.txt
 */
public class OCamlcBuildEventsConverter implements BuildOutputParser {
    private static final String GROUP_TITLE = "OCamlc Compiler";
    public static final Pattern FILE_LOCATION =
            Pattern.compile("File \"([^\"]+)\", line ([^,]+), characters ([^-]+)-([0-9]+):(.*)");
    public static final Pattern FILE_LOCATION_LINE =
            Pattern.compile("File \"([^\"]+)\", line ([^,]+):(.*)");

    private final OCamlcBuildContext myContext;
    private final boolean myIsWsl;

    private boolean firstLineDone = false;

    // "One task => One file"
    private final String task0Name = "Compiling files";
    private final Integer task0Id = 0;

    private final HashSet<Message> myMessages = new HashSet<>();

    public OCamlcBuildEventsConverter(OCamlcBuildContext context) {
        myContext = context;

        // we will have to parse back the file
        // WSL -> Windows
        // if we are using a WSL
        ORSettings service = context.myProject.getService(ORSettings.class);
        myIsWsl = service.isWsl();
    }

    /*
    Note: Send Start (id=myBuildId) when starting a new "task" (id=eventId)
    Note: Send some events belonging to the task
    Note: Send Stop
    ...
    Preview of the result
      test.ml 2 warnings
        - Warning 39: unused rec flag.:1 ==> Warning 39: unused rec flag.
        - Warning 27: unused variable i.:1 ==> Warning 27: unused variable i.
     */
    @Override public boolean parse(String line,
                                   BuildOutputInstantReader reader,
                                   Consumer<? super BuildEvent> messageConsumer) {
        // Consume the line
        // with the command, send start "$task0Name"
        if (!firstLineDone) {
            fireTask0Start(messageConsumer);
            firstLineDone = true;
            return true;
        }
        // We are done, this was the last line
        if (line.startsWith("Process finished with exit code")) {
            fireTask0Message(messageConsumer);
            fireTask0Done(messageConsumer);
            return true;
        }

        return tryFireMessage(line, messageConsumer);
    }

    private void fireTask0Start(Consumer<? super BuildEvent> messageConsumer) {
        messageConsumer.accept(new StartEventImpl(
                task0Id,
                myContext.myBuildId,
                System.currentTimeMillis(),
                task0Name
        ));
    }

    private void fireTask0Done(Consumer<? super BuildEvent> messageConsumer) {
        messageConsumer.accept(new FinishEventImpl(
                task0Id,
                myContext.myBuildId,
                System.currentTimeMillis(),
                task0Name,
                new SuccessResultImpl(true)
        ));
    }

    private void fireTask0Message(Consumer<? super BuildEvent> messageConsumer) {
        if (currentMessage == null) return;
        if (myMessages.contains(currentMessage)) {
            currentMessage = null;
            return;
        }
        boolean isError = currentMessage.kind == MessageEvent.Kind.ERROR;

        // format messages
        String title = (isError ? "Error:" : "Warning") + currentMessage.shortMessage;
        String detailedMessage = "";
        if (!currentMessage.context.isEmpty()) detailedMessage += currentMessage.context;
        detailedMessage += title + "\n";
        if (!currentMessage.message.isEmpty()) detailedMessage += currentMessage.message;

        // send
        messageConsumer.accept(new FileMessageEventImpl(
                task0Id,
                currentMessage.kind,
                GROUP_TITLE,
                (isError ? "Error:" : "Warning") + currentMessage.shortMessage,
                detailedMessage,
                currentMessage.filePosition
        ));

        // reset
        currentMessage = null;

        // update
        if (isError) myContext.myErrors.incrementAndGet();
        else myContext.myWarnings.incrementAndGet();
    }

    private static class Message {
        public FilePosition filePosition;
        public MessageEvent.Kind kind;
        public String shortMessage;
        public String message = "";
        public String context = "";

        @Override public String toString() {
            if (filePosition == null) return "Message{No file}";
            return "Message{" +
                    "filePosition=" + filePosition.getFile().getName() +
                    ", kind=" + kind +
                    ", shortMessage='" + shortMessage + '\'' +
                    ", message='" + message.replace("\n","\\n") + '\'' +
                    ", context='" + context.replace("\n","\\n") + '\'' +
                    '}';
        }

        @Override public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof Message)) return false;
            Message message = (Message) o;
            // same file
            if (!Objects.equals(filePosition, message.filePosition)) return false;
            // same error
            return Objects.equals(shortMessage, message.shortMessage);
        }
    }

    private Message currentMessage;

    /**
     * Pattern:
     *
     * 1) A file for the file, line, column
     *
     * Followed by multiples lines
     *
     * Until
     * Error*
     *  - the title is "Error: some error here" (first line)
     *  - the content is  title(+remaining lines if we got ones)(+context if we got one)
     * Warning* or Alert* ->
     *  - the title is "Warning: some warning here" (first line)
     *  - the content is  title(+remaining lines if we got ones)(+context if we got one)
     *
     * ==Note== the first line "File" may be nested inside some message.
     * If so, the part after : contains some message.
     */
    private boolean tryFireMessage(String line, Consumer<? super BuildEvent> messageConsumer) {
        System.out.println("here with Line:"+line);
        System.out.println("currentMessage is:"+currentMessage);

        // if we are done with the previous message
        // we need to fire and start anew
        if (line.startsWith("File") && currentMessage != null) {
            fireTask0Message(messageConsumer);
        }

        // we are supposed to read a file
        if (currentMessage == null) {
            var fileLine = extractFile(line, false);
            if (fileLine == null) return false;
            currentMessage = new Message();
            currentMessage.filePosition = fileLine.getFirst();
        } else {
            if (line.startsWith("Error")) {
                currentMessage.kind = MessageEvent.Kind.ERROR;
                currentMessage.shortMessage = extractErrorMessage(line);
            } else if (line.startsWith("Warning")) {
                currentMessage.kind = MessageEvent.Kind.WARNING;
                currentMessage.shortMessage = extractWarningMessage(line);
            } else if (line.startsWith("Alert")) {
                currentMessage.kind = MessageEvent.Kind.WARNING;
                currentMessage.shortMessage = extractAlertMessage(line);
            } else if (currentMessage.kind == null) {
                // we are reading the context
                currentMessage.context += line+"\n";
            } else {
                // we are reading a message on multiples lines
                // todo: handle nested files
                currentMessage.message += line+"\n";
                System.out.println(currentMessage);
            }
        }

        return true;
    }

    /**
     * File \"path\", line startColumn(-endLine)?, characters startColumn-endColumn
     * ==Note== "-endLine" is not present when endLine=startColumn+1
     *
     * It may be nested inside some message.
     * If so, the part after : contains some message,
     * but the MESSAGE MAY BE ON MULTIPLES LINES (and the part after ":" empty? not sure)
     *
     * @return a pair with the file position, and a string (null if not nested, the message if nested).
     */
    private Pair<FilePosition, String> extractFile(String fileLine, boolean nested) {
        boolean line = false;
        Matcher matcher = FILE_LOCATION.matcher(fileLine);
        // try again
        if (!matcher.matches()) {
            matcher = FILE_LOCATION_LINE.matcher(fileLine);
            line = true;
        }
        if (!matcher.matches()) return null;
        String path = matcher.group(1);
        File file = new File(myIsWsl ? OpamUtils.WSLtoWindowsPath(path) : path);

        String lineInterval = matcher.group(2);
        int startLine, endLine;
        int dash = lineInterval.indexOf('-');
        if (dash == -1) endLine = (startLine = Integer.parseInt(lineInterval) - 1) + 1;
        else {
            startLine = Integer.parseInt(lineInterval.substring(0, dash));
            endLine = Integer.parseInt(lineInterval.substring(dash+1));
        }

        FilePosition filePosition;

        if (line) {
            filePosition = new FilePosition(
                    file,
                    startLine,
                    0,
                    endLine,
                    1
            );
        } else {
            int startColumn = Integer.parseInt(matcher.group(3));
            int endColumn = Integer.parseInt(matcher.group(4));
            filePosition = new FilePosition(
                    file,
                    startLine,
                    startColumn,
                    endLine,
                    endColumn
            );
        }

        return new Pair<>(filePosition, nested ? matcher.group(5) : null);
    }

    private String extractErrorMessage(String line) {
        int sep = line.indexOf(':');
        return line.substring(sep+1); // skip :
    }

    private String extractAlertMessage(String line) {
        int sep = line.indexOf(':');
        String message = line.substring(sep+1); // skip ":"
        // faster/better than "deprecated" right?
        if (line.contains("Alert deprecated")) {
            message += " is deprecated";
        }
        return message;
    }

    private String extractWarningMessage(String line) {
        int sep = line.indexOf(':');
        return line.substring(sep+1); // skip :
    }
}
