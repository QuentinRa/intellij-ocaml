package com.ocaml.ide.console;

import com.intellij.openapi.ui.*;
import com.intellij.ui.*;
import com.intellij.ui.treeStructure.*;

import javax.swing.tree.*;

public class OCamlVariablesView extends SimpleToolWindowPanel {

    public OCamlVariablesView(OCamlConsoleView console) {
        super(true, true);

        // todo: :(
        var tree = new Tree((TreeNode) null);
        tree.setRootVisible(false);
        tree.getEmptyText().setText("No variables yet!");

        setContent(ScrollPaneFactory.createScrollPane(tree));
    }
}
