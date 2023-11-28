/**
 * A presentation is what is displayed in the editor, such as
 * in the structure view, or inside the other menus.
 */
package com.ocaml.ide.presentation

import com.intellij.ide.projectView.PresentationData
import com.intellij.navigation.ItemPresentation
import com.intellij.openapi.util.Iconable
import com.intellij.openapi.vfs.VfsUtil
import com.intellij.psi.PsiElement
import com.ocaml.language.base.OCamlFileBase
import com.ocaml.language.psi.OCamlLetBindings
import com.ocaml.language.psi.api.OCamlNamedElement
import com.ocaml.language.psi.api.OCamlQualifiedNamedElement

private fun presentableName(psi: PsiElement): String? {
    return when(psi) {
        is OCamlNamedElement -> psi.name
        is OCamlLetBindings -> "let " + psi.letBindingList.map { it.name }.joinToString(", ")
        else -> null
    }
}

fun getPresentationForStructure(psi: PsiElement): ItemPresentation {
    if (psi is OCamlFileBase) return psi.presentation!!
    val presentation = buildString {
        append(presentableName(psi))
    }
    val icon = when(psi) {
        is OCamlLetBindings -> null
        else -> psi.getIcon(Iconable.ICON_FLAG_VISIBILITY)
    }
    val textAttributes = null
    return PresentationData(presentation, null, icon, textAttributes)
}

fun getPresentationForElement(psi: OCamlQualifiedNamedElement): ItemPresentation? {
    val presentation = psi.qualifiedName
    val icon = psi.getIcon(Iconable.ICON_FLAG_VISIBILITY)
    // Show a glimpse of where the element is stored
    // While it shouldn't occur often
    val file =  psi.containingFile.virtualFile
    val parentFolder = file.parent
    val locationString = if (parentFolder != null && parentFolder.isValid && parentFolder.isDirectory) {
        "..." + VfsUtil.VFS_SEPARATOR_CHAR + parentFolder.presentableName + VfsUtil.VFS_SEPARATOR_CHAR + file.presentableName
    } else null
    val textAttributes = null
    return PresentationData(presentation, locationString, icon, textAttributes)
}