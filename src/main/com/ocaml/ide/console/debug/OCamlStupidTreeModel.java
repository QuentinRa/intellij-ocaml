package com.ocaml.ide.console.debug;

import com.intellij.ide.structureView.StructureViewTreeElement;
import com.intellij.ide.util.treeView.smartTree.*;
import com.intellij.navigation.ItemPresentation;
import com.ocaml.ide.console.debug.groups.TreeElementGroup;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

/**
 * We are not using the proper classes, so this is
 * a stupid tree model. We should change this later, when we got
 * a parser, and more important features were added.
 */
public class OCamlStupidTreeModel implements TreeModel {

    public final TreeElementGroup modules = new TreeElementGroup("Modules");
    public final TreeElementGroup types = new TreeElementGroup("Types");
    public final TreeElementGroup functions = new TreeElementGroup("Functions");
    public final TreeElementGroup variables = new TreeElementGroup("Variables");

    @Override public @NotNull TreeElement getRoot() {
        return new StructureViewTreeElement() {
            @Override public Object getValue() {
                return null;
            }

            @Override public @NotNull ItemPresentation getPresentation() {
                return new ItemPresentation() {
                    @Override public String getPresentableText() {
                        return "Root";
                    }

                    @Override public @Nullable Icon getIcon(boolean unused) {
                        return null;
                    }
                };
            }

            @Override public TreeElement @NotNull [] getChildren() {
                return new TreeElement[]{
                        modules,
                        types,
                        functions,
                        variables
                };
            }

            @Override public void navigate(boolean requestFocus) {
            }

            @Override public boolean canNavigate() {
                return false;
            }

            @Override public boolean canNavigateToSource() {
                return false;
            }
        };
    }

    @Override public Grouper @NotNull [] getGroupers() {
        return new Grouper[0];
    }

    @Override public Sorter @NotNull [] getSorters() {
        return new Sorter[0];
    }

    @Override public Filter @NotNull [] getFilters() {
        return new Filter[0];
    }
}
