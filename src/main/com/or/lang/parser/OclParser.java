package com.or.lang.parser;

import com.intellij.lang.PsiBuilder;
import com.intellij.psi.tree.IElementType;
import com.or.lang.OCamlTypes;
import com.or.lang.core.type.ORTokenElementType;
import com.or.lang.utils.CommonParser;
import com.or.lang.utils.ParserScope;
import com.or.lang.utils.ParserState;
import org.jetbrains.annotations.NotNull;

import static com.intellij.codeInsight.completion.CompletionUtilCore.DUMMY_IDENTIFIER_TRIMMED;
import static com.or.lang.utils.ParserScopeEnum.*;

public class OclParser extends CommonParser {

    @Override
    protected void parseFile(@NotNull PsiBuilder builder, @NotNull ParserState state) {
        IElementType tokenType = null;
        state.previousElementType1 = null;

        while (true) {
            state.previousElementType2 = state.previousElementType1;
            state.previousElementType1 = tokenType;
            tokenType = builder.getTokenType();
            if (tokenType == null) {
                break;
            }

            if (tokenType == OCamlTypes.SEMI) {
                parseSemi(state);
            } else if (tokenType == OCamlTypes.IN) {
                parseIn(state);
            } else if (tokenType == OCamlTypes.END) { // end (like a })
                parseEnd(state);
            } else if (tokenType == OCamlTypes.RIGHT_ARROW) {
                parseRightArrow(state);
            } else if (tokenType == OCamlTypes.PIPE) {
                parsePipe(state);
            } else if (tokenType == OCamlTypes.EQ) {
                parseEq(state);
            } else if (tokenType == OCamlTypes.OF) {
                parseOf(state);
            } else if (tokenType == OCamlTypes.STAR) {
                parseStar(state);
            } else if (tokenType == OCamlTypes.COLON) {
                parseColon(state);
            } else if (tokenType == OCamlTypes.QUESTION_MARK) {
                parseQuestionMark(state);
            } else if (tokenType == OCamlTypes.LIDENT) {
                parseLIdent(state);
            } else if (tokenType == OCamlTypes.UIDENT) {
                parseUIdent(state);
            } else if (tokenType == OCamlTypes.SIG) {
                parseSig(state);
            } else if (tokenType == OCamlTypes.STRUCT) {
                parseStruct(state);
            } else if (tokenType == OCamlTypes.BEGIN) {
                parseBegin(state);
            } else if (tokenType == OCamlTypes.OBJECT) {
                parseObject(state);
            } else if (tokenType == OCamlTypes.IF) {
                parseIf(state);
            } else if (tokenType == OCamlTypes.THEN) {
                parseThen(state);
            } else if (tokenType == OCamlTypes.ELSE) {
                parseElse(state);
            } else if (tokenType == OCamlTypes.MATCH) {
                parseMatch(state);
            } else if (tokenType == OCamlTypes.TRY) {
                parseTry(state);
            } else if (tokenType == OCamlTypes.WITH) {
                parseWith(state);
            } else if (tokenType == OCamlTypes.AND) {
                parseAnd(state);
            } else if (tokenType == OCamlTypes.DOT) {
                parseDot(state);
            } else if (tokenType == OCamlTypes.DOTDOT) {
                parseDotDot(state);
            } else if (tokenType == OCamlTypes.FUNCTION) {
                // function is a shortcut for a pattern match
                parseFunction(state);
            } else if (tokenType == OCamlTypes.FUN) {
                parseFun(state);
            } else if (tokenType == OCamlTypes.ASSERT) {
                parseAssert(state);
            } else if (tokenType == OCamlTypes.RAISE) {
                parseRaise(state);
            } else if (tokenType == OCamlTypes.COMMA) {
                parseComma(state);
            } else if (tokenType == OCamlTypes.AT_SIGN) {
                parseArrobase(state);
            } else if (tokenType == OCamlTypes.AT_SIGN_2) {
                parseArrobase2(state);
            } else if (tokenType == OCamlTypes.AT_SIGN_3) {
                parseArrobase3(state);
            } else if (tokenType == OCamlTypes.OPTION) {
                parseOption(state);
            }
            // while ... do ... done
            else if (tokenType == OCamlTypes.WHILE) {
                parseWhile(state);
            }
            // do ... done
            else if (tokenType == OCamlTypes.DO) {
                parseDo(state);
            } else if (tokenType == OCamlTypes.DONE) {
                parseDone(state);
            }
            // ( ... )
            else if (tokenType == OCamlTypes.LPAREN) {
                parseLParen(state);
            } else if (tokenType == OCamlTypes.RPAREN) {
                parseRParen(state);
            }
            // { ... }
            else if (tokenType == OCamlTypes.LBRACE) {
                parseLBrace(state);
            } else if (tokenType == OCamlTypes.RBRACE) {
                parseRBrace(state);
            }
            // [ ... ]
            else if (tokenType == OCamlTypes.LBRACKET) {
                parseLBracket(state);
            } else if (tokenType == OCamlTypes.RBRACKET) {
                parseRBracket(state);
            }
            // < ... >
            else if (tokenType == OCamlTypes.LT) {
                parseLt(state);
            } else if (tokenType == OCamlTypes.GT) {
                parseGt(state);
            }
            // Starts expression
            else if (tokenType == OCamlTypes.OPEN) {
                parseOpen(state);
            } else if (tokenType == OCamlTypes.INCLUDE) {
                parseInclude(state);
            } else if (tokenType == OCamlTypes.EXTERNAL) {
                parseExternal(state);
            } else if (tokenType == OCamlTypes.TYPE) {
                parseType(state);
            } else if (tokenType == OCamlTypes.MODULE) {
                parseModule(state);
            } else if (tokenType == OCamlTypes.CLASS) {
                parseClass(state);
            } else if (tokenType == OCamlTypes.LET) {
                parseLet(state);
            } else if (tokenType == OCamlTypes.VAL) {
                parseVal(state);
            } else if (tokenType == OCamlTypes.METHOD) {
                parseMethod(state);
            } else if (tokenType == OCamlTypes.EXCEPTION) {
                parseException(state);
            } else if (tokenType == OCamlTypes.DIRECTIVE_IF) {
                parseDirectiveIf(state);
            } else if (tokenType == OCamlTypes.DIRECTIVE_ELSE) {
                parseDirectiveElse(/*builder,*/ state);
            } else if (tokenType == OCamlTypes.DIRECTIVE_ELIF) {
                parseDirectiveElif(/*builder,*/ state);
            } else if (tokenType == OCamlTypes.DIRECTIVE_END || tokenType == OCamlTypes.DIRECTIVE_ENDIF) {
                parseDirectiveEnd(/*builder,*/ state);
            }

            if (state.dontMove) {
                state.dontMove = false;
                // revert
                tokenType = state.previousElementType1;
                state.previousElementType1 = state.previousElementType2;
            } else {
                builder.advanceLexer();
            }
        }
    }

