package com.ocaml.ide.console.debug;

import com.intellij.openapi.util.Pair;
import com.ocaml.ide.console.debug.groups.TreeElementGroupKind;
import com.ocaml.ide.console.debug.groups.elements.*;
import com.ocaml.sdk.repl.OCamlREPLConstants;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Unmodifiable;

import java.util.ArrayList;
import java.util.List;

public class OCamlREPLOutputParser {

    /**
     * Parse the output
     * @param text the output
     * @return null, or the list of created elements
     */
    public static @Nullable List<Pair<OCamlTreeElement, TreeElementGroupKind>> parse(String text) {
        if (text == null || text.isBlank()) return null;
        if (text.startsWith(OCamlREPLConstants.VARIABLE))
            return parseVariables(text);
        if (text.startsWith(OCamlREPLConstants.TYPE))
            return parseType(text);
        if (text.startsWith(OCamlREPLConstants.EXCEPTION))
            return parseException(text);
        if (text.startsWith(OCamlREPLConstants.MODULE))
            return parseModule(text);
        return null;
    }

    private static List<Pair<OCamlTreeElement, TreeElementGroupKind>> parseException(String text) {
        // everything on one line
        text = text.replace("\n", " ").trim();

        // check
        int exception = text.indexOf(OCamlREPLConstants.EXCEPTION);
        if (exception == -1) return null; // not an exception

        // group
        TreeElementGroupKind group = TreeElementGroupKind.EXCEPTIONS;

        // element
        int tagSize = OCamlREPLConstants.EXCEPTION.length();
        String name = text.substring(exception + tagSize + 1);

        return List.of(new Pair<>(new OCamlExceptionElement(name), group));
    }

    private static @Nullable @Unmodifiable List<Pair<OCamlTreeElement, TreeElementGroupKind>> parseType(@NotNull String text) {
        // everything on one line
        text = text.replace("\n", " ").trim();

        // check
        int type = text.indexOf(OCamlREPLConstants.TYPE);
        if (type == -1) return null; // not a type
        int equals = text.indexOf("=");

        // group
        TreeElementGroupKind group = TreeElementGroupKind.TYPES;

        // element
        int tagSize = OCamlREPLConstants.TYPE.length();
        String name;
        String value;
        if (equals != -1) {
            name = text.substring(type + tagSize + 1, equals - 1);
            value = text.substring(equals + 2);
        } else {
            name = text.substring(type + tagSize + 1);
            value = null;
        }

        return List.of(new Pair<>(new OCamlTypeElement(name, value), group));
    }

    private static @NotNull List<Pair<OCamlTreeElement, TreeElementGroupKind>> parseVariables(@NotNull String text) {
        String[] declarations = text.split(OCamlREPLConstants.VARIABLE);
        List<Pair<OCamlTreeElement, TreeElementGroupKind>> variables = new ArrayList<>();
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
        if (text.endsWith(OCamlREPLConstants.FUN)) return parseFunction(text);

        // checks
        //noinspection DuplicatedCode
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

    private static @Nullable Pair<OCamlTreeElement, TreeElementGroupKind> parseFunction(@NotNull String text) {
        // checks
        //noinspection DuplicatedCode
        int val = text.indexOf(OCamlREPLConstants.VARIABLE);
        if (val == -1) return null; // not a variable
        int colon = text.indexOf(':');
        if (colon == -1) return null; // no type
        int equals = text.indexOf("=");
        if (equals == -1) return null; // no value

        // group
        TreeElementGroupKind group = TreeElementGroupKind.FUNCTIONS;

        // element
        int tagSize = OCamlREPLConstants.VARIABLE.length();
        String name = text.substring(val + tagSize + 1, colon - 1);
        String type = text.substring(colon + 2, equals - 1);

        return new Pair<>(new OCamlFunctionElement(name, type), group);
    }

    private static @Nullable @Unmodifiable List<Pair<OCamlTreeElement, TreeElementGroupKind>> parseModule(@NotNull String text) {
        if (text.startsWith(OCamlREPLConstants.MODULE_TYPE)) {
            System.out.println("Not supported");
            return null;
        }

        // checks
        int val = text.indexOf(OCamlREPLConstants.MODULE);
        if (val == -1) return null; // not a module
        int colon = text.indexOf(':');
        if (colon == -1) return null; // separator for definition

        // group
        TreeElementGroupKind group = TreeElementGroupKind.MODULES;

        // element
        int tagSize = OCamlREPLConstants.MODULE.length();
        String name = text.substring(val + tagSize + 1, colon - 1);

        return List.of(new Pair<>(new OCamlModuleElement(name, new ArrayList<>()), group));
    }
}
