package com.ocaml.language

import com.intellij.psi.PsiFile
import com.intellij.psi.impl.DebugUtil
import com.intellij.testFramework.ParsingTestCase
import com.ocaml.language.parser.OCamlParserImplementationDefinition
import com.ocaml.language.psi.file.OCamlFile
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

// PsiErrorElement
@RunWith(JUnit4::class)
abstract class OCamlParsingTestCase protected constructor() : ParsingTestCase("", "ml", OCamlParserImplementationDefinition()) {
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

    protected fun parseCode(code: String, assertNoErrors: Boolean = true, errorExpected: Boolean = false): OCamlFile {
        parseRawCode(code)
//        if (assertNoErrors)
//            if (! errorExpected)
//                TestCase.assertNull(myFile.firstChildOfType(PsiErrorElement::class.java))
//            else
//                TestCase.assertNotNull(myFile.firstChildOfType(PsiErrorElement::class.java))
        return myFile as OCamlFile
    }
}
