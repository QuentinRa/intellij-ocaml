package com.ocaml.ide.files.nodes;

import com.intellij.ide.projectView.PresentationData;
import com.intellij.ide.projectView.ProjectViewNode;
import com.intellij.ide.projectView.ProjectViewNodeDecorator;
import com.intellij.ide.projectView.impl.nodes.PsiDirectoryNode;
import com.intellij.ide.util.treeView.NodeDescriptor;
import com.intellij.packageDependencies.ui.PackageDependenciesNode;
import com.intellij.ui.ColoredTreeCellRenderer;
import com.ocaml.icons.OCamlIcons;
import com.ocaml.sdk.utils.OCamlSdkRootsManager;
import com.ocaml.utils.MayNeedToBeTested;

/**
 * Add an icon to the nodes in "External libraries" that
 * are inside an OCaml SDK.
 */
@MayNeedToBeTested
public class OCamlLibraryRootsNodeDecorator implements ProjectViewNodeDecorator {

    @Override public void decorate(ProjectViewNode<?> node, PresentationData data) {
        if (!(node instanceof PsiDirectoryNode)) return;

        // get library node
        NodeDescriptor<?> parentDescriptor = node.getParentDescriptor();
        if (!OCamlSdkRootsManager.isLibraryRootForOCamlSdk(parentDescriptor)) return;

        // set icon
        data.setIcon(OCamlIcons.Nodes.OCAML_LIBRARY);
    }

    @Override public void decorate(PackageDependenciesNode node, ColoredTreeCellRenderer cellRenderer) {
    }
}
