package com.ocaml.ide.lineMarkers

import com.intellij.codeInsight.daemon.RelatedItemLineMarkerInfo
import com.intellij.codeInsight.daemon.RelatedItemLineMarkerProvider
import com.intellij.codeInsight.navigation.NavigationGutterIconBuilder
import com.intellij.openapi.editor.markup.GutterIconRenderer
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiNameIdentifierOwner
import com.intellij.psi.search.GlobalSearchScope
import com.ocaml.icons.OCamlIcons
import com.ocaml.language.base.OCamlFileBase
import com.ocaml.language.psi.OCamlLetBinding
import com.ocaml.language.psi.OCamlValueDescription
import com.ocaml.language.psi.api.OCamlLetDeclaration
import com.ocaml.language.psi.api.OCamlNameIdentifierOwner
import com.ocaml.language.psi.api.OCamlNamedElement
import com.ocaml.language.psi.stubs.index.OCamlNamedElementIndex

// For tests:
// - In ML, can go to ML or MLI for VAL
// - In MLI, can go to ML for LET
class OCamlLineMarkerProvider : RelatedItemLineMarkerProvider() {
    override fun collectNavigationMarkers(
        element: PsiElement,
        result: MutableCollection<in RelatedItemLineMarkerInfo<*>>
    ) {
        val project = element.project
        val scope = GlobalSearchScope.allScope(project)
        (element as? OCamlLetBinding)?.let { collectLetNavigationMarkers(it, project, scope, result) }
        (element as? OCamlValueDescription)?.let { collectLetNavigationMarkers(it, project, scope, result) }
    }

    private fun collectLetNavigationMarkers(
        element: OCamlNameIdentifierOwner,
        project: Project,
        scope: GlobalSearchScope,
        result: MutableCollection<in RelatedItemLineMarkerInfo<*>>
    ) {
        // fixme: if inside a module, it is valid
        if (element !is OCamlLetDeclaration || !element.isGlobal()) return
        val isInterface = (element.containingFile as OCamlFileBase).isInterface()
        val letName = element.qualifiedName ?: return
        val elements = OCamlNamedElementIndex.Utils.findElementsByName(project, letName, scope)
        val filtered : Collection<OCamlNameIdentifierOwner> =
            if (isInterface) elements.filterIsInstance<OCamlLetBinding>() // LET => VAL
            else elements.filterIsInstance<OCamlValueDescription>()       // VAL => LET
        if (filtered.isEmpty()) return
        val marker: RelatedItemLineMarkerInfo<PsiElement>? = createMarkerInfo(element, isInterface, "let/val", filtered)
        if (marker != null) result.add(marker)
    }

    private fun <T : OCamlNamedElement?> createMarkerInfo(
        psiSource: PsiNameIdentifierOwner,
        isInterface: Boolean,
        method: String,
        relatedElements: Collection<T>?
    ): RelatedItemLineMarkerInfo<PsiElement>? {
        val nameIdentifier = psiSource.nameIdentifier
        return if (nameIdentifier != null && !relatedElements.isNullOrEmpty()) {
            NavigationGutterIconBuilder.create(if (isInterface) OCamlIcons.Gutter.IMPLEMENTED else OCamlIcons.Gutter.IMPLEMENTING)
                .setTooltipText((if (isInterface) "Implements " else "Declare ") + method)
                .setAlignment(GutterIconRenderer.Alignment.RIGHT)
                .setTargets(relatedElements)
                .createLineMarkerInfo(nameIdentifier)
        } else null
    }
}
