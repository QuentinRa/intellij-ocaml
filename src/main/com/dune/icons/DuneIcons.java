package com.dune.icons;

import com.intellij.icons.AllIcons;
import com.intellij.openapi.util.IconLoader;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;

/**
 * https://www.jetbrains.org/intellij/sdk/docs/reference_guide/work_with_icons_and_images.html
 * https://jetbrains.design/intellij/principles/icons/
 * https://jetbrains.design/intellij/resources/icons_list/ or https://intellij-icons.jetbrains.design/
 * https://github.com/bjansen/intellij-icon-generator
 * <p>
 * Node, action, filetype : 16x16
 * Tool window            : 13x13
 * Editor gutter          : 12x12
 * Font                   : Gotham
 *
 * @see AllIcons
 * @see AllIcons.FileTypes
 * @see AllIcons.General
 * @see AllIcons.Gutter
 * @see AllIcons.Nodes
 */
public class DuneIcons {

    private static @NotNull Icon loadIcon(@NotNull String path) {
        return IconLoader.getIcon(path, DuneIcons.class);
    }

    public static final class Nodes {
        public static final Icon DUNE = loadIcon("/icons/duneLogo.svg");
        public static final Icon OBJECT = AllIcons.Json.Object;
    }

    public static final class FileTypes {
        public static final Icon DUNE_FILE = loadIcon("/icons/duneFile.svg");
    }
}
