package com.ocaml.ide.console.debug;

import com.intellij.icons.AllIcons;
import com.intellij.ide.util.treeView.smartTree.SmartTreeStructure;
import com.intellij.openapi.Disposable;
import com.intellij.openapi.actionSystem.ActionManager;
import com.intellij.openapi.actionSystem.ActionPlaces;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.DefaultActionGroup;
import com.intellij.openapi.actionSystem.impl.ActionToolbarImpl;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.SimpleToolWindowPanel;
import com.intellij.ui.ScrollPaneFactory;
import com.intellij.ui.tree.AsyncTreeModel;
import com.intellij.ui.tree.StructureTreeModel;
import com.intellij.ui.treeStructure.Tree;
import com.ocaml.ide.console.OCamlConsoleView;
import com.ocaml.ide.console.debug.groups.ShowElementGroupAction;
import com.ocaml.ide.console.debug.groups.TreeElementGroup;
import com.ocaml.ide.console.debug.groups.TreeElementGroupKind;
import com.ocaml.ide.console.debug.groups.elements.OCamlFunctionElement;
import com.ocaml.ide.console.debug.groups.elements.OCamlTreeElement;
import com.ocaml.ide.console.debug.groups.elements.OCamlVariableElement;
import com.ocaml.utils.logs.OCamlLogger;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.*;

/**
 * Panel with the variables
 */
public class OCamlVariablesView extends SimpleToolWindowPanel implements Disposable {
    private static final Logger LOG = OCamlLogger.getREPLInstance("variables_view");

    private final StructureTreeModel<?> structureTreeModel;
    private final SmartTreeStructure treeStructure;
    private final OCamlStupidTreeModel treeModel;

    public OCamlVariablesView(@NotNull OCamlConsoleView consoleView) {
        super(true, true);
        Project project = consoleView.getProject();

        treeModel = new OCamlStupidTreeModel();
        treeStructure = new SmartTreeStructure(project, treeModel);

        structureTreeModel = new StructureTreeModel<>(treeStructure, this);
        var asyncTreeModel = new AsyncTreeModel(structureTreeModel, this);

        var tree = new Tree(asyncTreeModel);
        tree.setRootVisible(false);

        JPanel content = new JPanel(new BorderLayout());
        content.add(createToolbar(tree), BorderLayout.WEST);
        content.add(ScrollPaneFactory.createScrollPane(tree), BorderLayout.CENTER);
        setContent(content);
    }

    private @NotNull JComponent createToolbar(Tree tree) {
        var group = new DefaultActionGroup();
        group.addAction(createSettingsActionGroup());
        // create
        ActionToolbarImpl toolbar = (ActionToolbarImpl) ActionManager.getInstance().createActionToolbar(
                ActionPlaces.TOOLBAR, group, false
        );
        toolbar.setTargetComponent(tree);
        return toolbar.getComponent();
    }

    private @NotNull AnAction createSettingsActionGroup() {
        var group = new DefaultActionGroup("Variable View Settings", true);
        group.getTemplatePresentation().setIcon(AllIcons.Actions.Show);
        group.add(new ShowElementGroupAction(TreeElementGroupKind.EXCEPTION, this));
        group.add(new ShowElementGroupAction(TreeElementGroupKind.MODULE, this));
        group.add(new ShowElementGroupAction(TreeElementGroupKind.TYPE, this));
        group.add(new ShowElementGroupAction(TreeElementGroupKind.FUNCTIONS, this));
        group.add(new ShowElementGroupAction(TreeElementGroupKind.VARIABLES, this));
        return group;
    }

    /**
     * Add functions and variables in the tree.
     *
     * @param newEntry lines of the new item (ex: val x : int = 5)
     */
    public void rebuild(@NotNull String newEntry) {
//        System.out.println("with:"+newEntry);
        if (!newEntry.startsWith("val")) return; // not possible yet
        // handle "val" -> variables or functions

        newEntry = newEntry.trim();
        // todo: log
        if (newEntry.endsWith("<fun>")) LOG.debug("adding a function:" + newEntry);
        else LOG.debug("adding a variable:" + newEntry);

        // todo: const + parser

        // find name
        int val = newEntry.indexOf("val");
        int colon = newEntry.indexOf(":");
        int equals = newEntry.indexOf("=");
        String name = newEntry.substring(val + 3 + 1, colon - 1);

        // find type
        String type = newEntry.substring(colon + 1, equals - 1);

        OCamlTreeElement element;
        TreeElementGroup group;
        if (!newEntry.endsWith("<fun>")) {
            // find value
            String value = newEntry.substring(equals + 1);
            element = new OCamlVariableElement(name, value, type);
            group = treeModel.variables;
        } else {
            element = new OCamlFunctionElement(name, type);
            group = treeModel.functions;
        }

        // remove
        treeModel.remove(element);
        // add
        group.elements.add(element);

        invalidateTree();
    }

    public OCamlStupidTreeModel getTreeModel() {
        return treeModel;
    }

    public void invalidateTree() {
        ApplicationManager.getApplication().invokeLater(() -> ApplicationManager.getApplication().runWriteAction(() -> {
            structureTreeModel.getInvoker().invokeLater(() -> {
                treeStructure.rebuildTree();
                structureTreeModel.invalidate();
            });
        }));
    }

    @Override public void dispose() {
    }
}