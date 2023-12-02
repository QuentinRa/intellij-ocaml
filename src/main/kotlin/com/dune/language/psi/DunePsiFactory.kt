package com.dune.language.psi

import com.dune.ide.files.DuneFileType
import com.dune.language.psi.files.DuneFile
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiFileFactory

object DunePsiFactory {

    fun createAtom(project: Project, name: String): DuneAtom {
        val newChild = (PsiFileFactory.getInstance(project).createFileFromText(
            "dune",
            DuneFileType,
            "($name)",
        ) as DuneFile).firstChild as DuneList
        return newChild.value?.atom ?: error("DunePsiFactory#createValue failed: unable to create atom <$name>.")
    }

}