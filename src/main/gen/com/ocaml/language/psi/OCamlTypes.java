// This is a generated file. Not intended for manual editing.
package com.ocaml.language.psi;

import com.intellij.psi.tree.IElementType;
import com.intellij.psi.PsiElement;
import com.intellij.lang.ASTNode;
import com.ocaml.language.psi.api.OCamlElementType;
import com.ocaml.language.psi.api.OCamlTokenType;
import com.ocaml.language.psi.impl.*;

public interface OCamlTypes {

  IElementType MODULE_ITEMS = new OCamlElementType("MODULE_ITEMS");
  IElementType SPECIFICATION = new OCamlElementType("SPECIFICATION");

  IElementType AMPERSAND = new OCamlTokenType("&");
  IElementType AND = new OCamlTokenType("and");
  IElementType ARROBASE = new OCamlTokenType("@");
  IElementType AS = new OCamlTokenType("as");
  IElementType ASR = new OCamlTokenType("asr");
  IElementType ASSERT = new OCamlTokenType("assert");
  IElementType BACKTICK = new OCamlTokenType("`");
  IElementType BEGIN = new OCamlTokenType("begin");
  IElementType CARRET = new OCamlTokenType("^");
  IElementType CLASS = new OCamlTokenType("class");
  IElementType COLON = new OCamlTokenType(":");
  IElementType COLON_EQ = new OCamlTokenType(":=");
  IElementType COLON_GT = new OCamlTokenType(":>");
  IElementType COMMA = new OCamlTokenType(",");
  IElementType CONSTRAINT = new OCamlTokenType("constraint");
  IElementType DO = new OCamlTokenType("do");
  IElementType DOLLAR = new OCamlTokenType("$");
  IElementType DONE = new OCamlTokenType("done");
  IElementType DOT = new OCamlTokenType(".");
  IElementType DOTDOT = new OCamlTokenType("..");
  IElementType DOWNTO = new OCamlTokenType("downto");
  IElementType ELSE = new OCamlTokenType("else");
  IElementType END = new OCamlTokenType("end");
  IElementType EQ = new OCamlTokenType("=");
  IElementType EXCEPTION = new OCamlTokenType("exception");
  IElementType EXCLAMATION_MARK = new OCamlTokenType("!");
  IElementType EXTERNAL = new OCamlTokenType("external");
  IElementType FALSE = new OCamlTokenType("false");
  IElementType FOR = new OCamlTokenType("for");
  IElementType FUN = new OCamlTokenType("fun");
  IElementType FUNCTION = new OCamlTokenType("function");
  IElementType FUNCTOR = new OCamlTokenType("functor");
  IElementType GREATERRBRACE = new OCamlTokenType(">}");
  IElementType GT = new OCamlTokenType(">");
  IElementType IF = new OCamlTokenType("if");
  IElementType IN = new OCamlTokenType("in");
  IElementType INCLUDE = new OCamlTokenType("include");
  IElementType INHERIT = new OCamlTokenType("inherit");
  IElementType INITIALIZER = new OCamlTokenType("initializer");
  IElementType LAND = new OCamlTokenType("land");
  IElementType LARRAY = new OCamlTokenType("[|");
  IElementType LAZY = new OCamlTokenType("lazy");
  IElementType LBRACE = new OCamlTokenType("{");
  IElementType LBRACELESS = new OCamlTokenType("{<");
  IElementType LBRACKET = new OCamlTokenType("[");
  IElementType LBRACKETGREATER = new OCamlTokenType("[>");
  IElementType LBRACKETLESS = new OCamlTokenType("[<");
  IElementType LEFT_ARROW = new OCamlTokenType("<-");
  IElementType LET = new OCamlTokenType("let");
  IElementType LOR = new OCamlTokenType("lor");
  IElementType LPAREN = new OCamlTokenType("(");
  IElementType LSL = new OCamlTokenType("lsl");
  IElementType LSR = new OCamlTokenType("lsr");
  IElementType LT = new OCamlTokenType("<");
  IElementType LXOR = new OCamlTokenType("lxor");
  IElementType L_AND = new OCamlTokenType("&&");
  IElementType L_OR = new OCamlTokenType("||");
  IElementType MATCH = new OCamlTokenType("match");
  IElementType METHOD = new OCamlTokenType("method");
  IElementType MINUS = new OCamlTokenType("-");
  IElementType MINUSDOT = new OCamlTokenType("-.");
  IElementType MOD = new OCamlTokenType("mod");
  IElementType MODULE = new OCamlTokenType("module");
  IElementType MUTABLE = new OCamlTokenType("mutable");
  IElementType NEW = new OCamlTokenType("new");
  IElementType NONREC = new OCamlTokenType("nonrec");
  IElementType NOT_EQ = new OCamlTokenType("!=");
  IElementType OBJECT = new OCamlTokenType("object");
  IElementType OF = new OCamlTokenType("of");
  IElementType OPEN = new OCamlTokenType("open");
  IElementType OR = new OCamlTokenType("or");
  IElementType PERCENT = new OCamlTokenType("%");
  IElementType PIPE = new OCamlTokenType("|");
  IElementType PLUS = new OCamlTokenType("+");
  IElementType PRIVATE = new OCamlTokenType("private");
  IElementType QUESTION_MARK = new OCamlTokenType("?");
  IElementType RARRAY = new OCamlTokenType("|]");
  IElementType RBRACE = new OCamlTokenType("}");
  IElementType RBRACKET = new OCamlTokenType("]");
  IElementType REC = new OCamlTokenType("rec");
  IElementType RIGHT_ARROW = new OCamlTokenType("->");
  IElementType RPAREN = new OCamlTokenType(")");
  IElementType SEMI = new OCamlTokenType(";");
  IElementType SEMISEMI = new OCamlTokenType(";;");
  IElementType SHARP = new OCamlTokenType("#");
  IElementType SHORTCUT = new OCamlTokenType("::");
  IElementType SIG = new OCamlTokenType("sig");
  IElementType SINGLE_QUOTE = new OCamlTokenType("'");
  IElementType SLASH = new OCamlTokenType("/");
  IElementType STAR = new OCamlTokenType("*");
  IElementType STRUCT = new OCamlTokenType("struct");
  IElementType THEN = new OCamlTokenType("then");
  IElementType TILDE = new OCamlTokenType("~");
  IElementType TO = new OCamlTokenType("to");
  IElementType TRUE = new OCamlTokenType("true");
  IElementType TRY = new OCamlTokenType("try");
  IElementType TYPE = new OCamlTokenType("type");
  IElementType UNDERSCORE = new OCamlTokenType("_");
  IElementType VAL = new OCamlTokenType("val");
  IElementType VIRTUAL = new OCamlTokenType("virtual");
  IElementType WHEN = new OCamlTokenType("when");
  IElementType WHILE = new OCamlTokenType("while");
  IElementType WITH = new OCamlTokenType("with");

  class Factory {
    public static PsiElement createElement(ASTNode node) {
      IElementType type = node.getElementType();
      if (type == MODULE_ITEMS) {
        return new OCamlModuleItemsImpl(node);
      }
      else if (type == SPECIFICATION) {
        return new OCamlSpecificationImpl(node);
      }
      throw new AssertionError("Unknown element type: " + type);
    }
  }
}
