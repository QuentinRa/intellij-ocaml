package com.ocaml.language

import com.intellij.psi.PsiElement
import com.intellij.psi.PsiFile
import com.intellij.psi.impl.DebugUtil
import com.intellij.psi.util.PsiTreeUtil
import com.intellij.testFramework.ParsingTestCase
import com.ocaml.language.base.OCamlBaseParserDefinition
import com.ocaml.language.base.OCamlFileBase
import com.ocaml.language.parser.OCamlInterfaceParserDefinition
import com.ocaml.language.parser.OCamlParserDefinition
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
abstract class OCamlBaseParsingTestCase(fileExt: String, parserDefinition: OCamlBaseParserDefinition) :
    ParsingTestCase("", fileExt, parserDefinition) {
    protected object TestVariables {
        const val FILE_NAME = "dummy"
        const val OCAML_FILE_QUALIFIED_NAME = "Dummy"
    }


    override fun getTestDataPath(): String {
        return "resources/testData"
    }

    private fun parseRawCode(code: String): PsiFile {
        myFile = createPsiFile(TestVariables.FILE_NAME, code)
        println("» " + this.javaClass)
        println(DebugUtil.psiToString(myFile, false, true))
        return myFile
    }

    protected open fun parseCode(code: String): OCamlFileBase {
        return parseRawCode(code) as OCamlFileBase
    }

    protected inline fun <reified T : PsiElement> initWith(code: String): List<T> {
        return PsiTreeUtil.findChildrenOfType(
            parseCode(code), T::class.java
        ).toList()
    }
}
abstract class OCamlParsingTestCase : OCamlBaseParsingTestCase("ml", OCamlParserDefinition())
abstract class OCamlInterfaceParsingTestCase : OCamlBaseParsingTestCase("mli", OCamlInterfaceParserDefinition())