package university.innopolis.f.runtime.builtin

import university.innopolis.f.grammar.FBoolean
import university.innopolis.f.grammar.FElement
import university.innopolis.f.grammar.FLiteral
import university.innopolis.f.runtime.FContext
import university.innopolis.f.runtime.FRuntimeException
import university.innopolis.f.runtime.FValue
import university.innopolis.f.runtime.TargetWrapper

fun equal(
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
                rhsRaw is FValue.Quote &&
                rhsRaw.value is FElement.Literal -> {
                Pair(lhsRaw.value.value, rhsRaw.value.value)
            }
            else -> {
                yield(Result.failure(FRuntimeException.TypeError()))
                return@sequence
            }
        }

    target.value =
        FValue.Quote(
            FElement.Literal(
                FLiteral.Boolean(
                    FBoolean(
                        when {
                            lhs is FLiteral.Integer && rhs is FLiteral.Integer -> {
                                lhs.inner.inner == rhs.inner.inner
                            }
                            lhs is FLiteral.Real && rhs is FLiteral.Real -> {
                                lhs.inner.inner == rhs.inner.inner
                            }
                            lhs is FLiteral.Real && rhs is FLiteral.Integer -> {
                                lhs.inner.inner.compareTo(rhs.inner.inner.toBigDecimal()) == 0
                            }
                            lhs is FLiteral.Integer && rhs is FLiteral.Real -> {
                                lhs.inner.inner.toBigDecimal().compareTo(rhs.inner.inner) == 0
                            }
                            lhs is FLiteral.Boolean && rhs is FLiteral.Boolean -> {
                                lhs.inner.inner == rhs.inner.inner
                            }
                            else -> {
                                yield(Result.failure(FRuntimeException.TypeError()))
                                return@sequence
                            }
                        }
                    )
                )
            )
        )
}

fun nonequal(
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
                rhsRaw is FValue.Quote &&
                rhsRaw.value is FElement.Literal -> {
                Pair(lhsRaw.value.value, rhsRaw.value.value)
            }
            else -> {
                yield(Result.failure(FRuntimeException.TypeError()))
                return@sequence
            }
        }

    target.value =
        FValue.Quote(
            FElement.Literal(
                FLiteral.Boolean(
                    FBoolean(
                        when {
                            lhs is FLiteral.Integer && rhs is FLiteral.Integer -> {
                                lhs.inner.inner != rhs.inner.inner
                            }
                            lhs is FLiteral.Real && rhs is FLiteral.Real -> {
                                lhs.inner.inner != rhs.inner.inner
                            }
                            lhs is FLiteral.Real && rhs is FLiteral.Integer -> {
                                lhs.inner.inner.compareTo(rhs.inner.inner.toBigDecimal()) != 0
                            }
                            lhs is FLiteral.Integer && rhs is FLiteral.Real -> {
                                lhs.inner.inner.toBigDecimal().compareTo(rhs.inner.inner) != 0
                            }
                            lhs is FLiteral.Boolean && rhs is FLiteral.Boolean -> {
                                lhs.inner.inner != rhs.inner.inner
                            }
                            else -> {
                                yield(Result.failure(FRuntimeException.TypeError()))
                                return@sequence
                            }
                        }
                    )
                )
            )
        )
}

fun less(
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
                rhsRaw is FValue.Quote &&
                rhsRaw.value is FElement.Literal -> {
                Pair(lhsRaw.value.value, rhsRaw.value.value)
            }
            else -> {
                yield(Result.failure(FRuntimeException.TypeError()))
                return@sequence
            }
        }

    target.value =
        FValue.Quote(
            FElement.Literal(
                FLiteral.Boolean(
                    FBoolean(
                        when {
                            lhs is FLiteral.Integer && rhs is FLiteral.Integer -> {
                                lhs.inner.inner < rhs.inner.inner
                            }
                            lhs is FLiteral.Real && rhs is FLiteral.Real -> {
                                lhs.inner.inner < rhs.inner.inner
                            }
                            lhs is FLiteral.Real && rhs is FLiteral.Integer -> {
                                lhs.inner.inner < rhs.inner.inner.toBigDecimal()
                            }
                            lhs is FLiteral.Integer && rhs is FLiteral.Real -> {
                                lhs.inner.inner.toBigDecimal() < rhs.inner.inner
                            }
                            lhs is FLiteral.Boolean && rhs is FLiteral.Boolean -> {
                                lhs.inner.inner < rhs.inner.inner
                            }
                            else -> {
                                yield(Result.failure(FRuntimeException.TypeError()))
                                return@sequence
                            }
                        }
                    )
                )
            )
        )
}

