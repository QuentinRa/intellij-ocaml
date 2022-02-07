package com.ocaml.ide.console.debug.groups.elements;

import com.intellij.ide.structureView.StructureViewTreeElement;
import com.intellij.ide.util.treeView.smartTree.TreeElement;
import com.intellij.navigation.ItemPresentation;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;

/**
 * An element inside a group.
 * Could be a variable, a function, etc.
 */
public abstract class OCamlTreeElement implements StructureViewTreeElement {

    /**
     * Show the full value if less than MAX_VALUE_LENGTH characters.
     * otherwise, create two blocs of BLOC_LENGTH characters, separated by "...".
     */
    public static final int MAX_VALUE_LENGTH = 25;
    public static final int BLOC_LENGTH = 10;

    @NotNull private final String name;
    @NotNull private final String value;
    @NotNull private final String shortValuePreview;
    @NotNull private final String type;
    @NotNull private final Icon icon;

    public OCamlTreeElement(@NotNull String name, @NotNull String value,
                            @NotNull String type, @NotNull Icon icon) {
        this.name = name;
        this.value = value;
        this.icon = icon;
        this.type = type;

        // trunc the length of the value shown along the name
        int length = value.length();
        if (length > MAX_VALUE_LENGTH) {
            String end = value.substring(length - BLOC_LENGTH, length);
            shortValuePreview = value.substring(0, BLOC_LENGTH)+"..."+end;
        } else {
            shortValuePreview = value;
        }
    }

    @Override public Object getValue() {
        return value;
    }

    @Override public @NotNull ItemPresentation getPresentation() {
        return new ItemPresentation() {
            @Override public String getPresentableText() {
                return name + " = " + shortValuePreview;
            }

            @Override public Icon getIcon(boolean unused) {
                return icon;
            }

            @Override public String getLocationString() {
                return type;
            }
        };
    }

    @Override public TreeElement @NotNull [] getChildren() {
        return EMPTY_ARRAY;
    }

    @Override public void navigate(boolean requestFocus) {
    }

    @Override public boolean canNavigate() {
        return true;
    }

    @Override public boolean canNavigateToSource() {
        return true;
    }

    // compare
    @Override public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof OCamlTreeElement)) return false;

        OCamlTreeElement that = (OCamlTreeElement) o;

        return name.equals(that.name);
    }

    @Override public int hashCode() {
        return name.hashCode();
    }
}
