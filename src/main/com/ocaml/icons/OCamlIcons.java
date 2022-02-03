package com.ocaml.icons;

import com.dune.icons.DuneIcons;
import com.intellij.icons.AllIcons;
import com.intellij.openapi.util.IconLoader;
import com.intellij.ui.AnimatedIcon;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;

/**
 * https://www.jetbrains.org/intellij/sdk/docs/reference_guide/work_with_icons_and_images.html
 * https://jetbrains.design/intellij/principles/icons/
 * https://jetbrains.design/intellij/resources/icons_list/ or https://intellij-icons.jetbrains.design/
 * https://github.com/bjansen/intellij-icon-generator
 * <p>
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

    private static @NotNull Icon loadIcon(@NotNull String path) {
        return IconLoader.getIcon(path, OCamlIcons.class);
    }

    public static final class Gutter {
        public static final Icon IMPLEMENTED = AllIcons.Gutter.ImplementedMethod;
        public static final Icon IMPLEMENTING = AllIcons.Gutter.ImplementingMethod;
    }

    /**
     * Every icon used on a node (ex: module icon, sdk icon, library icon, console icon, etc.)
     */
    public static final class Nodes {
        public static final Icon OCAML_MODULE = loadIcon("/icons/ocamlModule.svg");
        public static final Icon OCAML_SDK = loadIcon("/icons/ocamlSdk.svg");
        public static final Icon OCAML_CONSOLE = loadIcon("/icons/ocamlTool.svg");
        public static final Icon OCAML_LIBRARY = loadIcon("/icons/ocamlModule.svg");

        public static final Icon OCL_FILE_MODULE = loadIcon("/icons/ocamlLogo.svg");
        public static final Icon OCL_FILE_MODULE_INTERFACE = loadIcon("/icons/ocamlBlue.png");
        public static final Icon INNER_MODULE = loadIcon("/icons/innerModule.svg");
        public static final Icon INNER_MODULE_INTF = loadIcon("/icons/innerModuleIntf.svg");
        public static final Icon MODULE_TYPE = loadIcon("/icons/javaModuleType.svg");
        public static final Icon FUNCTOR = AllIcons.Nodes.Artifact;
        public static final Icon LET = AllIcons.Nodes.Variable;
        public static final Icon VAL = AllIcons.Nodes.Variable;
        public static final Icon ATTRIBUTE = AllIcons.Nodes.Property;
        public static final Icon FUNCTION = AllIcons.Nodes.Function;
        public static final Icon METHOD = AllIcons.Nodes.Method;
        public static final Icon CLASS = AllIcons.Nodes.Class;
        public static final Icon EXCEPTION = AllIcons.Nodes.ExceptionClass;
        public static final Icon EXTERNAL = AllIcons.Nodes.Enum;
        public static final Icon OBJECT = AllIcons.Json.Object;
        public static final Icon VIRTUAL_NAMESPACE = AllIcons.Actions.GroupByPackage;
        public static final Icon OPEN = AllIcons.Actions.GroupByModule;
        public static final Icon INCLUDE = AllIcons.Actions.GroupByModule;
        public static final Icon TYPE = loadIcon("/icons/type.svg");
        public static final Icon VARIANT = loadIcon("/icons/variant.svg");
    }

    public static final class FileTypes {
        public static final Icon OCAML_SOURCE = loadIcon("/icons/mlFile.svg");
        public static final Icon OCAML_INTERFACE = loadIcon("/icons/mliFile.svg");
        public static final Icon OCAML_SOURCE_AND_INTERFACE = loadIcon("/icons/ocamlFiles.svg");
    }

    public static final class UI {
        public static final Icon LOADING = new AnimatedIcon.Default();
        public static final Icon FIELD_VALID = AllIcons.General.InspectionsOK; // check?
        public static final Icon FIELD_INVALID = AllIcons.General.Error;
        public static final Icon FIELD_WARNING = AllIcons.General.Warning;
    }

    public static final class External {
        // ClionMakefileIcons.Icons.Makefile
        // MakefileIcons.Icons.Makefile
        public static final Icon MAKEFILE = loadIcon("/icons/makefile.svg");
        // DuneIcons.Nodes.Dune
        public static final Icon DUNE = DuneIcons.Nodes.DUNE;
        // PycharmDSCustomizationIcons.Icons.ExecuteSelection
        public static final Icon ExecuteSelection = loadIcon("/icons/executeSelection.svg");;
    }

}
