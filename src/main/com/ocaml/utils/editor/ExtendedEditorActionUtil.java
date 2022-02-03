package com.ocaml.utils.editor;

import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.fileEditor.FileDocumentManager;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ExtendedEditorActionUtil {

    /**
     * Return the selected code
     * @param editor the editor
     * @return null if we aren't in a situation in which we can find code to executed,
     * otherwise the selected code
     */
    public static @Nullable String getSelectedCode(@NotNull Editor editor) {
        var code = editor.getSelectionModel().getSelectedText();
        var virtualFile = FileDocumentManager.getInstance().getFile(editor.getDocument());
        if (virtualFile == null) return null;
        if (code != null && !code.isBlank()) {
            return code;
        }
        return null;
    }
}