    private void parseOption(@NotNull ParserState state) {
        if (state.isDummy() && (state.isPrevious(OCamlTypes.C_SIG_ITEM) || state.isPrevious(OCamlTypes.C_TYPE_BINDING))) {
            // in type      :  type t = xxx |>option<|
            // or signature :  ... -> xxx |>option<| ...
            state.updateCurrentCompositeElementType(OCamlTypes.C_OPTION).complete();
        }
    }

    private void parseRaise(@NotNull ParserState state) {
        if (state.is(OCamlTypes.C_EXTERNAL_DECLARATION)) {
            state.remapCurrentToken(OCamlTypes.LIDENT).wrapWith(OCamlTypes.C_LOWER_IDENTIFIER);
        }
    }

    private void parseComma(@NotNull ParserState state) {
        if (state.isPrevious(OCamlTypes.C_LET_DECLARATION)) {
            // It must be a deconstruction ::  let (a |>,<| b)
            // We need to do it again because lower symbols must be wrapped with identifiers
            if (state.isCurrentResolution(genericExpression)) {
                ParserScope scope = state.pop();
                if (scope != null) {
                    scope.rollbackTo();
                    state
                            .markScope(OCamlTypes.C_DECONSTRUCTION, OCamlTypes.LPAREN)
                            .advance();
                }
            }
        } else if (state.is(OCamlTypes.C_LET_DECLARATION)) {
            // It must be a deconstruction ::  let a |>,<| b ...
            // We need to do it again because lower symbols must be wrapped with identifiers
            ParserScope scope = state.pop();
            if (scope != null) {
                scope.rollbackTo();
                state.mark(OCamlTypes.C_LET_DECLARATION)
                        .advance()
                        .markScope(OCamlTypes.C_DECONSTRUCTION, OCamlTypes.LPAREN);
            }
        }
    }

    private void parseArrobase(@NotNull ParserState state) {
        if (state.is(OCamlTypes.C_ANNOTATION)) {
            state.mark(OCamlTypes.C_MACRO_NAME);
        }
    }

    private void parseArrobase2(@NotNull ParserState state) {
        if (state.is(OCamlTypes.C_ANNOTATION)) {
            state.mark(OCamlTypes.C_MACRO_NAME);
        }
    }

    private void parseArrobase3(@NotNull ParserState state) {
        if (state.is(OCamlTypes.C_ANNOTATION)) {
            state.mark(OCamlTypes.C_MACRO_NAME);
        }
    }

    private void parseLt(@NotNull ParserState state) {
        if ((state.isDummy() && (state.isPrevious(OCamlTypes.C_SIG_ITEM) || state.isPrevious(OCamlTypes.C_TYPE_BINDING))) || state.is(OCamlTypes.C_OBJECT_FIELD)) {
            // |> < <| .. > ..
            state.markScope(OCamlTypes.C_OBJECT, OCamlTypes.LT)
                    .advance()
                    .mark(OCamlTypes.C_OBJECT_FIELD);
        }
    }

    private void parseGt(@NotNull ParserState state) {
        if (state.is(OCamlTypes.C_OBJECT)) {
            state.advance();
            if (state.getTokenType() == OCamlTypes.AS) {
                // type t = < .. > |>as<| ..
                state.advance().advance().popEnd();
            }
        } else if (state.isPrevious(OCamlTypes.C_OBJECT)) {
            // < ... |> > <| ..
            if (state.isCurrentResolution(objectFieldNamed)) {
                state.popEnd();
            }
            state.advance();

            if ("Js".equals(state.getTokenText())) {
                // it might be a Js object (same with Js.t at the end)
                state.advance();
                if (state.getTokenType() == OCamlTypes.DOT) {
                    state.advance();
                    if ("t".equals(state.getTokenText())) {
                        state.updateCurrentCompositeElementType(OCamlTypes.C_JS_OBJECT).advance().complete();
                    }
                }
            }

            state.popEnd();
        }
    }

    private void parseWhile(@NotNull ParserState state) {
        state.mark(OCamlTypes.C_WHILE).advance().mark(OCamlTypes.C_BINARY_CONDITION);
    }

    private void parseDo(@NotNull ParserState state) {
        if (state.is(OCamlTypes.C_BINARY_CONDITION) && state.isPrevious(OCamlTypes.C_WHILE)) {
            state.popEnd().advance().markScope(OCamlTypes.C_DO_LOOP, OCamlTypes.DO);
        } else {
            state.markScope(OCamlTypes.C_DO_LOOP, OCamlTypes.DO);
        }
    }

    private void parseDone(@NotNull ParserState state) {
        state.popEndUntil(OCamlTypes.C_DO_LOOP).popEnd();
        if (state.is(OCamlTypes.C_WHILE)) {
            state.popEnd();
        }
    }

    private void parseRightArrow(@NotNull ParserState state) {
        // intermediate results
        if (state.is(OCamlTypes.C_FUN_PARAM)) {
            state.popEnd();
        }

        if (state.isPrevious(OCamlTypes.C_SIG_ITEM)) {
            state.popEnd().popEnd();
            if (state.isPrevious(OCamlTypes.C_NAMED_PARAM)) {
                // can't have arrow in a named param signature ::  let fn x:int -> y:int
                state.popEnd().popEndUntil(OCamlTypes.C_SIG_EXPR);
            }
            state.advance()
                    .mark(OCamlTypes.C_SIG_ITEM)
                    .markDummy();
        } else if (state.is(OCamlTypes.C_PATTERN_MATCH_EXPR)) {
            state.advance().mark(OCamlTypes.C_PATTERN_MATCH_BODY);
        } else if (state.isCurrentResolution(maybeFunctionParameters)) {
            // fun ... |>-><| ...
            state.complete().popEnd().advance().mark(OCamlTypes.C_FUN_BODY);
        }
    }

    private void parseAssert(@NotNull ParserState state) {
        state.mark(OCamlTypes.C_ASSERT_STMT);
    }

