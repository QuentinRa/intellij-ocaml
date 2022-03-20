package com.ocaml.ide.insight;

import com.intellij.lang.parameterInfo.*;
import com.intellij.openapi.util.Pair;
import com.intellij.psi.PsiComment;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiWhiteSpace;
import com.intellij.psi.SyntaxTraverser;
import com.ocaml.OCamlLanguage;
import com.ocaml.lang.utils.OCamlPsiUtils;
import com.ocaml.sdk.annot.OCamlInferredSignature;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

public class OCamlParameterInfo implements ParameterInfoHandler<PsiElement, OCamlParameterInfo.ParameterInfoArgumentList> {

    private static final String DOTS = "...";
    private static final int MAX_DEFAULT_VALUE_LEN = 32;

    @Override
    public @Nullable PsiElement findElementForUpdatingParameterInfo(@NotNull UpdateParameterInfoContext context) {
        Pair<PsiElement, List<String>> pair = findArgumentList(context, context.getParameterListStart());
        return pair == null ? null : pair.first;
    }

    @Override public @Nullable PsiElement findElementForParameterInfo(@NotNull CreateParameterInfoContext context) {
        Pair<PsiElement, List<String>> element = findArgumentList(context, context.getParameterListStart());
        if (element == null) return null;
        context.setItemsToShow(new ParameterInfoArgumentList[]{new ParameterInfoArgumentList(
                element.second, List.of(), List.of())
        });
        return element.first;
    }

    private @Nullable Pair<PsiElement, List<String>> findArgumentList(@NotNull ParameterInfoContext context, int parameterListStart) {
        System.out.println("find at("+context.getOffset()+") index ("+parameterListStart+")");
        int offset = context.getOffset() - 1;
        PsiElement originalElement = context.getFile().findElementAt(offset);
        PsiElement startingElement = originalElement;
        if (startingElement == null) return null;
        System.out.println("  found '"+startingElement.getText()+"' ("+startingElement+")");

        OCamlAnnotResultsService annot = startingElement.getProject().getService(OCamlAnnotResultsService.class);

        // find some start
        while (startingElement instanceof PsiWhiteSpace || startingElement instanceof PsiComment)
            startingElement = OCamlPsiUtils.skipMeaninglessPreviousSibling(startingElement);
        if (startingElement == null) return null;

        // find our starting point
        OCamlInferredSignature annotation = null;
        List<PsiElement> psiElements = SyntaxTraverser.psiApi().parents(startingElement).toList();
        for (PsiElement candidate : psiElements) {
            annotation = annot.findAnnotationFor(candidate, true);
            startingElement = candidate;
            if (annotation != null) break;
        }
        if (annotation == null) return null;

        System.out.println("  candidate is: '"+startingElement.getText()+"' ("+startingElement+")");
        System.out.println("  annotation found: "+annotation);

        ArrayList<Pair<OCamlInferredSignature, PsiElement>> elements = new ArrayList<>();
        elements.add(new Pair<>(annotation, startingElement));

        int index;
        int firstFunctionIndex;

        do {
            PsiElement element = OCamlPsiUtils.skipMeaninglessPreviousSibling(startingElement);
            index = 0;
            firstFunctionIndex = updateFirstFunctionIndex(annotation, -1);

            while (element != null) {
                annotation = annot.findAnnotationFor(element, true);
                if (annotation == null) break;

                elements.add(0, new Pair<>(annotation, element));
                element = OCamlPsiUtils.skipMeaninglessPreviousSibling(element);
                index++;

                // we found another function
                firstFunctionIndex = updateFirstFunctionIndex(annotation, firstFunctionIndex);
            }

            // done
            if (firstFunctionIndex != -1) break;
            // try a new starting element
            startingElement = startingElement.getParent();
            if (startingElement == null) return null; // just in case
            annotation = annot.findAnnotationFor(startingElement, true);
            if (annotation == null) return null; // can't do anything

            System.out.println("  *new* candidate is: '"+startingElement.getText()+"' ("+startingElement+")");
            System.out.println("  *new* annotation found: "+annotation);
        } while (true);

        System.out.println("  "+elements);
        System.out.println("  fun is at index:"+firstFunctionIndex);
        System.out.println("  we are at index:"+index);

        // start with the function
        while (firstFunctionIndex != 0) {
            elements.remove(0);
            firstFunctionIndex--;
            index--;
        }

//        System.out.println("  *new* "+elements);
//        System.out.println("  *new* fun is at index:"+firstFunctionIndex);
//        System.out.println("  *new* we are at index:"+index);

        List<String> names = new ArrayList<>();

        Pair<OCamlInferredSignature, PsiElement> fun = elements.remove(0);
        String function = fun.first.type;

        final String separator = OCamlLanguage.FUNCTION_SIGNATURE_SEPARATOR;

        // guess the types that we got after
        while (function.contains(separator)) {
            int i = function.indexOf(separator);

            // oh, no, this is a function
            String substring = function.substring(0, i);
            if (substring.startsWith("(")) {
                i = function.indexOf(')') + 1; // the separator is after ')'
                substring = function.substring(1, i-1); // we don't want '(' nor ')'
            }

            // handle
            names.add(substring);

            // next
            function = function.substring(i + separator.length());
        }

        System.out.println("  names:"+names);

        return new Pair<>(originalElement, names);
    }

    private int updateFirstFunctionIndex(OCamlInferredSignature annotation, int firstFunctionIndex) {
        if (firstFunctionIndex != -1)
            firstFunctionIndex++;

        if (annotation.type.contains(OCamlLanguage.FUNCTION_SIGNATURE_SEPARATOR)) {
            // fix: bypass operators i.e. Stdlib.( + ) for instance
            String name = annotation.name;
            if (name == null) return firstFunctionIndex; // not null tho
            int dot = name.indexOf('.');
            if (dot != -1) name = name.substring(dot+1);
            // aside from operators
            if (!name.startsWith("("))
                firstFunctionIndex = 0;
        }

        return firstFunctionIndex;
    }

    @Override
    public void showParameterInfo(@NotNull PsiElement element, @NotNull CreateParameterInfoContext context) {
        context.showHint(element, element.getTextOffset(), this);
    }

    @Override
    public void updateParameterInfo(@NotNull PsiElement psiParameters, @NotNull UpdateParameterInfoContext context) {
        System.out.println("update");
    }

    @Override public void updateUI(ParameterInfoArgumentList p, @NotNull ParameterInfoUIContext context) {
        System.out.println("update UI");

        StringBuilder b = new StringBuilder();
        for (String name : p.names) {
            b.append(name).append(", ");
        }
        String text = b.toString();
        text = text.substring(0, text.length() - 2);

        context.setupUIComponentPresentation(text,
                0, text.length(), false,
                false, false, context.getDefaultParameterColor());
    }

    public static final class ParameterInfoArgumentList {
        public final List<String> names;
        public final List<String> defaultValues;
        public final List<Integer> permutation;
        public final int currentArgumentIndex;
        public final boolean isDisabled;

        public ParameterInfoArgumentList(List<String> names, List<String> defaultValues, List<Integer> permutation) {
            this(names, defaultValues, permutation, -1, false);
        }

        public ParameterInfoArgumentList(List<String> names, List<String> defaultValues,
                                         List<Integer> permutation, int currentArgumentIndex, boolean isDisabled) {

            this.names = names;
            this.defaultValues = defaultValues;
            this.permutation = permutation;
            this.currentArgumentIndex = currentArgumentIndex;
            this.isDisabled = isDisabled;
        }
    }
}
