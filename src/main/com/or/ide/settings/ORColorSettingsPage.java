package com.or.ide.settings;

import com.intellij.openapi.editor.colors.TextAttributesKey;
import com.intellij.openapi.options.colors.AttributesDescriptor;
import com.intellij.openapi.options.colors.ColorDescriptor;
import com.intellij.openapi.options.colors.ColorSettingsPage;
import com.ocaml.icons.OCamlIcons;
import com.ocaml.ide.highlight.OCamlSyntaxHighlighter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.util.HashMap;
import java.util.Map;

public class ORColorSettingsPage implements ColorSettingsPage {
    private static final AttributesDescriptor[] DESCRIPTORS =
            new AttributesDescriptor[]{
                    new AttributesDescriptor("Annotation", OCamlSyntaxHighlighter.ANNOTATION_),
                    new AttributesDescriptor("Braces", OCamlSyntaxHighlighter.BRACES_),
                    new AttributesDescriptor("Brackets", OCamlSyntaxHighlighter.BRACKETS_),
                    new AttributesDescriptor("Code lens", OCamlSyntaxHighlighter.CODE_LENS_),
                    new AttributesDescriptor("Comment", OCamlSyntaxHighlighter.RML_COMMENT_),
                    new AttributesDescriptor("Keyword", OCamlSyntaxHighlighter.KEYWORD_),
                    new AttributesDescriptor("Markup attribute", OCamlSyntaxHighlighter.MARKUP_ATTRIBUTE_),
                    new AttributesDescriptor("Markup tag", OCamlSyntaxHighlighter.MARKUP_TAG_),
                    new AttributesDescriptor("Module name", OCamlSyntaxHighlighter.MODULE_NAME_),
                    new AttributesDescriptor("Number", OCamlSyntaxHighlighter.NUMBER_),
                    new AttributesDescriptor("Option", OCamlSyntaxHighlighter.OPTION_),
                    new AttributesDescriptor("Operation", OCamlSyntaxHighlighter.OPERATION_SIGN_),
                    new AttributesDescriptor("Parenthesis", OCamlSyntaxHighlighter.PARENS_),
                    new AttributesDescriptor("Poly variant", OCamlSyntaxHighlighter.POLY_VARIANT_),
                    new AttributesDescriptor("Semicolon", OCamlSyntaxHighlighter.SEMICOLON_),
                    new AttributesDescriptor("String", OCamlSyntaxHighlighter.STRING_),
                    new AttributesDescriptor("Type argument", OCamlSyntaxHighlighter.TYPE_ARGUMENT_),
                    new AttributesDescriptor("Variant name", OCamlSyntaxHighlighter.VARIANT_NAME_),
                    new AttributesDescriptor("Interpolated ref", OCamlSyntaxHighlighter.INTERPOLATED_REF_),
            };
    private static final Map<String, TextAttributesKey> additionalTags = new HashMap<>();

    static {
        additionalTags.put("csAnnotation", OCamlSyntaxHighlighter.ANNOTATION_);
        additionalTags.put("csCodeLens", OCamlSyntaxHighlighter.CODE_LENS_);
        additionalTags.put("csMarkupAttribute", OCamlSyntaxHighlighter.MARKUP_ATTRIBUTE_);
        additionalTags.put("csMarkupTag", OCamlSyntaxHighlighter.MARKUP_TAG_);
        additionalTags.put("csModuleName", OCamlSyntaxHighlighter.MODULE_NAME_);
        additionalTags.put("csVariantName", OCamlSyntaxHighlighter.VARIANT_NAME_);
        additionalTags.put("csInterpolatedRef", OCamlSyntaxHighlighter.INTERPOLATED_REF_);
    }

    @Override
    public @Nullable Icon getIcon() {
        return OCamlIcons.FileTypes.OCAML_SOURCE;
    }

    @Override
    public @NotNull com.intellij.openapi.fileTypes.SyntaxHighlighter getHighlighter() {
        return new OCamlSyntaxHighlighter();
    }

    @Override
    public @NotNull String getDemoText() {
        return ""
                + "(* This is a comment *)\n\n"
                + "module <csModuleName>ModuleName</csModuleName> = struct\n"
                + "  type t\n"
                + "  type 'a tree =\n"
                + "    | <csVariantName>Node</csVariantName> ('a tree) * ('a tree)\n"
                + "    | <csVariantName>Leaf</csVariantName>\n\n"
                + "  let add x y = x + y;  <csCodeLens>int -> int -> int</csCodeLens>\n"
                + "  let constant = \"My constant\";  <csCodeLens>string</csCodeLens>\n"
                + "  let numericConstant = 123;  <csCodeLens>int</csCodeLens>\n"
                + "end\n\n";
    }

    @Override
    public @Nullable Map<String, TextAttributesKey> getAdditionalHighlightingTagToDescriptorMap() {
        return additionalTags;
    }

    @Override
    public AttributesDescriptor @NotNull [] getAttributeDescriptors() {
        return DESCRIPTORS;
    }

    @Override
    public ColorDescriptor @NotNull [] getColorDescriptors() {
        return ColorDescriptor.EMPTY_ARRAY;
    }

    @Override
    public @NotNull String getDisplayName() {
        return "OCaml";
    }
}
