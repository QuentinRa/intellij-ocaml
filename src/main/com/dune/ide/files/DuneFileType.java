package com.dune.ide.files;

import com.dune.DuneBundle;
import com.dune.icons.DuneIcons;
import com.dune.DuneLanguage;
import com.intellij.openapi.fileTypes.LanguageFileType;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;

public final class DuneFileType extends LanguageFileType {

    // instance
    public static final DuneFileType INSTANCE = new DuneFileType();
    private DuneFileType() {
        super(DuneLanguage.INSTANCE);
    }

    // implementation

    @Override @NotNull public String getName() {
        return "DUNE";
    }

    @Override @NotNull public String getDescription() {
        return DuneBundle.message("filetype.dune.description");
    }

    @Override @NotNull public String getDefaultExtension() {
        return "";
    }

    @Override public Icon getIcon() {
        return DuneIcons.FileTypes.DUNE_FILE;
    }

    @Override public @Nls @NotNull String getDisplayName() {
        return getDescription();
    }
}