    private void parseAnd(@NotNull ParserState state) {
        if (state.is(OCamlTypes.C_CONSTRAINT)) {
            state.popEnd().advance().mark(OCamlTypes.C_CONSTRAINT);
        } else {
            // pop scopes until a chainable expression is found
            ParserScope latestScope = state.getLatestScope();
            while (!state.isRoot() && !state.is(OCamlTypes.C_LET_DECLARATION) && !state.is(OCamlTypes.C_TYPE_DECLARATION)) {
                state.popEnd();
                latestScope = state.getLatestScope();
            }

            state.popEnd().advance();

            if (latestScope != null) {
                if (latestScope.isCompositeType(OCamlTypes.C_LET_DECLARATION)) {
                    state.mark(OCamlTypes.C_LET_DECLARATION).setStart();
                } else if (latestScope.isCompositeType(OCamlTypes.C_TYPE_DECLARATION)) {
                    state.mark(OCamlTypes.C_TYPE_DECLARATION).setStart();
                }
            }
        }
    }

    private void parseDot(@NotNull ParserState state) {
        if (state.is(OCamlTypes.C_TYPE_VARIABLE)) {
            state.popEnd()
                    .advance()
                    .mark(OCamlTypes.C_SIG_EXPR)
                    .mark(OCamlTypes.C_SIG_ITEM)
                    .markDummy();
        }
    }

    private void parseDotDot(@NotNull ParserState state) {
        if (state.is(OCamlTypes.C_OBJECT_FIELD)) {
            state.advance().popEnd();
        }
    }

    private void parsePipe(@NotNull ParserState state) {
        if (state.is(OCamlTypes.C_SCOPED_EXPR) && state.isPrevious(OCamlTypes.C_LET_DECLARATION)) {
            // let ( |>|<| ...
            return;
        }

        // Remove intermediate constructions
        if (state.is(OCamlTypes.C_IF_THEN_SCOPE)) {
            state.popEndUntil(OCamlTypes.C_IF).popEnd();
        }
        if (state.is(OCamlTypes.C_FUN_PARAM) && state.in(OCamlTypes.C_VARIANT_CONSTRUCTOR)) {
            state.popEndUntil(OCamlTypes.C_VARIANT_DECLARATION);
        }
        if (state.is(OCamlTypes.C_PATTERN_MATCH_BODY)) {
            state.popEndUntilOneOfResolution(matchWith, functionMatch);
        }

        if (state.isDummy() && state.isPrevious(OCamlTypes.C_TYPE_BINDING)) {
            state.popDummy()
                    .advance()
                    .mark(OCamlTypes.C_VARIANT_DECLARATION);
        } else if (state.is(OCamlTypes.C_VARIANT_DECLARATION)) {
            // type t = | V1 |>|<| ...
            state.popEnd().advance().mark(OCamlTypes.C_VARIANT_DECLARATION);
        } else {
            if (state.isCurrentResolution(maybeFunctionParameters)) {
                state.popEnd().resolution(matchWith);
            } else if (state.is(OCamlTypes.C_PATTERN_MATCH_EXPR)) {
                // pattern group ::  | X |>|<| Y ...
                state.popEnd();
            }

            // By default, a pattern match
            state.advance().mark(OCamlTypes.C_PATTERN_MATCH_EXPR);
        }
    }

    private void parseMatch(@NotNull ParserState state) {
        state.mark(OCamlTypes.C_MATCH_EXPR)
                .advance()
                .mark(OCamlTypes.C_BINARY_CONDITION);
    }

    private void parseTry(@NotNull ParserState state) {
        state
                .mark(OCamlTypes.C_TRY_EXPR)
                .advance()
                .mark(OCamlTypes.C_TRY_BODY);
    }

    private void parseWith(@NotNull ParserState state) {
        if (state.is(OCamlTypes.C_FUNCTOR_RESULT)) {
            // A functor with constraints
            //  module Make (M : Input) : S |>with<| ...
            state.popEnd().advance().mark(OCamlTypes.C_CONSTRAINTS).mark(OCamlTypes.C_CONSTRAINT);
        } else if (state.in(OCamlTypes.C_MODULE_TYPE)) {
            // A module with a signature and constraints
            //  module G : sig ... end |>with<| ...
            //  module G : X |>with<| ...
            state
                    .popEndUntil(OCamlTypes.C_MODULE_TYPE)
                    .popEnd()
                    .advance()
                    .mark(OCamlTypes.C_CONSTRAINTS)
                    .mark(OCamlTypes.C_CONSTRAINT);
        } else if (state.isCurrentResolution(maybeFunctorCall)) {
            if (state.isCurrentResolution(maybeFunctorCall)) {
                state.popEnd();
            }
            if (state.is(OCamlTypes.C_INCLUDE)) {
                // An include with constraints ::  include M |>with<| ...
                state.mark(OCamlTypes.C_CONSTRAINTS).advance().mark(OCamlTypes.C_CONSTRAINT);
            }
        } else if (state.is(OCamlTypes.C_TRY_BODY)) {
            // A try handler ::  try ... |>with<| ...
            state
                    .popEnd()
                    .advance()
                    .mark(OCamlTypes.C_TRY_HANDLERS)
                    .mark(OCamlTypes.C_TRY_HANDLER);
        } else if (state.is(OCamlTypes.C_BINARY_CONDITION)) {
            if (state.isPrevious(OCamlTypes.C_MATCH_EXPR)) {
                state.popEnd().resolution(matchWith);
            }
        }
    }

    private void parseIf(@NotNull ParserState state) {
        // |>if<| ...
        state.mark(OCamlTypes.C_IF).advance().mark(OCamlTypes.C_BINARY_CONDITION);
    }

    private void parseThen(@NotNull ParserState state) {
        if (!state.is(OCamlTypes.C_DIRECTIVE)) {
            // if ... |>then<| ...
            state.popEndUntil(OCamlTypes.C_IF).advance().mark(OCamlTypes.C_IF_THEN_SCOPE);
        }
    }

    private void parseElse(@NotNull ParserState state) {
        // if ... then ... |>else<| ...
        state.popEndUntil(OCamlTypes.C_IF).advance().mark(OCamlTypes.C_IF_THEN_SCOPE);
    }

