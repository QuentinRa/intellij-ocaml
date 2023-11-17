package com.ocaml.icons

import com.dune.icons.DuneIcons
import com.intellij.icons.AllIcons
import com.intellij.openapi.util.IconLoader
import com.intellij.ui.AnimatedIcon
import javax.swing.Icon


/**
 * [Working with Icons and Images](https://plugins.jetbrains.com/docs/intellij/work-with-icons-and-images.html)
 * [IntelliJ Platform UI Guidelines](https://jetbrains.design/intellij/principles/icons/)
 * [IntelliJ Platform Icons](https://intellij-icons.jetbrains.design/)
 * [intellij-icon-generator](https://github.com/bjansen/intellij-icon-generator)
 *
 * - Node, action, filetype : 16x16
 * - Tool window            : 13x13
 * - Editor gutter          : 12x12
 * - Font                   : Gotham
 *
 * @see com.intellij.icons.AllIcons
 * @see com.intellij.icons.AllIcons.FileTypes
 * @see com.intellij.icons.AllIcons.General
 * @see com.intellij.icons.AllIcons.Gutter
 * @see com.intellij.icons.AllIcons.Nodes
 */
object OCamlIcons {
    private fun loadIcon(path: String): Icon {
        return IconLoader.getIcon(path, OCamlIcons::class.java)
    }

    object Gutter {
        val IMPLEMENTED = AllIcons.Gutter.ImplementedMethod
        val IMPLEMENTING = AllIcons.Gutter.ImplementingMethod
    }

    /**
     * Every icon used on a node (ex: module icon, sdk icon, library icon, console icon, etc.)
     */
    object Nodes {
        val OCAML_MODULE = loadIcon("/icons/ocamlModule.svg")
        val OCAML_SDK = loadIcon("/icons/ocamlSdk.svg")
        val OCAML_CONSOLE = loadIcon("/icons/ocamlTool.svg")
        val OCAML_LIBRARY = loadIcon("/icons/ocamlModule.svg")
        val OCL_FILE_MODULE = loadIcon("/icons/ocamlLogo.svg")
        val OCL_FILE_MODULE_INTERFACE = loadIcon("/icons/ocamlBlue.png")
        val INNER_MODULE = loadIcon("/icons/innerModule.svg")
        val INNER_MODULE_INTF = loadIcon("/icons/innerModuleIntf.svg")
        val MODULE_TYPE = loadIcon("/icons/javaModuleType.svg")
        val FUNCTOR = AllIcons.Nodes.Artifact
        val LET = AllIcons.Nodes.Variable
        val VAL = AllIcons.Nodes.Variable
        val ATTRIBUTE = AllIcons.Nodes.Property
        val FUNCTION = AllIcons.Nodes.Function
        val VARIABLE = AllIcons.Nodes.Variable
        val METHOD = AllIcons.Nodes.Method
        val CLASS = AllIcons.Nodes.Class
        val EXCEPTION = AllIcons.Nodes.ExceptionClass
        val EXTERNAL = AllIcons.Nodes.Enum
        val OBJECT = AllIcons.Json.Object
        val VIRTUAL_NAMESPACE = AllIcons.Actions.GroupByPackage
        val OPEN = AllIcons.Actions.GroupByModule
        val INCLUDE = AllIcons.Actions.GroupByModule
        val TYPE = loadIcon("/icons/type.svg")
        val VARIANT = loadIcon("/icons/variant.svg")
    }

    object FileTypes {
        val OCAML_ANNOT = loadIcon("/icons/annotFile.svg")
        val OCAML_SOURCE = loadIcon("/icons/mlFile.svg")
        val OCAML_INTERFACE = loadIcon("/icons/mliFile.svg")
        val OCAML_SOURCE_AND_INTERFACE = loadIcon("/icons/ocamlFiles.svg")
    }

    object UI {
        val LOADING: Icon = AnimatedIcon.Default()
        val FIELD_VALID = AllIcons.General.InspectionsOK // check?
        val FIELD_INVALID = AllIcons.General.Error
        val FIELD_WARNING = AllIcons.General.Warning
    }

    object External {
        // ClionMakefileIcons.Icons.Makefile
        // MakefileIcons.Icons.Makefile
        val MAKEFILE = loadIcon("/icons/makefile.svg")

        // DuneIcons.Nodes.Dune
        val DUNE: Icon = DuneIcons.Nodes.DUNE

        // PycharmDSCustomizationIcons.Icons.ExecuteSelection
        val ExecuteSelection = loadIcon("/icons/executeSelection.svg")
    }
}
