package university.innopolis.f.runtime.builtin

import university.innopolis.f.grammar.FBoolean
import university.innopolis.f.grammar.FElement
import university.innopolis.f.grammar.FLiteral
import university.innopolis.f.runtime.FContext
import university.innopolis.f.runtime.FRuntimeException
import university.innopolis.f.runtime.FValue
import university.innopolis.f.runtime.TargetWrapper

fun isint(
    target: TargetWrapper<FValue?>,
    args: List<FValue>,
    _context: FContext,
): Sequence<Result<FValue>> = sequence {
    if (args.size != 1) {
        yield(Result.failure(FRuntimeException.InvalidNumOfArgs()))
        return@sequence
    }
    val arg = args.first()
    target.value =
        FValue.Quote(
            FElement.Literal(
                FLiteral.Boolean(
                    FBoolean(
                        arg is FValue.Quote &&
                            arg.value is FElement.Literal &&
                            arg.value.value is FLiteral.Integer
                    )
                )
            )
        )
}

fun isreal(
    target: TargetWrapper<FValue?>,
    args: List<FValue>,
    _context: FContext,
): Sequence<Result<FValue>> = sequence {
    if (args.size != 1) {
        yield(Result.failure(FRuntimeException.InvalidNumOfArgs()))
        return@sequence
    }
    val arg = args.first()
    target.value =
        FValue.Quote(
            FElement.Literal(
                FLiteral.Boolean(
                    FBoolean(
                        arg is FValue.Quote &&
                            arg.value is FElement.Literal &&
                            arg.value.value is FLiteral.Real
                    )
                )
            )
        )
}

fun isbool(
    target: TargetWrapper<FValue?>,
    args: List<FValue>,
    _context: FContext,
): Sequence<Result<FValue>> = sequence {
    if (args.size != 1) {
        yield(Result.failure(FRuntimeException.InvalidNumOfArgs()))
        return@sequence
    }
    val arg = args.first()
    target.value =
        FValue.Quote(
            FElement.Literal(
                FLiteral.Boolean(
                    FBoolean(
                        arg is FValue.Quote &&
                            arg.value is FElement.Literal &&
                            arg.value.value is FLiteral.Boolean
                    )
                )
            )
        )
}

fun isnull(
    target: TargetWrapper<FValue?>,
    args: List<FValue>,
    _context: FContext,
): Sequence<Result<FValue>> = sequence {
    if (args.size != 1) {
        yield(Result.failure(FRuntimeException.InvalidNumOfArgs()))
        return@sequence
    }
    val arg = args.first()
    target.value =
        FValue.Quote(
            FElement.Literal(
                FLiteral.Boolean(
                    FBoolean(
                        arg is FValue.Quote &&
                            arg.value is FElement.Literal &&
                            arg.value.value is FLiteral.Null
                    )
                )
            )
        )
}

fun isatom(
    target: TargetWrapper<FValue?>,
    args: List<FValue>,
    _context: FContext,
): Sequence<Result<FValue>> = sequence {
    if (args.size != 1) {
        yield(Result.failure(FRuntimeException.InvalidNumOfArgs()))
        return@sequence
    }
    val arg = args.first()
    target.value =
        FValue.Quote(
            FElement.Literal(
                FLiteral.Boolean(FBoolean(arg is FValue.Quote && arg.value is FElement.Atom))
            )
        )
}

fun islist(
    target: TargetWrapper<FValue?>,
    args: List<FValue>,
    _context: FContext,
): Sequence<Result<FValue>> = sequence {
    if (args.size != 1) {
        yield(Result.failure(FRuntimeException.InvalidNumOfArgs()))
        return@sequence
    }
    val arg = args.first()
    target.value =
        FValue.Quote(
            FElement.Literal(
                FLiteral.Boolean(FBoolean(arg is FValue.Quote && arg.value is FElement.List))
            )
        )
}
