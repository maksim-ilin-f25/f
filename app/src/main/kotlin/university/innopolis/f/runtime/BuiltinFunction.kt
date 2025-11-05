package university.innopolis.f.runtime

import university.innopolis.f.grammar.FInteger
import university.innopolis.f.grammar.FReal

fun plus(arg1: FValue, arg2: FValue): Result<FValue> {
    return when {
        arg1 is FValue.Integer && arg2 is FValue.Integer ->
            Result.success(FValue.Integer(FInteger(arg1.value.inner + arg2.value.inner)))

        arg1 is FValue.Integer && arg2 is FValue.Real ->
            Result.success(FValue.Real(FReal(arg1.value.inner.toBigDecimal() + arg2.value.inner)))

        arg1 is FValue.Real && arg2 is FValue.Integer ->
            Result.success(FValue.Real(FReal(arg1.value.inner + arg2.value.inner.toBigDecimal())))

        arg1 is FValue.Real && arg2 is FValue.Real ->
            Result.success(FValue.Real(FReal(arg1.value.inner + arg2.value.inner)))

        else -> Result.failure(FRuntimeException.TypeError())
    }
}
