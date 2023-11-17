// This is a generated file. Not intended for manual editing.
package com.ocaml.language.parser;

import com.intellij.lang.PsiBuilder;
import com.intellij.lang.PsiBuilder.Marker;
import static com.ocaml.language.psi.OCamlTypes.*;
import static com.ocaml.language.parser.OCamlParserUtils.*;
import com.intellij.psi.tree.IElementType;
import com.intellij.lang.ASTNode;
import com.intellij.psi.tree.TokenSet;
import com.intellij.lang.PsiParser;
import com.intellij.lang.LightPsiParser;

@SuppressWarnings({"SimplifiableIfStatement", "UnusedAssignment"})
public class OCamlParser implements PsiParser, LightPsiParser {

  public ASTNode parse(IElementType t, PsiBuilder b) {
    parseLight(t, b);
    return b.getTreeBuilt();
  }

  public void parseLight(IElementType t, PsiBuilder b) {
    boolean r;
    b = adapt_builder_(t, b, this, EXTENDS_SETS_);
    Marker m = enter_section_(b, 0, _COLLAPSE_, null);
    r = parse_root_(t, b);
    exit_section_(b, 0, m, t, r, true, TRUE_CONDITION);
  }

  protected boolean parse_root_(IElementType t, PsiBuilder b) {
    return parse_root_(t, b, 0);
  }

  static boolean parse_root_(IElementType t, PsiBuilder b, int l) {
    return file(b, l + 1);
  }

  public static final TokenSet[] EXTENDS_SETS_ = new TokenSet[] {
    create_token_set_(ARROW_MODULE_TYPE, FUNCTOR_MODULE_TYPE, MODTYPE_MODULE_TYPE, MODULE_TYPE,
      PAREN_MODULE_TYPE, SIG_MODULE_TYPE, WITH_MODULE_TYPE),
    create_token_set_(EXPR_MODULE_EXPR, FUNCTOR_MODULE_EXPR, MODPATH_MODULE_EXPR, MODULE_EXPR,
      PAREN_MODULE_EXPR, PAREN_TYPE_MODULE_EXPR, STRUCT_MODULE_EXPR),
    create_token_set_(ARG_CLASS_EXPR, CLASSPATH_CLASS_EXPR, CLASS_EXPR, FUN_CLASS_EXPR,
      LET_CLASS_EXPR, LET_OPEN_CLASS_EXPR, OBJECT_CLASS_EXPR, PAREN_CLASS_EXPR,
      PAREN_TYPE_CLASS_EXPR, TYPEEXPR_CLASS_EXPR),
    create_token_set_(AS_TYPEXPR, CLASSPATH_TYPEXPR, CLASSTYPE_TYPEXPR, COMMA_TYPEXPR,
      IDENT_TYPEXPR, LABEL_OPT_TYPEXPR, LABEL_TYPEXPR, LTGT_METHOD_TYPEXPR,
      LTGT_TYPEXPR, PARENTHESIS_TYPEXPR, PAREN_SHARP_TYPEXPR, POLYMORPHIC_VARIANT_TYPE_TYPEXPR,
      POLY_TYPEXPR, STAR_TYPEXPR, TYPECONSTR_TYPEXPR, TYPEXPR,
      UNDERSCORE_TYPEXPR),
    create_token_set_(AS_PATTERN, BACKTICK_PATTERN, BRACE_FIELD_PATTERN, BRACE_PATTERN,
      BRACE_TYPE_PATTERN, BRACKET_PATTERN, CHAR_LITERAL_PATTERN, COMMA_PATTERN,
      CONSTANT_PATTERN, CONSTR_PATTERN, EXCEPTION_PATTERN, LARRAY_PATTERN,
      LAZY_PATTERN, MODULE_PATH_ARRAY_PATTERN, MODULE_PATH_BRACE_PATTERN, MODULE_PATH_BRACKET_PATTERN,
      MODULE_PATH_PAREN_PATTERN, PATTERN, PATTERN_PIPE_PATTERN, SHARP_PATTERN,
      SHORTCUT_PATTERN, UNDERSCORE_PATTERN, VALUE_PATTERN),
    create_token_set_(ARGUMENT_BASE_EXPR, ASSERT_BASE_EXPR, BACKTICK_BASE_EXPR, BEGIN_BASE_EXPR,
      BRACE_BASE_EXPR, BRACE_FIELD_BASE_EXPR, BRACKET_BASE_EXPR, COLON_BASE_EXPR,
      CONSTANT_BASE_EXPR, CONSTR_BASE_EXPR, DOT_BRACKET_BASE_EXPR, DOT_BRACKET_LEFT_ARROW_BASE_EXPR,
      DOT_FIELD_BASE_EXPR, DOT_FIELD_LEFT_ARROW_BASE_EXPR, DOT_PAREN_BASE_EXPR, DOT_PAREN_LEFT_ARROW_BASE_EXPR,
      EXPR, EXPR_COERCION_BASE_EXPR, EXPR_TYPE_COERCION_BASE_EXPR, FOR_BASE_EXPR,
      FUNCTION_BASE_EXPR, FUN_BASE_EXPR, IF_ELSE_BASE_EXPR, INFIX_OP_BASE_EXPR,
      LARRAY_BASE_EXPR, LAZY_BASE_EXPR, LET_BASE_EXPR, LET_EXCEPTION_BASE_EXPR,
      LET_MODULE_BASE_EXPR, LOCAL_OPEN_BASE_EXPR, MATCH_BASE_EXPR, MINUS_BASE_EXPR,
      MINUS_DOT_BASE_EXPR, OBJECT_EXPR_INTERNAL_BASE_EXPR, OBJECT_EXPR_INTERNAL_REC_BASE_EXPR, PAREN_BASE_EXPR,
      PAREN_TYPE_BASE_EXPR, PREFIX_BASE_EXPR, SEMI_BASE_EXPR, SHORTCUT_BASE_EXPR,
      TRY_BASE_EXPR, VALUE_PATH_BASE_EXPR, WHILE_BASE_EXPR),
  };

