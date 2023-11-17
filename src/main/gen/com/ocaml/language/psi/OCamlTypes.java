// This is a generated file. Not intended for manual editing.
package com.ocaml.language.psi;

import com.intellij.psi.tree.IElementType;
import com.intellij.psi.PsiElement;
import com.intellij.lang.ASTNode;
import com.ocaml.language.psi.api.OCamlElementType;
import com.ocaml.language.psi.api.OCamlTokenType;
import com.ocaml.language.psi.impl.*;

public interface OCamlTypes {

  IElementType AND_TYPES = new OCamlElementType("AND_TYPES");
  IElementType ARGUMENT = new OCamlElementType("ARGUMENT");
  IElementType ARGUMENT_BASE_EXPR = new OCamlElementType("ARGUMENT_BASE_EXPR");
  IElementType ARG_CLASS_EXPR = new OCamlElementType("ARG_CLASS_EXPR");
  IElementType ARROW_MODULE_TYPE = new OCamlElementType("ARROW_MODULE_TYPE");
  IElementType ASSERT_BASE_EXPR = new OCamlElementType("ASSERT_BASE_EXPR");
  IElementType AS_PATTERN = new OCamlElementType("AS_PATTERN");
  IElementType AS_TYPEXPR = new OCamlElementType("AS_TYPEXPR");
  IElementType BACKTICK_BASE_EXPR = new OCamlElementType("BACKTICK_BASE_EXPR");
  IElementType BACKTICK_PATTERN = new OCamlElementType("BACKTICK_PATTERN");
  IElementType BEGIN_BASE_EXPR = new OCamlElementType("BEGIN_BASE_EXPR");
  IElementType BRACE_BASE_EXPR = new OCamlElementType("BRACE_BASE_EXPR");
  IElementType BRACE_FIELD_BASE_EXPR = new OCamlElementType("BRACE_FIELD_BASE_EXPR");
  IElementType BRACE_FIELD_PATTERN = new OCamlElementType("BRACE_FIELD_PATTERN");
  IElementType BRACE_PATTERN = new OCamlElementType("BRACE_PATTERN");
  IElementType BRACE_TYPE_PATTERN = new OCamlElementType("BRACE_TYPE_PATTERN");
  IElementType BRACKET_BASE_EXPR = new OCamlElementType("BRACKET_BASE_EXPR");
  IElementType BRACKET_PATTERN = new OCamlElementType("BRACKET_PATTERN");
  IElementType CHAR_LITERAL = new OCamlElementType("CHAR_LITERAL");
  IElementType CHAR_LITERAL_PATTERN = new OCamlElementType("CHAR_LITERAL_PATTERN");
  IElementType CLASSPATH_CLASS_EXPR = new OCamlElementType("CLASSPATH_CLASS_EXPR");
  IElementType CLASSPATH_TYPEXPR = new OCamlElementType("CLASSPATH_TYPEXPR");
  IElementType CLASSTYPE_DEF = new OCamlElementType("CLASSTYPE_DEF");
  IElementType CLASSTYPE_DEFINITION = new OCamlElementType("CLASSTYPE_DEFINITION");
  IElementType CLASSTYPE_PATH = new OCamlElementType("CLASSTYPE_PATH");
  IElementType CLASSTYPE_TYPEXPR = new OCamlElementType("CLASSTYPE_TYPEXPR");
  IElementType CLASS_BINDING = new OCamlElementType("CLASS_BINDING");
  IElementType CLASS_BODY = new OCamlElementType("CLASS_BODY");
  IElementType CLASS_BODY_TYPE = new OCamlElementType("CLASS_BODY_TYPE");
  IElementType CLASS_DEFINITION = new OCamlElementType("CLASS_DEFINITION");
  IElementType CLASS_EXPR = new OCamlElementType("CLASS_EXPR");
  IElementType CLASS_FIELD = new OCamlElementType("CLASS_FIELD");
  IElementType CLASS_FIELD_SPEC = new OCamlElementType("CLASS_FIELD_SPEC");
  IElementType CLASS_NAME = new OCamlElementType("CLASS_NAME");
  IElementType CLASS_PATH = new OCamlElementType("CLASS_PATH");
  IElementType CLASS_SPEC = new OCamlElementType("CLASS_SPEC");
  IElementType CLASS_SPECIFICATION = new OCamlElementType("CLASS_SPECIFICATION");
  IElementType CLASS_TYPE = new OCamlElementType("CLASS_TYPE");
  IElementType COLON_BASE_EXPR = new OCamlElementType("COLON_BASE_EXPR");
  IElementType COMMA_PATTERN = new OCamlElementType("COMMA_PATTERN");
  IElementType COMMA_TYPEXPR = new OCamlElementType("COMMA_TYPEXPR");
  IElementType CONSTANT_BASE_EXPR = new OCamlElementType("CONSTANT_BASE_EXPR");
  IElementType CONSTANT_PATTERN = new OCamlElementType("CONSTANT_PATTERN");
  IElementType CONSTR = new OCamlElementType("CONSTR");
  IElementType CONSTR_ARGS = new OCamlElementType("CONSTR_ARGS");
  IElementType CONSTR_BASE_EXPR = new OCamlElementType("CONSTR_BASE_EXPR");
  IElementType CONSTR_DECL = new OCamlElementType("CONSTR_DECL");
  IElementType CONSTR_NAME = new OCamlElementType("CONSTR_NAME");
  IElementType CONSTR_PATTERN = new OCamlElementType("CONSTR_PATTERN");
  IElementType CORE_OPERATOR_CHAR = new OCamlElementType("CORE_OPERATOR_CHAR");
  IElementType DEFINITION = new OCamlElementType("DEFINITION");
  IElementType DOT_BRACKET_BASE_EXPR = new OCamlElementType("DOT_BRACKET_BASE_EXPR");
  IElementType DOT_BRACKET_LEFT_ARROW_BASE_EXPR = new OCamlElementType("DOT_BRACKET_LEFT_ARROW_BASE_EXPR");
  IElementType DOT_FIELD_BASE_EXPR = new OCamlElementType("DOT_FIELD_BASE_EXPR");
  IElementType DOT_FIELD_LEFT_ARROW_BASE_EXPR = new OCamlElementType("DOT_FIELD_LEFT_ARROW_BASE_EXPR");
  IElementType DOT_PAREN_BASE_EXPR = new OCamlElementType("DOT_PAREN_BASE_EXPR");
  IElementType DOT_PAREN_LEFT_ARROW_BASE_EXPR = new OCamlElementType("DOT_PAREN_LEFT_ARROW_BASE_EXPR");
  IElementType EXCEPTION_DEFINITION = new OCamlElementType("EXCEPTION_DEFINITION");
  IElementType EXCEPTION_PATTERN = new OCamlElementType("EXCEPTION_PATTERN");
  IElementType EXPR = new OCamlElementType("EXPR");
  IElementType EXPR_COERCION_BASE_EXPR = new OCamlElementType("EXPR_COERCION_BASE_EXPR");
  IElementType EXPR_MODULE_EXPR = new OCamlElementType("EXPR_MODULE_EXPR");
  IElementType EXPR_TYPE_COERCION_BASE_EXPR = new OCamlElementType("EXPR_TYPE_COERCION_BASE_EXPR");
  IElementType EXTENDED_MODULE_NAME = new OCamlElementType("EXTENDED_MODULE_NAME");
  IElementType EXTENDED_MODULE_PATH = new OCamlElementType("EXTENDED_MODULE_PATH");
  IElementType EXTERNAL_DECLARATION = new OCamlElementType("EXTERNAL_DECLARATION");
  IElementType EXT_VARIANCE = new OCamlElementType("EXT_VARIANCE");
  IElementType FIELD = new OCamlElementType("FIELD");
  IElementType FIELD_DECL = new OCamlElementType("FIELD_DECL");
  IElementType FIELD_NAME = new OCamlElementType("FIELD_NAME");
  IElementType FLOAT_LITERAL = new OCamlElementType("FLOAT_LITERAL");
  IElementType FOR_BASE_EXPR = new OCamlElementType("FOR_BASE_EXPR");
  IElementType FUNCTION_BASE_EXPR = new OCamlElementType("FUNCTION_BASE_EXPR");
  IElementType FUNCTOR_MODULE_EXPR = new OCamlElementType("FUNCTOR_MODULE_EXPR");
  IElementType FUNCTOR_MODULE_TYPE = new OCamlElementType("FUNCTOR_MODULE_TYPE");
  IElementType FUN_BASE_EXPR = new OCamlElementType("FUN_BASE_EXPR");
  IElementType FUN_CLASS_EXPR = new OCamlElementType("FUN_CLASS_EXPR");
  IElementType IDENT_TYPEXPR = new OCamlElementType("IDENT_TYPEXPR");
  IElementType IF_ELSE_BASE_EXPR = new OCamlElementType("IF_ELSE_BASE_EXPR");
  IElementType INFIX_OP = new OCamlElementType("INFIX_OP");
  IElementType INFIX_OP_BASE_EXPR = new OCamlElementType("INFIX_OP_BASE_EXPR");
  IElementType INFIX_SYMBOL = new OCamlElementType("INFIX_SYMBOL");
  IElementType INJECTIVITY = new OCamlElementType("INJECTIVITY");
  IElementType INST_VAR_NAME = new OCamlElementType("INST_VAR_NAME");
  IElementType INTEGER_LITERAL = new OCamlElementType("INTEGER_LITERAL");
  IElementType INT_32_LITERAL = new OCamlElementType("INT_32_LITERAL");
  IElementType INT_64_LITERAL = new OCamlElementType("INT_64_LITERAL");
  IElementType LABEL = new OCamlElementType("LABEL");
  IElementType LABEL_NAME = new OCamlElementType("LABEL_NAME");
  IElementType LABEL_OPT_TYPEXPR = new OCamlElementType("LABEL_OPT_TYPEXPR");
  IElementType LABEL_TYPEXPR = new OCamlElementType("LABEL_TYPEXPR");
  IElementType LARRAY_BASE_EXPR = new OCamlElementType("LARRAY_BASE_EXPR");
  IElementType LARRAY_PATTERN = new OCamlElementType("LARRAY_PATTERN");
  IElementType LAZY_BASE_EXPR = new OCamlElementType("LAZY_BASE_EXPR");
  IElementType LAZY_PATTERN = new OCamlElementType("LAZY_PATTERN");
  IElementType LET_BASE_EXPR = new OCamlElementType("LET_BASE_EXPR");
  IElementType LET_BINDING = new OCamlElementType("LET_BINDING");
  IElementType LET_CLASS_EXPR = new OCamlElementType("LET_CLASS_EXPR");
  IElementType LET_EXCEPTION_BASE_EXPR = new OCamlElementType("LET_EXCEPTION_BASE_EXPR");
  IElementType LET_MODULE_BASE_EXPR = new OCamlElementType("LET_MODULE_BASE_EXPR");
  IElementType LET_OPEN_CLASS_EXPR = new OCamlElementType("LET_OPEN_CLASS_EXPR");
  IElementType LOCAL_OPEN = new OCamlElementType("LOCAL_OPEN");
  IElementType LOCAL_OPEN_BASE_EXPR = new OCamlElementType("LOCAL_OPEN_BASE_EXPR");
  IElementType LTGT_METHOD_TYPEXPR = new OCamlElementType("LTGT_METHOD_TYPEXPR");
  IElementType LTGT_TYPEXPR = new OCamlElementType("LTGT_TYPEXPR");
  IElementType MATCH_BASE_EXPR = new OCamlElementType("MATCH_BASE_EXPR");
  IElementType METHOD_NAME = new OCamlElementType("METHOD_NAME");
  IElementType METHOD_TYPE = new OCamlElementType("METHOD_TYPE");
  IElementType MINUS_BASE_EXPR = new OCamlElementType("MINUS_BASE_EXPR");
  IElementType MINUS_DOT_BASE_EXPR = new OCamlElementType("MINUS_DOT_BASE_EXPR");
  IElementType MODPATH_MODULE_EXPR = new OCamlElementType("MODPATH_MODULE_EXPR");
  IElementType MODTYPE_MODULE_TYPE = new OCamlElementType("MODTYPE_MODULE_TYPE");
  IElementType MODTYPE_NAME = new OCamlElementType("MODTYPE_NAME");
  IElementType MODTYPE_PATH = new OCamlElementType("MODTYPE_PATH");
  IElementType MODULE_EXPR = new OCamlElementType("MODULE_EXPR");
  IElementType MODULE_ITEMS = new OCamlElementType("MODULE_ITEMS");
  IElementType MODULE_NAME = new OCamlElementType("MODULE_NAME");
  IElementType MODULE_PATH = new OCamlElementType("MODULE_PATH");
  IElementType MODULE_PATH_ARRAY_PATTERN = new OCamlElementType("MODULE_PATH_ARRAY_PATTERN");
  IElementType MODULE_PATH_BRACE_PATTERN = new OCamlElementType("MODULE_PATH_BRACE_PATTERN");
  IElementType MODULE_PATH_BRACKET_PATTERN = new OCamlElementType("MODULE_PATH_BRACKET_PATTERN");
  IElementType MODULE_PATH_PAREN_PATTERN = new OCamlElementType("MODULE_PATH_PAREN_PATTERN");
  IElementType MODULE_TYPE = new OCamlElementType("MODULE_TYPE");
  IElementType MOD_CONSTRAINT = new OCamlElementType("MOD_CONSTRAINT");
  IElementType NATIVEINT_LITERAL = new OCamlElementType("NATIVEINT_LITERAL");
  IElementType OBJECT_CLASS_EXPR = new OCamlElementType("OBJECT_CLASS_EXPR");
  IElementType OBJECT_EXPR_INTERNAL_BASE_EXPR = new OCamlElementType("OBJECT_EXPR_INTERNAL_BASE_EXPR");
  IElementType OBJECT_EXPR_INTERNAL_REC_BASE_EXPR = new OCamlElementType("OBJECT_EXPR_INTERNAL_REC_BASE_EXPR");
  IElementType OPERATOR_CHAR = new OCamlElementType("OPERATOR_CHAR");
  IElementType OPERATOR_NAME = new OCamlElementType("OPERATOR_NAME");
  IElementType OPTLABEL = new OCamlElementType("OPTLABEL");
  IElementType PARAMETER = new OCamlElementType("PARAMETER");
  IElementType PARENTHESIS_TYPEXPR = new OCamlElementType("PARENTHESIS_TYPEXPR");
  IElementType PAREN_BASE_EXPR = new OCamlElementType("PAREN_BASE_EXPR");
  IElementType PAREN_CLASS_EXPR = new OCamlElementType("PAREN_CLASS_EXPR");
  IElementType PAREN_MODULE_EXPR = new OCamlElementType("PAREN_MODULE_EXPR");
  IElementType PAREN_MODULE_TYPE = new OCamlElementType("PAREN_MODULE_TYPE");
  IElementType PAREN_SHARP_TYPEXPR = new OCamlElementType("PAREN_SHARP_TYPEXPR");
  IElementType PAREN_TYPE_BASE_EXPR = new OCamlElementType("PAREN_TYPE_BASE_EXPR");
  IElementType PAREN_TYPE_CLASS_EXPR = new OCamlElementType("PAREN_TYPE_CLASS_EXPR");
  IElementType PAREN_TYPE_MODULE_EXPR = new OCamlElementType("PAREN_TYPE_MODULE_EXPR");
  IElementType PATTERN = new OCamlElementType("PATTERN");
  IElementType PATTERN_EXPR = new OCamlElementType("PATTERN_EXPR");
  IElementType PATTERN_MATCHING = new OCamlElementType("PATTERN_MATCHING");
  IElementType PATTERN_PIPE_PATTERN = new OCamlElementType("PATTERN_PIPE_PATTERN");
  IElementType POLYMORPHIC_VARIANT_TYPE = new OCamlElementType("POLYMORPHIC_VARIANT_TYPE");
  IElementType POLYMORPHIC_VARIANT_TYPE_TYPEXPR = new OCamlElementType("POLYMORPHIC_VARIANT_TYPE_TYPEXPR");
  IElementType POLY_TYPEXPR = new OCamlElementType("POLY_TYPEXPR");
  IElementType PREFIX_BASE_EXPR = new OCamlElementType("PREFIX_BASE_EXPR");
  IElementType PREFIX_SYMBOL = new OCamlElementType("PREFIX_SYMBOL");
  IElementType RECORD_DECL = new OCamlElementType("RECORD_DECL");
  IElementType RECURSIVE_MODULE_EXTENSION_DEF = new OCamlElementType("RECURSIVE_MODULE_EXTENSION_DEF");
  IElementType RECURSIVE_MODULE_EXTENSION_SPEC = new OCamlElementType("RECURSIVE_MODULE_EXTENSION_SPEC");
  IElementType SEMI_BASE_EXPR = new OCamlElementType("SEMI_BASE_EXPR");
  IElementType SHARP_PATTERN = new OCamlElementType("SHARP_PATTERN");
  IElementType SHORTCUT_BASE_EXPR = new OCamlElementType("SHORTCUT_BASE_EXPR");
  IElementType SHORTCUT_PATTERN = new OCamlElementType("SHORTCUT_PATTERN");
  IElementType SIG_MODULE_TYPE = new OCamlElementType("SIG_MODULE_TYPE");
  IElementType SPECIFICATION = new OCamlElementType("SPECIFICATION");
  IElementType STAR_TYPEXPR = new OCamlElementType("STAR_TYPEXPR");
  IElementType STRING_LITERAL = new OCamlElementType("STRING_LITERAL");
  IElementType STRUCT_MODULE_EXPR = new OCamlElementType("STRUCT_MODULE_EXPR");
  IElementType TAG_NAME = new OCamlElementType("TAG_NAME");
  IElementType TAG_SPEC = new OCamlElementType("TAG_SPEC");
  IElementType TAG_SPEC_FIRST = new OCamlElementType("TAG_SPEC_FIRST");
  IElementType TAG_SPEC_FULL = new OCamlElementType("TAG_SPEC_FULL");
  IElementType TRY_BASE_EXPR = new OCamlElementType("TRY_BASE_EXPR");
  IElementType TYPECONSTR = new OCamlElementType("TYPECONSTR");
  IElementType TYPECONSTR_NAME = new OCamlElementType("TYPECONSTR_NAME");
  IElementType TYPECONSTR_TYPEXPR = new OCamlElementType("TYPECONSTR_TYPEXPR");
  IElementType TYPEDEF = new OCamlElementType("TYPEDEF");
  IElementType TYPEEXPR_CLASS_EXPR = new OCamlElementType("TYPEEXPR_CLASS_EXPR");
  IElementType TYPEXPR = new OCamlElementType("TYPEXPR");
  IElementType TYPE_CONSTRAINT = new OCamlElementType("TYPE_CONSTRAINT");
  IElementType TYPE_DEFINITION = new OCamlElementType("TYPE_DEFINITION");
  IElementType TYPE_EQUATION = new OCamlElementType("TYPE_EQUATION");
  IElementType TYPE_INFORMATION = new OCamlElementType("TYPE_INFORMATION");
  IElementType TYPE_NAME = new OCamlElementType("TYPE_NAME");
  IElementType TYPE_PARAM = new OCamlElementType("TYPE_PARAM");
  IElementType TYPE_PARAMETERS = new OCamlElementType("TYPE_PARAMETERS");
  IElementType TYPE_PARAMS = new OCamlElementType("TYPE_PARAMS");
  IElementType TYPE_REPRESENTATION = new OCamlElementType("TYPE_REPRESENTATION");
  IElementType UNDERSCORE_PATTERN = new OCamlElementType("UNDERSCORE_PATTERN");
  IElementType UNDERSCORE_TYPEXPR = new OCamlElementType("UNDERSCORE_TYPEXPR");
  IElementType UNIT_IMPLEMENTATION = new OCamlElementType("UNIT_IMPLEMENTATION");
  IElementType UNIT_INTERFACE = new OCamlElementType("UNIT_INTERFACE");
  IElementType VALUE_NAME = new OCamlElementType("VALUE_NAME");
  IElementType VALUE_PATH = new OCamlElementType("VALUE_PATH");
  IElementType VALUE_PATH_BASE_EXPR = new OCamlElementType("VALUE_PATH_BASE_EXPR");
  IElementType VALUE_PATTERN = new OCamlElementType("VALUE_PATTERN");
  IElementType VARIANCE = new OCamlElementType("VARIANCE");
  IElementType WHILE_BASE_EXPR = new OCamlElementType("WHILE_BASE_EXPR");
  IElementType WITH_MODULE_TYPE = new OCamlElementType("WITH_MODULE_TYPE");

