package com.ocaml.sdk.utils;

import com.intellij.ide.projectView.impl.nodes.NamedLibraryElement;
import com.intellij.ide.projectView.impl.nodes.NamedLibraryElementNode;
import com.intellij.ide.util.treeView.NodeDescriptor;
import com.intellij.openapi.projectRoots.Sdk;
import com.intellij.openapi.roots.JdkOrderEntry;
import com.intellij.openapi.roots.OrderEntry;
import com.ocaml.sdk.OCamlSdkType;
import com.ocaml.sdk.providers.OCamlSdkProvidersManager;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public final class OCamlSdkRootsManager {

    /**
     * @see com.ocaml.sdk.providers.OCamlSdkProvider#getAssociatedSourcesFolders(String)
     */
    public static @NotNull List<String> getSourcesFolders(String sdkHome) {
        return new ArrayList<>(OCamlSdkProvidersManager.INSTANCE.getAssociatedSourcesFolders(sdkHome));
    }

    @SuppressWarnings("BooleanMethodIsAlwaysInverted")
    public static boolean isLibraryRootForOCamlSdk(NodeDescriptor<?> parent) {
        if (!(parent instanceof NamedLibraryElementNode)) return false;
        NamedLibraryElement library = ((NamedLibraryElementNode) parent).getValue();
        if (library == null) return false;

        // check SDK
        OrderEntry orderEntry = library.getOrderEntry();
        if (!(orderEntry instanceof JdkOrderEntry)) return false;
        Sdk sdk = ((JdkOrderEntry) orderEntry).getJdk();
        if (sdk == null) return false;

        // final ~~~boss~~ check
        return sdk.getSdkType() instanceof OCamlSdkType;
    }
}
