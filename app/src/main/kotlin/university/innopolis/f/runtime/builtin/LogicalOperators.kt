package university.innopolis.f.runtime.builtin

import university.innopolis.f.grammar.FBoolean
import university.innopolis.f.grammar.FElement
import university.innopolis.f.grammar.FLiteral
import university.innopolis.f.runtime.FContext
import university.innopolis.f.runtime.FRuntimeException
import university.innopolis.f.runtime.FValue
import university.innopolis.f.runtime.TargetWrapper

fun and(
    target: TargetWrapper<FValue?>,
    args: List<FValue>,
    _context: FContext,
): Sequence<Result<FValue>> = sequence {
    if (args.size != 2) {
        yield(Result.failure(FRuntimeException.InvalidNumOfArgs()))
        return@sequence
    }
    val (lhsRaw, rhsRaw) = args
    val (lhs, rhs) =
        when {
            lhsRaw is FValue.Quote &&
                lhsRaw.value is FElement.Literal &&
                lhsRaw.value.value is FLiteral.Boolean &&
                rhsRaw is FValue.Quote &&
                rhsRaw.value is FElement.Literal &&
                rhsRaw.value.value is FLiteral.Boolean -> {
                Pair(lhsRaw.value.value.inner.inner, rhsRaw.value.value.inner.inner)
            }
            else -> {
                yield(Result.failure(FRuntimeException.TypeError()))
                return@sequence
            }
        }

    target.value = FValue.Quote(FElement.Literal(FLiteral.Boolean(FBoolean(lhs && rhs))))
}

fun or(
    target: TargetWrapper<FValue?>,
    args: List<FValue>,
    _context: FContext,
): Sequence<Result<FValue>> = sequence {
    if (args.size != 2) {
        yield(Result.failure(FRuntimeException.InvalidNumOfArgs()))
        return@sequence
    }
    val (lhsRaw, rhsRaw) = args
    val (lhs, rhs) =
        when {
            lhsRaw is FValue.Quote &&
                lhsRaw.value is FElement.Literal &&
                lhsRaw.value.value is FLiteral.Boolean &&
                rhsRaw is FValue.Quote &&
                rhsRaw.value is FElement.Literal &&
                rhsRaw.value.value is FLiteral.Boolean -> {
                Pair(lhsRaw.value.value.inner.inner, rhsRaw.value.value.inner.inner)
            }
            else -> {
                yield(Result.failure(FRuntimeException.TypeError()))
                return@sequence
            }
        }

    target.value = FValue.Quote(FElement.Literal(FLiteral.Boolean(FBoolean(lhs || rhs))))
}

fun xor(
    target: TargetWrapper<FValue?>,
    args: List<FValue>,
    _context: FContext,
): Sequence<Result<FValue>> = sequence {
    if (args.size != 2) {
        yield(Result.failure(FRuntimeException.InvalidNumOfArgs()))
        return@sequence
    }
    val (lhsRaw, rhsRaw) = args
    val (lhs, rhs) =
        when {
            lhsRaw is FValue.Quote &&
                lhsRaw.value is FElement.Literal &&
                lhsRaw.value.value is FLiteral.Boolean &&
                rhsRaw is FValue.Quote &&
                rhsRaw.value is FElement.Literal &&
                rhsRaw.value.value is FLiteral.Boolean -> {
                Pair(lhsRaw.value.value.inner.inner, rhsRaw.value.value.inner.inner)
            }
            else -> {
                yield(Result.failure(FRuntimeException.TypeError()))
                return@sequence
            }
        }

    target.value = FValue.Quote(FElement.Literal(FLiteral.Boolean(FBoolean(lhs != rhs))))
}

fun not(
    target: TargetWrapper<FValue?>,
    args: List<FValue>,
    _context: FContext,
): Sequence<Result<FValue>> = sequence {
    if (args.size != 1) {
        yield(Result.failure(FRuntimeException.InvalidNumOfArgs()))
        return@sequence
    }
    val argRaw = args.first()
    val arg =
        when {
            argRaw is FValue.Quote &&
                argRaw.value is FElement.Literal &&
                argRaw.value.value is FLiteral.Boolean -> {
                argRaw.value.value.inner.inner
            }
            else -> {
                yield(Result.failure(FRuntimeException.TypeError()))
                return@sequence
            }
        }

    target.value = FValue.Quote(FElement.Literal(FLiteral.Boolean(FBoolean(!arg))))
}
