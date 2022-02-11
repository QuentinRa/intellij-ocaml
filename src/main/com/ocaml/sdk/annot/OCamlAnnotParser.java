package com.ocaml.sdk.annot;

import com.intellij.build.FilePosition;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class OCamlAnnotParser {
    private static final Pattern FILE_POSITION = Pattern.compile("\"([^\"]+)\" (\\d+) (\\d+) (\\d+) \"[^\"]+\" (\\d+) (\\d+) (\\d+).*");

    private final String[] lines;
    private int pos;

    public OCamlAnnotParser(@NotNull String input) {
        this.lines = input.split("\n");
        this.pos = 0;
    }

    public ArrayList<OCamlInferredSignature> parse() {
        ArrayList<OCamlInferredSignature> elements = new ArrayList<>();
        AnnotParserState state = parseInstruction();
        while (state != null) {
            elements.add(createAnnotResult(state));
            state = parseInstruction();
        }
        return elements;
    }

    private @Nullable AnnotParserState parseInstruction() {
        if (pos == lines.length) return null;
        Matcher matcher = FILE_POSITION.matcher(readLine());
        if (!matcher.matches())
            throw new IllegalStateException("Missing file position for:'"+previousLine()+"'.");
        AnnotParserState state = new AnnotParserState(matcher.group(1), matcher.group(2),
                matcher.group(3), matcher.group(4),
                matcher.group(5),
                matcher.group(6), matcher.group(7)
        );
        String line = readLine();
        // it's a module
        if(!line.startsWith("type(")) {
            System.out.println("module todo:");
        } else {
            // it's a variable
            // next is the type
            state.type = readLine().trim();
            pos++; // skip )
            line = tryReadLine();
            if (line != null && line.startsWith("ident(")) { // variable
                line = readLine().trim(); // look for variable name
                int end = line.indexOf("\"");
                line = line.substring("def".length()+1, end-1);
                state.name = line;
                state.kind = AnnotParserState.AnnotKind.VARIABLE;
                pos++; // skip ')'
            } else {
                // this is a value
                state.kind = AnnotParserState.AnnotKind.VALUE;
            }
        }
        return state;
    }

    private @NotNull OCamlInferredSignature createAnnotResult(@NotNull AnnotParserState state) {
        boolean isVariable = state.kind == AnnotParserState.AnnotKind.VARIABLE;
        boolean isValue = !isVariable && state.kind == AnnotParserState.AnnotKind.VALUE;

        OCamlInferredSignature annotatedElement = new OCamlInferredSignature();
        annotatedElement.name = state.name; // may be null for values
        annotatedElement.kind = isVariable ? OCamlInferredSignature.Kind.VARIABLE :
                isValue ? OCamlInferredSignature.Kind.VALUE : OCamlInferredSignature.Kind.MODULE;
        annotatedElement.type = state.type;
        annotatedElement.position = new FilePosition(
                new File(state.fileName),
                state.startLine, state.startColumn, state.endLine, state.endColumn
        );

        System.out.println("state:"+state);
        return annotatedElement;
    }

    //
    // Utils
    //

    // read AND move to the next line
    @Contract(mutates = "this")
    private @NotNull String readLine() {
        return lines[pos++];
    }

    private @Nullable String tryReadLine() {
        if (pos >= lines.length) return null;
        return lines[pos++];
    }

    @Contract(mutates = "this")
    private String previousLine() {
        return lines[--pos];
    }

    /**
     * State of the current element that is parsed
     */
    private static class AnnotParserState {
        private final String fileName;
        public int startLine;
        public int startColumn;
        public int endLine;
        public int endColumn;
        public String type; // type of the element
        public String name;

        enum AnnotKind {
            VARIABLE, VALUE, MODULE
        }
        public AnnotKind kind;

        public AnnotParserState(String fileName, String startLine, String startOffset,
                                String startColumnWithOffset, String endLine,
                                String endOffset, String endColumnWithOffset) {
            this.fileName = fileName;
            int offsetStart = Integer.parseInt(startOffset);
            int offsetEnd = Integer.parseInt(endOffset);
            this.startLine = Integer.parseInt(startLine);
            this.startColumn = Integer.parseInt(startColumnWithOffset) - offsetStart;
            this.endLine = Integer.parseInt(endLine);
            this.endColumn = Integer.parseInt(endColumnWithOffset) - offsetEnd;
        }

        @Contract(pure = true)
        @Override public @NotNull String toString() {
            return "AnnotParserState{" +
                    "fileName='" + fileName + '\'' +
                    ", startLine=" + startLine +
                    ", startColumn=" + startColumn +
                    ", endLine=" + endLine +
                    ", endColumn=" + endColumn +
                    ", type='" + type + '\'' +
                    ", name='" + name + '\'' +
                    ", kind=" + kind +
                    '}';
        }
    }
}