package com.ocaml.ide.console;

import com.intellij.ide.structureView.*;
import com.intellij.ide.util.treeView.smartTree.*;
import com.intellij.navigation.*;
import com.intellij.openapi.*;
import com.intellij.openapi.project.*;
import com.intellij.openapi.ui.*;
import com.intellij.ui.*;
import com.intellij.ui.tree.*;
import com.intellij.ui.treeStructure.*;
import org.jetbrains.annotations.*;

import javax.swing.*;

public class OCamlVariablesView extends SimpleToolWindowPanel implements Disposable {

    public OCamlVariablesView(OCamlConsoleView console) {
        super(true, true);
        Project project = console.getProject();

        var variablesFile = console.getFile();
        var treeStructure = new SmartTreeStructure(project, new StructureViewModelBase(variablesFile,
                console.getEditor(), new StructureViewTreeElement() {
            @Override public Object getValue() {
                return 5;
            }

            @Override public @NotNull ItemPresentation getPresentation() {
                return new ItemPresentation() {
                    @Override public String getPresentableText() {
                        return "x";
                    }

                    @Override public @Nullable Icon getIcon(boolean unused) {
                        return null;
                    }
                };
            }

            @Override public TreeElement @NotNull [] getChildren() {
                return new TreeElement[]{};
            }

            @Override public void navigate(boolean requestFocus) {

            }

            @Override public boolean canNavigate() {
                return false;
            }

            @Override public boolean canNavigateToSource() {
                return false;
            }
        }));
        var structureTreeModel = new StructureTreeModel<>(treeStructure, this);
        var asyncTreeModel = new AsyncTreeModel(structureTreeModel, this);

        var tree = new Tree(asyncTreeModel);
        tree.setRootVisible(false);
        tree.getEmptyText().setText("No variables yet!");

        setContent(ScrollPaneFactory.createScrollPane(tree));
    }

    @Override public void dispose() {

    }
}
