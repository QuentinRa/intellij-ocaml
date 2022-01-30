package com.ocaml.ide.actions.editor;

import com.intellij.openapi.actionSystem.ActionPromoter;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.DataContext;
import com.ocaml.ide.files.FileHelper;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.stream.Collectors;

public class OCamlActionPromoter implements ActionPromoter {

    @Override
    public @Nullable List<AnAction> promote(@NotNull List<? extends AnAction> actions, @NotNull DataContext context) {
        return FileHelper.isOCamlContext(context) ?
                actions.stream().filter(i -> i instanceof OCamlPromotedAction).collect(Collectors.toList()) :
                List.of();
    }

}