  IElementType AMPERSAND = new OCamlTokenType("&");
  IElementType AND = new OCamlTokenType("and");
  IElementType ANNOTATION = new OCamlTokenType("ANNOTATION");
  IElementType ARROBASE = new OCamlTokenType("@");
  IElementType AS = new OCamlTokenType("as");
  IElementType ASR = new OCamlTokenType("asr");
  IElementType ASSERT = new OCamlTokenType("assert");
  IElementType BACKTICK = new OCamlTokenType("`");
  IElementType BEGIN = new OCamlTokenType("begin");
  IElementType CAPITALIZED_IDENT = new OCamlTokenType("CAPITALIZED_IDENT");
  IElementType CARRET = new OCamlTokenType("^");
  IElementType CHAR_VALUE = new OCamlTokenType("CHAR_VALUE");
  IElementType CLASS = new OCamlTokenType("class");
  IElementType COLON = new OCamlTokenType(":");
  IElementType COLON_EQ = new OCamlTokenType(":=");
  IElementType COLON_GT = new OCamlTokenType(":>");
  IElementType COMMA = new OCamlTokenType(",");
  IElementType COMMENT = new OCamlTokenType("COMMENT");
  IElementType CONSTRAINT = new OCamlTokenType("constraint");
  IElementType DO = new OCamlTokenType("do");
  IElementType DOC_COMMENT = new OCamlTokenType("DOC_COMMENT");
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
  IElementType FLOAT_VALUE = new OCamlTokenType("FLOAT_VALUE");
  IElementType FOR = new OCamlTokenType("for");
  IElementType FUN = new OCamlTokenType("fun");
  IElementType FUNCTION = new OCamlTokenType("function");
  IElementType FUNCTOR = new OCamlTokenType("functor");
  IElementType GREATERRBRACE = new OCamlTokenType(">}");
  IElementType GT = new OCamlTokenType(">");
  IElementType IDENT = new OCamlTokenType("ident");
  IElementType IF = new OCamlTokenType("if");
  IElementType IN = new OCamlTokenType("in");
  IElementType INCLUDE = new OCamlTokenType("include");
  IElementType INHERIT = new OCamlTokenType("inherit");
  IElementType INITIALIZER = new OCamlTokenType("initializer");
  IElementType INTEGER_VALUE = new OCamlTokenType("INTEGER_VALUE");
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
  IElementType LOWERCASE_IDENT = new OCamlTokenType("LOWERCASE_IDENT");
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
  IElementType STRING_VALUE = new OCamlTokenType("STRING_VALUE");
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
      if (type == AND_TYPES) {
        return new OCamlAndTypesImpl(node);
      }
      else if (type == ARGUMENT) {
        return new OCamlArgumentImpl(node);
      }
      else if (type == ARGUMENT_BASE_EXPR) {
        return new OCamlArgumentBaseExprImpl(node);
      }
      else if (type == ARG_CLASS_EXPR) {
        return new OCamlArgClassExprImpl(node);
      }
      else if (type == ARROW_MODULE_TYPE) {
        return new OCamlArrowModuleTypeImpl(node);
      }
      else if (type == ASSERT_BASE_EXPR) {
        return new OCamlAssertBaseExprImpl(node);
      }
      else if (type == AS_PATTERN) {
        return new OCamlAsPatternImpl(node);
      }
      else if (type == AS_TYPEXPR) {
        return new OCamlAsTypexprImpl(node);
      }
      else if (type == BACKTICK_BASE_EXPR) {
        return new OCamlBacktickBaseExprImpl(node);
      }
      else if (type == BACKTICK_PATTERN) {
        return new OCamlBacktickPatternImpl(node);
      }
      else if (type == BEGIN_BASE_EXPR) {
        return new OCamlBeginBaseExprImpl(node);
      }
      else if (type == BRACE_BASE_EXPR) {
        return new OCamlBraceBaseExprImpl(node);
      }
      else if (type == BRACE_FIELD_BASE_EXPR) {
        return new OCamlBraceFieldBaseExprImpl(node);
      }
      else if (type == BRACE_FIELD_PATTERN) {
        return new OCamlBraceFieldPatternImpl(node);
      }
      else if (type == BRACE_PATTERN) {
        return new OCamlBracePatternImpl(node);
      }
      else if (type == BRACE_TYPE_PATTERN) {
        return new OCamlBraceTypePatternImpl(node);
      }
      else if (type == BRACKET_BASE_EXPR) {
        return new OCamlBracketBaseExprImpl(node);
      }
      else if (type == BRACKET_PATTERN) {
        return new OCamlBracketPatternImpl(node);
      }
      else if (type == CHAR_LITERAL) {
        return new OCamlCharLiteralImpl(node);
      }
      else if (type == CHAR_LITERAL_PATTERN) {
        return new OCamlCharLiteralPatternImpl(node);
      }
      else if (type == CLASSPATH_CLASS_EXPR) {
        return new OCamlClasspathClassExprImpl(node);
      }
      else if (type == CLASSPATH_TYPEXPR) {
        return new OCamlClasspathTypexprImpl(node);
      }
      else if (type == CLASSTYPE_DEF) {
        return new OCamlClasstypeDefImpl(node);
      }
      else if (type == CLASSTYPE_DEFINITION) {
        return new OCamlClasstypeDefinitionImpl(node);
      }
      else if (type == CLASSTYPE_PATH) {
        return new OCamlClasstypePathImpl(node);
      }
      else if (type == CLASSTYPE_TYPEXPR) {
        return new OCamlClasstypeTypexprImpl(node);
      }
      else if (type == CLASS_BINDING) {
        return new OCamlClassBindingImpl(node);
      }
      else if (type == CLASS_BODY) {
        return new OCamlClassBodyImpl(node);
      }
      else if (type == CLASS_BODY_TYPE) {
        return new OCamlClassBodyTypeImpl(node);
      }
      else if (type == CLASS_DEFINITION) {
        return new OCamlClassDefinitionImpl(node);
      }
      else if (type == CLASS_FIELD) {
        return new OCamlClassFieldImpl(node);
      }
      else if (type == CLASS_FIELD_SPEC) {
        return new OCamlClassFieldSpecImpl(node);
      }
      else if (type == CLASS_NAME) {
        return new OCamlClassNameImpl(node);
      }
      else if (type == CLASS_PATH) {
        return new OCamlClassPathImpl(node);
      }
      else if (type == CLASS_SPEC) {
        return new OCamlClassSpecImpl(node);
      }
      else if (type == CLASS_SPECIFICATION) {
        return new OCamlClassSpecificationImpl(node);
      }
      else if (type == CLASS_TYPE) {
        return new OCamlClassTypeImpl(node);
      }
      else if (type == COLON_BASE_EXPR) {
        return new OCamlColonBaseExprImpl(node);
      }
      else if (type == COMMA_PATTERN) {
        return new OCamlCommaPatternImpl(node);
      }
      else if (type == COMMA_TYPEXPR) {
        return new OCamlCommaTypexprImpl(node);
      }
      else if (type == CONSTANT_BASE_EXPR) {
        return new OCamlConstantBaseExprImpl(node);
      }
      else if (type == CONSTANT_PATTERN) {
        return new OCamlConstantPatternImpl(node);
      }
      else if (type == CONSTR) {
        return new OCamlConstrImpl(node);
      }
      else if (type == CONSTR_ARGS) {
        return new OCamlConstrArgsImpl(node);
      }
      else if (type == CONSTR_BASE_EXPR) {
        return new OCamlConstrBaseExprImpl(node);
      }
      else if (type == CONSTR_DECL) {
        return new OCamlConstrDeclImpl(node);
      }
      else if (type == CONSTR_NAME) {
        return new OCamlConstrNameImpl(node);
      }
      else if (type == CONSTR_PATTERN) {
        return new OCamlConstrPatternImpl(node);
      }
      else if (type == CORE_OPERATOR_CHAR) {
        return new OCamlCoreOperatorCharImpl(node);
      }
      else if (type == DEFINITION) {
        return new OCamlDefinitionImpl(node);
      }
      else if (type == DOT_BRACKET_BASE_EXPR) {
        return new OCamlDotBracketBaseExprImpl(node);
      }
      else if (type == DOT_BRACKET_LEFT_ARROW_BASE_EXPR) {
        return new OCamlDotBracketLeftArrowBaseExprImpl(node);
      }
      else if (type == DOT_FIELD_BASE_EXPR) {
        return new OCamlDotFieldBaseExprImpl(node);
      }
      else if (type == DOT_FIELD_LEFT_ARROW_BASE_EXPR) {
        return new OCamlDotFieldLeftArrowBaseExprImpl(node);
      }
      else if (type == DOT_PAREN_BASE_EXPR) {
        return new OCamlDotParenBaseExprImpl(node);
      }
      else if (type == DOT_PAREN_LEFT_ARROW_BASE_EXPR) {
        return new OCamlDotParenLeftArrowBaseExprImpl(node);
      }
      else if (type == EXCEPTION_DEFINITION) {
        return new OCamlExceptionDefinitionImpl(node);
      }
      else if (type == EXCEPTION_PATTERN) {
        return new OCamlExceptionPatternImpl(node);
      }
      else if (type == EXPR_COERCION_BASE_EXPR) {
        return new OCamlExprCoercionBaseExprImpl(node);
      }
      else if (type == EXPR_MODULE_EXPR) {
        return new OCamlExprModuleExprImpl(node);
      }
      else if (type == EXPR_TYPE_COERCION_BASE_EXPR) {
        return new OCamlExprTypeCoercionBaseExprImpl(node);
      }
      else if (type == EXTENDED_MODULE_NAME) {
        return new OCamlExtendedModuleNameImpl(node);
      }
      else if (type == EXTENDED_MODULE_PATH) {
        return new OCamlExtendedModulePathImpl(node);
      }
      else if (type == EXTERNAL_DECLARATION) {
        return new OCamlExternalDeclarationImpl(node);
      }
      else if (type == EXT_VARIANCE) {
        return new OCamlExtVarianceImpl(node);
      }
      else if (type == FIELD) {
        return new OCamlFieldImpl(node);
      }
      else if (type == FIELD_DECL) {
        return new OCamlFieldDeclImpl(node);
      }
      else if (type == FIELD_NAME) {
        return new OCamlFieldNameImpl(node);
      }
      else if (type == FLOAT_LITERAL) {
        return new OCamlFloatLiteralImpl(node);
      }
      else if (type == FOR_BASE_EXPR) {
        return new OCamlForBaseExprImpl(node);
      }
      else if (type == FUNCTION_BASE_EXPR) {
        return new OCamlFunctionBaseExprImpl(node);
      }
      else if (type == FUNCTOR_MODULE_EXPR) {
        return new OCamlFunctorModuleExprImpl(node);
      }
      else if (type == FUNCTOR_MODULE_TYPE) {
        return new OCamlFunctorModuleTypeImpl(node);
      }
      else if (type == FUN_BASE_EXPR) {
        return new OCamlFunBaseExprImpl(node);
      }
      else if (type == FUN_CLASS_EXPR) {
        return new OCamlFunClassExprImpl(node);
      }
      else if (type == IDENT_TYPEXPR) {
        return new OCamlIdentTypexprImpl(node);
      }
      else if (type == IF_ELSE_BASE_EXPR) {
        return new OCamlIfElseBaseExprImpl(node);
      }
      else if (type == INFIX_OP) {
        return new OCamlInfixOpImpl(node);
      }
      else if (type == INFIX_OP_BASE_EXPR) {
        return new OCamlInfixOpBaseExprImpl(node);
      }
      else if (type == INFIX_SYMBOL) {
        return new OCamlInfixSymbolImpl(node);
      }
      else if (type == INJECTIVITY) {
        return new OCamlInjectivityImpl(node);
      }
      else if (type == INST_VAR_NAME) {
        return new OCamlInstVarNameImpl(node);
      }
      else if (type == INTEGER_LITERAL) {
        return new OCamlIntegerLiteralImpl(node);
      }
      else if (type == INT_32_LITERAL) {
        return new OCamlInt32LiteralImpl(node);
      }
      else if (type == INT_64_LITERAL) {
        return new OCamlInt64LiteralImpl(node);
      }
      else if (type == LABEL) {
        return new OCamlLabelImpl(node);
      }
      else if (type == LABEL_NAME) {
        return new OCamlLabelNameImpl(node);
      }
      else if (type == LABEL_OPT_TYPEXPR) {
        return new OCamlLabelOptTypexprImpl(node);
      }
      else if (type == LABEL_TYPEXPR) {
        return new OCamlLabelTypexprImpl(node);
      }
      else if (type == LARRAY_BASE_EXPR) {
        return new OCamlLarrayBaseExprImpl(node);
      }
      else if (type == LARRAY_PATTERN) {
        return new OCamlLarrayPatternImpl(node);
      }
      else if (type == LAZY_BASE_EXPR) {
        return new OCamlLazyBaseExprImpl(node);
      }
      else if (type == LAZY_PATTERN) {
        return new OCamlLazyPatternImpl(node);
      }
      else if (type == LET_BASE_EXPR) {
        return new OCamlLetBaseExprImpl(node);
      }
      else if (type == LET_BINDING) {
        return new OCamlLetBindingImpl(node);
      }
      else if (type == LET_CLASS_EXPR) {
        return new OCamlLetClassExprImpl(node);
      }
      else if (type == LET_EXCEPTION_BASE_EXPR) {
        return new OCamlLetExceptionBaseExprImpl(node);
      }
      else if (type == LET_MODULE_BASE_EXPR) {
        return new OCamlLetModuleBaseExprImpl(node);
      }
      else if (type == LET_OPEN_CLASS_EXPR) {
        return new OCamlLetOpenClassExprImpl(node);
      }
      else if (type == LOCAL_OPEN) {
        return new OCamlLocalOpenImpl(node);
      }
      else if (type == LOCAL_OPEN_BASE_EXPR) {
        return new OCamlLocalOpenBaseExprImpl(node);
      }
      else if (type == LTGT_METHOD_TYPEXPR) {
        return new OCamlLtgtMethodTypexprImpl(node);
      }
      else if (type == LTGT_TYPEXPR) {
        return new OCamlLtgtTypexprImpl(node);
      }
      else if (type == MATCH_BASE_EXPR) {
        return new OCamlMatchBaseExprImpl(node);
      }
      else if (type == METHOD_NAME) {
        return new OCamlMethodNameImpl(node);
      }
      else if (type == METHOD_TYPE) {
        return new OCamlMethodTypeImpl(node);
      }
      else if (type == MINUS_BASE_EXPR) {
        return new OCamlMinusBaseExprImpl(node);
      }
      else if (type == MINUS_DOT_BASE_EXPR) {
        return new OCamlMinusDotBaseExprImpl(node);
      }
      else if (type == MODPATH_MODULE_EXPR) {
        return new OCamlModpathModuleExprImpl(node);
      }
      else if (type == MODTYPE_MODULE_TYPE) {
        return new OCamlModtypeModuleTypeImpl(node);
      }
      else if (type == MODTYPE_NAME) {
        return new OCamlModtypeNameImpl(node);
      }
      else if (type == MODTYPE_PATH) {
        return new OCamlModtypePathImpl(node);
      }
      else if (type == MODULE_ITEMS) {
        return new OCamlModuleItemsImpl(node);
      }
      else if (type == MODULE_NAME) {
        return new OCamlModuleNameImpl(node);
      }
      else if (type == MODULE_PATH) {
        return new OCamlModulePathImpl(node);
      }
      else if (type == MODULE_PATH_ARRAY_PATTERN) {
        return new OCamlModulePathArrayPatternImpl(node);
      }
      else if (type == MODULE_PATH_BRACE_PATTERN) {
        return new OCamlModulePathBracePatternImpl(node);
      }
      else if (type == MODULE_PATH_BRACKET_PATTERN) {
        return new OCamlModulePathBracketPatternImpl(node);
      }
      else if (type == MODULE_PATH_PAREN_PATTERN) {
        return new OCamlModulePathParenPatternImpl(node);
      }
      else if (type == MOD_CONSTRAINT) {
        return new OCamlModConstraintImpl(node);
      }
      else if (type == NATIVEINT_LITERAL) {
        return new OCamlNativeintLiteralImpl(node);
      }
      else if (type == OBJECT_CLASS_EXPR) {
        return new OCamlObjectClassExprImpl(node);
      }
      else if (type == OBJECT_EXPR_INTERNAL_BASE_EXPR) {
        return new OCamlObjectExprInternalBaseExprImpl(node);
      }
      else if (type == OBJECT_EXPR_INTERNAL_REC_BASE_EXPR) {
        return new OCamlObjectExprInternalRecBaseExprImpl(node);
      }
      else if (type == OPERATOR_CHAR) {
        return new OCamlOperatorCharImpl(node);
      }
      else if (type == OPERATOR_NAME) {
        return new OCamlOperatorNameImpl(node);
      }
      else if (type == OPTLABEL) {
        return new OCamlOptlabelImpl(node);
      }
      else if (type == PARAMETER) {
        return new OCamlParameterImpl(node);
      }
      else if (type == PARENTHESIS_TYPEXPR) {
        return new OCamlParenthesisTypexprImpl(node);
      }
      else if (type == PAREN_BASE_EXPR) {
        return new OCamlParenBaseExprImpl(node);
      }
      else if (type == PAREN_CLASS_EXPR) {
        return new OCamlParenClassExprImpl(node);
      }
      else if (type == PAREN_MODULE_EXPR) {
        return new OCamlParenModuleExprImpl(node);
      }
      else if (type == PAREN_MODULE_TYPE) {
        return new OCamlParenModuleTypeImpl(node);
      }
      else if (type == PAREN_SHARP_TYPEXPR) {
        return new OCamlParenSharpTypexprImpl(node);
      }
      else if (type == PAREN_TYPE_BASE_EXPR) {
        return new OCamlParenTypeBaseExprImpl(node);
      }
      else if (type == PAREN_TYPE_CLASS_EXPR) {
        return new OCamlParenTypeClassExprImpl(node);
      }
      else if (type == PAREN_TYPE_MODULE_EXPR) {
        return new OCamlParenTypeModuleExprImpl(node);
      }
      else if (type == PATTERN_EXPR) {
        return new OCamlPatternExprImpl(node);
      }
      else if (type == PATTERN_MATCHING) {
        return new OCamlPatternMatchingImpl(node);
      }
      else if (type == PATTERN_PIPE_PATTERN) {
        return new OCamlPatternPipePatternImpl(node);
      }
      else if (type == POLYMORPHIC_VARIANT_TYPE) {
        return new OCamlPolymorphicVariantTypeImpl(node);
      }
      else if (type == POLYMORPHIC_VARIANT_TYPE_TYPEXPR) {
        return new OCamlPolymorphicVariantTypeTypexprImpl(node);
      }
      else if (type == POLY_TYPEXPR) {
        return new OCamlPolyTypexprImpl(node);
      }
      else if (type == PREFIX_BASE_EXPR) {
        return new OCamlPrefixBaseExprImpl(node);
      }
      else if (type == PREFIX_SYMBOL) {
        return new OCamlPrefixSymbolImpl(node);
      }
      else if (type == RECORD_DECL) {
        return new OCamlRecordDeclImpl(node);
      }
      else if (type == RECURSIVE_MODULE_EXTENSION_DEF) {
        return new OCamlRecursiveModuleExtensionDefImpl(node);
      }
      else if (type == RECURSIVE_MODULE_EXTENSION_SPEC) {
        return new OCamlRecursiveModuleExtensionSpecImpl(node);
      }
      else if (type == SEMI_BASE_EXPR) {
        return new OCamlSemiBaseExprImpl(node);
      }
      else if (type == SHARP_PATTERN) {
        return new OCamlSharpPatternImpl(node);
      }
      else if (type == SHORTCUT_BASE_EXPR) {
        return new OCamlShortcutBaseExprImpl(node);
      }
      else if (type == SHORTCUT_PATTERN) {
        return new OCamlShortcutPatternImpl(node);
      }
      else if (type == SIG_MODULE_TYPE) {
        return new OCamlSigModuleTypeImpl(node);
      }
      else if (type == SPECIFICATION) {
        return new OCamlSpecificationImpl(node);
      }
      else if (type == STAR_TYPEXPR) {
        return new OCamlStarTypexprImpl(node);
      }
      else if (type == STRING_LITERAL) {
        return new OCamlStringLiteralImpl(node);
      }
      else if (type == STRUCT_MODULE_EXPR) {
        return new OCamlStructModuleExprImpl(node);
      }
      else if (type == TAG_NAME) {
        return new OCamlTagNameImpl(node);
      }
      else if (type == TAG_SPEC) {
        return new OCamlTagSpecImpl(node);
      }
      else if (type == TAG_SPEC_FIRST) {
        return new OCamlTagSpecFirstImpl(node);
      }
      else if (type == TAG_SPEC_FULL) {
        return new OCamlTagSpecFullImpl(node);
      }
      else if (type == TRY_BASE_EXPR) {
        return new OCamlTryBaseExprImpl(node);
      }
      else if (type == TYPECONSTR) {
        return new OCamlTypeconstrImpl(node);
      }
      else if (type == TYPECONSTR_NAME) {
        return new OCamlTypeconstrNameImpl(node);
      }
      else if (type == TYPECONSTR_TYPEXPR) {
        return new OCamlTypeconstrTypexprImpl(node);
      }
      else if (type == TYPEDEF) {
        return new OCamlTypedefImpl(node);
      }
      else if (type == TYPEEXPR_CLASS_EXPR) {
        return new OCamlTypeexprClassExprImpl(node);
      }
      else if (type == TYPE_CONSTRAINT) {
        return new OCamlTypeConstraintImpl(node);
      }
      else if (type == TYPE_DEFINITION) {
        return new OCamlTypeDefinitionImpl(node);
      }
      else if (type == TYPE_EQUATION) {
        return new OCamlTypeEquationImpl(node);
      }
      else if (type == TYPE_INFORMATION) {
        return new OCamlTypeInformationImpl(node);
      }
      else if (type == TYPE_NAME) {
        return new OCamlTypeNameImpl(node);
      }
      else if (type == TYPE_PARAM) {
        return new OCamlTypeParamImpl(node);
      }
      else if (type == TYPE_PARAMETERS) {
        return new OCamlTypeParametersImpl(node);
      }
      else if (type == TYPE_PARAMS) {
        return new OCamlTypeParamsImpl(node);
      }
      else if (type == TYPE_REPRESENTATION) {
        return new OCamlTypeRepresentationImpl(node);
      }
      else if (type == UNDERSCORE_PATTERN) {
        return new OCamlUnderscorePatternImpl(node);
      }
      else if (type == UNDERSCORE_TYPEXPR) {
        return new OCamlUnderscoreTypexprImpl(node);
      }
      else if (type == UNIT_IMPLEMENTATION) {
        return new OCamlUnitImplementationImpl(node);
      }
      else if (type == UNIT_INTERFACE) {
        return new OCamlUnitInterfaceImpl(node);
      }
      else if (type == VALUE_NAME) {
        return new OCamlValueNameImpl(node);
      }
      else if (type == VALUE_PATH) {
        return new OCamlValuePathImpl(node);
      }
      else if (type == VALUE_PATH_BASE_EXPR) {
        return new OCamlValuePathBaseExprImpl(node);
      }
      else if (type == VALUE_PATTERN) {
        return new OCamlValuePatternImpl(node);
      }
      else if (type == VARIANCE) {
        return new OCamlVarianceImpl(node);
      }
      else if (type == WHILE_BASE_EXPR) {
        return new OCamlWhileBaseExprImpl(node);
      }
      else if (type == WITH_MODULE_TYPE) {
        return new OCamlWithModuleTypeImpl(node);
      }
      throw new AssertionError("Unknown element type: " + type);
    }
  }
}
