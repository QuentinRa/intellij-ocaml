package com.ocaml.ide.annotations;

import com.intellij.openapi.editor.Editor;
import com.ocaml.sdk.output.CompilerOutputMessage;

import java.io.File;
import java.util.List;

class AnnotationResult {
    public final List<CompilerOutputMessage> myOutputInfo;
    public final Editor myEditor;
    public final File myCmtFile;

    public AnnotationResult(List<CompilerOutputMessage> outputInfo, Editor editor, File cmtFile) {
        myOutputInfo = outputInfo;
        myEditor = editor;
        myCmtFile = cmtFile;
    }
}