    private void parseStruct(@NotNull ParserState state) {
        if (state.isCurrentResolution(moduleBinding) || state.isPreviousResolution(module)) {
            // replace previous fake scope  ::  module X = |>struct<| ...
            state.popCancel().markScope(OCamlTypes.C_SCOPED_EXPR, OCamlTypes.STRUCT).resolution(moduleBinding);
        } else if (state.isCurrentResolution(functorNamedEq)) {
            // module X (...) = |>struct<| ...
            state.markScope(OCamlTypes.C_FUNCTOR_BINDING, OCamlTypes.STRUCT);
        } else {
            state.markScope(OCamlTypes.C_STRUCT_EXPR, OCamlTypes.STRUCT);
        }
    }

    private void parseSig(@NotNull ParserState state) {
        if (state.isCurrentResolution(moduleBinding) && state.isDummy()) {
            // This is the body of a module type
            // module type X = |>sig<| ...
            state.popDummy()
                    .markScope(OCamlTypes.C_SCOPED_EXPR, OCamlTypes.SIG).resolution(moduleBinding);
        } else if (state.isCurrentResolution(moduleNamedColon)) {
            state.markScope(OCamlTypes.C_SIG_EXPR, OCamlTypes.SIG);
        } else if (state.is(OCamlTypes.C_FUNCTOR_PARAM)) {
            state.markScope(OCamlTypes.C_SIG_EXPR, OCamlTypes.SIG);
        }
    }

    private void parseSemi(@NotNull ParserState state) {
        if (state.isPrevious(OCamlTypes.C_OBJECT)) {
            // SEMI ends the field, and starts a new one
            state.popEnd().advance().mark(OCamlTypes.C_OBJECT_FIELD);
        } else if (state.in(OCamlTypes.C_RECORD_FIELD)) {
            // SEMI ends the field, and starts a new one
            state.popEndUntil(OCamlTypes.C_RECORD_FIELD).popEnd().advance();
            if (state.getTokenType() != OCamlTypes.RBRACE) {
                state.mark(OCamlTypes.C_RECORD_FIELD);
            }
        } else {
            boolean isImplicitScope = state.isOneOf(OCamlTypes.C_FUN_BODY, OCamlTypes.C_LET_BINDING);

            // A SEMI operator ends the previous expression
            if (!isImplicitScope && !state.hasScopeToken()) {
                state.popEnd();
                if (state.is(OCamlTypes.C_OBJECT)) {
                    state.advance().mark(OCamlTypes.C_OBJECT_FIELD);
                }
            }
        }
    }

    private void parseIn(@NotNull ParserState state) {
        if (state.is(OCamlTypes.C_TRY_HANDLER)) {
            state.popEndUntil(OCamlTypes.C_TRY_EXPR);
        } else if (state.in(OCamlTypes.C_LET_DECLARATION)) {
            state.popEndUntil(OCamlTypes.C_LET_DECLARATION);
        }

        state.popEnd();
    }

    private void parseBegin(@NotNull ParserState state) {
        state.markScope(OCamlTypes.C_SCOPED_EXPR, OCamlTypes.BEGIN);
    }

    private void parseObject(@NotNull ParserState state) {
        state.markScope(OCamlTypes.C_OBJECT, OCamlTypes.OBJECT);
    }

    private void parseEnd(@NotNull ParserState state) {
        ParserScope scope =
                state.popEndUntilOneOfElementType(
                        OCamlTypes.BEGIN, OCamlTypes.SIG, OCamlTypes.STRUCT, OCamlTypes.OBJECT);

        state.advance().popEnd();

        if (scope != null) {
            if (scope.isCompositeType(OCamlTypes.C_MODULE_TYPE)) {
                IElementType nextToken = state.getTokenType();
                if (nextToken == OCamlTypes.WITH) {
                    state.advance().mark(OCamlTypes.C_CONSTRAINTS).mark(OCamlTypes.C_CONSTRAINT);
                }
            } else if (scope.isScopeToken(OCamlTypes.STRUCT) && state.is(OCamlTypes.C_MODULE_DECLARATION)) {
                // module M = struct .. |>end<|
                state.popEnd();

                IElementType nextToken = state.getTokenType();
                if (nextToken == OCamlTypes.AND) {
                    // module M = struct .. end |>and<|
                    state.advance().mark(OCamlTypes.C_MODULE_DECLARATION).resolution(module).setStart();
                }
            } else if (scope.isCompositeType(OCamlTypes.C_OBJECT)) {
                // Close a class
                state.popEnd();
            } else if (scope.isCompositeType(OCamlTypes.C_OBJECT)) {
                // Close a class
                state.popEnd();
            }
        }
    }

    private void parseColon(@NotNull ParserState state) {
        if (state.is(OCamlTypes.C_MODULE_DECLARATION)) {
            // module M |> : <| ...
            state.resolution(moduleNamedColon)
                    .advance()
                    .mark(OCamlTypes.C_MODULE_TYPE);
            IElementType nextToken = state.getTokenType();
            if (nextToken == OCamlTypes.LPAREN || nextToken == OCamlTypes.SIG) {
                state.updateScopeToken((ORTokenElementType) nextToken);
            }
        } else if (state.is(OCamlTypes.C_EXTERNAL_DECLARATION)
                || state.is(OCamlTypes.C_VAL_DECLARATION)
                || state.is(OCamlTypes.C_LET_DECLARATION)) {
            // external x |> : <| ...  OR  val x |> : <| ...  OR  let x |> : <| ...
            state.advance();
            if (state.getTokenType() == OCamlTypes.TYPE) {
                // Local type
                state.mark(OCamlTypes.C_TYPE_VARIABLE);
            } else {
                state.mark(OCamlTypes.C_SIG_EXPR)
                        .mark(OCamlTypes.C_SIG_ITEM)
                        .markDummy();
            }
        } else if (state.is(OCamlTypes.C_CLASS_DECLARATION) || state.is(OCamlTypes.C_CLASS_METHOD)) {
            // class x |> : <| ...  OR  method |> : <| ...
            state.advance().mark(OCamlTypes.C_SIG_EXPR)
                    .mark(OCamlTypes.C_SIG_ITEM)
                    .markDummy();
        } else if (state.is(OCamlTypes.C_OBJECT_FIELD)) {
            // < x |> : <| ...
            state.resolution(objectFieldNamed);
        } else if (state.is(OCamlTypes.C_RECORD_FIELD)) {
            state.advance()
                    .mark(OCamlTypes.C_SIG_EXPR)
                    .mark(OCamlTypes.C_SIG_ITEM)
                    .markDummy();
        } else if (state.is(OCamlTypes.C_FUN_PARAM)) {
            if (state.previousElementType2 == OCamlTypes.QUESTION_MARK) {
                // an optional param ::  ?x |> : <| ...
                state.advance().markOptionalParenDummyScope(OCamlTypes.C_NAMED_PARAM);
            } else {
                state.advance().mark(OCamlTypes.C_SIG_EXPR)
                        .mark(OCamlTypes.C_SIG_ITEM)
                        .markDummy();
            }
        } else if (state.is(OCamlTypes.C_FUNCTOR_DECLARATION)) {
            state.resolution(functorNamedColon)
                    .advance()
                    .mark(OCamlTypes.C_FUNCTOR_RESULT);
        } else if (state.is(OCamlTypes.C_NAMED_PARAM)) {
            state.advance()
                    .markOptionalParenDummyScope();
            if (state.isPrevious(OCamlTypes.C_SIG_ITEM)) {
                // A named param in signature ::  let x : c|> :<| ..
                state.mark(OCamlTypes.C_SIG_EXPR)
                        .mark(OCamlTypes.C_SIG_ITEM)
                        .markDummy();
            }
        } else if (state.is(OCamlTypes.C_SCOPED_EXPR)) {
            // Try to find the context
            ParserScope context = state.findScopeContext();
            if (context != null && context.isCompositeType(OCamlTypes.C_NAMED_PARAM)) {
                // a named param with a type signature ::  let fn ?x(|>(<|x: .. ) ..
                state.advance()
                        .mark(OCamlTypes.C_SIG_EXPR)
                        .mark(OCamlTypes.C_SIG_ITEM)
                        .markDummy();
            }
        }
    }