fun lesseq(
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
                rhsRaw is FValue.Quote &&
                rhsRaw.value is FElement.Literal -> {
                Pair(lhsRaw.value.value, rhsRaw.value.value)
            }
            else -> {
                yield(Result.failure(FRuntimeException.TypeError()))
                return@sequence
            }
        }

    target.value =
        FValue.Quote(
            FElement.Literal(
                FLiteral.Boolean(
                    FBoolean(
                        when {
                            lhs is FLiteral.Integer && rhs is FLiteral.Integer -> {
                                lhs.inner.inner <= rhs.inner.inner
                            }
                            lhs is FLiteral.Real && rhs is FLiteral.Real -> {
                                lhs.inner.inner <= rhs.inner.inner
                            }
                            lhs is FLiteral.Real && rhs is FLiteral.Integer -> {
                                lhs.inner.inner <= rhs.inner.inner.toBigDecimal()
                            }
                            lhs is FLiteral.Integer && rhs is FLiteral.Real -> {
                                lhs.inner.inner.toBigDecimal() <= rhs.inner.inner
                            }
                            lhs is FLiteral.Boolean && rhs is FLiteral.Boolean -> {
                                lhs.inner.inner <= rhs.inner.inner
                            }
                            else -> {
                                yield(Result.failure(FRuntimeException.TypeError()))
                                return@sequence
                            }
                        }
                    )
                )
            )
        )
}

fun greater(
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
                rhsRaw is FValue.Quote &&
                rhsRaw.value is FElement.Literal -> {
                Pair(lhsRaw.value.value, rhsRaw.value.value)
            }
            else -> {
                yield(Result.failure(FRuntimeException.TypeError()))
                return@sequence
            }
        }

    target.value =
        FValue.Quote(
            FElement.Literal(
                FLiteral.Boolean(
                    FBoolean(
                        when {
                            lhs is FLiteral.Integer && rhs is FLiteral.Integer -> {
                                lhs.inner.inner > rhs.inner.inner
                            }
                            lhs is FLiteral.Real && rhs is FLiteral.Real -> {
                                lhs.inner.inner > rhs.inner.inner
                            }
                            lhs is FLiteral.Real && rhs is FLiteral.Integer -> {
                                lhs.inner.inner > rhs.inner.inner.toBigDecimal()
                            }
                            lhs is FLiteral.Integer && rhs is FLiteral.Real -> {
                                lhs.inner.inner.toBigDecimal() > rhs.inner.inner
                            }
                            lhs is FLiteral.Boolean && rhs is FLiteral.Boolean -> {
                                lhs.inner.inner > rhs.inner.inner
                            }
                            else -> {
                                yield(Result.failure(FRuntimeException.TypeError()))
                                return@sequence
                            }
                        }
                    )
                )
            )
        )
}

fun greatereq(
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
                rhsRaw is FValue.Quote &&
                rhsRaw.value is FElement.Literal -> {
                Pair(lhsRaw.value.value, rhsRaw.value.value)
            }
            else -> {
                yield(Result.failure(FRuntimeException.TypeError()))
                return@sequence
            }
        }

    target.value =
        FValue.Quote(
            FElement.Literal(
                FLiteral.Boolean(
                    FBoolean(
                        when {
                            lhs is FLiteral.Integer && rhs is FLiteral.Integer -> {
                                lhs.inner.inner >= rhs.inner.inner
                            }
                            lhs is FLiteral.Real && rhs is FLiteral.Real -> {
                                lhs.inner.inner >= rhs.inner.inner
                            }
                            lhs is FLiteral.Real && rhs is FLiteral.Integer -> {
                                lhs.inner.inner >= rhs.inner.inner.toBigDecimal()
                            }
                            lhs is FLiteral.Integer && rhs is FLiteral.Real -> {
                                lhs.inner.inner.toBigDecimal() >= rhs.inner.inner
                            }
                            lhs is FLiteral.Boolean && rhs is FLiteral.Boolean -> {
                                lhs.inner.inner >= rhs.inner.inner
                            }
                            else -> {
                                yield(Result.failure(FRuntimeException.TypeError()))
                                return@sequence
                            }
                        }
                    )
                )
            )
        )
}
