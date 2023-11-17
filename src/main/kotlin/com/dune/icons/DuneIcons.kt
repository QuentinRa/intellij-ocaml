package com.dune.icons

import com.intellij.icons.AllIcons
import com.intellij.openapi.util.IconLoader
import javax.swing.Icon

/**
 * https://www.jetbrains.org/intellij/sdk/docs/reference_guide/work_with_icons_and_images.html
 * https://jetbrains.design/intellij/principles/icons/
 * https://jetbrains.design/intellij/resources/icons_list/ or https://intellij-icons.jetbrains.design/
 * https://github.com/bjansen/intellij-icon-generator
 *
 *
 * Node, action, filetype : 16x16
 * Tool window            : 13x13
 * Editor gutter          : 12x12
 * Font                   : Gotham
 *
 * @see AllIcons
 * @see AllIcons.FileTypes
 * @see AllIcons.General
 * @see AllIcons.Gutter
 * @see AllIcons.Nodes
 */
object DuneIcons {
    private fun loadIcon(path: String): Icon {
        return IconLoader.getIcon(path, DuneIcons::class.java)
    }

    object Nodes {
        val DUNE = loadIcon("/icons/duneLogo.svg")
        val OBJECT = AllIcons.Json.Object
    }

    object FileTypes {
        val DUNE_FILE = loadIcon("/icons/duneFile.svg")
    }
}