    private void parseQuestionMark(@NotNull ParserState state) {
        // First param ::  let f |>?<| ( x ...
        if (state.is(OCamlTypes.C_FUN_PARAMS)) {
            state.mark(OCamlTypes.C_FUN_PARAM)
                    .mark(OCamlTypes.C_NAMED_PARAM);
        }
        // Start of a new optional parameter ::  let f x |>?<|(y ...
        else if (state.is(OCamlTypes.C_FUN_PARAM) && !state.hasScopeToken()) {
            state.complete().popEnd()
                    .mark(OCamlTypes.C_FUN_PARAM)
                    .mark(OCamlTypes.C_NAMED_PARAM);
        }
        // Condition ?
        else if (state.is(OCamlTypes.C_BINARY_CONDITION) && !state.isPrevious(OCamlTypes.C_MATCH_EXPR)) {
            IElementType nextType = state.rawLookup(1);
            // ... |>?<| ... : ...
            if (nextType != OCamlTypes.LIDENT) {
                state.popEnd();
            }
        }
    }

    private void parseFunction(@NotNull ParserState state) {
        if (state.is(OCamlTypes.C_LET_BINDING)) {
            state.mark(OCamlTypes.C_FUN_EXPR)
                    .advance()
                    .mark(OCamlTypes.C_FUN_BODY);
        }

        state.mark(OCamlTypes.C_MATCH_EXPR).resolution(functionMatch).advance();
        if (state.getTokenType() != OCamlTypes.PIPE) {
            state.mark(OCamlTypes.C_PATTERN_MATCH_EXPR);
        }
    }

    private void parseFun(@NotNull ParserState state) {
        if (state.is(OCamlTypes.C_LET_BINDING)) {
            state
                    .mark(OCamlTypes.C_FUN_EXPR)
                    .advance()
                    .markOptional(OCamlTypes.C_FUN_PARAMS)
                    .resolution(maybeFunctionParameters);
        }
    }

    private void parseEq(@NotNull ParserState state) {
        // Remove intermediate constructions
        if (state.isPrevious(OCamlTypes.C_SIG_ITEM) || state.is(OCamlTypes.C_FUN_PARAMS)) {
            state.popEnd();
            if (state.is(OCamlTypes.C_SIG_ITEM)) {
                state.popEnd();
            }
        }
        if (state.is(OCamlTypes.C_FUNCTOR_RESULT) || state.is(OCamlTypes.C_SIG_EXPR) || state.is(OCamlTypes.C_DECONSTRUCTION)) {
            state.popEnd();
        }

        if (state.is(OCamlTypes.C_TYPE_DECLARATION)) {
            // type t  |> =<| ...
            state.advance()
                    .mark(OCamlTypes.C_TYPE_BINDING)
                    .markDummy();
        } else if (state.is(OCamlTypes.C_LET_DECLARATION) || (state.isPrevious(OCamlTypes.C_LET_DECLARATION) && state.is(OCamlTypes.C_PARAMETERS))) {
            // let x |> = <| ...
            // let (x) y z |> = <| ...
            state.popEndUntilStart();
            state.advance().mark(OCamlTypes.C_LET_BINDING);
        } else if (state.is(OCamlTypes.C_MODULE_DECLARATION)) {
            // module M |> = <| ...
            state.advance().markDummy().resolution(moduleBinding);
        } else if (state.is(OCamlTypes.C_FUNCTOR_DECLARATION) || state.isCurrentResolution(functorNamedColon)) {
            state.resolution(functorNamedEq);
        } else if (state.is(OCamlTypes.C_CONSTRAINT) && (state.isGrandPreviousResolution(functorNamedColon) || state.isGrandParent(OCamlTypes.C_MODULE_DECLARATION))) {
            IElementType nextElementType = state.lookAhead(1);
            if (nextElementType == OCamlTypes.STRUCT) {
                // Functor constraints ::  module M (...) : S with ... |> = <| struct ... end
                if (state.isGrandPreviousResolution(functorNamedColon)) {
                    state.popEnd().popEnd().resolution(functorNamedEq);
                } else {
                    state.popEndUntil(OCamlTypes.C_CONSTRAINTS).popEnd();
                }
            }
        } else if (state.is(OCamlTypes.C_FUN_PARAM)) {
            state.popEndUntil(OCamlTypes.C_FUN_EXPR).advance().mark(OCamlTypes.C_FUN_BODY);
        }
        // let fn ?(x |> = <| ...
        else if (state.isDummy() && state.isPrevious(OCamlTypes.C_NAMED_PARAM)) {
            state.advance().mark(OCamlTypes.C_DEFAULT_VALUE);
        }
    }

    private void parseOf(@NotNull ParserState state) {
        if (state.is(OCamlTypes.C_VARIANT_DECLARATION)) {
            // Variant params :: type t = | Variant «of» ..
            state.advance().mark(OCamlTypes.C_VARIANT_CONSTRUCTOR).mark(OCamlTypes.C_FUN_PARAM);
        } else if (state.is(OCamlTypes.C_MODULE_DECLARATION) && state.previousElementType1 == OCamlTypes.TYPE) {
            // extracting a module type ::  module type |>of<| X ...
            state.resolution(moduleTypeExtraction);
        }
    }

