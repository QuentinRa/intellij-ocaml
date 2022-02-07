package com.ocaml.ide.console.debug;

import com.intellij.openapi.util.Pair;
import com.ocaml.ide.console.debug.groups.TreeElementGroupKind;
import com.ocaml.ide.console.debug.groups.elements.OCamlTreeElement;
import com.ocaml.ide.console.debug.groups.elements.OCamlVariableElement;
import com.ocaml.sdk.repl.OCamlREPLConstants;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;

public class OCamlREPLOutputParser {

    /**
     * Parse the output
     * @param text the output
     * @return null, or the list of created elements
     */
    public static @Nullable ArrayList<Pair<OCamlTreeElement, TreeElementGroupKind>> parse(String text) {
        if (text == null || text.isBlank()) return null;
        if (text.startsWith(OCamlREPLConstants.VARIABLE))
            return parseVariables(text);
        return null;
    }

    private static @NotNull ArrayList<Pair<OCamlTreeElement, TreeElementGroupKind>> parseVariables(@NotNull String text) {
        String[] declarations = text.split(OCamlREPLConstants.VARIABLE);
        ArrayList<Pair<OCamlTreeElement, TreeElementGroupKind>> variables = new ArrayList<>();
        for (String s:declarations) {
            if (s.isBlank()) continue;
            var variable = parseVariable(OCamlREPLConstants.VARIABLE+s);
            if (variable != null) variables.add(variable);
        }
        return variables;
    }

    private static @Nullable Pair<OCamlTreeElement, TreeElementGroupKind> parseVariable(@NotNull String text) {
        // everything on one line
        text = text.replace("\n", " ").trim();

        // checks
        int val = text.indexOf(OCamlREPLConstants.VARIABLE);
        if (val == -1) return null; // not a variable
        int colon = text.indexOf(':');
        if (colon == -1) return null; // no type
        int equals = text.indexOf("=");
        if (equals == -1) return null; // no value

        // group
        TreeElementGroupKind group = TreeElementGroupKind.VARIABLES;

        // element
        int tagSize = OCamlREPLConstants.VARIABLE.length();
        String name = text.substring(val + tagSize + 1, colon - 1);
        String type = text.substring(colon + 2, equals - 1);
        String value = text.substring(equals + 2);

        return new Pair<>(new OCamlVariableElement(name, value, type), group);
    }
}
