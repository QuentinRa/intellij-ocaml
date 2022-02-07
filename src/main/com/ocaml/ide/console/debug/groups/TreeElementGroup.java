package com.ocaml.ide.console.debug.groups;

import com.intellij.ide.structureView.StructureViewTreeElement;
import com.intellij.ide.util.treeView.smartTree.TreeElement;
import com.intellij.navigation.ItemPresentation;
import com.ocaml.ide.console.debug.groups.elements.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.util.HashSet;
import java.util.Set;

/**
 * A group of elements.
 * This is used to split values in categories.
 */
public class TreeElementGroup implements StructureViewTreeElement {

    private final Set<OCamlTreeElement> elements = new HashSet<>();

    private final TreeElementGroupKind treeElementGroupKind;
    private boolean isVisible;

    public TreeElementGroup(@NotNull TreeElementGroupKind kind) {
        this.treeElementGroupKind = kind;
        this.isVisible = true;
    }

    @Override public @NotNull ItemPresentation getPresentation() {
        return new ItemPresentation() {
            @Override public String getPresentableText() {
                return treeElementGroupKind.displayName;
            }

            @Override public @Nullable Icon getIcon(boolean unused) {
                return null;
            }
        };
    }

    @Override public TreeElement @NotNull [] getChildren() {
        return elements.stream().sorted().toArray(OCamlTreeElement[]::new);
    }

    @Override public Object getValue() {
        return "";
    }

    @Override public void navigate(boolean requestFocus) {

    }

    @Override public boolean canNavigate() {
        return false;
    }

    @Override public boolean canNavigateToSource() {
        return false;
    }

    public boolean isVisible() {
        return isVisible;
    }

    public void setVisible(boolean state) {
        isVisible = state;
    }

    public void addElement(OCamlTreeElement e) {
        elements.add(e);
    }

    public void removeElement(TreeElement e) {
        switch (treeElementGroupKind) {
            case EXCEPTIONS:
                if (e instanceof OCamlExceptionElement)
                    elements.remove(e);
                break;
            case MODULES:
                if (e instanceof OCamlModuleElement)
                    elements.remove(e);
                break;
            case TYPES:
                if (e instanceof OCamlTypeElement)
                    elements.remove(e);
                break;
            case FUNCTIONS:
            case VARIABLES:
                if (e instanceof OCamlVariableElement || e instanceof OCamlFunctionElement)
                    elements.remove(e);
                break;
        }
    }

}
