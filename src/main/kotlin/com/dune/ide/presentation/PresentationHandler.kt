/**
 * A presentation is what is displayed in the editor, such as
 * in the structure view, or inside the other menus.
 */
package com.dune.ide.presentation

import com.dune.language.psi.api.DuneNamedElement
import com.dune.language.psi.files.DuneFile
import com.intellij.ide.projectView.PresentationData
import com.intellij.navigation.ItemPresentation
import com.intellij.psi.PsiElement

fun getPresentationForStructure(psi: PsiElement): ItemPresentation {
    if (psi is DuneFile) return psi.presentation!!
    val presentation = when(psi) {
        is DuneNamedElement -> psi.name
        else -> null
    }
    val icon = null
    val textAttributes = null
    return PresentationData(presentation, null, icon, textAttributes)
}