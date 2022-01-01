package com.ocaml.ide.settings;

import com.intellij.openapi.editor.colors.*;
import com.intellij.openapi.fileTypes.*;
import com.intellij.openapi.options.colors.*;
import com.ocaml.ide.highlight.*;
import icons.*;
import org.jetbrains.annotations.*;

import javax.swing.*;
import java.util.*;

public class ORColorSettingsPage implements ColorSettingsPage {
    private static final AttributesDescriptor[] DESCRIPTORS =
            new AttributesDescriptor[]{
                    new AttributesDescriptor("Braces", OclSyntaxHighlighter.BRACES_),
                    new AttributesDescriptor("Brackets", OclSyntaxHighlighter.BRACKETS_),
                    new AttributesDescriptor("Comment", OclSyntaxHighlighter.RML_COMMENT_),
                    new AttributesDescriptor("Keyword", OclSyntaxHighlighter.KEYWORD_),
                    new AttributesDescriptor("Number", OclSyntaxHighlighter.NUMBER_),
                    new AttributesDescriptor("Option", OclSyntaxHighlighter.OPTION_),
                    new AttributesDescriptor("Operation", OclSyntaxHighlighter.OPERATION_SIGN_),
                    new AttributesDescriptor("Parenthesis", OclSyntaxHighlighter.PARENS_),
                    new AttributesDescriptor("Poly variant", OclSyntaxHighlighter.POLY_VARIANT_),
                    new AttributesDescriptor("Semicolon", OclSyntaxHighlighter.SEMICOLON_),
                    new AttributesDescriptor("String", OclSyntaxHighlighter.STRING_),
                    new AttributesDescriptor("Type argument", OclSyntaxHighlighter.TYPE_ARGUMENT_)
            };

    @Nullable @Override public Icon getIcon() {
        return ORIcons.OCAML;
    }

    @Override public @NotNull SyntaxHighlighter getHighlighter() {
        return new OclSyntaxHighlighter();
    }

    @Override public @NotNull String getDemoText() {
        return ""
                + "/* This is a comment */\n\n"
                + "module <csModuleName>ModuleName</csModuleName> = {\n"
                + "  type t = { key: int };\n"
                + "  type tree 'a =\n"
                + "    | <csVariantName>Node</csVariantName> (tree 'a) (tree 'a)\n"
                + "    | <csVariantName>Leaf</csVariantName>;\n\n"
                + "  [<csAnnotation>@bs.deriving</csAnnotation> {accessors: accessors}]\n"
                + "  type t = [`Up | `Down | `Left | `Right];\n\n"
                + "  let add = (x y) => x + y;  <csCodeLens>int -> int</csCodeLens>\n"
                + "  let myList = [ 1.0, 2.0, 3. ];\n"
                + "  let array = [| 1, 2, 3 |];\n"
                + "  let choice x = switch (myOption)\n"
                + "    | None => \"nok\"\n"
                + "    | Some(value) => \"ok\";\n"
                + "  let constant = \"My constant\";  <csCodeLens>string</csCodeLens>\n"
                + "  let numericConstant = 123;  <csCodeLens>int</csCodeLens>\n"
                + "  let interpolation = {j|$<csInterpolatedRef>var</csInterpolatedRef>|j};\n"
                + "};\n\n"
                + "[<csAnnotation>@react.component</csAnnotation>]\nlet make = () =>\n  <csMarkupTag><div</csMarkupTag> <csMarkupAttribute>prop</csMarkupAttribute>=value<csMarkupTag>></csMarkupTag>\n    <csMarkupTag><Button/></csMarkupTag>\n    (React.string(\"ok\") <csMarkupTag></Button></csMarkupTag>\n  <csMarkupTag></div></csMarkupTag>;";
    }

    @Nullable @Override public Map<String, TextAttributesKey> getAdditionalHighlightingTagToDescriptorMap() {
        return null;
    }

    @Override public AttributesDescriptor @NotNull [] getAttributeDescriptors() {
        return DESCRIPTORS;
    }

    @Override public ColorDescriptor @NotNull [] getColorDescriptors() {
        return ColorDescriptor.EMPTY_ARRAY;
    }

    @NotNull @Override public String getDisplayName() {
        return "OCaml";
    }
}
