package com.ocaml.ide.settings

import com.intellij.openapi.editor.colors.TextAttributesKey
import com.intellij.openapi.fileTypes.SyntaxHighlighter
import com.intellij.openapi.options.colors.AttributesDescriptor
import com.intellij.openapi.options.colors.ColorDescriptor
import com.intellij.openapi.options.colors.ColorSettingsPage
import com.ocaml.icons.OCamlIcons
import com.ocaml.ide.colors.OCamlColor
import com.ocaml.ide.highlight.OCamlSyntaxHighlighter
import javax.swing.Icon

class OCamlColorSettingsPage : ColorSettingsPage {
    override fun getDisplayName(): String = "OCaml"
    override fun getIcon(): Icon = OCamlIcons.FileTypes.OCAML_SOURCE
    override fun getAttributeDescriptors() = Constants.ATTRS
    override fun getColorDescriptors(): Array<ColorDescriptor> = ColorDescriptor.EMPTY_ARRAY
    override fun getHighlighter(): SyntaxHighlighter = OCamlSyntaxHighlighter()
    override fun getAdditionalHighlightingTagToDescriptorMap() = Constants.ANNOTATOR_TAGS
    override fun getDemoText() = Constants.DEMO_TEXT

    internal object Constants {
        val ATTRS: Array<AttributesDescriptor> = OCamlColor.values().map{ it.attributesDescriptor }.toTypedArray()
        val ANNOTATOR_TAGS: Map<String, TextAttributesKey> = OCamlColor.values().associateBy({ it.name }, { it.textAttributesKey })
        val DEMO_TEXT: String by lazy {
            """(* Variables and Types *)
let <VARIABLE>x</VARIABLE> = 10 and z = 'c'
let <VARIABLE>y</VARIABLE> = 3.14
let <VARIABLE>name</VARIABLE> = "OCaml"

(* Pattern Matching *)
let rec factorial n =
  match n with
  | 0 -> 1
  | _ -> n * factorial (n - 1)

(* Functions *)
let add a b = a + b
let square x = x * x

let is_even n =
  if n mod 2 = 0 then
    true
  else
    false

let divide a b =
  try
    a / b
  with
  | Division_by_zero -> 0

(* Structures *)
let list = [1; 2; 3; 4; 5]
let array = [|1; 2; 3; 4; 5|]
let pair = (10, "hello")
type person = { name: string; age: int }
let john = { name = "John"; age = 30 }

(* Modules *)
module Math = struct
  let pi = 3.14
  let square x = x * x
end

module type Printable = sig
  val print : unit -> unit
end

module Printer (M: Printable) = struct
  let print_twice () =
    M.print ();
    M.print ()
end

module HelloPrinter = Printer(PrintHello)
"""
        }
    }

}