    private void parseStar(@NotNull ParserState state) {
        if (state.is(OCamlTypes.C_FUN_PARAM) && state.in(OCamlTypes.C_VARIANT_CONSTRUCTOR)) {
            // type t = | Variant of x |>*<| y ..
            state.popEnd().advance().mark(OCamlTypes.C_FUN_PARAM);
        }
    }

    private void parseLParen(@NotNull ParserState state) {
        if (state.is(OCamlTypes.C_EXTERNAL_DECLARATION)) {
            // Overloading an operator ::  external |>(<| ...
            state.markScope(OCamlTypes.C_SCOPED_EXPR, OCamlTypes.LPAREN).resolution(genericExpression);
        } else if (state.isCurrentResolution(maybeFunctorCall)) {
            // Yes, it is a functor call ::  module M = X |>(<| ... )
            state.complete()
                    .markScope(OCamlTypes.C_FUN_PARAMS, OCamlTypes.LPAREN)
                    .advance()
                    .mark(OCamlTypes.C_FUN_PARAM);
        } else if (state.previousElementType2 == OCamlTypes.UIDENT && state.previousElementType1 == OCamlTypes.DOT) {
            // Detecting a local open ::  M1.M2. |>(<| ... )
            state.markScope(OCamlTypes.C_LOCAL_OPEN, OCamlTypes.LPAREN);
        } else if (state.is(OCamlTypes.C_CLASS_DECLARATION)) {
            state.markScope(OCamlTypes.C_CLASS_CONSTR, OCamlTypes.LPAREN);
        } else if (state.is(OCamlTypes.C_FUN_PARAMS)) {
            // Start of the first parameter ::  let x |>(<| ...
            state.markScope(OCamlTypes.C_FUN_PARAM, OCamlTypes.LPAREN);
        } else if (state.is(OCamlTypes.C_FUN_PARAM)
                && !state.hasScopeToken()
                && state.previousElementType1 != OCamlTypes.QUESTION_MARK) {
            // Start of a new parameter
            //    let f xxx |>(<| ..tuple ) = ..
            state.popEnd().mark(OCamlTypes.C_FUN_PARAM).markScope(OCamlTypes.C_SCOPED_EXPR, OCamlTypes.LPAREN);
        }
        // A named param with default value ::  let fn ?|>(<| x ... )
        else if (state.is(OCamlTypes.C_NAMED_PARAM)) {
            state.markOptionalParenDummyScope();
        } else if (state.is(OCamlTypes.C_MODULE_DECLARATION)) {
            // This is a functor ::  module Make |>(<| ... )
            state.updateCurrentCompositeElementType(OCamlTypes.C_FUNCTOR_DECLARATION)
                    .markScope(OCamlTypes.C_FUNCTOR_PARAMS, OCamlTypes.LPAREN)
                    .advance()
                    .mark(OCamlTypes.C_FUNCTOR_PARAM);
        } else if (state.is(OCamlTypes.C_MODULE_TYPE)) {
            // module M : |>(<| ... )
            state
                    .popCancel()
                    .markScope(OCamlTypes.C_SCOPED_EXPR, OCamlTypes.LPAREN)
                    .resolution(genericExpression)
                    .dummy()
                    .advance()
                    .mark(OCamlTypes.C_MODULE_TYPE);
        } else {
            state.markScope(OCamlTypes.C_SCOPED_EXPR, OCamlTypes.LPAREN).resolution(genericExpression);
        }
    }

    private void parseRParen(@NotNull ParserState state) {
        ParserScope parenScope = state.popEndUntilScopeToken(OCamlTypes.LPAREN);
        if (parenScope == null) {
            return;
        }

        state.advance();

        int scopeLength = parenScope.getLength();
        if (scopeLength == 3 && state.isPrevious(OCamlTypes.C_LET_DECLARATION)) {
            // unit ::  let ()
            parenScope.updateCompositeElementType(OCamlTypes.C_UNIT);
        }

        IElementType nextToken = state.getTokenType();
        if (nextToken == OCamlTypes.LT || nextToken == OCamlTypes.LT_OR_EQUAL) {
            // are we in a binary op ?
            state.precedeScope(OCamlTypes.C_BINARY_CONDITION);
        } else if (!state.isPrevious(OCamlTypes.C_NAMED_PARAM)) {
            if (nextToken == OCamlTypes.QUESTION_MARK && !state.isPrevious(OCamlTypes.C_TERNARY) && !parenScope.isCompositeType(OCamlTypes.C_NAMED_PARAM) && !parenScope.isCompositeType(OCamlTypes.C_FUN_PARAM) && !state.isPrevious(OCamlTypes.C_FUN_PARAM)) {
                // ( ... |>)<| ? ...
                state.updateCurrentCompositeElementType(OCamlTypes.C_BINARY_CONDITION)
                        .precedeScope(OCamlTypes.C_TERNARY);
            }
        }


        state.popEnd();

        if (state.is(OCamlTypes.C_NAMED_PARAM) && nextToken != OCamlTypes.EQ) {
            state.popEnd();
        } else if (parenScope.isCompositeType(OCamlTypes.C_SCOPED_EXPR) && state.is(OCamlTypes.C_LET_DECLARATION) && nextToken != OCamlTypes.EQ) {
            // This is a custom infix operator
            state.mark(OCamlTypes.C_PARAMETERS);
        }
    }

    private void parseLBrace(@NotNull ParserState state) {
        // let fn |>{<| ... } = ...
        if (state.is(OCamlTypes.C_FUN_PARAMS)) {
            state.mark(OCamlTypes.C_FUN_PARAM);
        }

        state.markScope(OCamlTypes.C_RECORD_EXPR, OCamlTypes.LBRACE)
                .advance()
                .mark(OCamlTypes.C_RECORD_FIELD);
    }

    private void parseRBrace(@NotNull ParserState state) {
        ParserScope scope = state.popEndUntilScopeToken(OCamlTypes.LBRACE);
        state.advance();

        if (scope != null) {
            state.popEnd();
        }
    }

