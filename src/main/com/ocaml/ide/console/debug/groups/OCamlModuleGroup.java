package com.ocaml.ide.console.debug.groups;

import com.intellij.ide.structureView.StructureViewTreeElement;
import com.intellij.ide.util.treeView.smartTree.TreeElement;
import com.intellij.navigation.ItemPresentation;
import com.ocaml.OCamlBundle;
import com.ocaml.ide.console.debug.groups.elements.OCamlModuleTypeElement;
import com.ocaml.ide.console.debug.groups.elements.OCamlTreeElement;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * A module group,
 * with a nested group with the modules types.
 */
public class OCamlModuleGroup extends TreeElementGroup {

    private final OCamlModuleTypeGroup moduleTypeGroup = new OCamlModuleTypeGroup();

    public OCamlModuleGroup() {
        super(TreeElementGroupKind.MODULES);
    }

    @Override public TreeElement @NotNull [] getChildren() {
        Stream<OCamlTreeElement> sorted = elements.stream().sorted();
        List<TreeElement> l = sorted.collect(Collectors.toList());
        l.add(0, moduleTypeGroup);
        return l.toArray(new TreeElement[0]);
    }

    // we are faking that this is added in module
    @Override public void addElement(OCamlTreeElement e) {
        if (e instanceof OCamlModuleTypeElement) {
            moduleTypeGroup.elements.add(e);
        } else {
            super.addElement(e);
        }
    }

    // we are removing what we added in the module secretly
    @Override public void removeElement(TreeElement e) {
        if (e instanceof OCamlModuleTypeElement) {
            moduleTypeGroup.elements.remove(e);
        } else super.removeElement(e);
    }

    /**
     * Module types
     */
    private static class OCamlModuleTypeGroup implements StructureViewTreeElement {
        private final Set<OCamlTreeElement> elements = new HashSet<>();

        @Contract(pure = true) @Override public @NotNull Object getValue() { return ""; }
        @Override public void navigate(boolean requestFocus) {}
        @Override public boolean canNavigate() { return false; }
        @Override public boolean canNavigateToSource() { return false; }

        @Override public TreeElement @NotNull [] getChildren() {
            return elements.stream().sorted().toArray(OCamlTreeElement[]::new);
        }

        @Override public @NotNull ItemPresentation getPresentation() {
            return new ItemPresentation() {
                @Override public String getPresentableText() {
                    return OCamlBundle.message("repl.variable.view.modules.type");
                }

                @Override public @Nullable Icon getIcon(boolean unused) {
                    return null;
                }
            };
        }
    }
}
