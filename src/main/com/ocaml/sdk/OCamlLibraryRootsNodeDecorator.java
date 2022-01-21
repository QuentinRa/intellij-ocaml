package com.ocaml.sdk;

import com.intellij.ide.projectView.PresentationData;
import com.intellij.ide.projectView.ProjectViewNode;
import com.intellij.ide.projectView.ProjectViewNodeDecorator;
import com.intellij.ide.projectView.impl.nodes.NamedLibraryElement;
import com.intellij.ide.projectView.impl.nodes.NamedLibraryElementNode;
import com.intellij.ide.projectView.impl.nodes.PsiDirectoryNode;
import com.intellij.ide.util.treeView.NodeDescriptor;
import com.intellij.openapi.projectRoots.Sdk;
import com.intellij.openapi.roots.JdkOrderEntry;
import com.intellij.openapi.roots.OrderEntry;
import com.intellij.packageDependencies.ui.PackageDependenciesNode;
import com.intellij.ui.ColoredTreeCellRenderer;
import com.ocaml.icons.OCamlIcons;

/**
 * Add an icon to the nodes in "External libraries" that
 * are inside an OCaml SDK.
 */
public class OCamlLibraryRootsNodeDecorator implements ProjectViewNodeDecorator {

    @Override public void decorate(ProjectViewNode<?> node, PresentationData data) {
        if(!(node instanceof PsiDirectoryNode)) return;

        // get library node
        NodeDescriptor<?> parentDescriptor = node.getParentDescriptor();
        if (!(parentDescriptor instanceof NamedLibraryElementNode)) return;
        NamedLibraryElement library = ((NamedLibraryElementNode) parentDescriptor).getValue();
        if (library == null) return;

        // check SDK
        OrderEntry orderEntry = library.getOrderEntry();
        if (!(orderEntry instanceof JdkOrderEntry)) return;
        Sdk sdk = ((JdkOrderEntry) orderEntry).getJdk();
        if (sdk == null) return;
        if (!(sdk.getSdkType() instanceof OCamlSdkType)) return;
        // set icon
        data.setIcon(OCamlIcons.Nodes.OCAML_LIBRARY);
    }

    @Override public void decorate(PackageDependenciesNode node, ColoredTreeCellRenderer cellRenderer) {
    }
}
