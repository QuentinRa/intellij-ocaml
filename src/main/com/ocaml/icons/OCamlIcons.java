package com.ocaml.icons;

import com.intellij.icons.*;
import com.intellij.ui.*;

import javax.swing.*;

import static com.intellij.openapi.util.IconLoader.getIcon;

/**
 * https://www.jetbrains.org/intellij/sdk/docs/reference_guide/work_with_icons_and_images.html
 * https://jetbrains.design/intellij/principles/icons/
 * https://jetbrains.design/intellij/resources/icons_list/ or https://intellij-icons.jetbrains.design/
 * https://github.com/bjansen/intellij-icon-generator
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

    /** Every icon used on a node (ex: module icon, sdk icon, library icon, console icon, etc.) */
    public static final class Nodes {
        public static final Icon OCAML_MODULE = loadIcon("/icons/ocamlModule.svg");
        public static final Icon OCAML_SDK = loadIcon("/icons/ocamlSdk.svg");
    }

    public static final class UI {
        public static final Icon LOADING = new AnimatedIcon.Default();
        public static final Icon FIELD_VALID = AllIcons.General.InspectionsOK; // check?
        public static final Icon FIELD_INVALID = AllIcons.General.Error;
    }

    public static final class External {
        // ClionMakefileIcons.Icons.Makefile
        // MakefileIcons.Icons.Makefile
        public static final Icon MAKEFILE = loadIcon("/icons/makefile.svg");
    }

}
