package university.innopolis.f.runtime

import university.innopolis.f.grammar.FElement

fun setq(
    target: Wrapper<FValue?>,
    args: List<FElement>,
    context: FContext,
): Sequence<Result<FValue>> = sequence {
    if (args.size != 2) {
        yield(Result.failure(FRuntimeException.InvalidNumOfArgs()))
        return@sequence
    }

    val (lhsRaw, rhs) = args
    val lhs =
        when (lhsRaw) {
            is FElement.Atom -> lhsRaw.value
            else -> {
                yield(Result.failure(FRuntimeException.InvalidArgForm()))
                return@sequence
            }
        }

    val evaluated = Wrapper<FValue?>(null)
    for (result in evaluateElementTo(evaluated, rhs, context)) {
        yield(result)
        if (result.isFailure) {
            return@sequence
        }
    }

    context.set(lhs, evaluated.value!!)
}
