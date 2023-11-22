// Copyright 2000-2019 JetBrains s.r.o. Use of this source code is governed by the Apache 2.0 license that can be found in the LICENSE file.
// Was changed a bit (createPathEditor values, removing Jrt)
package com.ocaml.ide.wizard.minor.settings.java;

import com.intellij.icons.AllIcons;
import com.intellij.openapi.fileChooser.FileChooserDescriptor;
import com.intellij.openapi.projectRoots.Sdk;
import com.intellij.openapi.projectRoots.ui.SdkPathEditor;
import com.intellij.openapi.roots.OrderRootType;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.ui.components.JBList;
import com.ocaml.OCamlBundle;
import com.ocaml.icons.OCamlIcons;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;

/**
 * @author anna
 */
public class ClassesOrderRootTypeUIFactory implements FakeOrderRootTypeUIFactory {
    @Override
    public SdkPathEditor createPathEditor(Sdk sdk) {
        return new MySdkPathEditor(new FileChooserDescriptor(false, true, false, false, false, true));
    }

    @Override
    public Icon getIcon() {
        return AllIcons.Nodes.CompiledClassesFolder;
    }

    @Override
    public String getNodeText() {
        return OCamlBundle.message("library.classes.node");
    }

    private static class MySdkPathEditor extends SdkPathEditor {
        MySdkPathEditor(FileChooserDescriptor descriptor) {
            super(OCamlBundle.message("sdk.configure.classpath.tab"), OrderRootType.CLASSES, descriptor);
        }

        @Override
        protected ListCellRenderer<VirtualFile> createListCellRenderer(JBList<VirtualFile> list) {
            return new PathCellRenderer() {
                @Override
                protected void customizeCellRenderer(@NotNull JList<? extends VirtualFile> list, VirtualFile file, int index, boolean selected, boolean focused) {
                    super.customizeCellRenderer(list, file, index, selected, focused);
                    setIcon(OCamlIcons.Nodes.OCAML_MODULE);
                }
            };
        }
    }
}