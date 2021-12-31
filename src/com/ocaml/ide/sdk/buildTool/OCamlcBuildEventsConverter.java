package com.ocaml.ide.sdk.buildTool;

import com.intellij.build.*;
import com.intellij.build.events.*;
import com.intellij.build.events.impl.*;
import com.intellij.build.output.*;
import com.ocaml.ide.settings.*;

import java.io.*;
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
 * =========================================================
 * My strategy to parse this
 * =========================================================
 * The first line (if there is one) is the file -> line -> characters
 * Then we need to skip lines (note: not any more using compilation options) until I find
 * - Error*
 *  - extract the message after ":"
 * - Warning*
 *  - extract the number
 *  - extract the message after ":"
 */
public class OCamlcBuildEventsConverter implements BuildOutputParser {
    private final OCamlcBuildContext myContext;
    private final boolean myIsWsl;

    public OCamlcBuildEventsConverter(OCamlcBuildContext context) {
        myContext = context;

        // we will have to parse back the file
        // WSL -> Windows
        // if we are using a WSL
        ORSettings service = context.myProject.getService(ORSettings.class);
        myIsWsl = service.isWsl();
    }

    public static final Pattern FILE_LOCATION =
            Pattern.compile("File \"([^\"]+)\", line ([^,]+), characters ([^-]+)-([0-9]+):");

    private boolean firstLineDone = false;
    private FilePosition fileLocation = null;
    private Integer eventId = 0;

    /*
    Note: Send Start (id=myBuildId) when starting a new "task" (id=eventId)
    Note: Send some events belonging to the task
    Note: Send Stop
    ...
    - todo: This is only working for one task/file for now.
    - todo: Also, there "may" (not for now) be duplicates.
    - Preview
      test.ml 2 warnings
        - Warning 39: unused rec flag.:1 ==> Warning 39: unused rec flag.
        - Warning 27: unused variable i.:1 ==> Warning 27: unused variable i.
    - todo: there is no colors
    - todo: There is a bug, Compile file is marked as test
    - todo: sort the results? by name, then by line
    - todo: show some context (if available?) => see if (fileLocation != null) return true;
     */

    @Override public boolean parse(String line,
                                   BuildOutputInstantReader reader,
                                   Consumer<? super BuildEvent> messageConsumer) {
        if (!firstLineDone) {
            firstLineDone = true;
            var startEvent = new StartEventImpl(
                    eventId,
                    myContext.myBuildId,
                    System.currentTimeMillis(),
                    "Compiling file"
            );
            messageConsumer.accept(startEvent);
            return true;
        }
        int sep = line.indexOf(':');
        if (sep == -1) {
            // allowed if fileLocation was set
            if (fileLocation != null) return true;
            if (line.startsWith("Process finished with exit code")) {
                // Done
                var finishBuildEvent = new FinishBuildEventImpl(
                        eventId,
                        myContext.myBuildId,
                        System.currentTimeMillis(),
                        "Compiling file",
                        new SuccessResultImpl(true)
                );
                messageConsumer.accept(finishBuildEvent);
                return true;
            }
            return false;
        }

        if (fileLocation == null) { // this is the first line
            Matcher matcher = FILE_LOCATION.matcher(line);
            if (!matcher.matches()) return false;
            String path = matcher.group(1);
            int lineIndex = Integer.parseInt(matcher.group(2)) - 1;
            int columnIndexStart = Integer.parseInt(matcher.group(3));
            int columnIndexEnd = Integer.parseInt(matcher.group(4));

            File file = new File(myIsWsl ? WSLtoWindowsPath(path) : path);
            System.out.println(file);
            fileLocation = new FilePosition(
                    file,
                    lineIndex, columnIndexStart, lineIndex+1, columnIndexEnd
            );
        } else { // list is the second line
            boolean isError = line.startsWith("Error");
            MessageEvent.Kind kind = isError ? MessageEvent.Kind.ERROR : MessageEvent.Kind.WARNING;

            var fileEvent = new FileMessageEventImpl(
                    eventId++,
                    kind,
                    "OCamlc Compiler",
                    line,
                    line,
                    fileLocation
            );

            messageConsumer.accept(fileEvent);

            if (kind == MessageEvent.Kind.ERROR) myContext.myErrors.incrementAndGet();
            else myContext.myWarnings.incrementAndGet();

            fileLocation = null;
        }

        return true;
    }

    private String WSLtoWindowsPath(String path) {
        path = path.replace("/mnt/", "");
        int sep = path.indexOf("/");
        String mount = path.substring(0, sep) + ":";
        return mount+path.substring(sep).replace("/", "\\");
    }
}
