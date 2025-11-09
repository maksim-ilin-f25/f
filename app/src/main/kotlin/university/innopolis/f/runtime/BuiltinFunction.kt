package university.innopolis.f.runtime

import university.innopolis.f.grammar.FInteger
import university.innopolis.f.grammar.FReal

fun plus(args: List<FValue>): Sequence<Result<FValue>> = sequence {
    if (args.size != 2) {
        yield(Result.failure(FRuntimeException.InvalidNumOfArgs()))
        return@sequence
    }
    val (lhs, rhs) = args
    yield(
        when {
            lhs is FValue.Integer && rhs is FValue.Integer ->
                Result.success(FValue.Integer(FInteger(lhs.value.inner + rhs.value.inner)))

            lhs is FValue.Integer && rhs is FValue.Real ->
                Result.success(FValue.Real(FReal(lhs.value.inner.toBigDecimal() + rhs.value.inner)))

            lhs is FValue.Real && rhs is FValue.Integer ->
                Result.success(FValue.Real(FReal(lhs.value.inner + rhs.value.inner.toBigDecimal())))

            lhs is FValue.Real && rhs is FValue.Real ->
                Result.success(FValue.Real(FReal(lhs.value.inner + rhs.value.inner)))

            else -> Result.failure(FRuntimeException.TypeError())
        }
    )
}

fun minus(args: List<FValue>): Sequence<Result<FValue>> = sequence {
    if (args.size != 2) {
        yield(Result.failure(FRuntimeException.InvalidNumOfArgs()))
        return@sequence
    }
    val (lhs, rhs) = args
    yield(
        when {
            lhs is FValue.Integer && rhs is FValue.Integer ->
                Result.success(FValue.Integer(FInteger(lhs.value.inner - rhs.value.inner)))

            lhs is FValue.Integer && rhs is FValue.Real ->
                Result.success(FValue.Real(FReal(lhs.value.inner.toBigDecimal() - rhs.value.inner)))

            lhs is FValue.Real && rhs is FValue.Integer ->
                Result.success(FValue.Real(FReal(lhs.value.inner - rhs.value.inner.toBigDecimal())))

            lhs is FValue.Real && rhs is FValue.Real ->
                Result.success(FValue.Real(FReal(lhs.value.inner - rhs.value.inner)))

            else -> Result.failure(FRuntimeException.TypeError())
        }
    )
}

fun times(args: List<FValue>): Sequence<Result<FValue>> = sequence {
    if (args.size != 2) {
        yield(Result.failure(FRuntimeException.InvalidNumOfArgs()))
        return@sequence
    }
    val (lhs, rhs) = args
    yield(
        when {
            lhs is FValue.Integer && rhs is FValue.Integer ->
                Result.success(FValue.Integer(FInteger(lhs.value.inner * rhs.value.inner)))

            lhs is FValue.Integer && rhs is FValue.Real ->
                Result.success(FValue.Real(FReal(lhs.value.inner.toBigDecimal() * rhs.value.inner)))

            lhs is FValue.Real && rhs is FValue.Integer ->
                Result.success(FValue.Real(FReal(lhs.value.inner * rhs.value.inner.toBigDecimal())))

            lhs is FValue.Real && rhs is FValue.Real ->
                Result.success(FValue.Real(FReal(lhs.value.inner * rhs.value.inner)))

            else -> Result.failure(FRuntimeException.TypeError())
        }
    )
}

fun divide(args: List<FValue>): Sequence<Result<FValue>> = sequence {
    if (args.size != 2) {
        yield(Result.failure(FRuntimeException.InvalidNumOfArgs()))
        return@sequence
    }
    val (lhs, rhs) = args

    try {
        yield(
            when {
                lhs is FValue.Integer && rhs is FValue.Integer ->
                    Result.success(FValue.Integer(FInteger(lhs.value.inner.divide(rhs.value.inner))))

                lhs is FValue.Integer && rhs is FValue.Real ->
                    Result.success(FValue.Real(FReal(lhs.value.inner.toBigDecimal().divide(rhs.value.inner))))

                lhs is FValue.Real && rhs is FValue.Integer ->
                    Result.success(FValue.Real(FReal(lhs.value.inner.divide(rhs.value.inner.toBigDecimal()))))

                lhs is FValue.Real && rhs is FValue.Real ->
                    Result.success(FValue.Real(FReal(lhs.value.inner.divide(rhs.value.inner))))

                else -> Result.failure(FRuntimeException.TypeError())
            }
        )
    } catch (_: Exception) {
        yield(Result.failure(FRuntimeException.DivisionByZero()))
    }
}
