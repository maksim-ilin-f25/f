package university.innopolis.f.runtime

import university.innopolis.f.grammar.FInteger
import university.innopolis.f.grammar.FReal

fun plus(
    target: Wrapper<FValue?>,
    args: List<FValue>,
    _context: FContext,
): Sequence<Result<FValue>> = sequence {
    if (args.size != 2) {
        yield(Result.failure(FRuntimeException.InvalidNumOfArgs()))
        return@sequence
    }
    val (lhs, rhs) = args
    target.value =
        when {
            lhs is FValue.Quote &&
                lhs.value is FValueQuoted.Integer &&
                rhs is FValue.Quote &&
                rhs.value is FValueQuoted.Integer -> {
                FValue.Quote(
                    FValueQuoted.Integer(FInteger(lhs.value.value.inner + rhs.value.value.inner))
                )
            }

            lhs is FValue.Quote &&
                lhs.value is FValueQuoted.Integer &&
                rhs is FValue.Quote &&
                rhs.value is FValueQuoted.Real -> {
                FValue.Quote(
                    FValueQuoted.Real(
                        FReal(lhs.value.value.inner.toBigDecimal() + rhs.value.value.inner)
                    )
                )
            }

            lhs is FValue.Quote &&
                lhs.value is FValueQuoted.Real &&
                rhs is FValue.Quote &&
                rhs.value is FValueQuoted.Integer -> {
                FValue.Quote(
                    FValueQuoted.Real(
                        FReal(lhs.value.value.inner + rhs.value.value.inner.toBigDecimal())
                    )
                )
            }

            lhs is FValue.Quote &&
                lhs.value is FValueQuoted.Real &&
                rhs is FValue.Quote &&
                rhs.value is FValueQuoted.Real -> {
                FValue.Quote(
                    FValueQuoted.Real(FReal(lhs.value.value.inner + rhs.value.value.inner))
                )
            }

            else -> {
                yield(Result.failure(FRuntimeException.TypeError()))
                return@sequence
            }
        }
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
    val (lhs, rhs) = args
    target.value =
        when {
            lhs is FValue.Quote &&
                lhs.value is FValueQuoted.Integer &&
                rhs is FValue.Quote &&
                rhs.value is FValueQuoted.Integer -> {
                FValue.Quote(
                    FValueQuoted.Integer(FInteger(lhs.value.value.inner - rhs.value.value.inner))
                )
            }

            lhs is FValue.Quote &&
                lhs.value is FValueQuoted.Integer &&
                rhs is FValue.Quote &&
                rhs.value is FValueQuoted.Real -> {
                FValue.Quote(
                    FValueQuoted.Real(
                        FReal(lhs.value.value.inner.toBigDecimal() - rhs.value.value.inner)
                    )
                )
            }

            lhs is FValue.Quote &&
                lhs.value is FValueQuoted.Real &&
                rhs is FValue.Quote &&
                rhs.value is FValueQuoted.Integer -> {
                FValue.Quote(
                    FValueQuoted.Real(
                        FReal(lhs.value.value.inner - rhs.value.value.inner.toBigDecimal())
                    )
                )
            }

            lhs is FValue.Quote &&
                lhs.value is FValueQuoted.Real &&
                rhs is FValue.Quote &&
                rhs.value is FValueQuoted.Real -> {
                FValue.Quote(
                    FValueQuoted.Real(FReal(lhs.value.value.inner - rhs.value.value.inner))
                )
            }

            else -> {
                yield(Result.failure(FRuntimeException.TypeError()))
                return@sequence
            }
        }
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
    val (lhs, rhs) = args
    target.value =
        when {
            lhs is FValue.Quote &&
                lhs.value is FValueQuoted.Integer &&
                rhs is FValue.Quote &&
                rhs.value is FValueQuoted.Integer -> {
                FValue.Quote(
                    FValueQuoted.Integer(FInteger(lhs.value.value.inner * rhs.value.value.inner))
                )
            }

            lhs is FValue.Quote &&
                lhs.value is FValueQuoted.Integer &&
                rhs is FValue.Quote &&
                rhs.value is FValueQuoted.Real -> {
                FValue.Quote(
                    FValueQuoted.Real(
                        FReal(lhs.value.value.inner.toBigDecimal() * rhs.value.value.inner)
                    )
                )
            }

            lhs is FValue.Quote &&
                lhs.value is FValueQuoted.Real &&
                rhs is FValue.Quote &&
                rhs.value is FValueQuoted.Integer -> {
                FValue.Quote(
                    FValueQuoted.Real(
                        FReal(lhs.value.value.inner * rhs.value.value.inner.toBigDecimal())
                    )
                )
            }

            lhs is FValue.Quote &&
                lhs.value is FValueQuoted.Real &&
                rhs is FValue.Quote &&
                rhs.value is FValueQuoted.Real -> {
                FValue.Quote(
                    FValueQuoted.Real(FReal(lhs.value.value.inner * rhs.value.value.inner))
                )
            }

            else -> {
                yield(Result.failure(FRuntimeException.TypeError()))
                return@sequence
            }
        }
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
    val (lhs, rhs) = args

    try {
        target.value =
            when {
                lhs is FValue.Quote &&
                    lhs.value is FValueQuoted.Integer &&
                    rhs is FValue.Quote &&
                    rhs.value is FValueQuoted.Integer -> {
                    FValue.Quote(
                        FValueQuoted.Integer(
                            FInteger(lhs.value.value.inner.divide(rhs.value.value.inner))
                        )
                    )
                }

                lhs is FValue.Quote &&
                    lhs.value is FValueQuoted.Integer &&
                    rhs is FValue.Quote &&
                    rhs.value is FValueQuoted.Real -> {
                    FValue.Quote(
                        FValueQuoted.Real(
                            FReal(
                                lhs.value.value.inner.toBigDecimal().divide(rhs.value.value.inner)
                            )
                        )
                    )
                }

                lhs is FValue.Quote &&
                    lhs.value is FValueQuoted.Real &&
                    rhs is FValue.Quote &&
                    rhs.value is FValueQuoted.Integer -> {
                    FValue.Quote(
                        FValueQuoted.Real(
                            FReal(
                                lhs.value.value.inner.divide(rhs.value.value.inner.toBigDecimal())
                            )
                        )
                    )
                }

                lhs is FValue.Quote &&
                    lhs.value is FValueQuoted.Real &&
                    rhs is FValue.Quote &&
                    rhs.value is FValueQuoted.Real -> {
                    FValue.Quote(
                        FValueQuoted.Real(
                            FReal(lhs.value.value.inner.divide(rhs.value.value.inner))
                        )
                    )
                }

                else -> {
                    yield(Result.failure(FRuntimeException.TypeError()))
                    return@sequence
                }
            }
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
    if (quote !is FValue.Quote || quote.value !is FValueQuoted.ValueList) {
        yield(Result.failure(FRuntimeException.TypeError()))
        return@sequence
    }

    val headElement = quote.value.value.firstOrNull()
    if (headElement == null) {
        yield(Result.failure(FRuntimeException.NotEnoughElements()))
        return@sequence
    }

    target.value = headElement
}
