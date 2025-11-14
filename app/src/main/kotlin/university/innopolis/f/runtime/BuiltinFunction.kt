package university.innopolis.f.runtime

import university.innopolis.f.grammar.FInteger
import university.innopolis.f.grammar.FReal

fun plus(args: List<FValue>, _context: FContext): Sequence<Result<FValue>> = sequence {
    if (args.size != 2) {
        yield(Result.failure(FRuntimeException.InvalidNumOfArgs()))
        return@sequence
    }
    val (lhs, rhs) = args
    yield(
        when {
            lhs is FValue.Quote &&
                lhs.value is FValueQuoted.Integer &&
                rhs is FValue.Quote &&
                rhs.value is FValueQuoted.Integer -> {
                Result.success(
                    FValue.Quote(
                        FValueQuoted.Integer(
                            FInteger(lhs.value.value.inner + rhs.value.value.inner)
                        )
                    )
                )
            }

            lhs is FValue.Quote &&
                lhs.value is FValueQuoted.Integer &&
                rhs is FValue.Quote &&
                rhs.value is FValueQuoted.Real -> {
                Result.success(
                    FValue.Quote(
                        FValueQuoted.Real(
                            FReal(lhs.value.value.inner.toBigDecimal() + rhs.value.value.inner)
                        )
                    )
                )
            }

            lhs is FValue.Quote &&
                lhs.value is FValueQuoted.Real &&
                rhs is FValue.Quote &&
                rhs.value is FValueQuoted.Integer -> {
                Result.success(
                    FValue.Quote(
                        FValueQuoted.Real(
                            FReal(lhs.value.value.inner + rhs.value.value.inner.toBigDecimal())
                        )
                    )
                )
            }

            lhs is FValue.Quote &&
                lhs.value is FValueQuoted.Real &&
                rhs is FValue.Quote &&
                rhs.value is FValueQuoted.Real -> {
                Result.success(
                    FValue.Quote(
                        FValueQuoted.Real(FReal(lhs.value.value.inner + rhs.value.value.inner))
                    )
                )
            }

            else -> Result.failure(FRuntimeException.TypeError())
        }
    )
}

fun minus(args: List<FValue>, _context: FContext): Sequence<Result<FValue>> = sequence {
    if (args.size != 2) {
        yield(Result.failure(FRuntimeException.InvalidNumOfArgs()))
        return@sequence
    }
    val (lhs, rhs) = args
    yield(
        when {
            lhs is FValue.Quote &&
                lhs.value is FValueQuoted.Integer &&
                rhs is FValue.Quote &&
                rhs.value is FValueQuoted.Integer -> {
                Result.success(
                    FValue.Quote(
                        FValueQuoted.Integer(
                            FInteger(lhs.value.value.inner - rhs.value.value.inner)
                        )
                    )
                )
            }

            lhs is FValue.Quote &&
                lhs.value is FValueQuoted.Integer &&
                rhs is FValue.Quote &&
                rhs.value is FValueQuoted.Real -> {
                Result.success(
                    FValue.Quote(
                        FValueQuoted.Real(
                            FReal(lhs.value.value.inner.toBigDecimal() - rhs.value.value.inner)
                        )
                    )
                )
            }

            lhs is FValue.Quote &&
                lhs.value is FValueQuoted.Real &&
                rhs is FValue.Quote &&
                rhs.value is FValueQuoted.Integer -> {
                Result.success(
                    FValue.Quote(
                        FValueQuoted.Real(
                            FReal(lhs.value.value.inner - rhs.value.value.inner.toBigDecimal())
                        )
                    )
                )
            }

            lhs is FValue.Quote &&
                lhs.value is FValueQuoted.Real &&
                rhs is FValue.Quote &&
                rhs.value is FValueQuoted.Real -> {
                Result.success(
                    FValue.Quote(
                        FValueQuoted.Real(FReal(lhs.value.value.inner - rhs.value.value.inner))
                    )
                )
            }

            else -> Result.failure(FRuntimeException.TypeError())
        }
    )
}

