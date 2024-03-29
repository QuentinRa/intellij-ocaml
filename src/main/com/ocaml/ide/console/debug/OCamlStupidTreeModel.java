package com.ocaml.ide.console.debug;

import com.intellij.ide.structureView.StructureViewTreeElement;
import com.intellij.ide.util.treeView.smartTree.*;
import com.intellij.navigation.ItemPresentation;
import com.ocaml.ide.console.debug.groups.OCamlModuleGroup;
import com.ocaml.ide.console.debug.groups.TreeElementGroup;
import com.ocaml.ide.console.debug.groups.TreeElementGroupKind;
import com.ocaml.ide.console.debug.groups.elements.OCamlTreeElement;
import com.ocaml.utils.adaptor.UntilIdeVersion;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.util.ArrayList;

/**
 * We are not using the proper classes, so this is
 * a stupid tree model. We should change this later, when we got
 * a parser, and more important features were added.
 */
public class OCamlStupidTreeModel implements TreeModel {

    public final TreeElementGroup exceptions = new TreeElementGroup(TreeElementGroupKind.EXCEPTIONS);
    public final TreeElementGroup types = new TreeElementGroup(TreeElementGroupKind.TYPES);
    public final TreeElementGroup modules = new OCamlModuleGroup(); // this one is special
    public final TreeElementGroup functions = new TreeElementGroup(TreeElementGroupKind.FUNCTIONS);
    public final TreeElementGroup variables = new TreeElementGroup(TreeElementGroupKind.VARIABLES);
    public TreeElement root = new StructureViewTreeElement() {
        @Contract(pure = true) @Override public @Nullable Object getValue() {
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

                @UntilIdeVersion(release = "203")
                @Override public @Nullable String getLocationString() {
                    return null;
                }
            };
        }

        @Override public TreeElement [] getChildren() {
            ArrayList<TreeElement> e = new ArrayList<>();
            if (exceptions.isVisible()) e.add(exceptions);
            if (types.isVisible()) e.add(types);
            if (modules.isVisible()) e.add(modules);
            if (functions.isVisible()) e.add(functions);
            if (variables.isVisible()) e.add(variables);
            return e.toArray(new TreeElement[0]);
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

    /**
     * Remove an element in every group
     *
     * @param o the element
     */
    public void remove(Object o) {
        for (TreeElement e : root.getChildren()) {
            if (!(e instanceof TreeElementGroup)) continue;
            ((TreeElementGroup) e).removeElement((OCamlTreeElement) o);
        }
    }

    @Override public @NotNull TreeElement getRoot() {
        return root;
    }

    @Override public Grouper [] getGroupers() {
        return new Grouper[0];
    }

    @Override public Sorter [] getSorters() {
        return new Sorter[0];
    }

    @Override public Filter [] getFilters() {
        return new Filter[0];
    }
}
