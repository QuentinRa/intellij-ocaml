package com.or.lang.core.psi.impl;

import com.intellij.lang.ASTFactory;
import com.intellij.psi.impl.source.tree.CompositeElement;
import com.intellij.psi.impl.source.tree.CompositePsiElement;
import com.intellij.psi.impl.source.tree.LeafElement;
import com.intellij.psi.tree.IElementType;
import com.or.lang.OCamlTypes;
import com.or.lang.core.psi.PsiDefaultValue;
import com.or.lang.core.psi.PsiLetBinding;
import com.or.lang.core.psi.PsiLiteralExpression;
import com.or.lang.core.psi.PsiNamedParam;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ORASTFactory extends ASTFactory {
    @Override
    public @Nullable CompositeElement createComposite(@NotNull IElementType type) {
        if (type == OCamlTypes.C_LET_BINDING) {
            return new PsiLetBinding(type);
        }
        if (type == OCamlTypes.C_DEFAULT_VALUE) {
            return new PsiDefaultValue(type);
        }
        if (type == OCamlTypes.C_SIG_EXPR) {
            return new PsiSignatureImpl(type);
        }
        if (type == OCamlTypes.C_TAG) {
            return new PsiTag(type);
        }
        if (type == OCamlTypes.C_TAG_BODY) {
            return new PsiTagBody(type);
        }
        if (type == OCamlTypes.C_TAG_CLOSE) {
            return new PsiTagClose(type);
        }
        if (type == OCamlTypes.C_TAG_PROPERTY) {
            return new PsiTagPropertyImpl(type);
        }
        if (type == OCamlTypes.C_TAG_PROP_VALUE) {
            return new PsiTagPropertyValueImpl(type);
        }
        if (type == OCamlTypes.C_TAG_START) {
            return new PsiTagStartImpl(type);
        }
        if (type == OCamlTypes.C_FUN_CALL_PARAMS) {
            return new PsiFunctionCallParamsImpl(type);
        }
        if (type == OCamlTypes.C_FUN_BODY) {
            return new PsiFunctionBody(type);
        }
        if (type == OCamlTypes.C_SIG_ITEM) {
            return new PsiSignatureItemImpl(type);
        }
        if (type == OCamlTypes.C_CUSTOM_OPERATOR || type == OCamlTypes.C_SCOPED_EXPR || type == OCamlTypes.C_IF_THEN_SCOPE || type == OCamlTypes.C_DO_LOOP) {
            return new PsiScopedExpr(type);
        }
        if (type == OCamlTypes.C_LOCAL_OPEN) {
            return new PsiLocalOpen(type);
        }
        if (type == OCamlTypes.C_NAMED_PARAM) {
            return new PsiNamedParam(type);
        }
        if (type == OCamlTypes.C_PARAMETERS) {
            return new PsiParametersImpl(type);
        }
        if (type == OCamlTypes.C_PATTERN_MATCH_BODY) {
            return new PsiPatternMatchBody(type);
        }
        if (type == OCamlTypes.C_PATTERN_MATCH_EXPR) {
            return new PsiPatternMatch(type);
        }
        if (type == OCamlTypes.C_JS_OBJECT) {
            return new PsiJsObject(type);
        }
        if (type == OCamlTypes.C_FUN_EXPR) {
            return new PsiFunctionImpl(type);
        }
        if (type == OCamlTypes.C_FUN_PARAMS
                || type == OCamlTypes.C_FUNCTOR_PARAMS
                || type == OCamlTypes.C_VARIANT_CONSTRUCTOR) {
            return new PsiParametersImpl(type);
        }
        if (type == OCamlTypes.C_DECONSTRUCTION) {
            return new PsiDeconstruction(type);
        }
        if (type == OCamlTypes.C_TYPE_BINDING) {
            return new PsiTypeBinding(type);
        }
        if (type == OCamlTypes.C_RECORD_EXPR) {
            return new PsiRecord(type);
        }
        if (type == OCamlTypes.C_MODULE_TYPE) {
            return new PsiModuleType(type);
        }
        if (type == OCamlTypes.C_ANNOTATION) {
            return new PsiAnnotationImpl(type);
        }
        if (type == OCamlTypes.C_MACRO_RAW_BODY) {
            return new PsiMacroBody(type);
        }
        if (type == OCamlTypes.C_FUNCTOR_CALL) {
            return new PsiFunctorCall(type);
        }
        if (type == OCamlTypes.C_CONSTRAINTS) {
            return new PsiConstraints(type);
        }
        if (type == OCamlTypes.C_CONSTRAINT) {
            return new PsiConstraint(type);
        }
        if (type == OCamlTypes.C_ASSERT_STMT) {
            return new PsiAssert(type);
        }
        if (type == OCamlTypes.C_IF) {
            return new PsiIfStatement(type);
        }
        if (type == OCamlTypes.C_OBJECT) {
            return new PsiObject(type);
        }
        if (type == OCamlTypes.C_OPTION) {
            return new PsiOption(type);
        }
        if (type == OCamlTypes.C_CLASS_PARAMS) {
            return new PsiClassParameters(type);
        }
        if (type == OCamlTypes.C_CLASS_CONSTR) {
            return new PsiClassConstructor(type);
        }
        if (type == OCamlTypes.C_CLASS_FIELD) {
            return new PsiClassField(type);
        }
        if (type == OCamlTypes.C_CLASS_METHOD) {
            return new PsiClassMethod(type);
        }
        if (type == OCamlTypes.C_SWITCH_EXPR || type == OCamlTypes.C_MATCH_EXPR) {
            return new PsiSwitchImpl(type);
        }
        if (type == OCamlTypes.C_FUNCTOR_BINDING) {
            return new PsiFunctorBinding(type);
        }
        if (type == OCamlTypes.C_FUNCTOR_RESULT) {
            return new PsiFunctorResult(type);
        }
        if (type == OCamlTypes.C_BINARY_CONDITION) {
            return new PsiBinaryCondition(type);
        }
        if (type == OCamlTypes.C_TERNARY) {
            return new PsiTernary(type);
        }
        if (type == OCamlTypes.C_MIXIN_FIELD) {
            return new PsiMixinField(type);
        }
        if (type == OCamlTypes.C_LET_ATTR) {
            return new PsiLetAttribute(type);
        }
        if (type == OCamlTypes.C_LOWER_BOUND_CONSTRAINT) {
            return new PsiTypeConstraint(type);
        }
        if (type == OCamlTypes.C_MACRO_EXPR) {
            return new PsiMacro(type);
        }
        if (type == OCamlTypes.C_INTERPOLATION_EXPR) {
            return new PsiInterpolation(type);
        }
        if (type == OCamlTypes.C_INTERPOLATION_REF) {
            return new PsiInterpolationReference(type);
        }
        if (type == OCamlTypes.C_TRY_EXPR) {
            return new PsiTry(type);
        }
        if (type == OCamlTypes.C_ML_INTERPOLATOR) {
            return new PsiMultiLineInterpolator(type);
        }
        if (type == OCamlTypes.C_DIRECTIVE) {
            return new PsiDirective(type);
        }
        if (type == OCamlTypes.C_STRUCT_EXPR) {
            return new PsiStruct(type);
        }
        if (type == OCamlTypes.C_UNIT) {
            return new PsiUnit(type);
        }
        if (type == OCamlTypes.C_WHILE) {
            return new PsiWhile(type);
        }
        // Generic
        if (type == OCamlTypes.C_TRY_HANDLERS
                || type == OCamlTypes.C_TRY_HANDLER
                || type == OCamlTypes.C_TRY_BODY
                || type == OCamlTypes.C_INTERPOLATION_PART
                || type == OCamlTypes.C_TYPE_VARIABLE) {
            return new CompositePsiElement(type) {
            };
        }
        // Leaf !?
        if (type == OCamlTypes.C_LOWER_IDENTIFIER) {
            return new PsiLowerIdentifier(type);
        }
        if (type == OCamlTypes.C_LOWER_SYMBOL) {
            return new PsiLowerSymbolImpl(type);
        }
        if (type == OCamlTypes.C_UPPER_IDENTIFIER) {
            return new PsiUpperIdentifier(type);
        }
        if (type == OCamlTypes.C_UPPER_BOUND_CONSTRAINT) {
            return new PsiTypeConstraint(type);
        }
        if (type == OCamlTypes.C_UPPER_SYMBOL) {
            return new PsiUpperSymbolImpl(type);
        }
        if (type == OCamlTypes.C_VARIANT) {
            return new PsiUpperSymbolImpl(type);
        }
        if (type == OCamlTypes.C_MACRO_NAME) {
            return new PsiMacroName(type);
        }

        return null;
    }

    @Override
    public @Nullable LeafElement createLeaf(@NotNull IElementType type, @NotNull CharSequence text) {
        if (type == OCamlTypes.PROPERTY_NAME) {
            return new PsiLeafPropertyName(type, text);
        }
        if (type == OCamlTypes.TAG_NAME) {
            return new PsiLeafTagName(type, text);
        }
        if (type == OCamlTypes.STRING_VALUE) {
            return new PsiLiteralExpression(type, text);
        }

        return super.createLeaf(type, text);
    }
}