fun times(args: List<FValue>, _context: FContext): Sequence<Result<FValue>> = sequence {
    if (args.size != 2) {
        yield(Result.failure(FRuntimeException.InvalidNumOfArgs()))
        return@sequence
    }
    val (lhs, rhs) = args
    yield(
        when {
            lhs is FValue.Quote &&
                lhs.value is FValueQuoted.Integer &&
                rhs is FValue.Quote &&
                rhs.value is FValueQuoted.Integer -> {
                Result.success(
                    FValue.Quote(
                        FValueQuoted.Integer(
                            FInteger(lhs.value.value.inner * rhs.value.value.inner)
                        )
                    )
                )
            }

            lhs is FValue.Quote &&
                lhs.value is FValueQuoted.Integer &&
                rhs is FValue.Quote &&
                rhs.value is FValueQuoted.Real -> {
                Result.success(
                    FValue.Quote(
                        FValueQuoted.Real(
                            FReal(lhs.value.value.inner.toBigDecimal() * rhs.value.value.inner)
                        )
                    )
                )
            }

            lhs is FValue.Quote &&
                lhs.value is FValueQuoted.Real &&
                rhs is FValue.Quote &&
                rhs.value is FValueQuoted.Integer -> {
                Result.success(
                    FValue.Quote(
                        FValueQuoted.Real(
                            FReal(lhs.value.value.inner * rhs.value.value.inner.toBigDecimal())
                        )
                    )
                )
            }

            lhs is FValue.Quote &&
                lhs.value is FValueQuoted.Real &&
                rhs is FValue.Quote &&
                rhs.value is FValueQuoted.Real -> {
                Result.success(
                    FValue.Quote(
                        FValueQuoted.Real(FReal(lhs.value.value.inner * rhs.value.value.inner))
                    )
                )
            }

            else -> Result.failure(FRuntimeException.TypeError())
        }
    )
}

fun divide(args: List<FValue>, _context: FContext): Sequence<Result<FValue>> = sequence {
    if (args.size != 2) {
        yield(Result.failure(FRuntimeException.InvalidNumOfArgs()))
        return@sequence
    }
    val (lhs, rhs) = args

    try {
        yield(
            when {
                lhs is FValue.Quote &&
                    lhs.value is FValueQuoted.Integer &&
                    rhs is FValue.Quote &&
                    rhs.value is FValueQuoted.Integer -> {
                    Result.success(
                        FValue.Quote(
                            FValueQuoted.Integer(
                                FInteger(lhs.value.value.inner.divide(rhs.value.value.inner))
                            )
                        )
                    )
                }

                lhs is FValue.Quote &&
                    lhs.value is FValueQuoted.Integer &&
                    rhs is FValue.Quote &&
                    rhs.value is FValueQuoted.Real -> {
                    Result.success(
                        FValue.Quote(
                            FValueQuoted.Real(
                                FReal(
                                    lhs.value.value.inner
                                        .toBigDecimal()
                                        .divide(rhs.value.value.inner)
                                )
                            )
                        )
                    )
                }

                lhs is FValue.Quote &&
                    lhs.value is FValueQuoted.Real &&
                    rhs is FValue.Quote &&
                    rhs.value is FValueQuoted.Integer -> {
                    Result.success(
                        FValue.Quote(
                            FValueQuoted.Real(
                                FReal(
                                    lhs.value.value.inner.divide(
                                        rhs.value.value.inner.toBigDecimal()
                                    )
                                )
                            )
                        )
                    )
                }

                lhs is FValue.Quote &&
                    lhs.value is FValueQuoted.Real &&
                    rhs is FValue.Quote &&
                    rhs.value is FValueQuoted.Real -> {
                    Result.success(
                        FValue.Quote(
                            FValueQuoted.Real(
                                FReal(lhs.value.value.inner.divide(rhs.value.value.inner))
                            )
                        )
                    )
                }

                else -> Result.failure(FRuntimeException.TypeError())
            }
        )
    } catch (_: Exception) {
        yield(Result.failure(FRuntimeException.DivisionByZero()))
    }
}

// fun head(args: List<FValue>, context: FContext): Sequence<Result<FValue>> = sequence {
//    if (args.size != 1) {
//        yield(Result.failure(FRuntimeException.InvalidNumOfArgs()))
//        return@sequence
//    }
//
//    val quote = args.first()
//    if (quote !is FValue.Quote || quote.value !is FElement.List) {
//        yield(Result.failure(FRuntimeException.TypeError()))
//        return@sequence
//    }
//
//    val list = mutableListOf<FValue>()
//    for (result in evaluateListTo(list, quote.value.value.elements.toList(), context)) {
//        yield(result)
//        if (result.isFailure) {
//            return@sequence
//        }
//    }
//
//    if (list.isEmpty()) {
//        yield(Result.failure(FRuntimeException.NotEnoughElements()))
//        return@sequence
//    }
//
//    yield(Result.success(list.first()))
// }
