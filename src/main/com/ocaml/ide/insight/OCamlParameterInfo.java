package com.ocaml.ide.insight;

import com.intellij.lang.parameterInfo.*;
import com.intellij.psi.PsiElement;
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

    private @Nullable PsiElement findArgumentList(ParameterInfoContext context, int parameterListStart) {
        System.out.println("find");
        int offset = context.getOffset() - 1;
        PsiElement element = context.getFile().findElementAt(offset);
        if (element == null) return null;
        System.out.println("found:"+element.getText());
        OCamlAnnotResultsService annot = element.getProject().getService(OCamlAnnotResultsService.class);
        OCamlInferredSignature annotation = annot.findAnnotationFor(element);
        if (annotation == null) return null;
        // signature:OCamlInferredSignature{name='f', kind=VARIABLE, type=''a -> 'b -> int', range=(23,24)}
        System.out.println("signature:"+annotation);
        return element;
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
