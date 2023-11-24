package com.ocaml.ide.console.debug.groups.elements;

import com.intellij.ide.util.treeView.smartTree.TreeElement;
import com.ocaml.icons.OCamlIcons;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class OCamlModuleElement extends OCamlTreeElement {
    public OCamlModuleElement(@NotNull String name, ArrayList<TreeElement> content) {
        super(name, null, null, OCamlIcons.Nodes.INNER_MODULE);
    }

    @Override public TreeElement [] getChildren() {
        return EMPTY_ARRAY;
    }
}
