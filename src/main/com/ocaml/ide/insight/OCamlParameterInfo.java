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
import java.util.List;

public class OCamlParameterInfo implements ParameterInfoHandler<PsiElement, OCamlParameterInfo.ParameterInfoArgumentList> {

    private static final String DOTS = "...";
    private static final int MAX_DEFAULT_VALUE_LEN = 32;

    @Override
    public @Nullable PsiElement findElementForUpdatingParameterInfo(@NotNull UpdateParameterInfoContext context) {
//        return findArgumentList(context, context.getParameterListStart());
        return null;
    }

    @Override public @Nullable PsiElement findElementForParameterInfo(@NotNull CreateParameterInfoContext context) {
        PsiElement element = findArgumentList(context, -1);
        if (element == null) return null;
        context.setItemsToShow(new ParameterInfoArgumentList[]{new ParameterInfoArgumentList(
                List.of("toto"), List.of(), List.of())
        });
        return element;
    }

    private @Nullable PsiElement findArgumentList(@NotNull ParameterInfoContext context, int parameterListStart) {
        System.out.println("find at("+context.getOffset()+") index ("+parameterListStart+")");
        int offset = context.getOffset() - 1;
        PsiElement startingElement = context.getFile().findElementAt(offset);
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

        return null;
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
        context.setupUIComponentPresentation("one, two, ...",
                5, 8, false,
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
