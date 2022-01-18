package com.ocaml.icons;

import javax.swing.*;

import static com.intellij.openapi.util.IconLoader.getIcon;

/**
 * https://www.jetbrains.org/intellij/sdk/docs/reference_guide/work_with_icons_and_images.html
 * https://jetbrains.design/intellij/principles/icons/
 * https://jetbrains.design/intellij/resources/icons_list/
 *
 * Node, action, filetype : 16x16
 * Tool window            : 13x13
 * Editor gutter          : 12x12
 * Font                   : Gotham
 *
 * @see com.intellij.icons.AllIcons
 * @see com.intellij.icons.AllIcons.FileTypes
 * @see com.intellij.icons.AllIcons.General
 * @see com.intellij.icons.AllIcons.Gutter
 * @see com.intellij.icons.AllIcons.Nodes
 */
public class OCamlIcons {

    private static Icon loadIcon(String path) {
        return getIcon(path, OCamlIcons.class);
    }

    public static final class Nodes {
        public static final Icon OCAML_MODULE = loadIcon("/icons/ocamlModule.svg");
        public static final Icon OCAML_SDK = loadIcon("/icons/ocamlSdk.svg");
    }

    public static final class External {
        public static final Icon Markdown = loadIcon("/icons/makefile.svg");
    }

}
