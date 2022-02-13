package com.ocaml.sdk.annot;

import com.intellij.build.FilePosition;
import com.intellij.openapi.util.TextRange;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class OCamlAnnotParser {
    private static final Pattern FILE_POSITION = Pattern.compile("\"([^\"]+)\" (\\d+) (\\d+) (\\d+) \"[^\"]+\" (\\d+) (\\d+) (\\d+).*");
    // tested: same values for ocaml 4.05 to ocamlc 4.14
    private static final String TYPE_START = "type(";
    private static final String IDENT_START = "ident(";
    private static final String VARIABLE_DEF = "def";
    private static final String VARIABLE_REF = "ref";

    private final String[] lines;
    private int pos;

    public OCamlAnnotParser(@NotNull String input) {
        this.lines = input.split("\n");
        this.pos = 0;
    }

    /**
     * Parse and return the result of the parsing
     * @return list of signature that were found in the file
     */
    public ArrayList<OCamlInferredSignature> get() {
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
        if(!line.startsWith(TYPE_START)) {
            if (line.startsWith(IDENT_START)) {
                line = readLine().trim(); // look for module name
                state.name = line.substring(getStartingPosition(line), line.indexOf("\"")-1);
                pos++; // skip )
            } else {
                throw new IllegalStateException("Unexpected line:'"+previousLine()+"'.");
            }
        } else {
            // it's a variable
            // next is the type
            state.type = readLine().trim();
            pos++; // skip )
            line = tryReadLine();
            if (line != null && line.startsWith(IDENT_START)) { // variable
                line = readLine().trim(); // look for variable name
                int end = line.indexOf("\"");
                int start = getStartingPosition(line);
                line = line.substring(start, end-1);
                state.name = line;
                state.kind = AnnotParserState.AnnotKind.VARIABLE;
                pos++; // skip ')'
            } else {
                // this is a value
                state.kind = AnnotParserState.AnnotKind.VALUE;

                // we need to offset the "tryReadLine"
                if (line != null) pos--; // whoops, go back
            }
        }
        return state;
    }

    private int getStartingPosition(@NotNull String line) {
        if (!line.startsWith(VARIABLE_DEF)) {
            return line.indexOf(VARIABLE_REF)+VARIABLE_REF.length()+1; // ex: 'ref ' => 3+1
        } else {
            return VARIABLE_DEF.length()+1; // ex: 'def ' => 3 + 1
        }
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
        annotatedElement.range = new TextRange(state.startOffset, state.endOffset);

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

    @Contract(mutates = "this")
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
        private final int startOffset;
        private final int endOffset;
        private final int startLine;
        private final int startColumn;
        private final int endLine;
        private final int endColumn;
        private String type; // type of the element
        private String name;

        enum AnnotKind {
            VARIABLE, VALUE, MODULE
        }
        public AnnotKind kind;

        public AnnotParserState(String fileName, String startLine, String startOffset,
                                String startColumnWithOffset, String endLine,
                                String endOffset, String endColumnWithOffset) {
            int offsetStart = Integer.parseInt(startOffset);
            int offsetEnd = Integer.parseInt(endOffset);

            this.fileName = fileName;
            this.startOffset = Integer.parseInt(startColumnWithOffset);
            this.endOffset = Integer.parseInt(endColumnWithOffset);
            this.startLine = Integer.parseInt(startLine);
            this.startColumn = this.startOffset - offsetStart;
            this.endLine = Integer.parseInt(endLine);
            this.endColumn = this.endOffset - offsetEnd;
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