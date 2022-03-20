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
import java.util.concurrent.atomic.AtomicReference;

public class OCamlParameterInfo implements ParameterInfoHandler<PsiElement, OCamlParameterInfo.ParameterInfoArgumentList> {

    private static final String DOTS = "...";
    private static final int MAX_DEFAULT_VALUE_LEN = 32;

    @Override
    public @Nullable PsiElement findElementForUpdatingParameterInfo(@NotNull UpdateParameterInfoContext context) {
        return findArgumentList(context, context.getParameterListStart());
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

        PsiElement element = OCamlPsiUtils.skipMeaninglessPreviousSibling(startingElement);
        int index = 0;
        int firstFunctionIndex = annotation.type.contains(OCamlLanguage.FUNCTION_SIGNATURE_SEPARATOR) ? 0 : -1;
        while (element != null) {
            annotation = annot.findAnnotationFor(element, true);
            if (annotation == null) break;

            elements.add(0, new Pair<>(annotation, element));
            element = OCamlPsiUtils.skipMeaninglessPreviousSibling(element);
            index++;
            if (firstFunctionIndex != -1)
                firstFunctionIndex++;

            // we found another function
            if (annotation.type.contains(OCamlLanguage.FUNCTION_SIGNATURE_SEPARATOR)) {
                // fix: bypass operators i.e. Stdlib.( + ) for instance
                String name = annotation.name;
                System.out.println(name);
                if (name == null) continue; // not null tho
                int dot = name.indexOf('.');
                if (dot != -1) name = name.substring(dot+1);
                System.out.println(name+" "+dot);
                // aside from operators
                if (!name.startsWith("("))
                    firstFunctionIndex = 0;
            }
        }

        System.out.println(elements);

        System.out.println("  fun is at index:"+firstFunctionIndex);
        System.out.println("  we are at index:"+index);

        // first must be a function
//        if (function.first.kind != OCamlInferredSignature.Kind.VARIABLE) return null;
//        String type = function.first.type;
//        if (!type.contains(OCamlLanguage.FUNCTION_SIGNATURE_SEPARATOR)) return null;

        // then we are checking
//        System.out.println("  function with type:"+type);
//        System.out.println("  we are at index:"+index);

//        PsiElement psiElement = OCamlPsiUtils.skipMeaninglessPreviousSibling(startingElement);
//        System.out.println("    prev:"+psiElement+" ('"+(psiElement == null ? "null" : psiElement.getText()+"')"));
//        if (psiElement != null) {
//            psiElement = OCamlPsiUtils.skipMeaninglessPreviousSibling(psiElement);
//            System.out.println("    prev:"+psiElement+" ('"+(psiElement == null ? "null" : psiElement.getText())+"')");
//        }

        return null;

//        OCamlAnnotResultsService annot = startingElement.getProject().getService(OCamlAnnotResultsService.class);
//        OCamlInferredSignature annotation = annot.findAnnotationFor(startingElement);
//        if (annotation == null) return null;
//        // signature:OCamlInferredSignature{name='f', kind=VARIABLE, type=''a -> 'b -> int', range=(23,24)}
//        System.out.println("signature:"+annotation);
//        return startingElement;
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