    private void parseLBracket(@NotNull ParserState state) {
        IElementType nextElementType = state.rawLookup(1);
        if (nextElementType == OCamlTypes.AT_SIGN
                || nextElementType == OCamlTypes.AT_SIGN_2
                || nextElementType == OCamlTypes.AT_SIGN_3) {
            // |> [ <| @?? ...
            if (nextElementType == OCamlTypes.AT_SIGN_3) {
                // floating attribute
                endLikeSemi(state);
            } else if (state.isOneOf(OCamlTypes.C_FUN_BODY, OCamlTypes.C_FUN_EXPR)) {
                // block attribute inside a function
                state.popEndUntil(OCamlTypes.C_FUN_EXPR).popEnd();
            } else if (state.in(OCamlTypes.C_SIG_EXPR)) {
                // block attribute inside a signature
                state.popEndUntil(OCamlTypes.C_SIG_EXPR).popEnd();
            }
            state.markScope(OCamlTypes.C_ANNOTATION, OCamlTypes.LBRACKET);
        } else {
            state.markScope(OCamlTypes.C_SCOPED_EXPR, OCamlTypes.LBRACKET);
        }
    }

    private void parseRBracket(@NotNull ParserState state) {
        state.popEndUntilScopeToken(OCamlTypes.LBRACKET);
        state.advance().popEnd();
    }

    private void parseLIdent(@NotNull ParserState state) {
        if (state.is(OCamlTypes.C_FUN_PARAMS)) {
            state.mark(OCamlTypes.C_FUN_PARAM);
        } else if (state.is(OCamlTypes.C_FUN_PARAM)
                && !state.hasScopeToken()
                && state.previousElementType1 != OCamlTypes.OF
                && state.previousElementType1 != OCamlTypes.STAR
                && state.previousElementType1 != OCamlTypes.QUESTION_MARK) {
            // Start of a new parameter
            //    .. ( xxx |>yyy<| ) ..
            state.complete().popEnd().mark(OCamlTypes.C_FUN_PARAM);
        }

        if (state.is(OCamlTypes.C_LET_DECLARATION)) {
            // let |>x<| ...
            state.wrapWith(OCamlTypes.C_LOWER_IDENTIFIER);
            IElementType nextToken = state.getTokenType();
            if (nextToken == OCamlTypes.COMMA) {
                // A deconstruction without parenthesis
                // ...
            } else if (nextToken != OCamlTypes.EQ && nextToken != OCamlTypes.COLON) {
                // This is a function, we need to create the let binding now, to be in sync with reason
                //  let |>x<| y z = ...  vs    let x = y z => ...
                state.mark(OCamlTypes.C_LET_BINDING).mark(OCamlTypes.C_FUN_EXPR).mark(OCamlTypes.C_FUN_PARAMS);
            }
        } else if (state.is(OCamlTypes.C_EXTERNAL_DECLARATION)) {
            state.wrapWith(OCamlTypes.C_LOWER_IDENTIFIER);
        } else if (state.is(OCamlTypes.C_TYPE_DECLARATION)) {
            state.wrapWith(OCamlTypes.C_LOWER_IDENTIFIER);
        } else if (state.is(OCamlTypes.C_VAL_DECLARATION) || state.is(OCamlTypes.C_CLASS_METHOD)) {
            state.wrapWith(OCamlTypes.C_LOWER_IDENTIFIER);
        } else if (state.is(OCamlTypes.C_CLASS_DECLARATION)) {
            state.wrapWith(OCamlTypes.C_LOWER_IDENTIFIER);
        } else if (state.is(OCamlTypes.C_DECONSTRUCTION)) {
            state.wrapWith(OCamlTypes.C_LOWER_IDENTIFIER);
        } else if (state.is(OCamlTypes.C_RECORD_FIELD)) {
            state.wrapWith(OCamlTypes.C_LOWER_IDENTIFIER);
        } else if (state.is(OCamlTypes.C_MACRO_NAME)) {
            // [@ |>x.y<| ... ]
            state.advance();
            while (state.getTokenType() == OCamlTypes.DOT) {
                state.advance();
                if (state.getTokenType() == OCamlTypes.LIDENT) {
                    state.advance();
                }
            }
            state.popEnd();
        } else if ((state.is(OCamlTypes.C_FUN_PARAM) && !state.isPrevious(OCamlTypes.C_FUN_CALL_PARAMS)) || state.is(OCamlTypes.C_NAMED_PARAM) || (state.isDummy() && state.isPrevious(OCamlTypes.C_NAMED_PARAM))) {
            state.wrapWith(OCamlTypes.C_LOWER_IDENTIFIER);
        } else {
            IElementType nextTokenType = state.lookAhead(1);
            if (nextTokenType == OCamlTypes.COLON && (state.isDummy() && state.isPrevious(OCamlTypes.C_SIG_ITEM))) {
                // let fn |>x<| : ...
                state.updateCurrentCompositeElementType(OCamlTypes.C_NAMED_PARAM).complete();
            }

            state.wrapWith(OCamlTypes.C_LOWER_SYMBOL);
        }
    }

    private void parseUIdent(@NotNull ParserState state) {
        if (DUMMY_IDENTIFIER_TRIMMED.equals(state.getTokenText())) {
            return;
        }

        if (state.isCurrentResolution(module)) {
            // Module declaration  ::  module |>M<| ...
            state.wrapWith(OCamlTypes.C_UPPER_IDENTIFIER);
        } else if (state.is(OCamlTypes.C_OPEN) || state.is(OCamlTypes.C_INCLUDE)) {
            // It is a module name/path, or might be a functor call  ::  open/include |>M<| ...
            state
                    .markOptional(OCamlTypes.C_FUNCTOR_CALL)
                    .resolution(maybeFunctorCall)
                    .wrapWith(OCamlTypes.C_UPPER_SYMBOL);

            IElementType nextToken = state.getTokenType();
            if (nextToken != OCamlTypes.DOT && nextToken != OCamlTypes.LPAREN && nextToken != OCamlTypes.WITH) {
                // Not a path, nor a functor, must close that open
                state.popCancel();
                state.popEnd();
            }
            if (nextToken == OCamlTypes.IN) {
                // let _ = let open M |>in<| ..
                state.advance();
            }
        } else if (state.is(OCamlTypes.C_VARIANT_DECLARATION)) {
            // Declaring a variant  ::  type t = | |>X<| ...
            state.wrapWith(OCamlTypes.C_UPPER_IDENTIFIER);
        } else if (state.is(OCamlTypes.C_EXCEPTION_DECLARATION)) {
            // Declaring an exception  ::  exception |>X<| ...
            state.wrapWith(OCamlTypes.C_UPPER_IDENTIFIER);
        } else {
            if (state.isDummy() && state.isPrevious(OCamlTypes.C_TYPE_BINDING)) {
                // Might be a variant declaration without a pipe
                IElementType nextToken = state.lookAhead(1);
                if (nextToken == OCamlTypes.OF || nextToken == OCamlTypes.PIPE) {
                    // type t = |>X<| | ..   or   type t = |>X<| of ..
                    if (state.previousElementType1 == OCamlTypes.PIPE) {
                        state.advance();
                    }
                    state.mark(OCamlTypes.C_VARIANT_DECLARATION).wrapWith(OCamlTypes.C_UPPER_IDENTIFIER);
                    return;
                }
            } else if (state.isCurrentResolution(moduleBinding)) {
                // It might be a functor call, or just an alias ::  module M = |>X<| ( ... )
                state.markOptional(OCamlTypes.C_FUNCTOR_CALL).resolution(maybeFunctorCall);
            } else {
                IElementType nextToken = state.lookAhead(1);
                if (((state.is(OCamlTypes.C_PATTERN_MATCH_EXPR) || state.is(OCamlTypes.C_LET_BINDING)))
                        && nextToken != OCamlTypes.DOT) {
                    // Pattern matching a variant or using it
                    // match c with | |>X<| ... / let x = |>X<| ...
                    state.remapCurrentToken(OCamlTypes.VARIANT_NAME).wrapWith(OCamlTypes.C_VARIANT);
                    return;
                }
            }

            state.wrapWith(OCamlTypes.C_UPPER_SYMBOL);
        }
    }

