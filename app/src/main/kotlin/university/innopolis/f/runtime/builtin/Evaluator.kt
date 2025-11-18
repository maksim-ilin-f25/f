package university.innopolis.f.runtime.builtin

import university.innopolis.f.runtime.*

fun eval(
    target: TargetWrapper<FValue?>,
    args: List<FValue>,
    context: FContext,
): Sequence<Result<FValue>> = sequence {
    if (args.size != 1) {
        yield(Result.failure(FRuntimeException.InvalidNumOfArgs()))
        return@sequence
    }
    val body = args.first()
    when {
        body is FValue.Function || body is FValue.Break || body is FValue.Return -> {
            target.value = body
            return@sequence
        }
        body is FValue.Quote -> {
            for (result in evaluateElementTo(target, body.value, context)) {
                yield(result)
                if (result.isFailure) {
                    return@sequence
                }
            }
        }
    }
}
