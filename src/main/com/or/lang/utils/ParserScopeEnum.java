package com.or.lang.utils;

public enum ParserScopeEnum {
    module,
    moduleNamedColon,
    moduleBinding,
    moduleTypeExtraction,

    maybeFunctionParameters,
    functionMatch,

    objectFieldNamed,

    matchWith,

    genericExpression,

    functorNamedEq,
    functorNamedColon,
    maybeFunctorCall,

}