  /* ********************************************************** */
  // AND typedef
  public static boolean and_types(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "and_types")) return false;
    if (!nextTokenIs(b, AND)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, AND);
    r = r && typedef(b, l + 1);
    exit_section_(b, m, AND_TYPES, r);
    return r;
  }

  /* ********************************************************** */
  // expr
  //   |  TILDE label-name
  //   |  TILDE label-name COLON expr
  //   |  QUESTION_MARK label-name
  //   |  QUESTION_MARK label-name COLON expr
  public static boolean argument(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "argument")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, ARGUMENT, "<argument>");
    r = expr(b, l + 1, -1);
    if (!r) r = argument_1(b, l + 1);
    if (!r) r = argument_2(b, l + 1);
    if (!r) r = argument_3(b, l + 1);
    if (!r) r = argument_4(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  // TILDE label-name
  private static boolean argument_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "argument_1")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, TILDE);
    r = r && label_name(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // TILDE label-name COLON expr
  private static boolean argument_2(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "argument_2")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, TILDE);
    r = r && label_name(b, l + 1);
    r = r && consumeToken(b, COLON);
    r = r && expr(b, l + 1, -1);
    exit_section_(b, m, null, r);
    return r;
  }

  // QUESTION_MARK label-name
  private static boolean argument_3(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "argument_3")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, QUESTION_MARK);
    r = r && label_name(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // QUESTION_MARK label-name COLON expr
  private static boolean argument_4(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "argument_4")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, QUESTION_MARK);
    r = r && label_name(b, l + 1);
    r = r && consumeToken(b, COLON);
    r = r && expr(b, l + 1, -1);
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // CHAR_VALUE
  public static boolean char_literal(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "char_literal")) return false;
    if (!nextTokenIs(b, CHAR_VALUE)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, CHAR_VALUE);
    exit_section_(b, m, CHAR_LITERAL, r);
    return r;
  }

  /* ********************************************************** */
  // [VIRTUAL] [[ type-parameters ]] class-name { parameter } [ COLON class-type] EQ class_expr
  public static boolean class_binding(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "class_binding")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, CLASS_BINDING, "<class binding>");
    r = class_binding_0(b, l + 1);
    r = r && class_binding_1(b, l + 1);
    r = r && class_name(b, l + 1);
    r = r && class_binding_3(b, l + 1);
    r = r && class_binding_4(b, l + 1);
    r = r && consumeToken(b, EQ);
    r = r && class_expr(b, l + 1, -1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  // [VIRTUAL]
  private static boolean class_binding_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "class_binding_0")) return false;
    consumeToken(b, VIRTUAL);
    return true;
  }

  // [[ type-parameters ]]
  private static boolean class_binding_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "class_binding_1")) return false;
    class_binding_1_0(b, l + 1);
    return true;
  }

  // [ type-parameters ]
  private static boolean class_binding_1_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "class_binding_1_0")) return false;
    type_parameters(b, l + 1);
    return true;
  }

  // { parameter }
  private static boolean class_binding_3(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "class_binding_3")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = parameter(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // [ COLON class-type]
  private static boolean class_binding_4(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "class_binding_4")) return false;
    class_binding_4_0(b, l + 1);
    return true;
  }

  // COLON class-type
  private static boolean class_binding_4_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "class_binding_4_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, COLON);
    r = r && class_type(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // [( pattern [COLON typexpr] )] { class-field }*
  public static boolean class_body(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "class_body")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, CLASS_BODY, "<class body>");
    r = class_body_0(b, l + 1);
    r = r && class_body_1(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  // [( pattern [COLON typexpr] )]
  private static boolean class_body_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "class_body_0")) return false;
    class_body_0_0(b, l + 1);
    return true;
  }

  // pattern [COLON typexpr]
  private static boolean class_body_0_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "class_body_0_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = pattern(b, l + 1, -1);
    r = r && class_body_0_0_1(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // [COLON typexpr]
  private static boolean class_body_0_0_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "class_body_0_0_1")) return false;
    class_body_0_0_1_0(b, l + 1);
    return true;
  }

  // COLON typexpr
  private static boolean class_body_0_0_1_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "class_body_0_0_1_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, COLON);
    r = r && typexpr(b, l + 1, -1);
    exit_section_(b, m, null, r);
    return r;
  }

  // { class-field }*
  private static boolean class_body_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "class_body_1")) return false;
    while (true) {
      int c = current_position_(b);
      if (!class_body_1_0(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "class_body_1", c)) break;
    }
    return true;
  }

  // { class-field }
  private static boolean class_body_1_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "class_body_1_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = class_field(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // OBJECT [( typexpr )] { class-field-spec } END
  //   | [[ typexpr { COMMA typexpr } ]] classtype-path
  //   | LET OPEN module-path IN class-body-type
  public static boolean class_body_type(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "class_body_type")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, CLASS_BODY_TYPE, "<class body type>");
    r = class_body_type_0(b, l + 1);
    if (!r) r = class_body_type_1(b, l + 1);
    if (!r) r = class_body_type_2(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  // OBJECT [( typexpr )] { class-field-spec } END
  private static boolean class_body_type_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "class_body_type_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, OBJECT);
    r = r && class_body_type_0_1(b, l + 1);
    r = r && class_body_type_0_2(b, l + 1);
    r = r && consumeToken(b, END);
    exit_section_(b, m, null, r);
    return r;
  }

  // [( typexpr )]
  private static boolean class_body_type_0_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "class_body_type_0_1")) return false;
    class_body_type_0_1_0(b, l + 1);
    return true;
  }

  // ( typexpr )
  private static boolean class_body_type_0_1_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "class_body_type_0_1_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = typexpr(b, l + 1, -1);
    exit_section_(b, m, null, r);
    return r;
  }

  // { class-field-spec }
  private static boolean class_body_type_0_2(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "class_body_type_0_2")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = class_field_spec(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // [[ typexpr { COMMA typexpr } ]] classtype-path
  private static boolean class_body_type_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "class_body_type_1")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = class_body_type_1_0(b, l + 1);
    r = r && classtype_path(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // [[ typexpr { COMMA typexpr } ]]
  private static boolean class_body_type_1_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "class_body_type_1_0")) return false;
    class_body_type_1_0_0(b, l + 1);
    return true;
  }

  // [ typexpr { COMMA typexpr } ]
  private static boolean class_body_type_1_0_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "class_body_type_1_0_0")) return false;
    class_body_type_1_0_0_0(b, l + 1);
    return true;
  }

  // typexpr { COMMA typexpr }
  private static boolean class_body_type_1_0_0_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "class_body_type_1_0_0_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = typexpr(b, l + 1, -1);
    r = r && class_body_type_1_0_0_0_1(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // COMMA typexpr
  private static boolean class_body_type_1_0_0_0_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "class_body_type_1_0_0_0_1")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, COMMA);
    r = r && typexpr(b, l + 1, -1);
    exit_section_(b, m, null, r);
    return r;
  }

  // LET OPEN module-path IN class-body-type
  private static boolean class_body_type_2(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "class_body_type_2")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeTokens(b, 0, LET, OPEN);
    r = r && module_path(b, l + 1);
    r = r && consumeToken(b, IN);
    r = r && class_body_type(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // CLASS class-binding { AND class-binding }*
  public static boolean class_definition(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "class_definition")) return false;
    if (!nextTokenIs(b, CLASS)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, CLASS);
    r = r && class_binding(b, l + 1);
    r = r && class_definition_2(b, l + 1);
    exit_section_(b, m, CLASS_DEFINITION, r);
    return r;
  }

  // { AND class-binding }*
  private static boolean class_definition_2(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "class_definition_2")) return false;
    while (true) {
      int c = current_position_(b);
      if (!class_definition_2_0(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "class_definition_2", c)) break;
    }
    return true;
  }

  // AND class-binding
  private static boolean class_definition_2_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "class_definition_2_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, AND);
    r = r && class_binding(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // INHERIT class_expr [AS LOWERCASE_IDENT]
  //   |   INHERIT EXCLAMATION_MARK class_expr [AS LOWERCASE_IDENT]
  //   |   VAL [MUTABLE] inst-var-name [COLON typexpr] EQ expr
  //   |   VAL EXCLAMATION_MARK [MUTABLE] inst-var-name [COLON typexpr] EQ expr
  //   |   VAL [MUTABLE] VIRTUAL inst-var-name COLON typexpr
  //   |   VAL VIRTUAL MUTABLE inst-var-name COLON typexpr
  //   |   METHOD [PRIVATE] method-name { parameter }* [COLON typexpr] EQ expr
  //   |   METHOD EXCLAMATION_MARK [PRIVATE] method-name { parameter }* [COLON typexpr] EQ expr
  //   |   METHOD [PRIVATE] method-name COLON poly-typexpr EQ expr
  //   |   METHOD EXCLAMATION_MARK [PRIVATE] method-name COLON poly-typexpr EQ expr
  //   |   METHOD [PRIVATE] VIRTUAL method-name COLON poly-typexpr
  //   |   METHOD VIRTUAL PRIVATE method-name COLON poly-typexpr
  //   |   CONSTRAINT typexpr EQ typexpr
  //   |   INITIALIZER expr
  public static boolean class_field(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "class_field")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, CLASS_FIELD, "<class field>");
    r = class_field_0(b, l + 1);
    if (!r) r = class_field_1(b, l + 1);
    if (!r) r = class_field_2(b, l + 1);
    if (!r) r = class_field_3(b, l + 1);
    if (!r) r = class_field_4(b, l + 1);
    if (!r) r = class_field_5(b, l + 1);
    if (!r) r = class_field_6(b, l + 1);
    if (!r) r = class_field_7(b, l + 1);
    if (!r) r = class_field_8(b, l + 1);
    if (!r) r = class_field_9(b, l + 1);
    if (!r) r = class_field_10(b, l + 1);
    if (!r) r = class_field_11(b, l + 1);
    if (!r) r = class_field_12(b, l + 1);
    if (!r) r = class_field_13(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  // INHERIT class_expr [AS LOWERCASE_IDENT]
  private static boolean class_field_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "class_field_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, INHERIT);
    r = r && class_expr(b, l + 1, -1);
    r = r && class_field_0_2(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // [AS LOWERCASE_IDENT]
  private static boolean class_field_0_2(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "class_field_0_2")) return false;
    parseTokens(b, 0, AS, LOWERCASE_IDENT);
    return true;
  }

  // INHERIT EXCLAMATION_MARK class_expr [AS LOWERCASE_IDENT]
  private static boolean class_field_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "class_field_1")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeTokens(b, 0, INHERIT, EXCLAMATION_MARK);
    r = r && class_expr(b, l + 1, -1);
    r = r && class_field_1_3(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // [AS LOWERCASE_IDENT]
  private static boolean class_field_1_3(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "class_field_1_3")) return false;
    parseTokens(b, 0, AS, LOWERCASE_IDENT);
    return true;
  }

  // VAL [MUTABLE] inst-var-name [COLON typexpr] EQ expr
  private static boolean class_field_2(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "class_field_2")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, VAL);
    r = r && class_field_2_1(b, l + 1);
    r = r && inst_var_name(b, l + 1);
    r = r && class_field_2_3(b, l + 1);
    r = r && consumeToken(b, EQ);
    r = r && expr(b, l + 1, -1);
    exit_section_(b, m, null, r);
    return r;
  }

  // [MUTABLE]
  private static boolean class_field_2_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "class_field_2_1")) return false;
    consumeToken(b, MUTABLE);
    return true;
  }

  // [COLON typexpr]
  private static boolean class_field_2_3(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "class_field_2_3")) return false;
    class_field_2_3_0(b, l + 1);
    return true;
  }

  // COLON typexpr
  private static boolean class_field_2_3_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "class_field_2_3_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, COLON);
    r = r && typexpr(b, l + 1, -1);
    exit_section_(b, m, null, r);
    return r;
  }

  // VAL EXCLAMATION_MARK [MUTABLE] inst-var-name [COLON typexpr] EQ expr
  private static boolean class_field_3(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "class_field_3")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeTokens(b, 0, VAL, EXCLAMATION_MARK);
    r = r && class_field_3_2(b, l + 1);
    r = r && inst_var_name(b, l + 1);
    r = r && class_field_3_4(b, l + 1);
    r = r && consumeToken(b, EQ);
    r = r && expr(b, l + 1, -1);
    exit_section_(b, m, null, r);
    return r;
  }

  // [MUTABLE]
  private static boolean class_field_3_2(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "class_field_3_2")) return false;
    consumeToken(b, MUTABLE);
    return true;
  }

  // [COLON typexpr]
  private static boolean class_field_3_4(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "class_field_3_4")) return false;
    class_field_3_4_0(b, l + 1);
    return true;
  }

  // COLON typexpr
  private static boolean class_field_3_4_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "class_field_3_4_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, COLON);
    r = r && typexpr(b, l + 1, -1);
    exit_section_(b, m, null, r);
    return r;
  }

  // VAL [MUTABLE] VIRTUAL inst-var-name COLON typexpr
  private static boolean class_field_4(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "class_field_4")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, VAL);
    r = r && class_field_4_1(b, l + 1);
    r = r && consumeToken(b, VIRTUAL);
    r = r && inst_var_name(b, l + 1);
    r = r && consumeToken(b, COLON);
    r = r && typexpr(b, l + 1, -1);
    exit_section_(b, m, null, r);
    return r;
  }

  // [MUTABLE]
  private static boolean class_field_4_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "class_field_4_1")) return false;
    consumeToken(b, MUTABLE);
    return true;
  }

  // VAL VIRTUAL MUTABLE inst-var-name COLON typexpr
  private static boolean class_field_5(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "class_field_5")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeTokens(b, 0, VAL, VIRTUAL, MUTABLE);
    r = r && inst_var_name(b, l + 1);
    r = r && consumeToken(b, COLON);
    r = r && typexpr(b, l + 1, -1);
    exit_section_(b, m, null, r);
    return r;
  }

  // METHOD [PRIVATE] method-name { parameter }* [COLON typexpr] EQ expr
  private static boolean class_field_6(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "class_field_6")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, METHOD);
    r = r && class_field_6_1(b, l + 1);
    r = r && method_name(b, l + 1);
    r = r && class_field_6_3(b, l + 1);
    r = r && class_field_6_4(b, l + 1);
    r = r && consumeToken(b, EQ);
    r = r && expr(b, l + 1, -1);
    exit_section_(b, m, null, r);
    return r;
  }

  // [PRIVATE]
  private static boolean class_field_6_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "class_field_6_1")) return false;
    consumeToken(b, PRIVATE);
    return true;
  }

  // { parameter }*
  private static boolean class_field_6_3(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "class_field_6_3")) return false;
    while (true) {
      int c = current_position_(b);
      if (!class_field_6_3_0(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "class_field_6_3", c)) break;
    }
    return true;
  }

  // { parameter }
  private static boolean class_field_6_3_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "class_field_6_3_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = parameter(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // [COLON typexpr]
  private static boolean class_field_6_4(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "class_field_6_4")) return false;
    class_field_6_4_0(b, l + 1);
    return true;
  }

  // COLON typexpr
  private static boolean class_field_6_4_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "class_field_6_4_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, COLON);
    r = r && typexpr(b, l + 1, -1);
    exit_section_(b, m, null, r);
    return r;
  }

  // METHOD EXCLAMATION_MARK [PRIVATE] method-name { parameter }* [COLON typexpr] EQ expr
  private static boolean class_field_7(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "class_field_7")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeTokens(b, 0, METHOD, EXCLAMATION_MARK);
    r = r && class_field_7_2(b, l + 1);
    r = r && method_name(b, l + 1);
    r = r && class_field_7_4(b, l + 1);
    r = r && class_field_7_5(b, l + 1);
    r = r && consumeToken(b, EQ);
    r = r && expr(b, l + 1, -1);
    exit_section_(b, m, null, r);
    return r;
  }

  // [PRIVATE]
  private static boolean class_field_7_2(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "class_field_7_2")) return false;
    consumeToken(b, PRIVATE);
    return true;
  }

  // { parameter }*
  private static boolean class_field_7_4(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "class_field_7_4")) return false;
    while (true) {
      int c = current_position_(b);
      if (!class_field_7_4_0(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "class_field_7_4", c)) break;
    }
    return true;
  }

  // { parameter }
  private static boolean class_field_7_4_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "class_field_7_4_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = parameter(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // [COLON typexpr]
  private static boolean class_field_7_5(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "class_field_7_5")) return false;
    class_field_7_5_0(b, l + 1);
    return true;
  }

  // COLON typexpr
  private static boolean class_field_7_5_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "class_field_7_5_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, COLON);
    r = r && typexpr(b, l + 1, -1);
    exit_section_(b, m, null, r);
    return r;
  }

  // METHOD [PRIVATE] method-name COLON poly-typexpr EQ expr
  private static boolean class_field_8(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "class_field_8")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, METHOD);
    r = r && class_field_8_1(b, l + 1);
    r = r && method_name(b, l + 1);
    r = r && consumeToken(b, COLON);
    r = r && poly_typexpr(b, l + 1);
    r = r && consumeToken(b, EQ);
    r = r && expr(b, l + 1, -1);
    exit_section_(b, m, null, r);
    return r;
  }

  // [PRIVATE]
  private static boolean class_field_8_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "class_field_8_1")) return false;
    consumeToken(b, PRIVATE);
    return true;
  }

  // METHOD EXCLAMATION_MARK [PRIVATE] method-name COLON poly-typexpr EQ expr
  private static boolean class_field_9(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "class_field_9")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeTokens(b, 0, METHOD, EXCLAMATION_MARK);
    r = r && class_field_9_2(b, l + 1);
    r = r && method_name(b, l + 1);
    r = r && consumeToken(b, COLON);
    r = r && poly_typexpr(b, l + 1);
    r = r && consumeToken(b, EQ);
    r = r && expr(b, l + 1, -1);
    exit_section_(b, m, null, r);
    return r;
  }

  // [PRIVATE]
  private static boolean class_field_9_2(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "class_field_9_2")) return false;
    consumeToken(b, PRIVATE);
    return true;
  }

  // METHOD [PRIVATE] VIRTUAL method-name COLON poly-typexpr
  private static boolean class_field_10(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "class_field_10")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, METHOD);
    r = r && class_field_10_1(b, l + 1);
    r = r && consumeToken(b, VIRTUAL);
    r = r && method_name(b, l + 1);
    r = r && consumeToken(b, COLON);
    r = r && poly_typexpr(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // [PRIVATE]
  private static boolean class_field_10_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "class_field_10_1")) return false;
    consumeToken(b, PRIVATE);
    return true;
  }

  // METHOD VIRTUAL PRIVATE method-name COLON poly-typexpr
  private static boolean class_field_11(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "class_field_11")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeTokens(b, 0, METHOD, VIRTUAL, PRIVATE);
    r = r && method_name(b, l + 1);
    r = r && consumeToken(b, COLON);
    r = r && poly_typexpr(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // CONSTRAINT typexpr EQ typexpr
  private static boolean class_field_12(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "class_field_12")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, CONSTRAINT);
    r = r && typexpr(b, l + 1, -1);
    r = r && consumeToken(b, EQ);
    r = r && typexpr(b, l + 1, -1);
    exit_section_(b, m, null, r);
    return r;
  }

  // INITIALIZER expr
  private static boolean class_field_13(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "class_field_13")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, INITIALIZER);
    r = r && expr(b, l + 1, -1);
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // INHERIT class-body-type
  //   |   VAL [MUTABLE] [VIRTUAL] inst-var-name COLON typexpr
  //   |   VAL VIRTUAL MUTABLE inst-var-name COLON typexpr
  //   |   METHOD [PRIVATE] [VIRTUAL] method-name COLON poly-typexpr
  //   |   METHOD VIRTUAL PRIVATE method-name COLON poly-typexpr
  //   |   CONSTRAINT typexpr EQ typexpr
  public static boolean class_field_spec(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "class_field_spec")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, CLASS_FIELD_SPEC, "<class field spec>");
    r = class_field_spec_0(b, l + 1);
    if (!r) r = class_field_spec_1(b, l + 1);
    if (!r) r = class_field_spec_2(b, l + 1);
    if (!r) r = class_field_spec_3(b, l + 1);
    if (!r) r = class_field_spec_4(b, l + 1);
    if (!r) r = class_field_spec_5(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  // INHERIT class-body-type
  private static boolean class_field_spec_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "class_field_spec_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, INHERIT);
    r = r && class_body_type(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // VAL [MUTABLE] [VIRTUAL] inst-var-name COLON typexpr
  private static boolean class_field_spec_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "class_field_spec_1")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, VAL);
    r = r && class_field_spec_1_1(b, l + 1);
    r = r && class_field_spec_1_2(b, l + 1);
    r = r && inst_var_name(b, l + 1);
    r = r && consumeToken(b, COLON);
    r = r && typexpr(b, l + 1, -1);
    exit_section_(b, m, null, r);
    return r;
  }

  // [MUTABLE]
  private static boolean class_field_spec_1_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "class_field_spec_1_1")) return false;
    consumeToken(b, MUTABLE);
    return true;
  }

  // [VIRTUAL]
  private static boolean class_field_spec_1_2(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "class_field_spec_1_2")) return false;
    consumeToken(b, VIRTUAL);
    return true;
  }

  // VAL VIRTUAL MUTABLE inst-var-name COLON typexpr
  private static boolean class_field_spec_2(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "class_field_spec_2")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeTokens(b, 0, VAL, VIRTUAL, MUTABLE);
    r = r && inst_var_name(b, l + 1);
    r = r && consumeToken(b, COLON);
    r = r && typexpr(b, l + 1, -1);
    exit_section_(b, m, null, r);
    return r;
  }

  // METHOD [PRIVATE] [VIRTUAL] method-name COLON poly-typexpr
  private static boolean class_field_spec_3(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "class_field_spec_3")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, METHOD);
    r = r && class_field_spec_3_1(b, l + 1);
    r = r && class_field_spec_3_2(b, l + 1);
    r = r && method_name(b, l + 1);
    r = r && consumeToken(b, COLON);
    r = r && poly_typexpr(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // [PRIVATE]
  private static boolean class_field_spec_3_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "class_field_spec_3_1")) return false;
    consumeToken(b, PRIVATE);
    return true;
  }

  // [VIRTUAL]
  private static boolean class_field_spec_3_2(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "class_field_spec_3_2")) return false;
    consumeToken(b, VIRTUAL);
    return true;
  }

  // METHOD VIRTUAL PRIVATE method-name COLON poly-typexpr
  private static boolean class_field_spec_4(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "class_field_spec_4")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeTokens(b, 0, METHOD, VIRTUAL, PRIVATE);
    r = r && method_name(b, l + 1);
    r = r && consumeToken(b, COLON);
    r = r && poly_typexpr(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // CONSTRAINT typexpr EQ typexpr
  private static boolean class_field_spec_5(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "class_field_spec_5")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, CONSTRAINT);
    r = r && typexpr(b, l + 1, -1);
    r = r && consumeToken(b, EQ);
    r = r && typexpr(b, l + 1, -1);
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // LOWERCASE_IDENT
  public static boolean class_name(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "class_name")) return false;
    if (!nextTokenIs(b, LOWERCASE_IDENT)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, LOWERCASE_IDENT);
    exit_section_(b, m, CLASS_NAME, r);
    return r;
  }

  /* ********************************************************** */
  // [ module-path DOT ] class-name
  public static boolean class_path(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "class_path")) return false;
    if (!nextTokenIs(b, "<class path>", CAPITALIZED_IDENT, LOWERCASE_IDENT)) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, CLASS_PATH, "<class path>");
    r = class_path_0(b, l + 1);
    r = r && class_name(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  // [ module-path DOT ]
  private static boolean class_path_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "class_path_0")) return false;
    class_path_0_0(b, l + 1);
    return true;
  }

  // module-path DOT
  private static boolean class_path_0_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "class_path_0_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = module_path(b, l + 1);
    r = r && consumeToken(b, DOT);
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // [VIRTUAL] [[ type-parameters ]] class-name COLON class-type
  public static boolean class_spec(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "class_spec")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, CLASS_SPEC, "<class spec>");
    r = class_spec_0(b, l + 1);
    r = r && class_spec_1(b, l + 1);
    r = r && class_name(b, l + 1);
    r = r && consumeToken(b, COLON);
    r = r && class_type(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  // [VIRTUAL]
  private static boolean class_spec_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "class_spec_0")) return false;
    consumeToken(b, VIRTUAL);
    return true;
  }

  // [[ type-parameters ]]
  private static boolean class_spec_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "class_spec_1")) return false;
    class_spec_1_0(b, l + 1);
    return true;
  }

  // [ type-parameters ]
  private static boolean class_spec_1_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "class_spec_1_0")) return false;
    type_parameters(b, l + 1);
    return true;
  }

  /* ********************************************************** */
  // CLASS class-spec { AND class-spec }*
  public static boolean class_specification(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "class_specification")) return false;
    if (!nextTokenIs(b, CLASS)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, CLASS);
    r = r && class_spec(b, l + 1);
    r = r && class_specification_2(b, l + 1);
    exit_section_(b, m, CLASS_SPECIFICATION, r);
    return r;
  }

  // { AND class-spec }*
  private static boolean class_specification_2(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "class_specification_2")) return false;
    while (true) {
      int c = current_position_(b);
      if (!class_specification_2_0(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "class_specification_2", c)) break;
    }
    return true;
  }

  // AND class-spec
  private static boolean class_specification_2_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "class_specification_2_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, AND);
    r = r && class_spec(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // [[QUESTION_MARK] label-name COLON] typexpr RIGHT_ARROW class-type
  //   |    class-body-type
  public static boolean class_type(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "class_type")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, CLASS_TYPE, "<class type>");
    r = class_type_0(b, l + 1);
    if (!r) r = class_body_type(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  // [[QUESTION_MARK] label-name COLON] typexpr RIGHT_ARROW class-type
  private static boolean class_type_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "class_type_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = class_type_0_0(b, l + 1);
    r = r && typexpr(b, l + 1, -1);
    r = r && consumeToken(b, RIGHT_ARROW);
    r = r && class_type(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // [[QUESTION_MARK] label-name COLON]
  private static boolean class_type_0_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "class_type_0_0")) return false;
    class_type_0_0_0(b, l + 1);
    return true;
  }

  // [QUESTION_MARK] label-name COLON
  private static boolean class_type_0_0_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "class_type_0_0_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = class_type_0_0_0_0(b, l + 1);
    r = r && label_name(b, l + 1);
    r = r && consumeToken(b, COLON);
    exit_section_(b, m, null, r);
    return r;
  }

  // [QUESTION_MARK]
  private static boolean class_type_0_0_0_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "class_type_0_0_0_0")) return false;
    consumeToken(b, QUESTION_MARK);
    return true;
  }

  /* ********************************************************** */
  // [VIRTUAL] [[ type-parameters ]] class-name EQ class-body-type
  public static boolean classtype_def(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "classtype_def")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, CLASSTYPE_DEF, "<classtype def>");
    r = classtype_def_0(b, l + 1);
    r = r && classtype_def_1(b, l + 1);
    r = r && class_name(b, l + 1);
    r = r && consumeToken(b, EQ);
    r = r && class_body_type(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  // [VIRTUAL]
  private static boolean classtype_def_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "classtype_def_0")) return false;
    consumeToken(b, VIRTUAL);
    return true;
  }

  // [[ type-parameters ]]
  private static boolean classtype_def_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "classtype_def_1")) return false;
    classtype_def_1_0(b, l + 1);
    return true;
  }

  // [ type-parameters ]
  private static boolean classtype_def_1_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "classtype_def_1_0")) return false;
    type_parameters(b, l + 1);
    return true;
  }

  /* ********************************************************** */
  // CLASS TYPE classtype-def { AND classtype-def }*
  public static boolean classtype_definition(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "classtype_definition")) return false;
    if (!nextTokenIs(b, CLASS)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeTokens(b, 0, CLASS, TYPE);
    r = r && classtype_def(b, l + 1);
    r = r && classtype_definition_3(b, l + 1);
    exit_section_(b, m, CLASSTYPE_DEFINITION, r);
    return r;
  }

  // { AND classtype-def }*
  private static boolean classtype_definition_3(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "classtype_definition_3")) return false;
    while (true) {
      int c = current_position_(b);
      if (!classtype_definition_3_0(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "classtype_definition_3", c)) break;
    }
    return true;
  }

  // AND classtype-def
  private static boolean classtype_definition_3_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "classtype_definition_3_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, AND);
    r = r && classtype_def(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // [ extended-module-path DOT ] class-name
  public static boolean classtype_path(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "classtype_path")) return false;
    if (!nextTokenIs(b, "<classtype path>", CAPITALIZED_IDENT, LOWERCASE_IDENT)) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, CLASSTYPE_PATH, "<classtype path>");
    r = classtype_path_0(b, l + 1);
    r = r && class_name(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  // [ extended-module-path DOT ]
  private static boolean classtype_path_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "classtype_path_0")) return false;
    classtype_path_0_0(b, l + 1);
    return true;
  }

  // extended-module-path DOT
  private static boolean classtype_path_0_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "classtype_path_0_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = extended_module_path(b, l + 1);
    r = r && consumeToken(b, DOT);
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // integer-literal
  //   |  int32-literal
  //   |  int64-literal
  //   |  nativeint-literal
  //   |  float-literal
  //   |  char-literal
  //   |  string-literal
  //   |  constr
  //   |  FALSE
  //   |  TRUE
  //   |  LPAREN RPAREN
  //   |  BEGIN END
  //   |  LBRACKET RBRACKET
  //   |  LARRAY RARRAY
  //   |  BACKTICK tag-name
  static boolean constant(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "constant")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = integer_literal(b, l + 1);
    if (!r) r = int32_literal(b, l + 1);
    if (!r) r = int64_literal(b, l + 1);
    if (!r) r = nativeint_literal(b, l + 1);
    if (!r) r = float_literal(b, l + 1);
    if (!r) r = char_literal(b, l + 1);
    if (!r) r = string_literal(b, l + 1);
    if (!r) r = constr(b, l + 1);
    if (!r) r = consumeToken(b, FALSE);
    if (!r) r = consumeToken(b, TRUE);
    if (!r) r = parseTokens(b, 0, LPAREN, RPAREN);
    if (!r) r = parseTokens(b, 0, BEGIN, END);
    if (!r) r = parseTokens(b, 0, LBRACKET, RBRACKET);
    if (!r) r = parseTokens(b, 0, LARRAY, RARRAY);
    if (!r) r = constant_14(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // BACKTICK tag-name
  private static boolean constant_14(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "constant_14")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, BACKTICK);
    r = r && tag_name(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // [ module-path DOT ] constr-name
  public static boolean constr(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "constr")) return false;
    if (!nextTokenIs(b, CAPITALIZED_IDENT)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = constr_0(b, l + 1);
    r = r && constr_name(b, l + 1);
    exit_section_(b, m, CONSTR, r);
    return r;
  }

  // [ module-path DOT ]
  private static boolean constr_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "constr_0")) return false;
    constr_0_0(b, l + 1);
    return true;
  }

  // module-path DOT
  private static boolean constr_0_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "constr_0_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = module_path(b, l + 1);
    r = r && consumeToken(b, DOT);
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // typexpr { STAR typexpr }*
  public static boolean constr_args(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "constr_args")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, CONSTR_ARGS, "<constr args>");
    r = typexpr(b, l + 1, -1);
    r = r && constr_args_1(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  // { STAR typexpr }*
  private static boolean constr_args_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "constr_args_1")) return false;
    while (true) {
      int c = current_position_(b);
      if (!constr_args_1_0(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "constr_args_1", c)) break;
    }
    return true;
  }

  // STAR typexpr
  private static boolean constr_args_1_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "constr_args_1_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, STAR);
    r = r && typexpr(b, l + 1, -1);
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // (constr-name | LBRACKET RBRACKET | LPAREN SHORTCUT RPAREN) [ OF constr-args ]
  public static boolean constr_decl(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "constr_decl")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, CONSTR_DECL, "<constr decl>");
    r = constr_decl_0(b, l + 1);
    r = r && constr_decl_1(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  // constr-name | LBRACKET RBRACKET | LPAREN SHORTCUT RPAREN
  private static boolean constr_decl_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "constr_decl_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = constr_name(b, l + 1);
    if (!r) r = parseTokens(b, 0, LBRACKET, RBRACKET);
    if (!r) r = parseTokens(b, 0, LPAREN, SHORTCUT, RPAREN);
    exit_section_(b, m, null, r);
    return r;
  }

  // [ OF constr-args ]
  private static boolean constr_decl_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "constr_decl_1")) return false;
    constr_decl_1_0(b, l + 1);
    return true;
  }

  // OF constr-args
  private static boolean constr_decl_1_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "constr_decl_1_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, OF);
    r = r && constr_args(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // CAPITALIZED_IDENT
  public static boolean constr_name(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "constr_name")) return false;
    if (!nextTokenIs(b, CAPITALIZED_IDENT)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, CAPITALIZED_IDENT);
    exit_section_(b, m, CONSTR_NAME, r);
    return r;
  }

  /* ********************************************************** */
  // DOLLAR | AMPERSAND | STAR | PLUS | MINUS | SLASH | EQ | GT | ARROBASE | CARRET
  public static boolean core_operator_char(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "core_operator_char")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, CORE_OPERATOR_CHAR, "<core operator char>");
    r = consumeToken(b, DOLLAR);
    if (!r) r = consumeToken(b, AMPERSAND);
    if (!r) r = consumeToken(b, STAR);
    if (!r) r = consumeToken(b, PLUS);
    if (!r) r = consumeToken(b, MINUS);
    if (!r) r = consumeToken(b, SLASH);
    if (!r) r = consumeToken(b, EQ);
    if (!r) r = consumeToken(b, GT);
    if (!r) r = consumeToken(b, ARROBASE);
    if (!r) r = consumeToken(b, CARRET);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  /* ********************************************************** */
  // LET [REC] let-binding { AND let-binding }*
  //   | EXTERNAL value-name COLON typexpr EQ external-declaration
  //   | type-definition
  //   | exception-definition
  //   | class-definition
  //   | classtype-definition
  //   | MODULE module-name { ( module-name COLON module_type ) }* [ COLON module_type ] EQ module_expr
  //   | MODULE TYPE modtype-name EQ module_type
  //   | OPEN module-path
  //   | INCLUDE module_expr
  //   | recursive_module_extension_def
  //   | generalized_open_statements_def
  public static boolean definition(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "definition")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, DEFINITION, "<definition>");
    r = definition_0(b, l + 1);
    if (!r) r = definition_1(b, l + 1);
    if (!r) r = type_definition(b, l + 1);
    if (!r) r = exception_definition(b, l + 1);
    if (!r) r = class_definition(b, l + 1);
    if (!r) r = classtype_definition(b, l + 1);
    if (!r) r = definition_6(b, l + 1);
    if (!r) r = definition_7(b, l + 1);
    if (!r) r = definition_8(b, l + 1);
    if (!r) r = definition_9(b, l + 1);
    if (!r) r = recursive_module_extension_def(b, l + 1);
    if (!r) r = generalized_open_statements_def(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  // LET [REC] let-binding { AND let-binding }*
  private static boolean definition_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "definition_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, LET);
    r = r && definition_0_1(b, l + 1);
    r = r && let_binding(b, l + 1);
    r = r && definition_0_3(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // [REC]
  private static boolean definition_0_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "definition_0_1")) return false;
    consumeToken(b, REC);
    return true;
  }

  // { AND let-binding }*
  private static boolean definition_0_3(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "definition_0_3")) return false;
    while (true) {
      int c = current_position_(b);
      if (!definition_0_3_0(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "definition_0_3", c)) break;
    }
    return true;
  }

  // AND let-binding
  private static boolean definition_0_3_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "definition_0_3_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, AND);
    r = r && let_binding(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // EXTERNAL value-name COLON typexpr EQ external-declaration
  private static boolean definition_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "definition_1")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, EXTERNAL);
    r = r && value_name(b, l + 1);
    r = r && consumeToken(b, COLON);
    r = r && typexpr(b, l + 1, -1);
    r = r && consumeToken(b, EQ);
    r = r && external_declaration(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // MODULE module-name { ( module-name COLON module_type ) }* [ COLON module_type ] EQ module_expr
  private static boolean definition_6(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "definition_6")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, MODULE);
    r = r && module_name(b, l + 1);
    r = r && definition_6_2(b, l + 1);
    r = r && definition_6_3(b, l + 1);
    r = r && consumeToken(b, EQ);
    r = r && module_expr(b, l + 1, -1);
    exit_section_(b, m, null, r);
    return r;
  }

  // { ( module-name COLON module_type ) }*
  private static boolean definition_6_2(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "definition_6_2")) return false;
    while (true) {
      int c = current_position_(b);
      if (!definition_6_2_0(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "definition_6_2", c)) break;
    }
    return true;
  }

  // module-name COLON module_type
  private static boolean definition_6_2_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "definition_6_2_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = module_name(b, l + 1);
    r = r && consumeToken(b, COLON);
    r = r && module_type(b, l + 1, -1);
    exit_section_(b, m, null, r);
    return r;
  }

  // [ COLON module_type ]
  private static boolean definition_6_3(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "definition_6_3")) return false;
    definition_6_3_0(b, l + 1);
    return true;
  }

  // COLON module_type
  private static boolean definition_6_3_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "definition_6_3_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, COLON);
    r = r && module_type(b, l + 1, -1);
    exit_section_(b, m, null, r);
    return r;
  }

  // MODULE TYPE modtype-name EQ module_type
  private static boolean definition_7(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "definition_7")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeTokens(b, 0, MODULE, TYPE);
    r = r && modtype_name(b, l + 1);
    r = r && consumeToken(b, EQ);
    r = r && module_type(b, l + 1, -1);
    exit_section_(b, m, null, r);
    return r;
  }

  // OPEN module-path
  private static boolean definition_8(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "definition_8")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, OPEN);
    r = r && module_path(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // INCLUDE module_expr
  private static boolean definition_9(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "definition_9")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, INCLUDE);
    r = r && module_expr(b, l + 1, -1);
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // EXCEPTION constr-decl
  //   |  EXCEPTION constr-name EQ constr
  public static boolean exception_definition(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "exception_definition")) return false;
    if (!nextTokenIs(b, EXCEPTION)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = exception_definition_0(b, l + 1);
    if (!r) r = exception_definition_1(b, l + 1);
    exit_section_(b, m, EXCEPTION_DEFINITION, r);
    return r;
  }

  // EXCEPTION constr-decl
  private static boolean exception_definition_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "exception_definition_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, EXCEPTION);
    r = r && constr_decl(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // EXCEPTION constr-name EQ constr
  private static boolean exception_definition_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "exception_definition_1")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, EXCEPTION);
    r = r && constr_name(b, l + 1);
    r = r && consumeToken(b, EQ);
    r = r && constr(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // variance [injectivity]
  //   |  injectivity [variance]
  public static boolean ext_variance(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "ext_variance")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, EXT_VARIANCE, "<ext variance>");
    r = ext_variance_0(b, l + 1);
    if (!r) r = ext_variance_1(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  // variance [injectivity]
  private static boolean ext_variance_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "ext_variance_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = variance(b, l + 1);
    r = r && ext_variance_0_1(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // [injectivity]
  private static boolean ext_variance_0_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "ext_variance_0_1")) return false;
    injectivity(b, l + 1);
    return true;
  }

  // injectivity [variance]
  private static boolean ext_variance_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "ext_variance_1")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = injectivity(b, l + 1);
    r = r && ext_variance_1_1(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // [variance]
  private static boolean ext_variance_1_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "ext_variance_1_1")) return false;
    variance(b, l + 1);
    return true;
  }

  /* ********************************************************** */
  // module-name { LPAREN extended-module-path RPAREN }*
  public static boolean extended_module_name(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "extended_module_name")) return false;
    if (!nextTokenIs(b, CAPITALIZED_IDENT)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = module_name(b, l + 1);
    r = r && extended_module_name_1(b, l + 1);
    exit_section_(b, m, EXTENDED_MODULE_NAME, r);
    return r;
  }

  // { LPAREN extended-module-path RPAREN }*
  private static boolean extended_module_name_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "extended_module_name_1")) return false;
    while (true) {
      int c = current_position_(b);
      if (!extended_module_name_1_0(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "extended_module_name_1", c)) break;
    }
    return true;
  }

  // LPAREN extended-module-path RPAREN
  private static boolean extended_module_name_1_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "extended_module_name_1_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, LPAREN);
    r = r && extended_module_path(b, l + 1);
    r = r && consumeToken(b, RPAREN);
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // extended-module-name { DOT extended-module-name }*
  public static boolean extended_module_path(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "extended_module_path")) return false;
    if (!nextTokenIs(b, CAPITALIZED_IDENT)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = extended_module_name(b, l + 1);
    r = r && extended_module_path_1(b, l + 1);
    exit_section_(b, m, EXTENDED_MODULE_PATH, r);
    return r;
  }

  // { DOT extended-module-name }*
  private static boolean extended_module_path_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "extended_module_path_1")) return false;
    while (true) {
      int c = current_position_(b);
      if (!extended_module_path_1_0(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "extended_module_path_1", c)) break;
    }
    return true;
  }

  // DOT extended-module-name
  private static boolean extended_module_path_1_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "extended_module_path_1_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, DOT);
    r = r && extended_module_name(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // string-literal [ string-literal [ string-literal ] ]
  public static boolean external_declaration(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "external_declaration")) return false;
    if (!nextTokenIs(b, STRING_VALUE)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = string_literal(b, l + 1);
    r = r && external_declaration_1(b, l + 1);
    exit_section_(b, m, EXTERNAL_DECLARATION, r);
    return r;
  }

  // [ string-literal [ string-literal ] ]
  private static boolean external_declaration_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "external_declaration_1")) return false;
    external_declaration_1_0(b, l + 1);
    return true;
  }

  // string-literal [ string-literal ]
  private static boolean external_declaration_1_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "external_declaration_1_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = string_literal(b, l + 1);
    r = r && external_declaration_1_0_1(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // [ string-literal ]
  private static boolean external_declaration_1_0_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "external_declaration_1_0_1")) return false;
    string_literal(b, l + 1);
    return true;
  }

  /* ********************************************************** */
  // [ module-path DOT ] field-name
  public static boolean field(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "field")) return false;
    if (!nextTokenIs(b, "<field>", CAPITALIZED_IDENT, LOWERCASE_IDENT)) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, FIELD, "<field>");
    r = field_0(b, l + 1);
    r = r && field_name(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  // [ module-path DOT ]
  private static boolean field_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "field_0")) return false;
    field_0_0(b, l + 1);
    return true;
  }

  // module-path DOT
  private static boolean field_0_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "field_0_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = module_path(b, l + 1);
    r = r && consumeToken(b, DOT);
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // [MUTABLE] field-name COLON poly-typexpr
  public static boolean field_decl(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "field_decl")) return false;
    if (!nextTokenIs(b, "<field decl>", LOWERCASE_IDENT, MUTABLE)) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, FIELD_DECL, "<field decl>");
    r = field_decl_0(b, l + 1);
    r = r && field_name(b, l + 1);
    r = r && consumeToken(b, COLON);
    r = r && poly_typexpr(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  // [MUTABLE]
  private static boolean field_decl_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "field_decl_0")) return false;
    consumeToken(b, MUTABLE);
    return true;
  }

  /* ********************************************************** */
  // LOWERCASE_IDENT
  public static boolean field_name(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "field_name")) return false;
    if (!nextTokenIs(b, LOWERCASE_IDENT)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, LOWERCASE_IDENT);
    exit_section_(b, m, FIELD_NAME, r);
    return r;
  }

  /* ********************************************************** */
  // COMMENT
  // | DOC_COMMENT
  // | ANNOTATION
  // //| unit-interface
  // | unit-implementation
  static boolean file(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "file")) return false;
    boolean r;
    r = consumeToken(b, COMMENT);
    if (!r) r = consumeToken(b, DOC_COMMENT);
    if (!r) r = consumeToken(b, ANNOTATION);
    if (!r) r = unit_implementation(b, l + 1);
    return r;
  }

  /* ********************************************************** */
  // [MINUS] FLOAT_VALUE
  public static boolean float_literal(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "float_literal")) return false;
    if (!nextTokenIs(b, "<float literal>", FLOAT_VALUE, MINUS)) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, FLOAT_LITERAL, "<float literal>");
    r = float_literal_0(b, l + 1);
    r = r && consumeToken(b, FLOAT_VALUE);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  // [MINUS]
  private static boolean float_literal_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "float_literal_0")) return false;
    consumeToken(b, MINUS);
    return true;
  }

  /* ********************************************************** */
  // OPEN module_expr
  //   |   OPEN! module_expr
  static boolean generalized_open_statements_def(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "generalized_open_statements_def")) return false;
    if (!nextTokenIs(b, OPEN)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = generalized_open_statements_def_0(b, l + 1);
    if (!r) r = generalized_open_statements_def_1(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // OPEN module_expr
  private static boolean generalized_open_statements_def_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "generalized_open_statements_def_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, OPEN);
    r = r && module_expr(b, l + 1, -1);
    exit_section_(b, m, null, r);
    return r;
  }

  // OPEN! module_expr
  private static boolean generalized_open_statements_def_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "generalized_open_statements_def_1")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, OPEN);
    r = r && generalized_open_statements_def_1_1(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // ! module_expr
  private static boolean generalized_open_statements_def_1_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "generalized_open_statements_def_1_1")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NOT_);
    r = !module_expr(b, l + 1, -1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  /* ********************************************************** */
  // LET OPEN module_expr IN expr
  //  | LET OPEN EXCLAMATION_MARK module_expr IN expr
  static boolean generalized_open_statements_expr(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "generalized_open_statements_expr")) return false;
    if (!nextTokenIs(b, LET)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = generalized_open_statements_expr_0(b, l + 1);
    if (!r) r = generalized_open_statements_expr_1(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // LET OPEN module_expr IN expr
  private static boolean generalized_open_statements_expr_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "generalized_open_statements_expr_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeTokens(b, 0, LET, OPEN);
    r = r && module_expr(b, l + 1, -1);
    r = r && consumeToken(b, IN);
    r = r && expr(b, l + 1, -1);
    exit_section_(b, m, null, r);
    return r;
  }

  // LET OPEN EXCLAMATION_MARK module_expr IN expr
  private static boolean generalized_open_statements_expr_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "generalized_open_statements_expr_1")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeTokens(b, 0, LET, OPEN, EXCLAMATION_MARK);
    r = r && module_expr(b, l + 1, -1);
    r = r && consumeToken(b, IN);
    r = r && expr(b, l + 1, -1);
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // OPEN extended-module-path
  //   | OPEN! extended-module-path
  static boolean generalized_open_statements_spec(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "generalized_open_statements_spec")) return false;
    if (!nextTokenIs(b, OPEN)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = generalized_open_statements_spec_0(b, l + 1);
    if (!r) r = generalized_open_statements_spec_1(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // OPEN extended-module-path
  private static boolean generalized_open_statements_spec_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "generalized_open_statements_spec_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, OPEN);
    r = r && extended_module_path(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // OPEN! extended-module-path
  private static boolean generalized_open_statements_spec_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "generalized_open_statements_spec_1")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, OPEN);
    r = r && generalized_open_statements_spec_1_1(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // ! extended-module-path
  private static boolean generalized_open_statements_spec_1_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "generalized_open_statements_spec_1_1")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NOT_);
    r = !extended_module_path(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  /* ********************************************************** */
  // LOWERCASE_IDENT | CAPITALIZED_IDENT
  static boolean ident(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "ident")) return false;
    if (!nextTokenIs(b, "", CAPITALIZED_IDENT, LOWERCASE_IDENT)) return false;
    boolean r;
    r = consumeToken(b, LOWERCASE_IDENT);
    if (!r) r = consumeToken(b, CAPITALIZED_IDENT);
    return r;
  }

  /* ********************************************************** */
  // infix-symbol
  //   |  STAR | PLUS | MINUS | MINUSDOT | EQ | NOT_EQ | LT | GT | OR | L_OR | AMPERSAND | L_AND | COLON_EQ
  //   |  MOD | LAND | LOR | LXOR | LSL | LSR | ASR
  public static boolean infix_op(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "infix_op")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, INFIX_OP, "<infix op>");
    r = infix_symbol(b, l + 1);
    if (!r) r = consumeToken(b, STAR);
    if (!r) r = consumeToken(b, PLUS);
    if (!r) r = consumeToken(b, MINUS);
    if (!r) r = consumeToken(b, MINUSDOT);
    if (!r) r = consumeToken(b, EQ);
    if (!r) r = consumeToken(b, NOT_EQ);
    if (!r) r = consumeToken(b, LT);
    if (!r) r = consumeToken(b, GT);
    if (!r) r = consumeToken(b, OR);
    if (!r) r = consumeToken(b, L_OR);
    if (!r) r = consumeToken(b, AMPERSAND);
    if (!r) r = consumeToken(b, L_AND);
    if (!r) r = consumeToken(b, COLON_EQ);
    if (!r) r = consumeToken(b, MOD);
    if (!r) r = consumeToken(b, LAND);
    if (!r) r = consumeToken(b, LOR);
    if (!r) r = consumeToken(b, LXOR);
    if (!r) r = consumeToken(b, LSL);
    if (!r) r = consumeToken(b, LSR);
    if (!r) r = consumeToken(b, ASR);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  /* ********************************************************** */
  // (core-operator-char | PERCENT | LT) { operator-char }*
  //   |  SHARP { operator-char }+
  public static boolean infix_symbol(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "infix_symbol")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, INFIX_SYMBOL, "<infix symbol>");
    r = infix_symbol_0(b, l + 1);
    if (!r) r = infix_symbol_1(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  // (core-operator-char | PERCENT | LT) { operator-char }*
  private static boolean infix_symbol_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "infix_symbol_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = infix_symbol_0_0(b, l + 1);
    r = r && infix_symbol_0_1(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // core-operator-char | PERCENT | LT
  private static boolean infix_symbol_0_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "infix_symbol_0_0")) return false;
    boolean r;
    r = core_operator_char(b, l + 1);
    if (!r) r = consumeToken(b, PERCENT);
    if (!r) r = consumeToken(b, LT);
    return r;
  }

  // { operator-char }*
  private static boolean infix_symbol_0_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "infix_symbol_0_1")) return false;
    while (true) {
      int c = current_position_(b);
      if (!infix_symbol_0_1_0(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "infix_symbol_0_1", c)) break;
    }
    return true;
  }

  // { operator-char }
  private static boolean infix_symbol_0_1_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "infix_symbol_0_1_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = operator_char(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // SHARP { operator-char }+
  private static boolean infix_symbol_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "infix_symbol_1")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, SHARP);
    r = r && infix_symbol_1_1(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // { operator-char }+
  private static boolean infix_symbol_1_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "infix_symbol_1_1")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = infix_symbol_1_1_0(b, l + 1);
    while (r) {
      int c = current_position_(b);
      if (!infix_symbol_1_1_0(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "infix_symbol_1_1", c)) break;
    }
    exit_section_(b, m, null, r);
    return r;
  }

  // { operator-char }
  private static boolean infix_symbol_1_1_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "infix_symbol_1_1_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = operator_char(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // EXCLAMATION_MARK
  public static boolean injectivity(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "injectivity")) return false;
    if (!nextTokenIs(b, EXCLAMATION_MARK)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, EXCLAMATION_MARK);
    exit_section_(b, m, INJECTIVITY, r);
    return r;
  }

  /* ********************************************************** */
  // LOWERCASE_IDENT
  public static boolean inst_var_name(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "inst_var_name")) return false;
    if (!nextTokenIs(b, LOWERCASE_IDENT)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, LOWERCASE_IDENT);
    exit_section_(b, m, INST_VAR_NAME, r);
    return r;
  }

  /* ********************************************************** */
  // integer-literal "l"
  public static boolean int32_literal(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "int32_literal")) return false;
    if (!nextTokenIs(b, "<int 32 literal>", INTEGER_VALUE, MINUS)) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, INT_32_LITERAL, "<int 32 literal>");
    r = integer_literal(b, l + 1);
    r = r && consumeToken(b, "l");
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  /* ********************************************************** */
  // integer-literal "L"
  public static boolean int64_literal(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "int64_literal")) return false;
    if (!nextTokenIs(b, "<int 64 literal>", INTEGER_VALUE, MINUS)) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, INT_64_LITERAL, "<int 64 literal>");
    r = integer_literal(b, l + 1);
    r = r && consumeToken(b, "L");
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  /* ********************************************************** */
  // [MINUS] INTEGER_VALUE
  public static boolean integer_literal(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "integer_literal")) return false;
    if (!nextTokenIs(b, "<integer literal>", INTEGER_VALUE, MINUS)) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, INTEGER_LITERAL, "<integer literal>");
    r = integer_literal_0(b, l + 1);
    r = r && consumeToken(b, INTEGER_VALUE);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  // [MINUS]
  private static boolean integer_literal_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "integer_literal_0")) return false;
    consumeToken(b, MINUS);
    return true;
  }

  /* ********************************************************** */
  // TILDE label-name COLON
  public static boolean label(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "label")) return false;
    if (!nextTokenIs(b, TILDE)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, TILDE);
    r = r && label_name(b, l + 1);
    r = r && consumeToken(b, COLON);
    exit_section_(b, m, LABEL, r);
    return r;
  }

  /* ********************************************************** */
  // LOWERCASE_IDENT
  public static boolean label_name(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "label_name")) return false;
    if (!nextTokenIs(b, LOWERCASE_IDENT)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, LOWERCASE_IDENT);
    exit_section_(b, m, LABEL_NAME, r);
    return r;
  }

  /* ********************************************************** */
  // pattern EQ expr
  //   |  value-name { parameter }* [COLON typexpr] [COLON_GT typexpr] EQ expr
  //   |  value-name COLON poly-typexpr EQ expr
  public static boolean let_binding(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "let_binding")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, LET_BINDING, "<let binding>");
    r = let_binding_0(b, l + 1);
    if (!r) r = let_binding_1(b, l + 1);
    if (!r) r = let_binding_2(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  // pattern EQ expr
  private static boolean let_binding_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "let_binding_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = pattern(b, l + 1, -1);
    r = r && consumeToken(b, EQ);
    r = r && expr(b, l + 1, -1);
    exit_section_(b, m, null, r);
    return r;
  }

  // value-name { parameter }* [COLON typexpr] [COLON_GT typexpr] EQ expr
  private static boolean let_binding_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "let_binding_1")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = value_name(b, l + 1);
    r = r && let_binding_1_1(b, l + 1);
    r = r && let_binding_1_2(b, l + 1);
    r = r && let_binding_1_3(b, l + 1);
    r = r && consumeToken(b, EQ);
    r = r && expr(b, l + 1, -1);
    exit_section_(b, m, null, r);
    return r;
  }

  // { parameter }*
  private static boolean let_binding_1_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "let_binding_1_1")) return false;
    while (true) {
      int c = current_position_(b);
      if (!let_binding_1_1_0(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "let_binding_1_1", c)) break;
    }
    return true;
  }

  // { parameter }
  private static boolean let_binding_1_1_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "let_binding_1_1_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = parameter(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // [COLON typexpr]
  private static boolean let_binding_1_2(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "let_binding_1_2")) return false;
    let_binding_1_2_0(b, l + 1);
    return true;
  }

  // COLON typexpr
  private static boolean let_binding_1_2_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "let_binding_1_2_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, COLON);
    r = r && typexpr(b, l + 1, -1);
    exit_section_(b, m, null, r);
    return r;
  }

  // [COLON_GT typexpr]
  private static boolean let_binding_1_3(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "let_binding_1_3")) return false;
    let_binding_1_3_0(b, l + 1);
    return true;
  }

  // COLON_GT typexpr
  private static boolean let_binding_1_3_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "let_binding_1_3_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, COLON_GT);
    r = r && typexpr(b, l + 1, -1);
    exit_section_(b, m, null, r);
    return r;
  }

  // value-name COLON poly-typexpr EQ expr
  private static boolean let_binding_2(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "let_binding_2")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = value_name(b, l + 1);
    r = r && consumeToken(b, COLON);
    r = r && poly_typexpr(b, l + 1);
    r = r && consumeToken(b, EQ);
    r = r && expr(b, l + 1, -1);
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // LET OPEN module-path IN expr
  //   |  module-path DOT LPAREN expr RPAREN
  //   |  module-path DOT LBRACKET expr RBRACKET
  //   |  module-path DOT LARRAY expr RARRAY
  //   |  module-path DOT LBRACE expr RBRACE
  //   |  module-path DOT LBRACELESS expr GREATERRBRACE
  public static boolean local_open(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "local_open")) return false;
    if (!nextTokenIs(b, "<local open>", CAPITALIZED_IDENT, LET)) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, LOCAL_OPEN, "<local open>");
    r = local_open_0(b, l + 1);
    if (!r) r = local_open_1(b, l + 1);
    if (!r) r = local_open_2(b, l + 1);
    if (!r) r = local_open_3(b, l + 1);
    if (!r) r = local_open_4(b, l + 1);
    if (!r) r = local_open_5(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  // LET OPEN module-path IN expr
  private static boolean local_open_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "local_open_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeTokens(b, 0, LET, OPEN);
    r = r && module_path(b, l + 1);
    r = r && consumeToken(b, IN);
    r = r && expr(b, l + 1, -1);
    exit_section_(b, m, null, r);
    return r;
  }

  // module-path DOT LPAREN expr RPAREN
  private static boolean local_open_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "local_open_1")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = module_path(b, l + 1);
    r = r && consumeTokens(b, 0, DOT, LPAREN);
    r = r && expr(b, l + 1, -1);
    r = r && consumeToken(b, RPAREN);
    exit_section_(b, m, null, r);
    return r;
  }

  // module-path DOT LBRACKET expr RBRACKET
  private static boolean local_open_2(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "local_open_2")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = module_path(b, l + 1);
    r = r && consumeTokens(b, 0, DOT, LBRACKET);
    r = r && expr(b, l + 1, -1);
    r = r && consumeToken(b, RBRACKET);
    exit_section_(b, m, null, r);
    return r;
  }

  // module-path DOT LARRAY expr RARRAY
  private static boolean local_open_3(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "local_open_3")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = module_path(b, l + 1);
    r = r && consumeTokens(b, 0, DOT, LARRAY);
    r = r && expr(b, l + 1, -1);
    r = r && consumeToken(b, RARRAY);
    exit_section_(b, m, null, r);
    return r;
  }

  // module-path DOT LBRACE expr RBRACE
  private static boolean local_open_4(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "local_open_4")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = module_path(b, l + 1);
    r = r && consumeTokens(b, 0, DOT, LBRACE);
    r = r && expr(b, l + 1, -1);
    r = r && consumeToken(b, RBRACE);
    exit_section_(b, m, null, r);
    return r;
  }

  // module-path DOT LBRACELESS expr GREATERRBRACE
  private static boolean local_open_5(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "local_open_5")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = module_path(b, l + 1);
    r = r && consumeTokens(b, 0, DOT, LBRACELESS);
    r = r && expr(b, l + 1, -1);
    r = r && consumeToken(b, GREATERRBRACE);
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // LOWERCASE_IDENT
  public static boolean method_name(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "method_name")) return false;
    if (!nextTokenIs(b, LOWERCASE_IDENT)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, LOWERCASE_IDENT);
    exit_section_(b, m, METHOD_NAME, r);
    return r;
  }

  /* ********************************************************** */
  // method-name COLON poly-typexpr
  public static boolean method_type(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "method_type")) return false;
    if (!nextTokenIs(b, LOWERCASE_IDENT)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = method_name(b, l + 1);
    r = r && consumeToken(b, COLON);
    r = r && poly_typexpr(b, l + 1);
    exit_section_(b, m, METHOD_TYPE, r);
    return r;
  }

  /* ********************************************************** */
  // TYPE [type-params] typeconstr type-equation { type-constraint }
  //   |  MODULE module-path EQ extended-module-path
  public static boolean mod_constraint(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "mod_constraint")) return false;
    if (!nextTokenIs(b, "<mod constraint>", MODULE, TYPE)) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, MOD_CONSTRAINT, "<mod constraint>");
    r = mod_constraint_0(b, l + 1);
    if (!r) r = mod_constraint_1(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  // TYPE [type-params] typeconstr type-equation { type-constraint }
  private static boolean mod_constraint_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "mod_constraint_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, TYPE);
    r = r && mod_constraint_0_1(b, l + 1);
    r = r && typeconstr(b, l + 1);
    r = r && type_equation(b, l + 1);
    r = r && mod_constraint_0_4(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // [type-params]
  private static boolean mod_constraint_0_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "mod_constraint_0_1")) return false;
    type_params(b, l + 1);
    return true;
  }

  // { type-constraint }
  private static boolean mod_constraint_0_4(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "mod_constraint_0_4")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = type_constraint(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // MODULE module-path EQ extended-module-path
  private static boolean mod_constraint_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "mod_constraint_1")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, MODULE);
    r = r && module_path(b, l + 1);
    r = r && consumeToken(b, EQ);
    r = r && extended_module_path(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // ident
  public static boolean modtype_name(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "modtype_name")) return false;
    if (!nextTokenIs(b, "<modtype name>", CAPITALIZED_IDENT, LOWERCASE_IDENT)) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, MODTYPE_NAME, "<modtype name>");
    r = ident(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  /* ********************************************************** */
  // [ extended-module-path DOT ] modtype-name
  public static boolean modtype_path(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "modtype_path")) return false;
    if (!nextTokenIs(b, "<modtype path>", CAPITALIZED_IDENT, LOWERCASE_IDENT)) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, MODTYPE_PATH, "<modtype path>");
    r = modtype_path_0(b, l + 1);
    r = r && modtype_name(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  // [ extended-module-path DOT ]
  private static boolean modtype_path_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "modtype_path_0")) return false;
    modtype_path_0_0(b, l + 1);
    return true;
  }

  // extended-module-path DOT
  private static boolean modtype_path_0_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "modtype_path_0_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = extended_module_path(b, l + 1);
    r = r && consumeToken(b, DOT);
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // { SEMISEMI }* ( definition | expr ) { { SEMISEMI }* ( definition | SEMISEMI expr) }* { SEMISEMI }*
  public static boolean module_items(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "module_items")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, MODULE_ITEMS, "<module items>");
    r = module_items_0(b, l + 1);
    r = r && module_items_1(b, l + 1);
    r = r && module_items_2(b, l + 1);
    r = r && module_items_3(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  // { SEMISEMI }*
  private static boolean module_items_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "module_items_0")) return false;
    while (true) {
      int c = current_position_(b);
      if (!consumeToken(b, SEMISEMI)) break;
      if (!empty_element_parsed_guard_(b, "module_items_0", c)) break;
    }
    return true;
  }

  // definition | expr
  private static boolean module_items_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "module_items_1")) return false;
    boolean r;
    r = definition(b, l + 1);
    if (!r) r = expr(b, l + 1, -1);
    return r;
  }

  // { { SEMISEMI }* ( definition | SEMISEMI expr) }*
  private static boolean module_items_2(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "module_items_2")) return false;
    while (true) {
      int c = current_position_(b);
      if (!module_items_2_0(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "module_items_2", c)) break;
    }
    return true;
  }

  // { SEMISEMI }* ( definition | SEMISEMI expr)
  private static boolean module_items_2_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "module_items_2_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = module_items_2_0_0(b, l + 1);
    r = r && module_items_2_0_1(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // { SEMISEMI }*
  private static boolean module_items_2_0_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "module_items_2_0_0")) return false;
    while (true) {
      int c = current_position_(b);
      if (!consumeToken(b, SEMISEMI)) break;
      if (!empty_element_parsed_guard_(b, "module_items_2_0_0", c)) break;
    }
    return true;
  }

  // definition | SEMISEMI expr
  private static boolean module_items_2_0_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "module_items_2_0_1")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = definition(b, l + 1);
    if (!r) r = module_items_2_0_1_1(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // SEMISEMI expr
  private static boolean module_items_2_0_1_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "module_items_2_0_1_1")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, SEMISEMI);
    r = r && expr(b, l + 1, -1);
    exit_section_(b, m, null, r);
    return r;
  }

  // { SEMISEMI }*
  private static boolean module_items_3(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "module_items_3")) return false;
    while (true) {
      int c = current_position_(b);
      if (!consumeToken(b, SEMISEMI)) break;
      if (!empty_element_parsed_guard_(b, "module_items_3", c)) break;
    }
    return true;
  }

  /* ********************************************************** */
  // CAPITALIZED_IDENT
  public static boolean module_name(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "module_name")) return false;
    if (!nextTokenIs(b, CAPITALIZED_IDENT)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, CAPITALIZED_IDENT);
    exit_section_(b, m, MODULE_NAME, r);
    return r;
  }

  /* ********************************************************** */
  // module-name { DOT module-name }*
  public static boolean module_path(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "module_path")) return false;
    if (!nextTokenIs(b, CAPITALIZED_IDENT)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = module_name(b, l + 1);
    r = r && module_path_1(b, l + 1);
    exit_section_(b, m, MODULE_PATH, r);
    return r;
  }

  // { DOT module-name }*
  private static boolean module_path_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "module_path_1")) return false;
    while (true) {
      int c = current_position_(b);
      if (!module_path_1_0(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "module_path_1", c)) break;
    }
    return true;
  }

  // DOT module-name
  private static boolean module_path_1_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "module_path_1_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, DOT);
    r = r && module_name(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // integer-literal "n"
  public static boolean nativeint_literal(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "nativeint_literal")) return false;
    if (!nextTokenIs(b, "<nativeint literal>", INTEGER_VALUE, MINUS)) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, NATIVEINT_LITERAL, "<nativeint literal>");
    r = integer_literal(b, l + 1);
    r = r && consumeToken(b, "n");
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  /* ********************************************************** */
  // TILDE | EXCLAMATION_MARK | QUESTION_MARK | core-operator-char | PERCENT | LT | COLON | DOT
  public static boolean operator_char(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "operator_char")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, OPERATOR_CHAR, "<operator char>");
    r = consumeToken(b, TILDE);
    if (!r) r = consumeToken(b, EXCLAMATION_MARK);
    if (!r) r = consumeToken(b, QUESTION_MARK);
    if (!r) r = core_operator_char(b, l + 1);
    if (!r) r = consumeToken(b, PERCENT);
    if (!r) r = consumeToken(b, LT);
    if (!r) r = consumeToken(b, COLON);
    if (!r) r = consumeToken(b, DOT);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  /* ********************************************************** */
  // prefix-symbol | infix-op
  public static boolean operator_name(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "operator_name")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, OPERATOR_NAME, "<operator name>");
    r = prefix_symbol(b, l + 1);
    if (!r) r = infix_op(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  /* ********************************************************** */
  // QUESTION_MARK label-name COLON
  public static boolean optlabel(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "optlabel")) return false;
    if (!nextTokenIs(b, QUESTION_MARK)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, QUESTION_MARK);
    r = r && label_name(b, l + 1);
    r = r && consumeToken(b, COLON);
    exit_section_(b, m, OPTLABEL, r);
    return r;
  }

  /* ********************************************************** */
  // pattern
  //   |  TILDE label-name
  //   |  TILDE ( label-name [COLON typexpr] )
  //   |  TILDE label-name COLON pattern
  //   |  QUESTION_MARK label-name
  //   |  QUESTION_MARK ( label-name [COLON typexpr] [EQ expr] )
  //   |  QUESTION_MARK label-name COLON pattern
  //   |  QUESTION_MARK label-name COLON ( pattern [COLON typexpr] [EQ expr] )
  public static boolean parameter(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "parameter")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, PARAMETER, "<parameter>");
    r = pattern(b, l + 1, -1);
    if (!r) r = parameter_1(b, l + 1);
    if (!r) r = parameter_2(b, l + 1);
    if (!r) r = parameter_3(b, l + 1);
    if (!r) r = parameter_4(b, l + 1);
    if (!r) r = parameter_5(b, l + 1);
    if (!r) r = parameter_6(b, l + 1);
    if (!r) r = parameter_7(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  // TILDE label-name
  private static boolean parameter_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "parameter_1")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, TILDE);
    r = r && label_name(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // TILDE ( label-name [COLON typexpr] )
  private static boolean parameter_2(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "parameter_2")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, TILDE);
    r = r && parameter_2_1(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // label-name [COLON typexpr]
  private static boolean parameter_2_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "parameter_2_1")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = label_name(b, l + 1);
    r = r && parameter_2_1_1(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // [COLON typexpr]
  private static boolean parameter_2_1_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "parameter_2_1_1")) return false;
    parameter_2_1_1_0(b, l + 1);
    return true;
  }

  // COLON typexpr
  private static boolean parameter_2_1_1_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "parameter_2_1_1_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, COLON);
    r = r && typexpr(b, l + 1, -1);
    exit_section_(b, m, null, r);
    return r;
  }

  // TILDE label-name COLON pattern
  private static boolean parameter_3(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "parameter_3")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, TILDE);
    r = r && label_name(b, l + 1);
    r = r && consumeToken(b, COLON);
    r = r && pattern(b, l + 1, -1);
    exit_section_(b, m, null, r);
    return r;
  }

  // QUESTION_MARK label-name
  private static boolean parameter_4(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "parameter_4")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, QUESTION_MARK);
    r = r && label_name(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // QUESTION_MARK ( label-name [COLON typexpr] [EQ expr] )
  private static boolean parameter_5(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "parameter_5")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, QUESTION_MARK);
    r = r && parameter_5_1(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // label-name [COLON typexpr] [EQ expr]
  private static boolean parameter_5_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "parameter_5_1")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = label_name(b, l + 1);
    r = r && parameter_5_1_1(b, l + 1);
    r = r && parameter_5_1_2(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // [COLON typexpr]
  private static boolean parameter_5_1_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "parameter_5_1_1")) return false;
    parameter_5_1_1_0(b, l + 1);
    return true;
  }

  // COLON typexpr
  private static boolean parameter_5_1_1_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "parameter_5_1_1_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, COLON);
    r = r && typexpr(b, l + 1, -1);
    exit_section_(b, m, null, r);
    return r;
  }

  // [EQ expr]
  private static boolean parameter_5_1_2(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "parameter_5_1_2")) return false;
    parameter_5_1_2_0(b, l + 1);
    return true;
  }

  // EQ expr
  private static boolean parameter_5_1_2_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "parameter_5_1_2_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, EQ);
    r = r && expr(b, l + 1, -1);
    exit_section_(b, m, null, r);
    return r;
  }

  // QUESTION_MARK label-name COLON pattern
  private static boolean parameter_6(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "parameter_6")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, QUESTION_MARK);
    r = r && label_name(b, l + 1);
    r = r && consumeToken(b, COLON);
    r = r && pattern(b, l + 1, -1);
    exit_section_(b, m, null, r);
    return r;
  }

  // QUESTION_MARK label-name COLON ( pattern [COLON typexpr] [EQ expr] )
  private static boolean parameter_7(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "parameter_7")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, QUESTION_MARK);
    r = r && label_name(b, l + 1);
    r = r && consumeToken(b, COLON);
    r = r && parameter_7_3(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // pattern [COLON typexpr] [EQ expr]
  private static boolean parameter_7_3(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "parameter_7_3")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = pattern(b, l + 1, -1);
    r = r && parameter_7_3_1(b, l + 1);
    r = r && parameter_7_3_2(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // [COLON typexpr]
  private static boolean parameter_7_3_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "parameter_7_3_1")) return false;
    parameter_7_3_1_0(b, l + 1);
    return true;
  }

  // COLON typexpr
  private static boolean parameter_7_3_1_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "parameter_7_3_1_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, COLON);
    r = r && typexpr(b, l + 1, -1);
    exit_section_(b, m, null, r);
    return r;
  }

  // [EQ expr]
  private static boolean parameter_7_3_2(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "parameter_7_3_2")) return false;
    parameter_7_3_2_0(b, l + 1);
    return true;
  }

  // EQ expr
  private static boolean parameter_7_3_2_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "parameter_7_3_2_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, EQ);
    r = r && expr(b, l + 1, -1);
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // [ PIPE ] pattern_expr { PIPE pattern_expr }*
  public static boolean pattern_matching(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "pattern_matching")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, PATTERN_MATCHING, "<pattern matching>");
    r = pattern_matching_0(b, l + 1);
    r = r && pattern_expr(b, l + 1);
    r = r && pattern_matching_2(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  // [ PIPE ]
  private static boolean pattern_matching_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "pattern_matching_0")) return false;
    consumeToken(b, PIPE);
    return true;
  }

  // { PIPE pattern_expr }*
  private static boolean pattern_matching_2(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "pattern_matching_2")) return false;
    while (true) {
      int c = current_position_(b);
      if (!pattern_matching_2_0(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "pattern_matching_2", c)) break;
    }
    return true;
  }

  // PIPE pattern_expr
  private static boolean pattern_matching_2_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "pattern_matching_2_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, PIPE);
    r = r && pattern_expr(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // pattern [WHEN expr] RIGHT_ARROW expr
  public static boolean pattern_expr(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "pattern_expr")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, PATTERN_EXPR, "<pattern expr>");
    r = pattern(b, l + 1, -1);
    r = r && pattern_expr_1(b, l + 1);
    r = r && consumeToken(b, RIGHT_ARROW);
    r = r && expr(b, l + 1, -1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  // [WHEN expr]
  private static boolean pattern_expr_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "pattern_expr_1")) return false;
    pattern_expr_1_0(b, l + 1);
    return true;
  }

  // WHEN expr
  private static boolean pattern_expr_1_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "pattern_expr_1_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, WHEN);
    r = r && expr(b, l + 1, -1);
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // typexpr
  //   |  { SINGLE_QUOTE ident }+ DOT typexpr
  public static boolean poly_typexpr(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "poly_typexpr")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _COLLAPSE_, POLY_TYPEXPR, "<poly typexpr>");
    r = typexpr(b, l + 1, -1);
    if (!r) r = poly_typexpr_1(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  // { SINGLE_QUOTE ident }+ DOT typexpr
  private static boolean poly_typexpr_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "poly_typexpr_1")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = poly_typexpr_1_0(b, l + 1);
    r = r && consumeToken(b, DOT);
    r = r && typexpr(b, l + 1, -1);
    exit_section_(b, m, null, r);
    return r;
  }

  // { SINGLE_QUOTE ident }+
  private static boolean poly_typexpr_1_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "poly_typexpr_1_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = poly_typexpr_1_0_0(b, l + 1);
    while (r) {
      int c = current_position_(b);
      if (!poly_typexpr_1_0_0(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "poly_typexpr_1_0", c)) break;
    }
    exit_section_(b, m, null, r);
    return r;
  }

  // SINGLE_QUOTE ident
  private static boolean poly_typexpr_1_0_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "poly_typexpr_1_0_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, SINGLE_QUOTE);
    r = r && ident(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // LBRACKET tag-spec-first { PIPE tag-spec }* RBRACKET
  //   |  LBRACKETGREATER [ tag-spec ] { PIPE tag-spec }* RBRACKET
  //   |  LBRACKETLESS [PIPE] tag-spec-full { PIPE tag-spec-full }* [ GT { BACKTICK tag-name }+ ] RBRACKET
  public static boolean polymorphic_variant_type(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "polymorphic_variant_type")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, POLYMORPHIC_VARIANT_TYPE, "<polymorphic variant type>");
    r = polymorphic_variant_type_0(b, l + 1);
    if (!r) r = polymorphic_variant_type_1(b, l + 1);
    if (!r) r = polymorphic_variant_type_2(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  // LBRACKET tag-spec-first { PIPE tag-spec }* RBRACKET
  private static boolean polymorphic_variant_type_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "polymorphic_variant_type_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, LBRACKET);
    r = r && tag_spec_first(b, l + 1);
    r = r && polymorphic_variant_type_0_2(b, l + 1);
    r = r && consumeToken(b, RBRACKET);
    exit_section_(b, m, null, r);
    return r;
  }

  // { PIPE tag-spec }*
  private static boolean polymorphic_variant_type_0_2(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "polymorphic_variant_type_0_2")) return false;
    while (true) {
      int c = current_position_(b);
      if (!polymorphic_variant_type_0_2_0(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "polymorphic_variant_type_0_2", c)) break;
    }
    return true;
  }

  // PIPE tag-spec
  private static boolean polymorphic_variant_type_0_2_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "polymorphic_variant_type_0_2_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, PIPE);
    r = r && tag_spec(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // LBRACKETGREATER [ tag-spec ] { PIPE tag-spec }* RBRACKET
  private static boolean polymorphic_variant_type_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "polymorphic_variant_type_1")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, LBRACKETGREATER);
    r = r && polymorphic_variant_type_1_1(b, l + 1);
    r = r && polymorphic_variant_type_1_2(b, l + 1);
    r = r && consumeToken(b, RBRACKET);
    exit_section_(b, m, null, r);
    return r;
  }

  // [ tag-spec ]
  private static boolean polymorphic_variant_type_1_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "polymorphic_variant_type_1_1")) return false;
    tag_spec(b, l + 1);
    return true;
  }

  // { PIPE tag-spec }*
  private static boolean polymorphic_variant_type_1_2(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "polymorphic_variant_type_1_2")) return false;
    while (true) {
      int c = current_position_(b);
      if (!polymorphic_variant_type_1_2_0(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "polymorphic_variant_type_1_2", c)) break;
    }
    return true;
  }

  // PIPE tag-spec
  private static boolean polymorphic_variant_type_1_2_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "polymorphic_variant_type_1_2_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, PIPE);
    r = r && tag_spec(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // LBRACKETLESS [PIPE] tag-spec-full { PIPE tag-spec-full }* [ GT { BACKTICK tag-name }+ ] RBRACKET
  private static boolean polymorphic_variant_type_2(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "polymorphic_variant_type_2")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, LBRACKETLESS);
    r = r && polymorphic_variant_type_2_1(b, l + 1);
    r = r && tag_spec_full(b, l + 1);
    r = r && polymorphic_variant_type_2_3(b, l + 1);
    r = r && polymorphic_variant_type_2_4(b, l + 1);
    r = r && consumeToken(b, RBRACKET);
    exit_section_(b, m, null, r);
    return r;
  }

  // [PIPE]
  private static boolean polymorphic_variant_type_2_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "polymorphic_variant_type_2_1")) return false;
    consumeToken(b, PIPE);
    return true;
  }

  // { PIPE tag-spec-full }*
  private static boolean polymorphic_variant_type_2_3(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "polymorphic_variant_type_2_3")) return false;
    while (true) {
      int c = current_position_(b);
      if (!polymorphic_variant_type_2_3_0(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "polymorphic_variant_type_2_3", c)) break;
    }
    return true;
  }

  // PIPE tag-spec-full
  private static boolean polymorphic_variant_type_2_3_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "polymorphic_variant_type_2_3_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, PIPE);
    r = r && tag_spec_full(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // [ GT { BACKTICK tag-name }+ ]
  private static boolean polymorphic_variant_type_2_4(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "polymorphic_variant_type_2_4")) return false;
    polymorphic_variant_type_2_4_0(b, l + 1);
    return true;
  }

  // GT { BACKTICK tag-name }+
  private static boolean polymorphic_variant_type_2_4_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "polymorphic_variant_type_2_4_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, GT);
    r = r && polymorphic_variant_type_2_4_0_1(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // { BACKTICK tag-name }+
  private static boolean polymorphic_variant_type_2_4_0_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "polymorphic_variant_type_2_4_0_1")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = polymorphic_variant_type_2_4_0_1_0(b, l + 1);
    while (r) {
      int c = current_position_(b);
      if (!polymorphic_variant_type_2_4_0_1_0(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "polymorphic_variant_type_2_4_0_1", c)) break;
    }
    exit_section_(b, m, null, r);
    return r;
  }

  // BACKTICK tag-name
  private static boolean polymorphic_variant_type_2_4_0_1_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "polymorphic_variant_type_2_4_0_1_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, BACKTICK);
    r = r && tag_name(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // EXCLAMATION_MARK { operator-char }*
  //   |  (QUESTION_MARK | TILDE) { operator-char }+
  public static boolean prefix_symbol(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "prefix_symbol")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, PREFIX_SYMBOL, "<prefix symbol>");
    r = prefix_symbol_0(b, l + 1);
    if (!r) r = prefix_symbol_1(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  // EXCLAMATION_MARK { operator-char }*
  private static boolean prefix_symbol_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "prefix_symbol_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, EXCLAMATION_MARK);
    r = r && prefix_symbol_0_1(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // { operator-char }*
  private static boolean prefix_symbol_0_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "prefix_symbol_0_1")) return false;
    while (true) {
      int c = current_position_(b);
      if (!prefix_symbol_0_1_0(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "prefix_symbol_0_1", c)) break;
    }
    return true;
  }

  // { operator-char }
  private static boolean prefix_symbol_0_1_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "prefix_symbol_0_1_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = operator_char(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // (QUESTION_MARK | TILDE) { operator-char }+
  private static boolean prefix_symbol_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "prefix_symbol_1")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = prefix_symbol_1_0(b, l + 1);
    r = r && prefix_symbol_1_1(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // QUESTION_MARK | TILDE
  private static boolean prefix_symbol_1_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "prefix_symbol_1_0")) return false;
    boolean r;
    r = consumeToken(b, QUESTION_MARK);
    if (!r) r = consumeToken(b, TILDE);
    return r;
  }

  // { operator-char }+
  private static boolean prefix_symbol_1_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "prefix_symbol_1_1")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = prefix_symbol_1_1_0(b, l + 1);
    while (r) {
      int c = current_position_(b);
      if (!prefix_symbol_1_1_0(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "prefix_symbol_1_1", c)) break;
    }
    exit_section_(b, m, null, r);
    return r;
  }

  // { operator-char }
  private static boolean prefix_symbol_1_1_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "prefix_symbol_1_1_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = operator_char(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // prefix-symbol expr
  public static boolean prefix_base_expr(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "prefix_base_expr")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, PREFIX_BASE_EXPR, "<prefix base expr>");
    r = prefix_symbol(b, l + 1);
    r = r && expr(b, l + 1, -1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  /* ********************************************************** */
  // LBRACE field-decl { SEMI field-decl }* [SEMI] RBRACE
  public static boolean record_decl(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "record_decl")) return false;
    if (!nextTokenIs(b, LBRACE)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, LBRACE);
    r = r && field_decl(b, l + 1);
    r = r && record_decl_2(b, l + 1);
    r = r && record_decl_3(b, l + 1);
    r = r && consumeToken(b, RBRACE);
    exit_section_(b, m, RECORD_DECL, r);
    return r;
  }

  // { SEMI field-decl }*
  private static boolean record_decl_2(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "record_decl_2")) return false;
    while (true) {
      int c = current_position_(b);
      if (!record_decl_2_0(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "record_decl_2", c)) break;
    }
    return true;
  }

  // SEMI field-decl
  private static boolean record_decl_2_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "record_decl_2_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, SEMI);
    r = r && field_decl(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // [SEMI]
  private static boolean record_decl_3(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "record_decl_3")) return false;
    consumeToken(b, SEMI);
    return true;
  }

  /* ********************************************************** */
  // MODULE REC module-name COLON module_type EQ module_expr { AND module-name COLON module_type EQ module_expr } *
  public static boolean recursive_module_extension_def(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "recursive_module_extension_def")) return false;
    if (!nextTokenIs(b, MODULE)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeTokens(b, 0, MODULE, REC);
    r = r && module_name(b, l + 1);
    r = r && consumeToken(b, COLON);
    r = r && module_type(b, l + 1, -1);
    r = r && consumeToken(b, EQ);
    r = r && module_expr(b, l + 1, -1);
    r = r && recursive_module_extension_def_7(b, l + 1);
    exit_section_(b, m, RECURSIVE_MODULE_EXTENSION_DEF, r);
    return r;
  }

  // { AND module-name COLON module_type EQ module_expr } *
  private static boolean recursive_module_extension_def_7(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "recursive_module_extension_def_7")) return false;
    while (true) {
      int c = current_position_(b);
      if (!recursive_module_extension_def_7_0(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "recursive_module_extension_def_7", c)) break;
    }
    return true;
  }

  // AND module-name COLON module_type EQ module_expr
  private static boolean recursive_module_extension_def_7_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "recursive_module_extension_def_7_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, AND);
    r = r && module_name(b, l + 1);
    r = r && consumeToken(b, COLON);
    r = r && module_type(b, l + 1, -1);
    r = r && consumeToken(b, EQ);
    r = r && module_expr(b, l + 1, -1);
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // MODULE REC module-name COLON module_type { AND module-name COLON module_type } *
  public static boolean recursive_module_extension_spec(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "recursive_module_extension_spec")) return false;
    if (!nextTokenIs(b, MODULE)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeTokens(b, 0, MODULE, REC);
    r = r && module_name(b, l + 1);
    r = r && consumeToken(b, COLON);
    r = r && module_type(b, l + 1, -1);
    r = r && recursive_module_extension_spec_5(b, l + 1);
    exit_section_(b, m, RECURSIVE_MODULE_EXTENSION_SPEC, r);
    return r;
  }

  // { AND module-name COLON module_type } *
  private static boolean recursive_module_extension_spec_5(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "recursive_module_extension_spec_5")) return false;
    while (true) {
      int c = current_position_(b);
      if (!recursive_module_extension_spec_5_0(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "recursive_module_extension_spec_5", c)) break;
    }
    return true;
  }

  // AND module-name COLON module_type
  private static boolean recursive_module_extension_spec_5_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "recursive_module_extension_spec_5_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, AND);
    r = r && module_name(b, l + 1);
    r = r && consumeToken(b, COLON);
    r = r && module_type(b, l + 1, -1);
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // VAL value-name COLON typexpr
  //   | EXTERNAL value-name COLON typexpr EQ external-declaration
  //   | type-definition
  //   | EXCEPTION constr-decl
  //   | class-specification
  //   | classtype-definition
  //   | MODULE module-name COLON module_type
  //   | MODULE module-name { ( module-name COLON module_type ) }* COLON module_type
  //   | MODULE TYPE modtype-name
  //   | MODULE type modtype-name EQ module_type
  //   | OPEN module-path
  //   | INCLUDE module_type
  //   | recursive_module_extension_spec
  //   | generalized_open_statements_spec
  public static boolean specification(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "specification")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, SPECIFICATION, "<specification>");
    r = specification_0(b, l + 1);
    if (!r) r = specification_1(b, l + 1);
    if (!r) r = type_definition(b, l + 1);
    if (!r) r = specification_3(b, l + 1);
    if (!r) r = class_specification(b, l + 1);
    if (!r) r = classtype_definition(b, l + 1);
    if (!r) r = specification_6(b, l + 1);
    if (!r) r = specification_7(b, l + 1);
    if (!r) r = specification_8(b, l + 1);
    if (!r) r = specification_9(b, l + 1);
    if (!r) r = specification_10(b, l + 1);
    if (!r) r = specification_11(b, l + 1);
    if (!r) r = recursive_module_extension_spec(b, l + 1);
    if (!r) r = generalized_open_statements_spec(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  // VAL value-name COLON typexpr
  private static boolean specification_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "specification_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, VAL);
    r = r && value_name(b, l + 1);
    r = r && consumeToken(b, COLON);
    r = r && typexpr(b, l + 1, -1);
    exit_section_(b, m, null, r);
    return r;
  }

  // EXTERNAL value-name COLON typexpr EQ external-declaration
  private static boolean specification_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "specification_1")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, EXTERNAL);
    r = r && value_name(b, l + 1);
    r = r && consumeToken(b, COLON);
    r = r && typexpr(b, l + 1, -1);
    r = r && consumeToken(b, EQ);
    r = r && external_declaration(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // EXCEPTION constr-decl
  private static boolean specification_3(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "specification_3")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, EXCEPTION);
    r = r && constr_decl(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // MODULE module-name COLON module_type
  private static boolean specification_6(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "specification_6")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, MODULE);
    r = r && module_name(b, l + 1);
    r = r && consumeToken(b, COLON);
    r = r && module_type(b, l + 1, -1);
    exit_section_(b, m, null, r);
    return r;
  }

  // MODULE module-name { ( module-name COLON module_type ) }* COLON module_type
  private static boolean specification_7(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "specification_7")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, MODULE);
    r = r && module_name(b, l + 1);
    r = r && specification_7_2(b, l + 1);
    r = r && consumeToken(b, COLON);
    r = r && module_type(b, l + 1, -1);
    exit_section_(b, m, null, r);
    return r;
  }

  // { ( module-name COLON module_type ) }*
  private static boolean specification_7_2(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "specification_7_2")) return false;
    while (true) {
      int c = current_position_(b);
      if (!specification_7_2_0(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "specification_7_2", c)) break;
    }
    return true;
  }

  // module-name COLON module_type
  private static boolean specification_7_2_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "specification_7_2_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = module_name(b, l + 1);
    r = r && consumeToken(b, COLON);
    r = r && module_type(b, l + 1, -1);
    exit_section_(b, m, null, r);
    return r;
  }

  // MODULE TYPE modtype-name
  private static boolean specification_8(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "specification_8")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeTokens(b, 0, MODULE, TYPE);
    r = r && modtype_name(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // MODULE type modtype-name EQ module_type
  private static boolean specification_9(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "specification_9")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeTokens(b, 0, MODULE, TYPE);
    r = r && modtype_name(b, l + 1);
    r = r && consumeToken(b, EQ);
    r = r && module_type(b, l + 1, -1);
    exit_section_(b, m, null, r);
    return r;
  }

  // OPEN module-path
  private static boolean specification_10(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "specification_10")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, OPEN);
    r = r && module_path(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // INCLUDE module_type
  private static boolean specification_11(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "specification_11")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, INCLUDE);
    r = r && module_type(b, l + 1, -1);
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // STRING_VALUE
  public static boolean string_literal(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "string_literal")) return false;
    if (!nextTokenIs(b, STRING_VALUE)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, STRING_VALUE);
    exit_section_(b, m, STRING_LITERAL, r);
    return r;
  }

  /* ********************************************************** */
  // CAPITALIZED_IDENT
  public static boolean tag_name(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "tag_name")) return false;
    if (!nextTokenIs(b, CAPITALIZED_IDENT)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, CAPITALIZED_IDENT);
    exit_section_(b, m, TAG_NAME, r);
    return r;
  }

  /* ********************************************************** */
  // BACKTICK tag-name [ OF typexpr ]
  //   |  typexpr
  public static boolean tag_spec(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "tag_spec")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, TAG_SPEC, "<tag spec>");
    r = tag_spec_0(b, l + 1);
    if (!r) r = typexpr(b, l + 1, -1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  // BACKTICK tag-name [ OF typexpr ]
  private static boolean tag_spec_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "tag_spec_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, BACKTICK);
    r = r && tag_name(b, l + 1);
    r = r && tag_spec_0_2(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // [ OF typexpr ]
  private static boolean tag_spec_0_2(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "tag_spec_0_2")) return false;
    tag_spec_0_2_0(b, l + 1);
    return true;
  }

  // OF typexpr
  private static boolean tag_spec_0_2_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "tag_spec_0_2_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, OF);
    r = r && typexpr(b, l + 1, -1);
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // BACKTICK tag-name [ OF typexpr ]
  //   |  [ typexpr ] PIPE tag-spec
  public static boolean tag_spec_first(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "tag_spec_first")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, TAG_SPEC_FIRST, "<tag spec first>");
    r = tag_spec_first_0(b, l + 1);
    if (!r) r = tag_spec_first_1(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  // BACKTICK tag-name [ OF typexpr ]
  private static boolean tag_spec_first_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "tag_spec_first_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, BACKTICK);
    r = r && tag_name(b, l + 1);
    r = r && tag_spec_first_0_2(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // [ OF typexpr ]
  private static boolean tag_spec_first_0_2(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "tag_spec_first_0_2")) return false;
    tag_spec_first_0_2_0(b, l + 1);
    return true;
  }

  // OF typexpr
  private static boolean tag_spec_first_0_2_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "tag_spec_first_0_2_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, OF);
    r = r && typexpr(b, l + 1, -1);
    exit_section_(b, m, null, r);
    return r;
  }

  // [ typexpr ] PIPE tag-spec
  private static boolean tag_spec_first_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "tag_spec_first_1")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = tag_spec_first_1_0(b, l + 1);
    r = r && consumeToken(b, PIPE);
    r = r && tag_spec(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // [ typexpr ]
  private static boolean tag_spec_first_1_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "tag_spec_first_1_0")) return false;
    typexpr(b, l + 1, -1);
    return true;
  }

  /* ********************************************************** */
  // BACKTICK tag-name [ OF [AMPERSAND] typexpr { AMPERSAND typexpr }* ]
  //   |  typexpr
  public static boolean tag_spec_full(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "tag_spec_full")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, TAG_SPEC_FULL, "<tag spec full>");
    r = tag_spec_full_0(b, l + 1);
    if (!r) r = typexpr(b, l + 1, -1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  // BACKTICK tag-name [ OF [AMPERSAND] typexpr { AMPERSAND typexpr }* ]
  private static boolean tag_spec_full_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "tag_spec_full_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, BACKTICK);
    r = r && tag_name(b, l + 1);
    r = r && tag_spec_full_0_2(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // [ OF [AMPERSAND] typexpr { AMPERSAND typexpr }* ]
  private static boolean tag_spec_full_0_2(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "tag_spec_full_0_2")) return false;
    tag_spec_full_0_2_0(b, l + 1);
    return true;
  }

  // OF [AMPERSAND] typexpr { AMPERSAND typexpr }*
  private static boolean tag_spec_full_0_2_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "tag_spec_full_0_2_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, OF);
    r = r && tag_spec_full_0_2_0_1(b, l + 1);
    r = r && typexpr(b, l + 1, -1);
    r = r && tag_spec_full_0_2_0_3(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // [AMPERSAND]
  private static boolean tag_spec_full_0_2_0_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "tag_spec_full_0_2_0_1")) return false;
    consumeToken(b, AMPERSAND);
    return true;
  }

  // { AMPERSAND typexpr }*
  private static boolean tag_spec_full_0_2_0_3(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "tag_spec_full_0_2_0_3")) return false;
    while (true) {
      int c = current_position_(b);
      if (!tag_spec_full_0_2_0_3_0(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "tag_spec_full_0_2_0_3", c)) break;
    }
    return true;
  }

  // AMPERSAND typexpr
  private static boolean tag_spec_full_0_2_0_3_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "tag_spec_full_0_2_0_3_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, AMPERSAND);
    r = r && typexpr(b, l + 1, -1);
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // CONSTRAINT typexpr EQ typexpr
  public static boolean type_constraint(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "type_constraint")) return false;
    if (!nextTokenIs(b, CONSTRAINT)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, CONSTRAINT);
    r = r && typexpr(b, l + 1, -1);
    r = r && consumeToken(b, EQ);
    r = r && typexpr(b, l + 1, -1);
    exit_section_(b, m, TYPE_CONSTRAINT, r);
    return r;
  }

  /* ********************************************************** */
  // TYPE [NONREC] typedef { and_types }*
  public static boolean type_definition(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "type_definition")) return false;
    if (!nextTokenIs(b, TYPE)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, TYPE);
    r = r && type_definition_1(b, l + 1);
    r = r && typedef(b, l + 1);
    r = r && type_definition_3(b, l + 1);
    exit_section_(b, m, TYPE_DEFINITION, r);
    return r;
  }

  // [NONREC]
  private static boolean type_definition_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "type_definition_1")) return false;
    consumeToken(b, NONREC);
    return true;
  }

  // { and_types }*
  private static boolean type_definition_3(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "type_definition_3")) return false;
    while (true) {
      int c = current_position_(b);
      if (!type_definition_3_0(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "type_definition_3", c)) break;
    }
    return true;
  }

  // { and_types }
  private static boolean type_definition_3_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "type_definition_3_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = and_types(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // EQ typexpr
  public static boolean type_equation(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "type_equation")) return false;
    if (!nextTokenIs(b, EQ)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, EQ);
    r = r && typexpr(b, l + 1, -1);
    exit_section_(b, m, TYPE_EQUATION, r);
    return r;
  }

  /* ********************************************************** */
  // [type-equation] [type-representation] { type-constraint }*
  public static boolean type_information(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "type_information")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, TYPE_INFORMATION, "<type information>");
    r = type_information_0(b, l + 1);
    r = r && type_information_1(b, l + 1);
    r = r && type_information_2(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  // [type-equation]
  private static boolean type_information_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "type_information_0")) return false;
    type_equation(b, l + 1);
    return true;
  }

  // [type-representation]
  private static boolean type_information_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "type_information_1")) return false;
    type_representation(b, l + 1);
    return true;
  }

  // { type-constraint }*
  private static boolean type_information_2(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "type_information_2")) return false;
    while (true) {
      int c = current_position_(b);
      if (!type_information_2_0(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "type_information_2", c)) break;
    }
    return true;
  }

  // { type-constraint }
  private static boolean type_information_2_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "type_information_2_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = type_constraint(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // [ext-variance] SINGLE_QUOTE ident
  public static boolean type_param(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "type_param")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, TYPE_PARAM, "<type param>");
    r = type_param_0(b, l + 1);
    r = r && consumeToken(b, SINGLE_QUOTE);
    r = r && ident(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  // [ext-variance]
  private static boolean type_param_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "type_param_0")) return false;
    ext_variance(b, l + 1);
    return true;
  }

  /* ********************************************************** */
  // SINGLE_QUOTE ident { COMMA SINGLE_QUOTE ident }
  public static boolean type_parameters(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "type_parameters")) return false;
    if (!nextTokenIs(b, SINGLE_QUOTE)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, SINGLE_QUOTE);
    r = r && ident(b, l + 1);
    r = r && type_parameters_2(b, l + 1);
    exit_section_(b, m, TYPE_PARAMETERS, r);
    return r;
  }

  // COMMA SINGLE_QUOTE ident
  private static boolean type_parameters_2(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "type_parameters_2")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeTokens(b, 0, COMMA, SINGLE_QUOTE);
    r = r && ident(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // type-param
  //   |  LPAREN type-param { COMMA type-param } RPAREN
  public static boolean type_params(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "type_params")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, TYPE_PARAMS, "<type params>");
    r = type_param(b, l + 1);
    if (!r) r = type_params_1(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  // LPAREN type-param { COMMA type-param } RPAREN
  private static boolean type_params_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "type_params_1")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, LPAREN);
    r = r && type_param(b, l + 1);
    r = r && type_params_1_2(b, l + 1);
    r = r && consumeToken(b, RPAREN);
    exit_section_(b, m, null, r);
    return r;
  }

  // COMMA type-param
  private static boolean type_params_1_2(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "type_params_1_2")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, COMMA);
    r = r && type_param(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // EQ [PIPE] constr-decl { PIPE constr-decl }*
  //   | EQ record-decl
  //   | EQ PIPE
  public static boolean type_representation(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "type_representation")) return false;
    if (!nextTokenIs(b, EQ)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = type_representation_0(b, l + 1);
    if (!r) r = type_representation_1(b, l + 1);
    if (!r) r = parseTokens(b, 0, EQ, PIPE);
    exit_section_(b, m, TYPE_REPRESENTATION, r);
    return r;
  }

  // EQ [PIPE] constr-decl { PIPE constr-decl }*
  private static boolean type_representation_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "type_representation_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, EQ);
    r = r && type_representation_0_1(b, l + 1);
    r = r && constr_decl(b, l + 1);
    r = r && type_representation_0_3(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // [PIPE]
  private static boolean type_representation_0_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "type_representation_0_1")) return false;
    consumeToken(b, PIPE);
    return true;
  }

  // { PIPE constr-decl }*
  private static boolean type_representation_0_3(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "type_representation_0_3")) return false;
    while (true) {
      int c = current_position_(b);
      if (!type_representation_0_3_0(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "type_representation_0_3", c)) break;
    }
    return true;
  }

  // PIPE constr-decl
  private static boolean type_representation_0_3_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "type_representation_0_3_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, PIPE);
    r = r && constr_decl(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // EQ record-decl
  private static boolean type_representation_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "type_representation_1")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, EQ);
    r = r && record_decl(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // typeconstr-name
  public static boolean type_name(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "type_name")) return false;
    if (!nextTokenIs(b, LOWERCASE_IDENT)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = typeconstr_name(b, l + 1);
    exit_section_(b, m, TYPE_NAME, r);
    return r;
  }

  /* ********************************************************** */
  // [ extended-module-path DOT ] typeconstr-name
  public static boolean typeconstr(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "typeconstr")) return false;
    if (!nextTokenIs(b, "<typeconstr>", CAPITALIZED_IDENT, LOWERCASE_IDENT)) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, TYPECONSTR, "<typeconstr>");
    r = typeconstr_0(b, l + 1);
    r = r && typeconstr_name(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  // [ extended-module-path DOT ]
  private static boolean typeconstr_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "typeconstr_0")) return false;
    typeconstr_0_0(b, l + 1);
    return true;
  }

  // extended-module-path DOT
  private static boolean typeconstr_0_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "typeconstr_0_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = extended_module_path(b, l + 1);
    r = r && consumeToken(b, DOT);
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // LOWERCASE_IDENT
  public static boolean typeconstr_name(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "typeconstr_name")) return false;
    if (!nextTokenIs(b, LOWERCASE_IDENT)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, LOWERCASE_IDENT);
    exit_section_(b, m, TYPECONSTR_NAME, r);
    return r;
  }

  /* ********************************************************** */
  // [type-params] type_name type-information
  public static boolean typedef(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "typedef")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, TYPEDEF, "<typedef>");
    r = typedef_0(b, l + 1);
    r = r && type_name(b, l + 1);
    r = r && type_information(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  // [type-params]
  private static boolean typedef_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "typedef_0")) return false;
    type_params(b, l + 1);
    return true;
  }

  /* ********************************************************** */
  // [ module-items ]
  public static boolean unit_implementation(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "unit_implementation")) return false;
    Marker m = enter_section_(b, l, _NONE_, UNIT_IMPLEMENTATION, "<unit implementation>");
    module_items(b, l + 1);
    exit_section_(b, l, m, true, false, null);
    return true;
  }

  /* ********************************************************** */
  // { specification [SEMISEMI] }+
  public static boolean unit_interface(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "unit_interface")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, UNIT_INTERFACE, "<unit interface>");
    r = unit_interface_0(b, l + 1);
    while (r) {
      int c = current_position_(b);
      if (!unit_interface_0(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "unit_interface", c)) break;
    }
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  // specification [SEMISEMI]
  private static boolean unit_interface_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "unit_interface_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = specification(b, l + 1);
    r = r && unit_interface_0_1(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // [SEMISEMI]
  private static boolean unit_interface_0_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "unit_interface_0_1")) return false;
    consumeToken(b, SEMISEMI);
    return true;
  }

  /* ********************************************************** */
  // LOWERCASE_IDENT
  //   |  LPAREN operator-name RPAREN
  public static boolean value_name(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "value_name")) return false;
    if (!nextTokenIs(b, "<value name>", LOWERCASE_IDENT, LPAREN)) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, VALUE_NAME, "<value name>");
    r = consumeToken(b, LOWERCASE_IDENT);
    if (!r) r = value_name_1(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  // LPAREN operator-name RPAREN
  private static boolean value_name_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "value_name_1")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, LPAREN);
    r = r && operator_name(b, l + 1);
    r = r && consumeToken(b, RPAREN);
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // [ module-path DOT ] value-name
  public static boolean value_path(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "value_path")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, VALUE_PATH, "<value path>");
    r = value_path_0(b, l + 1);
    r = r && value_name(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  // [ module-path DOT ]
  private static boolean value_path_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "value_path_0")) return false;
    value_path_0_0(b, l + 1);
    return true;
  }

  // module-path DOT
  private static boolean value_path_0_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "value_path_0_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = module_path(b, l + 1);
    r = r && consumeToken(b, DOT);
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // PLUS
  //   | MINUS
  public static boolean variance(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "variance")) return false;
    if (!nextTokenIs(b, "<variance>", MINUS, PLUS)) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, VARIANCE, "<variance>");
    r = consumeToken(b, PLUS);
    if (!r) r = consumeToken(b, MINUS);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  /* ********************************************************** */
  // Expression root: class_expr
  // Operator priority table:
  // 0: ATOM(classpath_class_expr)
  // 1: ATOM(typeexpr_class_expr)
  // 2: PREFIX(paren_class_expr)
  // 3: PREFIX(paren_type_class_expr)
  // 4: POSTFIX(arg_class_expr)
  // 5: PREFIX(fun_class_expr)
  // 6: PREFIX(let_class_expr)
  // 7: ATOM(object_class_expr)
  // 8: PREFIX(let_open_class_expr)
  public static boolean class_expr(PsiBuilder b, int l, int g) {
    if (!recursion_guard_(b, l, "class_expr")) return false;
    addVariant(b, "<class expr>");
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_, "<class expr>");
    r = classpath_class_expr(b, l + 1);
    if (!r) r = typeexpr_class_expr(b, l + 1);
    if (!r) r = paren_class_expr(b, l + 1);
    if (!r) r = fun_class_expr(b, l + 1);
    if (!r) r = let_class_expr(b, l + 1);
    if (!r) r = object_class_expr(b, l + 1);
    if (!r) r = let_open_class_expr(b, l + 1);
    p = r;
    r = r && class_expr_0(b, l + 1, g);
    exit_section_(b, l, m, null, r, p, null);
    return r || p;
  }

  public static boolean class_expr_0(PsiBuilder b, int l, int g) {
    if (!recursion_guard_(b, l, "class_expr_0")) return false;
    boolean r = true;
    while (true) {
      Marker m = enter_section_(b, l, _LEFT_, null);
      if (g < 4 && arg_class_expr_0(b, l + 1)) {
        r = true;
        exit_section_(b, l, m, ARG_CLASS_EXPR, r, true, null);
      }
      else {
        exit_section_(b, l, m, null, false, false, null);
        break;
      }
    }
    return r;
  }

  // class-path
  public static boolean classpath_class_expr(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "classpath_class_expr")) return false;
    if (!nextTokenIsSmart(b, CAPITALIZED_IDENT, LOWERCASE_IDENT)) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, CLASSPATH_CLASS_EXPR, "<classpath class expr>");
    r = class_path(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  // [ typexpr { COMMA typexpr }* ] class-path
  public static boolean typeexpr_class_expr(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "typeexpr_class_expr")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, TYPEEXPR_CLASS_EXPR, "<typeexpr class expr>");
    r = typeexpr_class_expr_0(b, l + 1);
    r = r && class_path(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  // [ typexpr { COMMA typexpr }* ]
  private static boolean typeexpr_class_expr_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "typeexpr_class_expr_0")) return false;
    typeexpr_class_expr_0_0(b, l + 1);
    return true;
  }

  // typexpr { COMMA typexpr }*
  private static boolean typeexpr_class_expr_0_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "typeexpr_class_expr_0_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = typexpr(b, l + 1, -1);
    r = r && typeexpr_class_expr_0_0_1(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // { COMMA typexpr }*
  private static boolean typeexpr_class_expr_0_0_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "typeexpr_class_expr_0_0_1")) return false;
    while (true) {
      int c = current_position_(b);
      if (!typeexpr_class_expr_0_0_1_0(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "typeexpr_class_expr_0_0_1", c)) break;
    }
    return true;
  }

  // COMMA typexpr
  private static boolean typeexpr_class_expr_0_0_1_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "typeexpr_class_expr_0_0_1_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeTokenSmart(b, COMMA);
    r = r && typexpr(b, l + 1, -1);
    exit_section_(b, m, null, r);
    return r;
  }

  public static boolean paren_class_expr(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "paren_class_expr")) return false;
    if (!nextTokenIsSmart(b, LPAREN)) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_, null);
    r = consumeTokenSmart(b, LPAREN);
    p = r;
    r = p && class_expr(b, l, 2);
    r = p && report_error_(b, consumeToken(b, RPAREN)) && r;
    exit_section_(b, l, m, PAREN_CLASS_EXPR, r, p, null);
    return r || p;
  }

  public static boolean paren_type_class_expr(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "paren_type_class_expr")) return false;
    if (!nextTokenIsSmart(b, LPAREN)) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_, null);
    r = consumeTokenSmart(b, LPAREN);
    p = r;
    r = p && class_expr(b, l, 3);
    r = p && report_error_(b, paren_type_class_expr_1(b, l + 1)) && r;
    exit_section_(b, l, m, PAREN_TYPE_CLASS_EXPR, r, p, null);
    return r || p;
  }

  // COLON class-type RPAREN
  private static boolean paren_type_class_expr_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "paren_type_class_expr_1")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, COLON);
    r = r && class_type(b, l + 1);
    r = r && consumeToken(b, RPAREN);
    exit_section_(b, m, null, r);
    return r;
  }

  // { argument }+
  private static boolean arg_class_expr_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "arg_class_expr_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = arg_class_expr_0_0(b, l + 1);
    while (r) {
      int c = current_position_(b);
      if (!arg_class_expr_0_0(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "arg_class_expr_0", c)) break;
    }
    exit_section_(b, m, null, r);
    return r;
  }

  // { argument }
  private static boolean arg_class_expr_0_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "arg_class_expr_0_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = argument(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  public static boolean fun_class_expr(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "fun_class_expr")) return false;
    if (!nextTokenIsSmart(b, FUN)) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_, null);
    r = fun_class_expr_0(b, l + 1);
    p = r;
    r = p && class_expr(b, l, 5);
    exit_section_(b, l, m, FUN_CLASS_EXPR, r, p, null);
    return r || p;
  }

  // FUN { parameter }+ RIGHT_ARROW
  private static boolean fun_class_expr_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "fun_class_expr_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeTokenSmart(b, FUN);
    r = r && fun_class_expr_0_1(b, l + 1);
    r = r && consumeToken(b, RIGHT_ARROW);
    exit_section_(b, m, null, r);
    return r;
  }

  // { parameter }+
  private static boolean fun_class_expr_0_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "fun_class_expr_0_1")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = fun_class_expr_0_1_0(b, l + 1);
    while (r) {
      int c = current_position_(b);
      if (!fun_class_expr_0_1_0(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "fun_class_expr_0_1", c)) break;
    }
    exit_section_(b, m, null, r);
    return r;
  }

  // { parameter }
  private static boolean fun_class_expr_0_1_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "fun_class_expr_0_1_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = parameter(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  public static boolean let_class_expr(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "let_class_expr")) return false;
    if (!nextTokenIsSmart(b, LET)) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_, null);
    r = let_class_expr_0(b, l + 1);
    p = r;
    r = p && class_expr(b, l, 6);
    exit_section_(b, l, m, LET_CLASS_EXPR, r, p, null);
    return r || p;
  }

  // LET [REC] let-binding { AND let-binding }* IN
  private static boolean let_class_expr_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "let_class_expr_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeTokenSmart(b, LET);
    r = r && let_class_expr_0_1(b, l + 1);
    r = r && let_binding(b, l + 1);
    r = r && let_class_expr_0_3(b, l + 1);
    r = r && consumeToken(b, IN);
    exit_section_(b, m, null, r);
    return r;
  }

  // [REC]
  private static boolean let_class_expr_0_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "let_class_expr_0_1")) return false;
    consumeTokenSmart(b, REC);
    return true;
  }

  // { AND let-binding }*
  private static boolean let_class_expr_0_3(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "let_class_expr_0_3")) return false;
    while (true) {
      int c = current_position_(b);
      if (!let_class_expr_0_3_0(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "let_class_expr_0_3", c)) break;
    }
    return true;
  }

  // AND let-binding
  private static boolean let_class_expr_0_3_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "let_class_expr_0_3_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeTokenSmart(b, AND);
    r = r && let_binding(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // OBJECT class-body END
  public static boolean object_class_expr(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "object_class_expr")) return false;
    if (!nextTokenIsSmart(b, OBJECT)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeTokenSmart(b, OBJECT);
    r = r && class_body(b, l + 1);
    r = r && consumeToken(b, END);
    exit_section_(b, m, OBJECT_CLASS_EXPR, r);
    return r;
  }

  public static boolean let_open_class_expr(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "let_open_class_expr")) return false;
    if (!nextTokenIsSmart(b, LET)) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_, null);
    r = let_open_class_expr_0(b, l + 1);
    p = r;
    r = p && class_expr(b, l, -1);
    exit_section_(b, l, m, LET_OPEN_CLASS_EXPR, r, p, null);
    return r || p;
  }

  // LET OPEN module-path IN
  private static boolean let_open_class_expr_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "let_open_class_expr_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeTokensSmart(b, 0, LET, OPEN);
    r = r && module_path(b, l + 1);
    r = r && consumeToken(b, IN);
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // Expression root: expr
  // Operator priority table:
  // 0: ATOM(value_path_base_expr)
  // 1: ATOM(constant_base_expr)
  // 2: PREFIX(paren_base_expr)
  // 3: PREFIX(begin_base_expr)
  // 4: PREFIX(paren_type_base_expr)
  // 5: N_ARY(colon_base_expr)
  // 6: PREFIX(constr_base_expr)
  // 7: PREFIX(backtick_base_expr)
  // 8: BINARY(shortcut_base_expr)
  // 9: ATOM(bracket_base_expr)
  // 10: ATOM(larray_base_expr)
  // 11: ATOM(brace_field_base_expr)
  // 12: ATOM(brace_base_expr)
  // 13: POSTFIX(argument_base_expr)
  // 14: PREFIX(minus_base_expr)
  // 15: PREFIX(minus_dot_base_expr)
  // 16: BINARY(infix_op_base_expr)
  // 17: POSTFIX(dot_field_base_expr)
  // 18: BINARY(dot_field_left_arrow_base_expr)
  // 19: PREFIX(dot_paren_base_expr)
  // 20: ATOM(dot_paren_left_arrow_base_expr)
  // 21: PREFIX(dot_bracket_base_expr)
  // 22: ATOM(dot_bracket_left_arrow_base_expr)
  // 23: ATOM(if_else_base_expr)
  // 24: ATOM(while_base_expr)
  // 25: ATOM(for_base_expr)
  // 26: BINARY(semi_base_expr)
  // 27: PREFIX(match_base_expr)
  // 28: ATOM(function_base_expr)
  // 29: PREFIX(fun_base_expr)
  // 30: PREFIX(try_base_expr)
  // 31: PREFIX(let_base_expr)
  // 32: PREFIX(let_exception_base_expr)
  // 33: PREFIX(let_module_base_expr)
  // 34: PREFIX(expr_coercion_base_expr)
  // 35: PREFIX(expr_type_coercion_base_expr)
  // 36: PREFIX(assert_base_expr)
  // 37: PREFIX(lazy_base_expr)
  // 38: ATOM(local-open_base_expr)
  // 39: POSTFIX(object_expr_internal_rec_base_expr)
  // 40: ATOM(object_expr_internal_base_expr)
  public static boolean expr(PsiBuilder b, int l, int g) {
    if (!recursion_guard_(b, l, "expr")) return false;
    addVariant(b, "<expr>");
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_, "<expr>");
    r = value_path_base_expr(b, l + 1);
    if (!r) r = constant_base_expr(b, l + 1);
    if (!r) r = paren_base_expr(b, l + 1);
    if (!r) r = begin_base_expr(b, l + 1);
    if (!r) r = constr_base_expr(b, l + 1);
    if (!r) r = backtick_base_expr(b, l + 1);
    if (!r) r = bracket_base_expr(b, l + 1);
    if (!r) r = larray_base_expr(b, l + 1);
    if (!r) r = brace_field_base_expr(b, l + 1);
    if (!r) r = brace_base_expr(b, l + 1);
    if (!r) r = minus_base_expr(b, l + 1);
    if (!r) r = minus_dot_base_expr(b, l + 1);
    if (!r) r = dot_paren_base_expr(b, l + 1);
    if (!r) r = dot_paren_left_arrow_base_expr(b, l + 1);
    if (!r) r = dot_bracket_base_expr(b, l + 1);
    if (!r) r = dot_bracket_left_arrow_base_expr(b, l + 1);
    if (!r) r = if_else_base_expr(b, l + 1);
    if (!r) r = while_base_expr(b, l + 1);
    if (!r) r = for_base_expr(b, l + 1);
    if (!r) r = match_base_expr(b, l + 1);
    if (!r) r = function_base_expr(b, l + 1);
    if (!r) r = fun_base_expr(b, l + 1);
    if (!r) r = try_base_expr(b, l + 1);
    if (!r) r = let_base_expr(b, l + 1);
    if (!r) r = let_exception_base_expr(b, l + 1);
    if (!r) r = let_module_base_expr(b, l + 1);
    if (!r) r = assert_base_expr(b, l + 1);
    if (!r) r = lazy_base_expr(b, l + 1);
    if (!r) r = local_open_base_expr(b, l + 1);
    if (!r) r = object_expr_internal_base_expr(b, l + 1);
    p = r;
    r = r && expr_0(b, l + 1, g);
    exit_section_(b, l, m, null, r, p, null);
    return r || p;
  }

  public static boolean expr_0(PsiBuilder b, int l, int g) {
    if (!recursion_guard_(b, l, "expr_0")) return false;
    boolean r = true;
    while (true) {
      Marker m = enter_section_(b, l, _LEFT_, null);
      if (g < 5 && consumeTokenSmart(b, COLON)) {
        while (true) {
          r = report_error_(b, expr(b, l, 5));
          if (!consumeTokenSmart(b, COLON)) break;
        }
        exit_section_(b, l, m, COLON_BASE_EXPR, r, true, null);
      }
      else if (g < 8 && consumeTokenSmart(b, SHORTCUT)) {
        r = expr(b, l, 8);
        exit_section_(b, l, m, SHORTCUT_BASE_EXPR, r, true, null);
      }
      else if (g < 13 && argument_base_expr_0(b, l + 1)) {
        r = true;
        exit_section_(b, l, m, ARGUMENT_BASE_EXPR, r, true, null);
      }
      else if (g < 16 && infix_op(b, l + 1)) {
        r = expr(b, l, 16);
        exit_section_(b, l, m, INFIX_OP_BASE_EXPR, r, true, null);
      }
      else if (g < 17 && dot_field_base_expr_0(b, l + 1)) {
        r = true;
        exit_section_(b, l, m, DOT_FIELD_BASE_EXPR, r, true, null);
      }
      else if (g < 18 && dot_field_left_arrow_base_expr_0(b, l + 1)) {
        r = expr(b, l, 18);
        exit_section_(b, l, m, DOT_FIELD_LEFT_ARROW_BASE_EXPR, r, true, null);
      }
      else if (g < 26 && consumeTokenSmart(b, SEMI)) {
        r = expr(b, l, 26);
        exit_section_(b, l, m, SEMI_BASE_EXPR, r, true, null);
      }
      else if (g < 39 && object_expr_internal_rec_base_expr_0(b, l + 1)) {
        r = true;
        exit_section_(b, l, m, OBJECT_EXPR_INTERNAL_REC_BASE_EXPR, r, true, null);
      }
      else {
        exit_section_(b, l, m, null, false, false, null);
        break;
      }
    }
    return r;
  }

  // value-path
  public static boolean value_path_base_expr(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "value_path_base_expr")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, VALUE_PATH_BASE_EXPR, "<value path base expr>");
    r = value_path(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  // constant
  public static boolean constant_base_expr(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "constant_base_expr")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, CONSTANT_BASE_EXPR, "<constant base expr>");
    r = constant(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  public static boolean paren_base_expr(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "paren_base_expr")) return false;
    if (!nextTokenIsSmart(b, LPAREN)) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_, null);
    r = consumeTokenSmart(b, LPAREN);
    p = r;
    r = p && expr(b, l, 2);
    r = p && report_error_(b, consumeToken(b, RPAREN)) && r;
    exit_section_(b, l, m, PAREN_BASE_EXPR, r, p, null);
    return r || p;
  }

  public static boolean paren_type_base_expr(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "paren_type_base_expr")) return false;
    if (!nextTokenIsSmart(b, LPAREN)) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_, null);
    r = consumeTokenSmart(b, LPAREN);
    p = r;
    r = p && expr(b, l, 4);
    r = p && report_error_(b, paren_type_base_expr_1(b, l + 1)) && r;
    exit_section_(b, l, m, PAREN_TYPE_BASE_EXPR, r, p, null);
    return r || p;
  }

  // COLON typexpr RPAREN
  private static boolean paren_type_base_expr_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "paren_type_base_expr_1")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, COLON);
    r = r && typexpr(b, l + 1, -1);
    r = r && consumeToken(b, RPAREN);
    exit_section_(b, m, null, r);
    return r;
  }

  public static boolean expr_coercion_base_expr(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "expr_coercion_base_expr")) return false;
    if (!nextTokenIsSmart(b, LPAREN)) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_, null);
    r = consumeTokenSmart(b, LPAREN);
    p = r;
    r = p && expr(b, l, 34);
    r = p && report_error_(b, expr_coercion_base_expr_1(b, l + 1)) && r;
    exit_section_(b, l, m, EXPR_COERCION_BASE_EXPR, r, p, null);
    return r || p;
  }

  // COLON_GT typexpr RPAREN
  private static boolean expr_coercion_base_expr_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "expr_coercion_base_expr_1")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, COLON_GT);
    r = r && typexpr(b, l + 1, -1);
    r = r && consumeToken(b, RPAREN);
    exit_section_(b, m, null, r);
    return r;
  }

  public static boolean expr_type_coercion_base_expr(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "expr_type_coercion_base_expr")) return false;
    if (!nextTokenIsSmart(b, LPAREN)) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_, null);
    r = consumeTokenSmart(b, LPAREN);
    p = r;
    r = p && expr(b, l, 35);
    r = p && report_error_(b, expr_type_coercion_base_expr_1(b, l + 1)) && r;
    exit_section_(b, l, m, EXPR_TYPE_COERCION_BASE_EXPR, r, p, null);
    return r || p;
  }

  // COLON typexpr COLON_GT typexpr RPAREN
  private static boolean expr_type_coercion_base_expr_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "expr_type_coercion_base_expr_1")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, COLON);
    r = r && typexpr(b, l + 1, -1);
    r = r && consumeToken(b, COLON_GT);
    r = r && typexpr(b, l + 1, -1);
    r = r && consumeToken(b, RPAREN);
    exit_section_(b, m, null, r);
    return r;
  }

  public static boolean begin_base_expr(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "begin_base_expr")) return false;
    if (!nextTokenIsSmart(b, BEGIN)) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_, null);
    r = consumeTokenSmart(b, BEGIN);
    p = r;
    r = p && expr(b, l, 3);
    r = p && report_error_(b, consumeToken(b, END)) && r;
    exit_section_(b, l, m, BEGIN_BASE_EXPR, r, p, null);
    return r || p;
  }

  public static boolean constr_base_expr(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "constr_base_expr")) return false;
    if (!nextTokenIsSmart(b, CAPITALIZED_IDENT)) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_, null);
    r = constr(b, l + 1);
    p = r;
    r = p && expr(b, l, 6);
    exit_section_(b, l, m, CONSTR_BASE_EXPR, r, p, null);
    return r || p;
  }

  public static boolean backtick_base_expr(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "backtick_base_expr")) return false;
    if (!nextTokenIsSmart(b, BACKTICK)) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_, null);
    r = backtick_base_expr_0(b, l + 1);
    p = r;
    r = p && expr(b, l, 7);
    exit_section_(b, l, m, BACKTICK_BASE_EXPR, r, p, null);
    return r || p;
  }

  // BACKTICK tag-name
  private static boolean backtick_base_expr_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "backtick_base_expr_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeTokenSmart(b, BACKTICK);
    r = r && tag_name(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // LBRACKET expr { SEMI expr }* [SEMI] RBRACKET
  public static boolean bracket_base_expr(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "bracket_base_expr")) return false;
    if (!nextTokenIsSmart(b, LBRACKET)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeTokenSmart(b, LBRACKET);
    r = r && expr(b, l + 1, -1);
    r = r && bracket_base_expr_2(b, l + 1);
    r = r && bracket_base_expr_3(b, l + 1);
    r = r && consumeToken(b, RBRACKET);
    exit_section_(b, m, BRACKET_BASE_EXPR, r);
    return r;
  }

  // { SEMI expr }*
  private static boolean bracket_base_expr_2(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "bracket_base_expr_2")) return false;
    while (true) {
      int c = current_position_(b);
      if (!bracket_base_expr_2_0(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "bracket_base_expr_2", c)) break;
    }
    return true;
  }

  // SEMI expr
  private static boolean bracket_base_expr_2_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "bracket_base_expr_2_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeTokenSmart(b, SEMI);
    r = r && expr(b, l + 1, -1);
    exit_section_(b, m, null, r);
    return r;
  }

  // [SEMI]
  private static boolean bracket_base_expr_3(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "bracket_base_expr_3")) return false;
    consumeTokenSmart(b, SEMI);
    return true;
  }

  // LARRAY expr { SEMI expr }* [SEMI] RARRAY
  public static boolean larray_base_expr(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "larray_base_expr")) return false;
    if (!nextTokenIsSmart(b, LARRAY)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeTokenSmart(b, LARRAY);
    r = r && expr(b, l + 1, -1);
    r = r && larray_base_expr_2(b, l + 1);
    r = r && larray_base_expr_3(b, l + 1);
    r = r && consumeToken(b, RARRAY);
    exit_section_(b, m, LARRAY_BASE_EXPR, r);
    return r;
  }

  // { SEMI expr }*
  private static boolean larray_base_expr_2(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "larray_base_expr_2")) return false;
    while (true) {
      int c = current_position_(b);
      if (!larray_base_expr_2_0(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "larray_base_expr_2", c)) break;
    }
    return true;
  }

  // SEMI expr
  private static boolean larray_base_expr_2_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "larray_base_expr_2_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeTokenSmart(b, SEMI);
    r = r && expr(b, l + 1, -1);
    exit_section_(b, m, null, r);
    return r;
  }

  // [SEMI]
  private static boolean larray_base_expr_3(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "larray_base_expr_3")) return false;
    consumeTokenSmart(b, SEMI);
    return true;
  }

  // LBRACE field [COLON typexpr] [EQ expr] { SEMI field [COLON typexpr] [EQ expr] }* [SEMI] RBRACE
  public static boolean brace_field_base_expr(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "brace_field_base_expr")) return false;
    if (!nextTokenIsSmart(b, LBRACE)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeTokenSmart(b, LBRACE);
    r = r && field(b, l + 1);
    r = r && brace_field_base_expr_2(b, l + 1);
    r = r && brace_field_base_expr_3(b, l + 1);
    r = r && brace_field_base_expr_4(b, l + 1);
    r = r && brace_field_base_expr_5(b, l + 1);
    r = r && consumeToken(b, RBRACE);
    exit_section_(b, m, BRACE_FIELD_BASE_EXPR, r);
    return r;
  }

  // [COLON typexpr]
  private static boolean brace_field_base_expr_2(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "brace_field_base_expr_2")) return false;
    brace_field_base_expr_2_0(b, l + 1);
    return true;
  }

  // COLON typexpr
  private static boolean brace_field_base_expr_2_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "brace_field_base_expr_2_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeTokenSmart(b, COLON);
    r = r && typexpr(b, l + 1, -1);
    exit_section_(b, m, null, r);
    return r;
  }

  // [EQ expr]
  private static boolean brace_field_base_expr_3(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "brace_field_base_expr_3")) return false;
    brace_field_base_expr_3_0(b, l + 1);
    return true;
  }

  // EQ expr
  private static boolean brace_field_base_expr_3_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "brace_field_base_expr_3_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeTokenSmart(b, EQ);
    r = r && expr(b, l + 1, -1);
    exit_section_(b, m, null, r);
    return r;
  }

  // { SEMI field [COLON typexpr] [EQ expr] }*
  private static boolean brace_field_base_expr_4(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "brace_field_base_expr_4")) return false;
    while (true) {
      int c = current_position_(b);
      if (!brace_field_base_expr_4_0(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "brace_field_base_expr_4", c)) break;
    }
    return true;
  }

  // SEMI field [COLON typexpr] [EQ expr]
  private static boolean brace_field_base_expr_4_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "brace_field_base_expr_4_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeTokenSmart(b, SEMI);
    r = r && field(b, l + 1);
    r = r && brace_field_base_expr_4_0_2(b, l + 1);
    r = r && brace_field_base_expr_4_0_3(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // [COLON typexpr]
  private static boolean brace_field_base_expr_4_0_2(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "brace_field_base_expr_4_0_2")) return false;
    brace_field_base_expr_4_0_2_0(b, l + 1);
    return true;
  }

  // COLON typexpr
  private static boolean brace_field_base_expr_4_0_2_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "brace_field_base_expr_4_0_2_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeTokenSmart(b, COLON);
    r = r && typexpr(b, l + 1, -1);
    exit_section_(b, m, null, r);
    return r;
  }

  // [EQ expr]
  private static boolean brace_field_base_expr_4_0_3(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "brace_field_base_expr_4_0_3")) return false;
    brace_field_base_expr_4_0_3_0(b, l + 1);
    return true;
  }

  // EQ expr
  private static boolean brace_field_base_expr_4_0_3_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "brace_field_base_expr_4_0_3_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeTokenSmart(b, EQ);
    r = r && expr(b, l + 1, -1);
    exit_section_(b, m, null, r);
    return r;
  }

  // [SEMI]
  private static boolean brace_field_base_expr_5(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "brace_field_base_expr_5")) return false;
    consumeTokenSmart(b, SEMI);
    return true;
  }

  // LBRACE expr WITH field [ COLON typexpr] [EQ expr] { SEMI field [COLON typexpr] [EQ expr]}* [SEMI] RBRACE
  public static boolean brace_base_expr(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "brace_base_expr")) return false;
    if (!nextTokenIsSmart(b, LBRACE)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeTokenSmart(b, LBRACE);
    r = r && expr(b, l + 1, -1);
    r = r && consumeToken(b, WITH);
    r = r && field(b, l + 1);
    r = r && brace_base_expr_4(b, l + 1);
    r = r && brace_base_expr_5(b, l + 1);
    r = r && brace_base_expr_6(b, l + 1);
    r = r && brace_base_expr_7(b, l + 1);
    r = r && consumeToken(b, RBRACE);
    exit_section_(b, m, BRACE_BASE_EXPR, r);
    return r;
  }

  // [ COLON typexpr]
  private static boolean brace_base_expr_4(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "brace_base_expr_4")) return false;
    brace_base_expr_4_0(b, l + 1);
    return true;
  }

  // COLON typexpr
  private static boolean brace_base_expr_4_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "brace_base_expr_4_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeTokenSmart(b, COLON);
    r = r && typexpr(b, l + 1, -1);
    exit_section_(b, m, null, r);
    return r;
  }

  // [EQ expr]
  private static boolean brace_base_expr_5(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "brace_base_expr_5")) return false;
    brace_base_expr_5_0(b, l + 1);
    return true;
  }

  // EQ expr
  private static boolean brace_base_expr_5_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "brace_base_expr_5_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeTokenSmart(b, EQ);
    r = r && expr(b, l + 1, -1);
    exit_section_(b, m, null, r);
    return r;
  }

  // { SEMI field [COLON typexpr] [EQ expr]}*
  private static boolean brace_base_expr_6(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "brace_base_expr_6")) return false;
    while (true) {
      int c = current_position_(b);
      if (!brace_base_expr_6_0(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "brace_base_expr_6", c)) break;
    }
    return true;
  }

  // SEMI field [COLON typexpr] [EQ expr]
  private static boolean brace_base_expr_6_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "brace_base_expr_6_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeTokenSmart(b, SEMI);
    r = r && field(b, l + 1);
    r = r && brace_base_expr_6_0_2(b, l + 1);
    r = r && brace_base_expr_6_0_3(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // [COLON typexpr]
  private static boolean brace_base_expr_6_0_2(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "brace_base_expr_6_0_2")) return false;
    brace_base_expr_6_0_2_0(b, l + 1);
    return true;
  }

  // COLON typexpr
  private static boolean brace_base_expr_6_0_2_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "brace_base_expr_6_0_2_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeTokenSmart(b, COLON);
    r = r && typexpr(b, l + 1, -1);
    exit_section_(b, m, null, r);
    return r;
  }

  // [EQ expr]
  private static boolean brace_base_expr_6_0_3(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "brace_base_expr_6_0_3")) return false;
    brace_base_expr_6_0_3_0(b, l + 1);
    return true;
  }

  // EQ expr
  private static boolean brace_base_expr_6_0_3_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "brace_base_expr_6_0_3_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeTokenSmart(b, EQ);
    r = r && expr(b, l + 1, -1);
    exit_section_(b, m, null, r);
    return r;
  }

  // [SEMI]
  private static boolean brace_base_expr_7(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "brace_base_expr_7")) return false;
    consumeTokenSmart(b, SEMI);
    return true;
  }

  // { argument }+
  private static boolean argument_base_expr_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "argument_base_expr_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = argument_base_expr_0_0(b, l + 1);
    while (r) {
      int c = current_position_(b);
      if (!argument_base_expr_0_0(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "argument_base_expr_0", c)) break;
    }
    exit_section_(b, m, null, r);
    return r;
  }

  // { argument }
  private static boolean argument_base_expr_0_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "argument_base_expr_0_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = argument(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  public static boolean minus_base_expr(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "minus_base_expr")) return false;
    if (!nextTokenIsSmart(b, MINUS)) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_, null);
    r = consumeTokenSmart(b, MINUS);
    p = r;
    r = p && expr(b, l, 14);
    exit_section_(b, l, m, MINUS_BASE_EXPR, r, p, null);
    return r || p;
  }

  public static boolean minus_dot_base_expr(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "minus_dot_base_expr")) return false;
    if (!nextTokenIsSmart(b, MINUSDOT)) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_, null);
    r = consumeTokenSmart(b, MINUSDOT);
    p = r;
    r = p && expr(b, l, 15);
    exit_section_(b, l, m, MINUS_DOT_BASE_EXPR, r, p, null);
    return r || p;
  }

  // DOT field
  private static boolean dot_field_base_expr_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "dot_field_base_expr_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeTokenSmart(b, DOT);
    r = r && field(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // DOT field LEFT_ARROW
  private static boolean dot_field_left_arrow_base_expr_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "dot_field_left_arrow_base_expr_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeTokenSmart(b, DOT);
    r = r && field(b, l + 1);
    r = r && consumeToken(b, LEFT_ARROW);
    exit_section_(b, m, null, r);
    return r;
  }

  public static boolean dot_paren_base_expr(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "dot_paren_base_expr")) return false;
    if (!nextTokenIsSmart(b, DOT)) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_, null);
    r = parseTokensSmart(b, 0, DOT, LPAREN);
    p = r;
    r = p && expr(b, l, 19);
    r = p && report_error_(b, consumeToken(b, RPAREN)) && r;
    exit_section_(b, l, m, DOT_PAREN_BASE_EXPR, r, p, null);
    return r || p;
  }

  // DOT LPAREN expr RPAREN LEFT_ARROW expr
  public static boolean dot_paren_left_arrow_base_expr(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "dot_paren_left_arrow_base_expr")) return false;
    if (!nextTokenIsSmart(b, DOT)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeTokensSmart(b, 0, DOT, LPAREN);
    r = r && expr(b, l + 1, -1);
    r = r && consumeTokensSmart(b, 0, RPAREN, LEFT_ARROW);
    r = r && expr(b, l + 1, -1);
    exit_section_(b, m, DOT_PAREN_LEFT_ARROW_BASE_EXPR, r);
    return r;
  }

  public static boolean dot_bracket_base_expr(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "dot_bracket_base_expr")) return false;
    if (!nextTokenIsSmart(b, DOT)) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_, null);
    r = parseTokensSmart(b, 0, DOT, LBRACKET);
    p = r;
    r = p && expr(b, l, 21);
    r = p && report_error_(b, consumeToken(b, RBRACKET)) && r;
    exit_section_(b, l, m, DOT_BRACKET_BASE_EXPR, r, p, null);
    return r || p;
  }

  // DOT LBRACKET expr RBRACKET LEFT_ARROW expr
  public static boolean dot_bracket_left_arrow_base_expr(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "dot_bracket_left_arrow_base_expr")) return false;
    if (!nextTokenIsSmart(b, DOT)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeTokensSmart(b, 0, DOT, LBRACKET);
    r = r && expr(b, l + 1, -1);
    r = r && consumeTokensSmart(b, 0, RBRACKET, LEFT_ARROW);
    r = r && expr(b, l + 1, -1);
    exit_section_(b, m, DOT_BRACKET_LEFT_ARROW_BASE_EXPR, r);
    return r;
  }

  // IF expr THEN expr [ ELSE expr ]
  public static boolean if_else_base_expr(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "if_else_base_expr")) return false;
    if (!nextTokenIsSmart(b, IF)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeTokenSmart(b, IF);
    r = r && expr(b, l + 1, -1);
    r = r && consumeToken(b, THEN);
    r = r && expr(b, l + 1, -1);
    r = r && if_else_base_expr_4(b, l + 1);
    exit_section_(b, m, IF_ELSE_BASE_EXPR, r);
    return r;
  }

  // [ ELSE expr ]
  private static boolean if_else_base_expr_4(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "if_else_base_expr_4")) return false;
    if_else_base_expr_4_0(b, l + 1);
    return true;
  }

  // ELSE expr
  private static boolean if_else_base_expr_4_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "if_else_base_expr_4_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeTokenSmart(b, ELSE);
    r = r && expr(b, l + 1, -1);
    exit_section_(b, m, null, r);
    return r;
  }

  // WHILE expr DO expr DONE
  public static boolean while_base_expr(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "while_base_expr")) return false;
    if (!nextTokenIsSmart(b, WHILE)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeTokenSmart(b, WHILE);
    r = r && expr(b, l + 1, -1);
    r = r && consumeToken(b, DO);
    r = r && expr(b, l + 1, -1);
    r = r && consumeToken(b, DONE);
    exit_section_(b, m, WHILE_BASE_EXPR, r);
    return r;
  }

  // FOR value-name EQ expr ( TO | DOWNTO ) expr DO expr DONE
  public static boolean for_base_expr(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "for_base_expr")) return false;
    if (!nextTokenIsSmart(b, FOR)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeTokenSmart(b, FOR);
    r = r && value_name(b, l + 1);
    r = r && consumeToken(b, EQ);
    r = r && expr(b, l + 1, -1);
    r = r && for_base_expr_4(b, l + 1);
    r = r && expr(b, l + 1, -1);
    r = r && consumeToken(b, DO);
    r = r && expr(b, l + 1, -1);
    r = r && consumeToken(b, DONE);
    exit_section_(b, m, FOR_BASE_EXPR, r);
    return r;
  }

  // TO | DOWNTO
  private static boolean for_base_expr_4(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "for_base_expr_4")) return false;
    boolean r;
    r = consumeTokenSmart(b, TO);
    if (!r) r = consumeTokenSmart(b, DOWNTO);
    return r;
  }

  public static boolean match_base_expr(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "match_base_expr")) return false;
    if (!nextTokenIsSmart(b, MATCH)) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_, null);
    r = consumeTokenSmart(b, MATCH);
    p = r;
    r = p && expr(b, l, 27);
    r = p && report_error_(b, match_base_expr_1(b, l + 1)) && r;
    exit_section_(b, l, m, MATCH_BASE_EXPR, r, p, null);
    return r || p;
  }

  // WITH pattern-matching
  private static boolean match_base_expr_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "match_base_expr_1")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, WITH);
    r = r && pattern_matching(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // FUNCTION pattern-matching
  public static boolean function_base_expr(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "function_base_expr")) return false;
    if (!nextTokenIsSmart(b, FUNCTION)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeTokenSmart(b, FUNCTION);
    r = r && pattern_matching(b, l + 1);
    exit_section_(b, m, FUNCTION_BASE_EXPR, r);
    return r;
  }

  public static boolean fun_base_expr(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "fun_base_expr")) return false;
    if (!nextTokenIsSmart(b, FUN)) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_, null);
    r = fun_base_expr_0(b, l + 1);
    p = r;
    r = p && expr(b, l, 29);
    exit_section_(b, l, m, FUN_BASE_EXPR, r, p, null);
    return r || p;
  }

  // FUN { parameter }+ [ COLON typexpr ] RIGHT_ARROW
  private static boolean fun_base_expr_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "fun_base_expr_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeTokenSmart(b, FUN);
    r = r && fun_base_expr_0_1(b, l + 1);
    r = r && fun_base_expr_0_2(b, l + 1);
    r = r && consumeToken(b, RIGHT_ARROW);
    exit_section_(b, m, null, r);
    return r;
  }

  // { parameter }+
  private static boolean fun_base_expr_0_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "fun_base_expr_0_1")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = fun_base_expr_0_1_0(b, l + 1);
    while (r) {
      int c = current_position_(b);
      if (!fun_base_expr_0_1_0(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "fun_base_expr_0_1", c)) break;
    }
    exit_section_(b, m, null, r);
    return r;
  }

  // { parameter }
  private static boolean fun_base_expr_0_1_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "fun_base_expr_0_1_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = parameter(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // [ COLON typexpr ]
  private static boolean fun_base_expr_0_2(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "fun_base_expr_0_2")) return false;
    fun_base_expr_0_2_0(b, l + 1);
    return true;
  }

  // COLON typexpr
  private static boolean fun_base_expr_0_2_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "fun_base_expr_0_2_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeTokenSmart(b, COLON);
    r = r && typexpr(b, l + 1, -1);
    exit_section_(b, m, null, r);
    return r;
  }

  public static boolean try_base_expr(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "try_base_expr")) return false;
    if (!nextTokenIsSmart(b, TRY)) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_, null);
    r = consumeTokenSmart(b, TRY);
    p = r;
    r = p && expr(b, l, 30);
    r = p && report_error_(b, try_base_expr_1(b, l + 1)) && r;
    exit_section_(b, l, m, TRY_BASE_EXPR, r, p, null);
    return r || p;
  }

  // WITH pattern-matching
  private static boolean try_base_expr_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "try_base_expr_1")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, WITH);
    r = r && pattern_matching(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  public static boolean let_base_expr(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "let_base_expr")) return false;
    if (!nextTokenIsSmart(b, LET)) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_, null);
    r = let_base_expr_0(b, l + 1);
    p = r;
    r = p && expr(b, l, 31);
    exit_section_(b, l, m, LET_BASE_EXPR, r, p, null);
    return r || p;
  }

  // LET [REC] let-binding { AND let-binding }* IN
  private static boolean let_base_expr_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "let_base_expr_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeTokenSmart(b, LET);
    r = r && let_base_expr_0_1(b, l + 1);
    r = r && let_binding(b, l + 1);
    r = r && let_base_expr_0_3(b, l + 1);
    r = r && consumeToken(b, IN);
    exit_section_(b, m, null, r);
    return r;
  }

  // [REC]
  private static boolean let_base_expr_0_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "let_base_expr_0_1")) return false;
    consumeTokenSmart(b, REC);
    return true;
  }

  // { AND let-binding }*
  private static boolean let_base_expr_0_3(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "let_base_expr_0_3")) return false;
    while (true) {
      int c = current_position_(b);
      if (!let_base_expr_0_3_0(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "let_base_expr_0_3", c)) break;
    }
    return true;
  }

  // AND let-binding
  private static boolean let_base_expr_0_3_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "let_base_expr_0_3_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeTokenSmart(b, AND);
    r = r && let_binding(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  public static boolean let_exception_base_expr(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "let_exception_base_expr")) return false;
    if (!nextTokenIsSmart(b, LET)) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_, null);
    r = let_exception_base_expr_0(b, l + 1);
    p = r;
    r = p && expr(b, l, 32);
    exit_section_(b, l, m, LET_EXCEPTION_BASE_EXPR, r, p, null);
    return r || p;
  }

  // LET EXCEPTION constr-decl IN
  private static boolean let_exception_base_expr_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "let_exception_base_expr_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeTokensSmart(b, 0, LET, EXCEPTION);
    r = r && constr_decl(b, l + 1);
    r = r && consumeToken(b, IN);
    exit_section_(b, m, null, r);
    return r;
  }

  public static boolean let_module_base_expr(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "let_module_base_expr")) return false;
    if (!nextTokenIsSmart(b, LET)) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_, null);
    r = let_module_base_expr_0(b, l + 1);
    p = r;
    r = p && expr(b, l, 33);
    exit_section_(b, l, m, LET_MODULE_BASE_EXPR, r, p, null);
    return r || p;
  }

  // LET MODULE module-name { ( module-name COLON module_type ) }* [COLON module_type ] EQ module_expr IN
  private static boolean let_module_base_expr_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "let_module_base_expr_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeTokensSmart(b, 0, LET, MODULE);
    r = r && module_name(b, l + 1);
    r = r && let_module_base_expr_0_3(b, l + 1);
    r = r && let_module_base_expr_0_4(b, l + 1);
    r = r && consumeToken(b, EQ);
    r = r && module_expr(b, l + 1, -1);
    r = r && consumeToken(b, IN);
    exit_section_(b, m, null, r);
    return r;
  }

  // { ( module-name COLON module_type ) }*
  private static boolean let_module_base_expr_0_3(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "let_module_base_expr_0_3")) return false;
    while (true) {
      int c = current_position_(b);
      if (!let_module_base_expr_0_3_0(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "let_module_base_expr_0_3", c)) break;
    }
    return true;
  }

  // module-name COLON module_type
  private static boolean let_module_base_expr_0_3_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "let_module_base_expr_0_3_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = module_name(b, l + 1);
    r = r && consumeToken(b, COLON);
    r = r && module_type(b, l + 1, -1);
    exit_section_(b, m, null, r);
    return r;
  }

  // [COLON module_type ]
  private static boolean let_module_base_expr_0_4(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "let_module_base_expr_0_4")) return false;
    let_module_base_expr_0_4_0(b, l + 1);
    return true;
  }

  // COLON module_type
  private static boolean let_module_base_expr_0_4_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "let_module_base_expr_0_4_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeTokenSmart(b, COLON);
    r = r && module_type(b, l + 1, -1);
    exit_section_(b, m, null, r);
    return r;
  }

  public static boolean assert_base_expr(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "assert_base_expr")) return false;
    if (!nextTokenIsSmart(b, ASSERT)) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_, null);
    r = consumeTokenSmart(b, ASSERT);
    p = r;
    r = p && expr(b, l, 36);
    exit_section_(b, l, m, ASSERT_BASE_EXPR, r, p, null);
    return r || p;
  }

  public static boolean lazy_base_expr(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "lazy_base_expr")) return false;
    if (!nextTokenIsSmart(b, LAZY)) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_, null);
    r = consumeTokenSmart(b, LAZY);
    p = r;
    r = p && expr(b, l, 37);
    exit_section_(b, l, m, LAZY_BASE_EXPR, r, p, null);
    return r || p;
  }

  // local-open
  public static boolean local_open_base_expr(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "local_open_base_expr")) return false;
    if (!nextTokenIsSmart(b, CAPITALIZED_IDENT, LET)) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, LOCAL_OPEN_BASE_EXPR, "<local open base expr>");
    r = local_open(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  // SHARP method-name
  private static boolean object_expr_internal_rec_base_expr_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "object_expr_internal_rec_base_expr_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeTokenSmart(b, SHARP);
    r = r && method_name(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // NEW class-path
  //   | OBJECT class-body END
  //   | inst-var-name
  //   | inst-var-name LEFT_ARROW expr
  //   | LBRACELESS [ inst-var-name [EQ expr] { SEMI inst-var-name [EQ expr] } [SEMI] ] GREATERRBRACE
  public static boolean object_expr_internal_base_expr(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "object_expr_internal_base_expr")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, OBJECT_EXPR_INTERNAL_BASE_EXPR, "<object expr internal base expr>");
    r = object_expr_internal_base_expr_0(b, l + 1);
    if (!r) r = object_expr_internal_base_expr_1(b, l + 1);
    if (!r) r = inst_var_name(b, l + 1);
    if (!r) r = object_expr_internal_base_expr_3(b, l + 1);
    if (!r) r = object_expr_internal_base_expr_4(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  // NEW class-path
  private static boolean object_expr_internal_base_expr_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "object_expr_internal_base_expr_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeTokenSmart(b, NEW);
    r = r && class_path(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // OBJECT class-body END
  private static boolean object_expr_internal_base_expr_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "object_expr_internal_base_expr_1")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeTokenSmart(b, OBJECT);
    r = r && class_body(b, l + 1);
    r = r && consumeToken(b, END);
    exit_section_(b, m, null, r);
    return r;
  }

  // inst-var-name LEFT_ARROW expr
  private static boolean object_expr_internal_base_expr_3(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "object_expr_internal_base_expr_3")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = inst_var_name(b, l + 1);
    r = r && consumeToken(b, LEFT_ARROW);
    r = r && expr(b, l + 1, -1);
    exit_section_(b, m, null, r);
    return r;
  }

  // LBRACELESS [ inst-var-name [EQ expr] { SEMI inst-var-name [EQ expr] } [SEMI] ] GREATERRBRACE
  private static boolean object_expr_internal_base_expr_4(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "object_expr_internal_base_expr_4")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeTokenSmart(b, LBRACELESS);
    r = r && object_expr_internal_base_expr_4_1(b, l + 1);
    r = r && consumeToken(b, GREATERRBRACE);
    exit_section_(b, m, null, r);
    return r;
  }

  // [ inst-var-name [EQ expr] { SEMI inst-var-name [EQ expr] } [SEMI] ]
  private static boolean object_expr_internal_base_expr_4_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "object_expr_internal_base_expr_4_1")) return false;
    object_expr_internal_base_expr_4_1_0(b, l + 1);
    return true;
  }

  // inst-var-name [EQ expr] { SEMI inst-var-name [EQ expr] } [SEMI]
  private static boolean object_expr_internal_base_expr_4_1_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "object_expr_internal_base_expr_4_1_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = inst_var_name(b, l + 1);
    r = r && object_expr_internal_base_expr_4_1_0_1(b, l + 1);
    r = r && object_expr_internal_base_expr_4_1_0_2(b, l + 1);
    r = r && object_expr_internal_base_expr_4_1_0_3(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // [EQ expr]
  private static boolean object_expr_internal_base_expr_4_1_0_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "object_expr_internal_base_expr_4_1_0_1")) return false;
    object_expr_internal_base_expr_4_1_0_1_0(b, l + 1);
    return true;
  }

  // EQ expr
  private static boolean object_expr_internal_base_expr_4_1_0_1_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "object_expr_internal_base_expr_4_1_0_1_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeTokenSmart(b, EQ);
    r = r && expr(b, l + 1, -1);
    exit_section_(b, m, null, r);
    return r;
  }

  // SEMI inst-var-name [EQ expr]
  private static boolean object_expr_internal_base_expr_4_1_0_2(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "object_expr_internal_base_expr_4_1_0_2")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeTokenSmart(b, SEMI);
    r = r && inst_var_name(b, l + 1);
    r = r && object_expr_internal_base_expr_4_1_0_2_2(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // [EQ expr]
  private static boolean object_expr_internal_base_expr_4_1_0_2_2(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "object_expr_internal_base_expr_4_1_0_2_2")) return false;
    object_expr_internal_base_expr_4_1_0_2_2_0(b, l + 1);
    return true;
  }

  // EQ expr
  private static boolean object_expr_internal_base_expr_4_1_0_2_2_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "object_expr_internal_base_expr_4_1_0_2_2_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeTokenSmart(b, EQ);
    r = r && expr(b, l + 1, -1);
    exit_section_(b, m, null, r);
    return r;
  }

  // [SEMI]
  private static boolean object_expr_internal_base_expr_4_1_0_3(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "object_expr_internal_base_expr_4_1_0_3")) return false;
    consumeTokenSmart(b, SEMI);
    return true;
  }

  /* ********************************************************** */
  // Expression root: module_expr
  // Operator priority table:
  // 0: ATOM(modpath_module_expr)
  // 1: ATOM(struct_module_expr)
  // 2: PREFIX(functor_module_expr)
  // 3: BINARY(expr_module_expr)
  // 4: PREFIX(paren_module_expr)
  // 5: PREFIX(paren_type_module_expr)
  public static boolean module_expr(PsiBuilder b, int l, int g) {
    if (!recursion_guard_(b, l, "module_expr")) return false;
    addVariant(b, "<module expr>");
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_, "<module expr>");
    r = modpath_module_expr(b, l + 1);
    if (!r) r = struct_module_expr(b, l + 1);
    if (!r) r = functor_module_expr(b, l + 1);
    if (!r) r = paren_module_expr(b, l + 1);
    p = r;
    r = r && module_expr_0(b, l + 1, g);
    exit_section_(b, l, m, null, r, p, null);
    return r || p;
  }

  public static boolean module_expr_0(PsiBuilder b, int l, int g) {
    if (!recursion_guard_(b, l, "module_expr_0")) return false;
    boolean r = true;
    while (true) {
      Marker m = enter_section_(b, l, _LEFT_, null);
      if (g < 3 && consumeTokenSmart(b, LPAREN)) {
        r = report_error_(b, module_expr(b, l, 3));
        r = consumeToken(b, RPAREN) && r;
        exit_section_(b, l, m, EXPR_MODULE_EXPR, r, true, null);
      }
      else {
        exit_section_(b, l, m, null, false, false, null);
        break;
      }
    }
    return r;
  }

  // module-path
  public static boolean modpath_module_expr(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "modpath_module_expr")) return false;
    if (!nextTokenIsSmart(b, CAPITALIZED_IDENT)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = module_path(b, l + 1);
    exit_section_(b, m, MODPATH_MODULE_EXPR, r);
    return r;
  }

  // STRUCT [ module-items ] END
  public static boolean struct_module_expr(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "struct_module_expr")) return false;
    if (!nextTokenIsSmart(b, STRUCT)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeTokenSmart(b, STRUCT);
    r = r && struct_module_expr_1(b, l + 1);
    r = r && consumeToken(b, END);
    exit_section_(b, m, STRUCT_MODULE_EXPR, r);
    return r;
  }

  // [ module-items ]
  private static boolean struct_module_expr_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "struct_module_expr_1")) return false;
    module_items(b, l + 1);
    return true;
  }

  public static boolean functor_module_expr(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "functor_module_expr")) return false;
    if (!nextTokenIsSmart(b, FUNCTOR)) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_, null);
    r = functor_module_expr_0(b, l + 1);
    p = r;
    r = p && module_expr(b, l, 2);
    exit_section_(b, l, m, FUNCTOR_MODULE_EXPR, r, p, null);
    return r || p;
  }

  // FUNCTOR ( module-name COLON module_type ) RIGHT_ARROW
  private static boolean functor_module_expr_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "functor_module_expr_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeTokenSmart(b, FUNCTOR);
    r = r && functor_module_expr_0_1(b, l + 1);
    r = r && consumeToken(b, RIGHT_ARROW);
    exit_section_(b, m, null, r);
    return r;
  }

  // module-name COLON module_type
  private static boolean functor_module_expr_0_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "functor_module_expr_0_1")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = module_name(b, l + 1);
    r = r && consumeToken(b, COLON);
    r = r && module_type(b, l + 1, -1);
    exit_section_(b, m, null, r);
    return r;
  }

  public static boolean paren_module_expr(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "paren_module_expr")) return false;
    if (!nextTokenIsSmart(b, LPAREN)) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_, null);
    r = consumeTokenSmart(b, LPAREN);
    p = r;
    r = p && module_expr(b, l, 4);
    r = p && report_error_(b, consumeToken(b, RPAREN)) && r;
    exit_section_(b, l, m, PAREN_MODULE_EXPR, r, p, null);
    return r || p;
  }

  public static boolean paren_type_module_expr(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "paren_type_module_expr")) return false;
    if (!nextTokenIsSmart(b, LPAREN)) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_, null);
    r = consumeTokenSmart(b, LPAREN);
    p = r;
    r = p && module_expr(b, l, -1);
    r = p && report_error_(b, paren_type_module_expr_1(b, l + 1)) && r;
    exit_section_(b, l, m, PAREN_TYPE_MODULE_EXPR, r, p, null);
    return r || p;
  }

  // COLON module_type RPAREN
  private static boolean paren_type_module_expr_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "paren_type_module_expr_1")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, COLON);
    r = r && module_type(b, l + 1, -1);
    r = r && consumeToken(b, RPAREN);
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // Expression root: module_type
  // Operator priority table:
  // 0: ATOM(modtype_module_type)
  // 1: ATOM(sig_module_type)
  // 2: ATOM(functor_module_type)
  // 3: BINARY(arrow_module_type)
  // 4: POSTFIX(with_module_type)
  // 5: PREFIX(paren_module_type)
  public static boolean module_type(PsiBuilder b, int l, int g) {
    if (!recursion_guard_(b, l, "module_type")) return false;
    addVariant(b, "<module type>");
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_, "<module type>");
    r = modtype_module_type(b, l + 1);
    if (!r) r = sig_module_type(b, l + 1);
    if (!r) r = functor_module_type(b, l + 1);
    if (!r) r = paren_module_type(b, l + 1);
    p = r;
    r = r && module_type_0(b, l + 1, g);
    exit_section_(b, l, m, null, r, p, null);
    return r || p;
  }

  public static boolean module_type_0(PsiBuilder b, int l, int g) {
    if (!recursion_guard_(b, l, "module_type_0")) return false;
    boolean r = true;
    while (true) {
      Marker m = enter_section_(b, l, _LEFT_, null);
      if (g < 3 && consumeTokenSmart(b, RIGHT_ARROW)) {
        r = module_type(b, l, 3);
        exit_section_(b, l, m, ARROW_MODULE_TYPE, r, true, null);
      }
      else if (g < 4 && with_module_type_0(b, l + 1)) {
        r = true;
        exit_section_(b, l, m, WITH_MODULE_TYPE, r, true, null);
      }
      else {
        exit_section_(b, l, m, null, false, false, null);
        break;
      }
    }
    return r;
  }

  // modtype-path
  public static boolean modtype_module_type(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "modtype_module_type")) return false;
    if (!nextTokenIsSmart(b, CAPITALIZED_IDENT, LOWERCASE_IDENT)) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, MODTYPE_MODULE_TYPE, "<modtype module type>");
    r = modtype_path(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  // SIG { specification [SEMISEMI] }* END
  public static boolean sig_module_type(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "sig_module_type")) return false;
    if (!nextTokenIsSmart(b, SIG)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeTokenSmart(b, SIG);
    r = r && sig_module_type_1(b, l + 1);
    r = r && consumeToken(b, END);
    exit_section_(b, m, SIG_MODULE_TYPE, r);
    return r;
  }

  // { specification [SEMISEMI] }*
  private static boolean sig_module_type_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "sig_module_type_1")) return false;
    while (true) {
      int c = current_position_(b);
      if (!sig_module_type_1_0(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "sig_module_type_1", c)) break;
    }
    return true;
  }

  // specification [SEMISEMI]
  private static boolean sig_module_type_1_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "sig_module_type_1_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = specification(b, l + 1);
    r = r && sig_module_type_1_0_1(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // [SEMISEMI]
  private static boolean sig_module_type_1_0_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "sig_module_type_1_0_1")) return false;
    consumeTokenSmart(b, SEMISEMI);
    return true;
  }

  // FUNCTOR ( module-name COLON module_type ) RIGHT_ARROW module_type
  public static boolean functor_module_type(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "functor_module_type")) return false;
    if (!nextTokenIsSmart(b, FUNCTOR)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeTokenSmart(b, FUNCTOR);
    r = r && functor_module_type_1(b, l + 1);
    r = r && consumeToken(b, RIGHT_ARROW);
    r = r && module_type(b, l + 1, -1);
    exit_section_(b, m, FUNCTOR_MODULE_TYPE, r);
    return r;
  }

  // module-name COLON module_type
  private static boolean functor_module_type_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "functor_module_type_1")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = module_name(b, l + 1);
    r = r && consumeToken(b, COLON);
    r = r && module_type(b, l + 1, -1);
    exit_section_(b, m, null, r);
    return r;
  }

  // WITH mod-constraint { AND mod-constraint }*
  private static boolean with_module_type_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "with_module_type_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeTokenSmart(b, WITH);
    r = r && mod_constraint(b, l + 1);
    r = r && with_module_type_0_2(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // { AND mod-constraint }*
  private static boolean with_module_type_0_2(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "with_module_type_0_2")) return false;
    while (true) {
      int c = current_position_(b);
      if (!with_module_type_0_2_0(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "with_module_type_0_2", c)) break;
    }
    return true;
  }

  // AND mod-constraint
  private static boolean with_module_type_0_2_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "with_module_type_0_2_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeTokenSmart(b, AND);
    r = r && mod_constraint(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  public static boolean paren_module_type(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "paren_module_type")) return false;
    if (!nextTokenIsSmart(b, LPAREN)) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_, null);
    r = consumeTokenSmart(b, LPAREN);
    p = r;
    r = p && module_type(b, l, -1);
    r = p && report_error_(b, consumeToken(b, RPAREN)) && r;
    exit_section_(b, l, m, PAREN_MODULE_TYPE, r, p, null);
    return r || p;
  }

  /* ********************************************************** */
  // Expression root: pattern
  // Operator priority table:
  // 0: ATOM(value_pattern)
  // 1: ATOM(underscore_pattern)
  // 2: ATOM(constant_pattern)
  // 3: POSTFIX(as_pattern)
  // 4: PREFIX(brace_pattern)
  // 5: PREFIX(brace_type_pattern)
  // 6: BINARY(pattern_pipe_pattern)
  // 7: PREFIX(constr_pattern)
  // 8: PREFIX(backtick_pattern)
  // 9: ATOM(sharp_pattern)
  // 10: N_ARY(comma_pattern)
  // 11: ATOM(brace_field_pattern)
  // 12: ATOM(bracket_pattern)
  // 13: BINARY(shortcut_pattern)
  // 14: ATOM(larray_pattern)
  // 15: ATOM(char-literal_pattern)
  // 16: PREFIX(lazy_pattern)
  // 17: PREFIX(exception_pattern)
  // 18: PREFIX(module_path_paren_pattern)
  // 19: PREFIX(module_path_bracket_pattern)
  // 20: PREFIX(module_path_array_pattern)
  // 21: PREFIX(module_path_brace_pattern)
  public static boolean pattern(PsiBuilder b, int l, int g) {
    if (!recursion_guard_(b, l, "pattern")) return false;
    addVariant(b, "<pattern>");
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_, "<pattern>");
    r = value_pattern(b, l + 1);
    if (!r) r = underscore_pattern(b, l + 1);
    if (!r) r = constant_pattern(b, l + 1);
    if (!r) r = brace_pattern(b, l + 1);
    if (!r) r = constr_pattern(b, l + 1);
    if (!r) r = backtick_pattern(b, l + 1);
    if (!r) r = sharp_pattern(b, l + 1);
    if (!r) r = brace_field_pattern(b, l + 1);
    if (!r) r = bracket_pattern(b, l + 1);
    if (!r) r = larray_pattern(b, l + 1);
    if (!r) r = char_literal_pattern(b, l + 1);
    if (!r) r = lazy_pattern(b, l + 1);
    if (!r) r = exception_pattern(b, l + 1);
    if (!r) r = module_path_paren_pattern(b, l + 1);
    if (!r) r = module_path_bracket_pattern(b, l + 1);
    if (!r) r = module_path_array_pattern(b, l + 1);
    if (!r) r = module_path_brace_pattern(b, l + 1);
    p = r;
    r = r && pattern_0(b, l + 1, g);
    exit_section_(b, l, m, null, r, p, null);
    return r || p;
  }

  public static boolean pattern_0(PsiBuilder b, int l, int g) {
    if (!recursion_guard_(b, l, "pattern_0")) return false;
    boolean r = true;
    while (true) {
      Marker m = enter_section_(b, l, _LEFT_, null);
      if (g < 3 && as_pattern_0(b, l + 1)) {
        r = true;
        exit_section_(b, l, m, AS_PATTERN, r, true, null);
      }
      else if (g < 6 && consumeTokenSmart(b, PIPE)) {
        r = pattern(b, l, 6);
        exit_section_(b, l, m, PATTERN_PIPE_PATTERN, r, true, null);
      }
      else if (g < 10 && consumeTokenSmart(b, COMMA)) {
        while (true) {
          r = report_error_(b, pattern(b, l, 10));
          if (!consumeTokenSmart(b, COMMA)) break;
        }
        exit_section_(b, l, m, COMMA_PATTERN, r, true, null);
      }
      else if (g < 13 && consumeTokenSmart(b, SHORTCUT)) {
        r = pattern(b, l, 13);
        exit_section_(b, l, m, SHORTCUT_PATTERN, r, true, null);
      }
      else {
        exit_section_(b, l, m, null, false, false, null);
        break;
      }
    }
    return r;
  }

  // value-name
  public static boolean value_pattern(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "value_pattern")) return false;
    if (!nextTokenIsSmart(b, LOWERCASE_IDENT, LPAREN)) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, VALUE_PATTERN, "<value pattern>");
    r = value_name(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  // UNDERSCORE
  public static boolean underscore_pattern(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "underscore_pattern")) return false;
    if (!nextTokenIsSmart(b, UNDERSCORE)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeTokenSmart(b, UNDERSCORE);
    exit_section_(b, m, UNDERSCORE_PATTERN, r);
    return r;
  }

  // constant
  public static boolean constant_pattern(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "constant_pattern")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, CONSTANT_PATTERN, "<constant pattern>");
    r = constant(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  // as value-name
  private static boolean as_pattern_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "as_pattern_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeTokenSmart(b, AS);
    r = r && value_name(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  public static boolean brace_pattern(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "brace_pattern")) return false;
    if (!nextTokenIsSmart(b, LPAREN)) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_, null);
    r = consumeTokenSmart(b, LPAREN);
    p = r;
    r = p && pattern(b, l, 4);
    r = p && report_error_(b, consumeToken(b, RPAREN)) && r;
    exit_section_(b, l, m, BRACE_PATTERN, r, p, null);
    return r || p;
  }

  public static boolean brace_type_pattern(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "brace_type_pattern")) return false;
    if (!nextTokenIsSmart(b, LPAREN)) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_, null);
    r = consumeTokenSmart(b, LPAREN);
    p = r;
    r = p && pattern(b, l, 5);
    r = p && report_error_(b, brace_type_pattern_1(b, l + 1)) && r;
    exit_section_(b, l, m, BRACE_TYPE_PATTERN, r, p, null);
    return r || p;
  }

  // COLON typexpr RPAREN
  private static boolean brace_type_pattern_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "brace_type_pattern_1")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, COLON);
    r = r && typexpr(b, l + 1, -1);
    r = r && consumeToken(b, RPAREN);
    exit_section_(b, m, null, r);
    return r;
  }

  public static boolean constr_pattern(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "constr_pattern")) return false;
    if (!nextTokenIsSmart(b, CAPITALIZED_IDENT)) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_, null);
    r = constr(b, l + 1);
    p = r;
    r = p && pattern(b, l, 7);
    exit_section_(b, l, m, CONSTR_PATTERN, r, p, null);
    return r || p;
  }

  public static boolean backtick_pattern(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "backtick_pattern")) return false;
    if (!nextTokenIsSmart(b, BACKTICK)) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_, null);
    r = backtick_pattern_0(b, l + 1);
    p = r;
    r = p && pattern(b, l, 8);
    exit_section_(b, l, m, BACKTICK_PATTERN, r, p, null);
    return r || p;
  }

  // BACKTICK tag-name
  private static boolean backtick_pattern_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "backtick_pattern_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeTokenSmart(b, BACKTICK);
    r = r && tag_name(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // SHARP typeconstr
  public static boolean sharp_pattern(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "sharp_pattern")) return false;
    if (!nextTokenIsSmart(b, SHARP)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeTokenSmart(b, SHARP);
    r = r && typeconstr(b, l + 1);
    exit_section_(b, m, SHARP_PATTERN, r);
    return r;
  }

  // LBRACE field [COLON typexpr] [EQ pattern]{ SEMI field [COLON typexpr] [EQ pattern]}* [SEMI UNDERSCORE] [ SEMI ] RBRACE
  public static boolean brace_field_pattern(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "brace_field_pattern")) return false;
    if (!nextTokenIsSmart(b, LBRACE)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeTokenSmart(b, LBRACE);
    r = r && field(b, l + 1);
    r = r && brace_field_pattern_2(b, l + 1);
    r = r && brace_field_pattern_3(b, l + 1);
    r = r && brace_field_pattern_4(b, l + 1);
    r = r && brace_field_pattern_5(b, l + 1);
    r = r && brace_field_pattern_6(b, l + 1);
    r = r && consumeToken(b, RBRACE);
    exit_section_(b, m, BRACE_FIELD_PATTERN, r);
    return r;
  }

  // [COLON typexpr]
  private static boolean brace_field_pattern_2(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "brace_field_pattern_2")) return false;
    brace_field_pattern_2_0(b, l + 1);
    return true;
  }

  // COLON typexpr
  private static boolean brace_field_pattern_2_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "brace_field_pattern_2_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeTokenSmart(b, COLON);
    r = r && typexpr(b, l + 1, -1);
    exit_section_(b, m, null, r);
    return r;
  }

  // [EQ pattern]
  private static boolean brace_field_pattern_3(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "brace_field_pattern_3")) return false;
    brace_field_pattern_3_0(b, l + 1);
    return true;
  }

  // EQ pattern
  private static boolean brace_field_pattern_3_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "brace_field_pattern_3_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeTokenSmart(b, EQ);
    r = r && pattern(b, l + 1, -1);
    exit_section_(b, m, null, r);
    return r;
  }

  // { SEMI field [COLON typexpr] [EQ pattern]}*
  private static boolean brace_field_pattern_4(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "brace_field_pattern_4")) return false;
    while (true) {
      int c = current_position_(b);
      if (!brace_field_pattern_4_0(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "brace_field_pattern_4", c)) break;
    }
    return true;
  }

  // SEMI field [COLON typexpr] [EQ pattern]
  private static boolean brace_field_pattern_4_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "brace_field_pattern_4_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeTokenSmart(b, SEMI);
    r = r && field(b, l + 1);
    r = r && brace_field_pattern_4_0_2(b, l + 1);
    r = r && brace_field_pattern_4_0_3(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // [COLON typexpr]
  private static boolean brace_field_pattern_4_0_2(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "brace_field_pattern_4_0_2")) return false;
    brace_field_pattern_4_0_2_0(b, l + 1);
    return true;
  }

  // COLON typexpr
  private static boolean brace_field_pattern_4_0_2_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "brace_field_pattern_4_0_2_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeTokenSmart(b, COLON);
    r = r && typexpr(b, l + 1, -1);
    exit_section_(b, m, null, r);
    return r;
  }

  // [EQ pattern]
  private static boolean brace_field_pattern_4_0_3(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "brace_field_pattern_4_0_3")) return false;
    brace_field_pattern_4_0_3_0(b, l + 1);
    return true;
  }

  // EQ pattern
  private static boolean brace_field_pattern_4_0_3_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "brace_field_pattern_4_0_3_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeTokenSmart(b, EQ);
    r = r && pattern(b, l + 1, -1);
    exit_section_(b, m, null, r);
    return r;
  }

  // [SEMI UNDERSCORE]
  private static boolean brace_field_pattern_5(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "brace_field_pattern_5")) return false;
    parseTokensSmart(b, 0, SEMI, UNDERSCORE);
    return true;
  }

  // [ SEMI ]
  private static boolean brace_field_pattern_6(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "brace_field_pattern_6")) return false;
    consumeTokenSmart(b, SEMI);
    return true;
  }

  // LBRACKET pattern { SEMI pattern }* [ SEMI ] RBRACKET
  public static boolean bracket_pattern(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "bracket_pattern")) return false;
    if (!nextTokenIsSmart(b, LBRACKET)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeTokenSmart(b, LBRACKET);
    r = r && pattern(b, l + 1, -1);
    r = r && bracket_pattern_2(b, l + 1);
    r = r && bracket_pattern_3(b, l + 1);
    r = r && consumeToken(b, RBRACKET);
    exit_section_(b, m, BRACKET_PATTERN, r);
    return r;
  }

  // { SEMI pattern }*
  private static boolean bracket_pattern_2(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "bracket_pattern_2")) return false;
    while (true) {
      int c = current_position_(b);
      if (!bracket_pattern_2_0(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "bracket_pattern_2", c)) break;
    }
    return true;
  }

  // SEMI pattern
  private static boolean bracket_pattern_2_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "bracket_pattern_2_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeTokenSmart(b, SEMI);
    r = r && pattern(b, l + 1, -1);
    exit_section_(b, m, null, r);
    return r;
  }

  // [ SEMI ]
  private static boolean bracket_pattern_3(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "bracket_pattern_3")) return false;
    consumeTokenSmart(b, SEMI);
    return true;
  }

  // LARRAY pattern { SEMI pattern }* [ SEMI ] RARRAY
  public static boolean larray_pattern(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "larray_pattern")) return false;
    if (!nextTokenIsSmart(b, LARRAY)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeTokenSmart(b, LARRAY);
    r = r && pattern(b, l + 1, -1);
    r = r && larray_pattern_2(b, l + 1);
    r = r && larray_pattern_3(b, l + 1);
    r = r && consumeToken(b, RARRAY);
    exit_section_(b, m, LARRAY_PATTERN, r);
    return r;
  }

  // { SEMI pattern }*
  private static boolean larray_pattern_2(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "larray_pattern_2")) return false;
    while (true) {
      int c = current_position_(b);
      if (!larray_pattern_2_0(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "larray_pattern_2", c)) break;
    }
    return true;
  }

  // SEMI pattern
  private static boolean larray_pattern_2_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "larray_pattern_2_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeTokenSmart(b, SEMI);
    r = r && pattern(b, l + 1, -1);
    exit_section_(b, m, null, r);
    return r;
  }

  // [ SEMI ]
  private static boolean larray_pattern_3(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "larray_pattern_3")) return false;
    consumeTokenSmart(b, SEMI);
    return true;
  }

  // char-literal DOTDOT char-literal
  public static boolean char_literal_pattern(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "char_literal_pattern")) return false;
    if (!nextTokenIsSmart(b, CHAR_VALUE)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = char_literal(b, l + 1);
    r = r && consumeToken(b, DOTDOT);
    r = r && char_literal(b, l + 1);
    exit_section_(b, m, CHAR_LITERAL_PATTERN, r);
    return r;
  }

  public static boolean lazy_pattern(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "lazy_pattern")) return false;
    if (!nextTokenIsSmart(b, LAZY)) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_, null);
    r = consumeTokenSmart(b, LAZY);
    p = r;
    r = p && pattern(b, l, 16);
    exit_section_(b, l, m, LAZY_PATTERN, r, p, null);
    return r || p;
  }

  public static boolean exception_pattern(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "exception_pattern")) return false;
    if (!nextTokenIsSmart(b, EXCEPTION)) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_, null);
    r = consumeTokenSmart(b, EXCEPTION);
    p = r;
    r = p && pattern(b, l, 17);
    exit_section_(b, l, m, EXCEPTION_PATTERN, r, p, null);
    return r || p;
  }

  public static boolean module_path_paren_pattern(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "module_path_paren_pattern")) return false;
    if (!nextTokenIsSmart(b, CAPITALIZED_IDENT)) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_, null);
    r = module_path_paren_pattern_0(b, l + 1);
    p = r;
    r = p && pattern(b, l, 18);
    r = p && report_error_(b, consumeToken(b, RPAREN)) && r;
    exit_section_(b, l, m, MODULE_PATH_PAREN_PATTERN, r, p, null);
    return r || p;
  }

  // module-path DOT LPAREN
  private static boolean module_path_paren_pattern_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "module_path_paren_pattern_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = module_path(b, l + 1);
    r = r && consumeTokensSmart(b, 0, DOT, LPAREN);
    exit_section_(b, m, null, r);
    return r;
  }

  public static boolean module_path_bracket_pattern(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "module_path_bracket_pattern")) return false;
    if (!nextTokenIsSmart(b, CAPITALIZED_IDENT)) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_, null);
    r = module_path_bracket_pattern_0(b, l + 1);
    p = r;
    r = p && pattern(b, l, 19);
    r = p && report_error_(b, consumeToken(b, RBRACKET)) && r;
    exit_section_(b, l, m, MODULE_PATH_BRACKET_PATTERN, r, p, null);
    return r || p;
  }

  // module-path DOT LBRACKET
  private static boolean module_path_bracket_pattern_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "module_path_bracket_pattern_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = module_path(b, l + 1);
    r = r && consumeTokensSmart(b, 0, DOT, LBRACKET);
    exit_section_(b, m, null, r);
    return r;
  }

  public static boolean module_path_array_pattern(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "module_path_array_pattern")) return false;
    if (!nextTokenIsSmart(b, CAPITALIZED_IDENT)) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_, null);
    r = module_path_array_pattern_0(b, l + 1);
    p = r;
    r = p && pattern(b, l, 20);
    r = p && report_error_(b, consumeToken(b, RARRAY)) && r;
    exit_section_(b, l, m, MODULE_PATH_ARRAY_PATTERN, r, p, null);
    return r || p;
  }

  // module-path DOT LARRAY
  private static boolean module_path_array_pattern_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "module_path_array_pattern_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = module_path(b, l + 1);
    r = r && consumeTokensSmart(b, 0, DOT, LARRAY);
    exit_section_(b, m, null, r);
    return r;
  }

  public static boolean module_path_brace_pattern(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "module_path_brace_pattern")) return false;
    if (!nextTokenIsSmart(b, CAPITALIZED_IDENT)) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_, null);
    r = module_path_brace_pattern_0(b, l + 1);
    p = r;
    r = p && pattern(b, l, -1);
    r = p && report_error_(b, consumeToken(b, RBRACE)) && r;
    exit_section_(b, l, m, MODULE_PATH_BRACE_PATTERN, r, p, null);
    return r || p;
  }

  // module-path DOT LBRACE
  private static boolean module_path_brace_pattern_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "module_path_brace_pattern_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = module_path(b, l + 1);
    r = r && consumeTokensSmart(b, 0, DOT, LBRACE);
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // Expression root: typexpr
  // Operator priority table:
  // 0: ATOM(ident_typexpr)
  // 1: ATOM(underscore_typexpr)
  // 2: PREFIX(parenthesis_typexpr)
  // 3: BINARY(label_typexpr)
  // 4: ATOM(label_opt_typexpr)
  // 5: N_ARY(star_typexpr)
  // 6: POSTFIX(typeconstr_typexpr)
  // 7: ATOM(comma_typexpr)
  // 8: POSTFIX(as_typexpr)
  // 9: ATOM(ltgt_typexpr)
  // 10: ATOM(ltgt_method_typexpr)
  // 11: ATOM(classtype_typexpr)
  // 12: POSTFIX(classpath_typexpr)
  // 13: ATOM(paren_sharp_typexpr)
  // 14: ATOM(polymorphic-variant-type_typexpr)
  public static boolean typexpr(PsiBuilder b, int l, int g) {
    if (!recursion_guard_(b, l, "typexpr")) return false;
    addVariant(b, "<typexpr>");
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_, "<typexpr>");
    r = ident_typexpr(b, l + 1);
    if (!r) r = underscore_typexpr(b, l + 1);
    if (!r) r = parenthesis_typexpr(b, l + 1);
    if (!r) r = label_opt_typexpr(b, l + 1);
    if (!r) r = comma_typexpr(b, l + 1);
    if (!r) r = ltgt_typexpr(b, l + 1);
    if (!r) r = ltgt_method_typexpr(b, l + 1);
    if (!r) r = classtype_typexpr(b, l + 1);
    if (!r) r = paren_sharp_typexpr(b, l + 1);
    if (!r) r = polymorphic_variant_type_typexpr(b, l + 1);
    p = r;
    r = r && typexpr_0(b, l + 1, g);
    exit_section_(b, l, m, null, r, p, null);
    return r || p;
  }

  public static boolean typexpr_0(PsiBuilder b, int l, int g) {
    if (!recursion_guard_(b, l, "typexpr_0")) return false;
    boolean r = true;
    while (true) {
      Marker m = enter_section_(b, l, _LEFT_, null);
      if (g < 3 && consumeTokenSmart(b, RIGHT_ARROW)) {
        r = typexpr(b, l, 3);
        exit_section_(b, l, m, LABEL_TYPEXPR, r, true, null);
      }
      else if (g < 5 && consumeTokenSmart(b, STAR)) {
        while (true) {
          r = report_error_(b, typexpr(b, l, 5));
          if (!consumeTokenSmart(b, STAR)) break;
        }
        exit_section_(b, l, m, STAR_TYPEXPR, r, true, null);
      }
      else if (g < 6 && typeconstr(b, l + 1)) {
        r = true;
        exit_section_(b, l, m, TYPECONSTR_TYPEXPR, r, true, null);
      }
      else if (g < 8 && as_typexpr_0(b, l + 1)) {
        r = true;
        exit_section_(b, l, m, AS_TYPEXPR, r, true, null);
      }
      else if (g < 12 && classpath_typexpr_0(b, l + 1)) {
        r = true;
        exit_section_(b, l, m, CLASSPATH_TYPEXPR, r, true, null);
      }
      else {
        exit_section_(b, l, m, null, false, false, null);
        break;
      }
    }
    return r;
  }

  // SINGLE_QUOTE ident
  public static boolean ident_typexpr(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "ident_typexpr")) return false;
    if (!nextTokenIsSmart(b, SINGLE_QUOTE)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeTokenSmart(b, SINGLE_QUOTE);
    r = r && ident(b, l + 1);
    exit_section_(b, m, IDENT_TYPEXPR, r);
    return r;
  }

  // UNDERSCORE
  public static boolean underscore_typexpr(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "underscore_typexpr")) return false;
    if (!nextTokenIsSmart(b, UNDERSCORE)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeTokenSmart(b, UNDERSCORE);
    exit_section_(b, m, UNDERSCORE_TYPEXPR, r);
    return r;
  }

  public static boolean parenthesis_typexpr(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "parenthesis_typexpr")) return false;
    if (!nextTokenIsSmart(b, LPAREN)) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_, null);
    r = consumeTokenSmart(b, LPAREN);
    p = r;
    r = p && typexpr(b, l, 2);
    r = p && report_error_(b, consumeToken(b, RPAREN)) && r;
    exit_section_(b, l, m, PARENTHESIS_TYPEXPR, r, p, null);
    return r || p;
  }

  // [QUESTION_MARK] label-name COLON typexpr RIGHT_ARROW typexpr
  public static boolean label_opt_typexpr(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "label_opt_typexpr")) return false;
    if (!nextTokenIsSmart(b, LOWERCASE_IDENT, QUESTION_MARK)) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, LABEL_OPT_TYPEXPR, "<label opt typexpr>");
    r = label_opt_typexpr_0(b, l + 1);
    r = r && label_name(b, l + 1);
    r = r && consumeToken(b, COLON);
    r = r && typexpr(b, l + 1, -1);
    r = r && consumeToken(b, RIGHT_ARROW);
    r = r && typexpr(b, l + 1, -1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  // [QUESTION_MARK]
  private static boolean label_opt_typexpr_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "label_opt_typexpr_0")) return false;
    consumeTokenSmart(b, QUESTION_MARK);
    return true;
  }

  // LPAREN typexpr { COMMA typexpr } RPAREN typeconstr
  public static boolean comma_typexpr(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "comma_typexpr")) return false;
    if (!nextTokenIsSmart(b, LPAREN)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeTokenSmart(b, LPAREN);
    r = r && typexpr(b, l + 1, -1);
    r = r && comma_typexpr_2(b, l + 1);
    r = r && consumeToken(b, RPAREN);
    r = r && typeconstr(b, l + 1);
    exit_section_(b, m, COMMA_TYPEXPR, r);
    return r;
  }

  // COMMA typexpr
  private static boolean comma_typexpr_2(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "comma_typexpr_2")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeTokenSmart(b, COMMA);
    r = r && typexpr(b, l + 1, -1);
    exit_section_(b, m, null, r);
    return r;
  }

  // AS SINGLE_QUOTE ident
  private static boolean as_typexpr_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "as_typexpr_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeTokensSmart(b, 0, AS, SINGLE_QUOTE);
    r = r && ident(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // LT [DOTDOT] GT
  public static boolean ltgt_typexpr(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "ltgt_typexpr")) return false;
    if (!nextTokenIsSmart(b, LT)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeTokenSmart(b, LT);
    r = r && ltgt_typexpr_1(b, l + 1);
    r = r && consumeToken(b, GT);
    exit_section_(b, m, LTGT_TYPEXPR, r);
    return r;
  }

  // [DOTDOT]
  private static boolean ltgt_typexpr_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "ltgt_typexpr_1")) return false;
    consumeTokenSmart(b, DOTDOT);
    return true;
  }

  // LT method-type { SEMI method-type } [SEMI PIPE SEMI DOTDOT] GT
  public static boolean ltgt_method_typexpr(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "ltgt_method_typexpr")) return false;
    if (!nextTokenIsSmart(b, LT)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeTokenSmart(b, LT);
    r = r && method_type(b, l + 1);
    r = r && ltgt_method_typexpr_2(b, l + 1);
    r = r && ltgt_method_typexpr_3(b, l + 1);
    r = r && consumeToken(b, GT);
    exit_section_(b, m, LTGT_METHOD_TYPEXPR, r);
    return r;
  }

  // SEMI method-type
  private static boolean ltgt_method_typexpr_2(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "ltgt_method_typexpr_2")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeTokenSmart(b, SEMI);
    r = r && method_type(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // [SEMI PIPE SEMI DOTDOT]
  private static boolean ltgt_method_typexpr_3(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "ltgt_method_typexpr_3")) return false;
    parseTokensSmart(b, 0, SEMI, PIPE, SEMI, DOTDOT);
    return true;
  }

  // SHARP classtype-path
  public static boolean classtype_typexpr(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "classtype_typexpr")) return false;
    if (!nextTokenIsSmart(b, SHARP)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeTokenSmart(b, SHARP);
    r = r && classtype_path(b, l + 1);
    exit_section_(b, m, CLASSTYPE_TYPEXPR, r);
    return r;
  }

  // SHARP class-path
  private static boolean classpath_typexpr_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "classpath_typexpr_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeTokenSmart(b, SHARP);
    r = r && class_path(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // LPAREN typexpr { COMMA typexpr } RPAREN SHARP class-path
  public static boolean paren_sharp_typexpr(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "paren_sharp_typexpr")) return false;
    if (!nextTokenIsSmart(b, LPAREN)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeTokenSmart(b, LPAREN);
    r = r && typexpr(b, l + 1, -1);
    r = r && paren_sharp_typexpr_2(b, l + 1);
    r = r && consumeTokensSmart(b, 0, RPAREN, SHARP);
    r = r && class_path(b, l + 1);
    exit_section_(b, m, PAREN_SHARP_TYPEXPR, r);
    return r;
  }

  // COMMA typexpr
  private static boolean paren_sharp_typexpr_2(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "paren_sharp_typexpr_2")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeTokenSmart(b, COMMA);
    r = r && typexpr(b, l + 1, -1);
    exit_section_(b, m, null, r);
    return r;
  }

  // polymorphic-variant-type
  public static boolean polymorphic_variant_type_typexpr(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "polymorphic_variant_type_typexpr")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, POLYMORPHIC_VARIANT_TYPE_TYPEXPR, "<polymorphic variant type typexpr>");
    r = polymorphic_variant_type(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

}
