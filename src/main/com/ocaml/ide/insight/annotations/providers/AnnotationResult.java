package com.ocaml.ide.insight.annotations.providers;

import com.intellij.openapi.editor.Editor;
import com.ocaml.sdk.output.CompilerOutputMessage;

import java.io.File;
import java.util.List;

class AnnotationResult {
    public final List<CompilerOutputMessage> myOutputInfo;
    public final Editor myEditor;
    public final File myAnnotFile;

    public AnnotationResult(List<CompilerOutputMessage> outputInfo, Editor editor, File annotFile) {
        myOutputInfo = outputInfo;
        myEditor = editor;
        myAnnotFile = annotFile;
    }
}
