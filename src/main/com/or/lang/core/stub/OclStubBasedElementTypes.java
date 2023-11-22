package com.or.lang.core.stub;

import com.intellij.psi.stubs.IStubElementType;
import com.or.lang.core.psi.*;
import com.or.lang.core.psi.impl.PsiObjectField;
import com.or.lang.core.stub.type.*;

public interface OclStubBasedElementTypes {
    IStubElementType<PsiModuleStub, PsiModule> C_FAKE_MODULE = new PsiFakeModuleStubElementType();
    IStubElementType<PsiModuleStub, PsiModule> C_FUNCTOR_DECLARATION = new PsiFunctorModuleStubElementType();
    IStubElementType<PsiModuleStub, PsiModule> C_MODULE_DECLARATION = new PsiInnerModuleStubElementType();

    IStubElementType<PsiKlassStub, PsiKlass> C_CLASS_DECLARATION = new PsiKlassStubElementType();
    IStubElementType<PsiExceptionStub, PsiException> C_EXCEPTION_DECLARATION = new PsiExceptionStubElementType();
    IStubElementType<PsiTypeStub, PsiType> C_TYPE_DECLARATION = new PsiTypeStubElementType();
    IStubElementType<PsiExternalStub, PsiExternal> C_EXTERNAL_DECLARATION = new PsiExternalStubElementType();
    IStubElementType<PsiLetStub, PsiLet> C_LET_DECLARATION = new PsiLetStubElementType();
    IStubElementType<PsiRecordFieldStub, PsiRecordField> C_RECORD_FIELD = new PsiRecordFieldStubElementType();
    IStubElementType<PsiObjectFieldStub, PsiObjectField> C_OBJECT_FIELD = new PsiObjectFieldStubElementType();
    IStubElementType<PsiValStub, PsiVal> C_VAL_DECLARATION = new PsiValStubElementType();
    IStubElementType<PsiVariantDeclarationStub, PsiVariantDeclaration> C_VARIANT_DECLARATION = new PsiVariantStubElementType();

    IStubElementType<PsiIncludeStub, PsiInclude> C_INCLUDE = new PsiIncludeStubElementType();
    IStubElementType<PsiOpenStub, PsiOpen> C_OPEN = new PsiOpenStubElementType();

    // ?
    IStubElementType<PsiParameterStub, PsiParameter> C_FUN_PARAM = new PsiParameterStubElementType("C_FUN_PARAM");
    IStubElementType<PsiParameterStub, PsiParameter> C_FUNCTOR_PARAM = new PsiParameterStubElementType("C_FUNCTOR_PARAM");
}
