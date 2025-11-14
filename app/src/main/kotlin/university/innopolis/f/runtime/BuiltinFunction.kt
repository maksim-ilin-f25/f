package university.innopolis.f.runtime

import university.innopolis.f.grammar.*

fun plus(
    target: Wrapper<FValue?>,
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
                lhsRaw.value is FElementQuoted.Literal &&
                rhsRaw is FValue.Quote &&
                rhsRaw.value is FElementQuoted.Literal -> {
                Pair(lhsRaw.value.value, rhsRaw.value.value)
            }
            else -> {
                yield(Result.failure(FRuntimeException.TypeError()))
                return@sequence
            }
        }

    target.value =
        FValue.Quote(
            FElementQuoted.Literal(
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
    target: Wrapper<FValue?>,
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
                lhsRaw.value is FElementQuoted.Literal &&
                rhsRaw is FValue.Quote &&
                rhsRaw.value is FElementQuoted.Literal -> {
                Pair(lhsRaw.value.value, rhsRaw.value.value)
            }
            else -> {
                yield(Result.failure(FRuntimeException.TypeError()))
                return@sequence
            }
        }

    target.value =
        FValue.Quote(
            FElementQuoted.Literal(
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
    target: Wrapper<FValue?>,
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
                lhsRaw.value is FElementQuoted.Literal &&
                rhsRaw is FValue.Quote &&
                rhsRaw.value is FElementQuoted.Literal -> {
                Pair(lhsRaw.value.value, rhsRaw.value.value)
            }
            else -> {
                yield(Result.failure(FRuntimeException.TypeError()))
                return@sequence
            }
        }

    target.value =
        FValue.Quote(
            FElementQuoted.Literal(
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
    target: Wrapper<FValue?>,
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
                lhsRaw.value is FElementQuoted.Literal &&
                rhsRaw is FValue.Quote &&
                rhsRaw.value is FElementQuoted.Literal -> {
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
                FElementQuoted.Literal(
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

fun head(
    target: Wrapper<FValue?>,
    args: List<FValue>,
    _context: FContext,
): Sequence<Result<FValue>> = sequence {
    if (args.size != 1) {
        yield(Result.failure(FRuntimeException.InvalidNumOfArgs()))
        return@sequence
    }

    val quote = args.first()
    if (quote !is FValue.Quote || quote.value !is FElementQuoted.List) {
        yield(Result.failure(FRuntimeException.TypeError()))
        return@sequence
    }

    val headElement = quote.value.value.elements.firstOrNull()
    if (headElement == null) {
        yield(Result.failure(FRuntimeException.NotEnoughElements()))
        return@sequence
    }

    target.value =
        FValue.Quote(
            when (headElement) {
                is FElement.Atom -> FElementQuoted.Atom(headElement.value)
                is FElement.List -> FElementQuoted.List(headElement.value)
                is FElement.Quote -> {
                    when (headElement.value) {
                        is FElementQuoted.Atom -> FElementQuoted.Atom(headElement.value.value)
                        is FElementQuoted.Keyword -> FElementQuoted.Keyword(headElement.value.value)
                        is FElementQuoted.List -> FElementQuoted.List(headElement.value.value)
                        is FElementQuoted.Literal -> FElementQuoted.Literal(headElement.value.value)
                        is FElementQuoted.Quote -> FElementQuoted.Quote(headElement.value.value)
                    }
                }
            }
        )
}
