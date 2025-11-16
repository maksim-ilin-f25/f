package university.innopolis.f.runtime.builtin

import university.innopolis.f.grammar.FElement
import university.innopolis.f.grammar.FInteger
import university.innopolis.f.grammar.FLiteral
import university.innopolis.f.grammar.FReal
import university.innopolis.f.runtime.FContext
import university.innopolis.f.runtime.FRuntimeException
import university.innopolis.f.runtime.FValue
import university.innopolis.f.runtime.TargetWrapper

fun plus(
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
                when {
                    lhs is FLiteral.Integer && rhs is FLiteral.Integer -> {
                        FLiteral.Integer(FInteger(lhs.inner.inner + rhs.inner.inner))
                    }
                    lhs is FLiteral.Integer && rhs is FLiteral.Real -> {
                        FLiteral.Real(FReal(lhs.inner.inner.toBigDecimal() + rhs.inner.inner))
                    }
                    lhs is FLiteral.Real && rhs is FLiteral.Integer -> {
                        FLiteral.Real(FReal(lhs.inner.inner + rhs.inner.inner.toBigDecimal()))
                    }
                    lhs is FLiteral.Real && rhs is FLiteral.Real -> {
                        FLiteral.Real(FReal(lhs.inner.inner + rhs.inner.inner))
                    }
                    else -> {
                        yield(Result.failure(FRuntimeException.TypeError()))
                        return@sequence
                    }
                }
            )
        )
}

fun minus(
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
                when {
                    lhs is FLiteral.Integer && rhs is FLiteral.Integer -> {
                        FLiteral.Integer(FInteger(lhs.inner.inner - rhs.inner.inner))
                    }
                    lhs is FLiteral.Integer && rhs is FLiteral.Real -> {
                        FLiteral.Real(FReal(lhs.inner.inner.toBigDecimal() - rhs.inner.inner))
                    }
                    lhs is FLiteral.Real && rhs is FLiteral.Integer -> {
                        FLiteral.Real(FReal(lhs.inner.inner - rhs.inner.inner.toBigDecimal()))
                    }
                    lhs is FLiteral.Real && rhs is FLiteral.Real -> {
                        FLiteral.Real(FReal(lhs.inner.inner - rhs.inner.inner))
                    }
                    else -> {
                        yield(Result.failure(FRuntimeException.TypeError()))
                        return@sequence
                    }
                }
            )
        )
}

fun times(
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
                when {
                    lhs is FLiteral.Integer && rhs is FLiteral.Integer -> {
                        FLiteral.Integer(FInteger(lhs.inner.inner * rhs.inner.inner))
                    }
                    lhs is FLiteral.Integer && rhs is FLiteral.Real -> {
                        FLiteral.Real(FReal(lhs.inner.inner.toBigDecimal() * rhs.inner.inner))
                    }
                    lhs is FLiteral.Real && rhs is FLiteral.Integer -> {
                        FLiteral.Real(FReal(lhs.inner.inner * rhs.inner.inner.toBigDecimal()))
                    }
                    lhs is FLiteral.Real && rhs is FLiteral.Real -> {
                        FLiteral.Real(FReal(lhs.inner.inner * rhs.inner.inner))
                    }
                    else -> {
                        yield(Result.failure(FRuntimeException.TypeError()))
                        return@sequence
                    }
                }
            )
        )
}

fun divide(
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

    try {
        target.value =
            FValue.Quote(
                FElement.Literal(
                    when {
                        lhs is FLiteral.Integer && rhs is FLiteral.Integer -> {
                            FLiteral.Integer(FInteger(lhs.inner.inner.divide(rhs.inner.inner)))
                        }
                        lhs is FLiteral.Integer && rhs is FLiteral.Real -> {
                            FLiteral.Real(
                                FReal(lhs.inner.inner.toBigDecimal().divide(rhs.inner.inner))
                            )
                        }
                        lhs is FLiteral.Real && rhs is FLiteral.Integer -> {
                            FLiteral.Real(
                                FReal(lhs.inner.inner.divide(rhs.inner.inner.toBigDecimal()))
                            )
                        }
                        lhs is FLiteral.Real && rhs is FLiteral.Real -> {
                            FLiteral.Real(FReal(lhs.inner.inner.divide(rhs.inner.inner)))
                        }
                        else -> {
                            yield(Result.failure(FRuntimeException.TypeError()))
                            return@sequence
                        }
                    }
                )
            )
    } catch (_: Exception) {
        yield(Result.failure(FRuntimeException.DivisionByZero()))
    }
}