    private void parseOpen(@NotNull ParserState state) {
        if (state.is(OCamlTypes.C_LET_DECLARATION)) {
            // let open X (coq/indtypes.ml)
            state.updateCurrentCompositeElementType(OCamlTypes.C_OPEN);
        } else {
            state.popEndUntilScope();
            state.mark(OCamlTypes.C_OPEN);
        }
    }

    private void parseInclude(@NotNull ParserState state) {
        state.popEndUntilScope();
        state.mark(OCamlTypes.C_INCLUDE).setStart();
    }

    private void parseExternal(@NotNull ParserState state) {
        state.popEndUntilScope();
        state.mark(OCamlTypes.C_EXTERNAL_DECLARATION).setStart();
    }

    private void parseType(@NotNull ParserState state) {
        if (    // module |>type<| M = ...
                state.isCurrentResolution(module) ||
                        // let x : |>type<| ...
                        state.is(OCamlTypes.C_TYPE_VARIABLE) ||
                        // class |>type<| ...
                        state.is(OCamlTypes.C_CLASS_DECLARATION) ||
                        // module M : X with |>type<| t ...
                        // include X with |>type<| t ...
                        // ?
                        (state.is(OCamlTypes.C_CONSTRAINT) && (state.previousElementType1 == OCamlTypes.WITH || state.previousElementType1 == OCamlTypes.AND))) {
            return;
        }

        state.popEndUntilScope();
        state.mark(OCamlTypes.C_TYPE_DECLARATION).setStart();
    }

    private void parseException(@NotNull ParserState state) {
        if (state.previousElementType1 != OCamlTypes.PIPE) {
            state.popEndUntilScope();
            state.mark(OCamlTypes.C_EXCEPTION_DECLARATION);
        }
    }

    private void parseDirectiveIf(@NotNull ParserState state) {
        state.popEndUntilScope();
        state.mark(OCamlTypes.C_DIRECTIVE).setStart();
    }

    private void parseDirectiveElse(@NotNull ParserState state) {
        state.popEndUntil(OCamlTypes.C_DIRECTIVE);
    }

    private void parseDirectiveElif(@NotNull ParserState state) {
        state.popEndUntil(OCamlTypes.C_DIRECTIVE);
    }

    private void parseDirectiveEnd(@NotNull ParserState state) {
        state.popEndUntil(OCamlTypes.C_DIRECTIVE);
        if (state.is(OCamlTypes.C_DIRECTIVE)) {
            state.advance().popEnd();
        }
    }

    private void parseVal(@NotNull ParserState state) {
        endLikeSemi(state);
        state
                .mark(state.is(OCamlTypes.C_OBJECT) ? OCamlTypes.C_CLASS_FIELD : OCamlTypes.C_VAL_DECLARATION)
                .setStart();
    }

    private void parseMethod(@NotNull ParserState state) {
        state.popEndUntilScope();
        state.mark(OCamlTypes.C_CLASS_METHOD).setStart();
    }

    private void parseLet(@NotNull ParserState state) {
        endLikeSemi(state); // state.popEndUntilScope();
        state.mark(OCamlTypes.C_LET_DECLARATION).setStart();
    }

    private void parseModule(@NotNull ParserState state) {
        if (state.is(OCamlTypes.C_LET_DECLARATION)) {
            state.resolution(module).updateCurrentCompositeElementType(OCamlTypes.C_MODULE_DECLARATION);
        } else if (!state.is(OCamlTypes.C_MACRO_NAME)) {
            if (!state.is(OCamlTypes.C_MODULE_TYPE)) {
                state.popEndUntilScope();
            }
            state.mark(OCamlTypes.C_MODULE_DECLARATION).resolution(module).setStart();
        }
    }

    private void parseClass(@NotNull ParserState state) {
        endLikeSemi(state);
        state.mark(OCamlTypes.C_CLASS_DECLARATION).setStart();
    }

    private void endLikeSemi(@NotNull ParserState state) {
        if (state.previousElementType1 != OCamlTypes.EQ
                && state.previousElementType1 != OCamlTypes.RIGHT_ARROW
                && state.previousElementType1 != OCamlTypes.TRY
                && state.previousElementType1 != OCamlTypes.SEMI
                && state.previousElementType1 != OCamlTypes.THEN
                && state.previousElementType1 != OCamlTypes.ELSE
                && state.previousElementType1 != OCamlTypes.IN
                && state.previousElementType1 != OCamlTypes.LPAREN
                && state.previousElementType1 != OCamlTypes.DO
                && state.previousElementType1 != OCamlTypes.STRUCT
                && state.previousElementType1 != OCamlTypes.SIG
                && state.previousElementType1 != OCamlTypes.COLON) {
            ParserScope parserScope = state.getLatestScope();
            while (parserScope != null && !parserScope.hasScope()) {
                state.popEnd();
                parserScope = state.getLatestScope();
            }
        }
    }
}
