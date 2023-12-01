package com.dune.language.psi

import com.dune.ide.files.DuneFileType
import com.dune.language.psi.files.DuneFile
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiFileFactory

object DunePsiFactory {

    fun createValue(project: Project, name: String): DuneValue {
        val newChild = (PsiFileFactory.getInstance(project).createFileFromText(
            "dune",
            DuneFileType,
            "($name)",
        ) as DuneFile).firstChild as DuneList
        return newChild.value ?: error("DunePsiFactory#createValue failed: unable to create simple text file.")
    }

}