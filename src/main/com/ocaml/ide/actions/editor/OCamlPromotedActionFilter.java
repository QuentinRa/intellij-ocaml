package com.ocaml.ide.actions.editor;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.DataContext;
import com.ocaml.ide.files.FileHelper;
import com.ocaml.utils.adaptor.SinceIdeVersion;
import com.ocaml.utils.adaptor.UntilIdeVersion;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.stream.Collectors;

public class OCamlPromotedActionFilter {

    @SuppressWarnings("unused") @UntilIdeVersion(release = "203")
    public static List<AnAction> filterActions(@NotNull List<AnAction> actions, @NotNull DataContext context) {
        return filterActions2(actions, context);
    }

    @SuppressWarnings("unused") @SinceIdeVersion(release = "211")
    public static List<AnAction> filterActions2(@NotNull List<? extends AnAction> actions, @NotNull DataContext context) {
        return FileHelper.isOCamlContext(context) ?
                actions.stream().filter(i -> i instanceof OCamlPromotedAction).collect(Collectors.toList()) :
                List.of();
    }

}
