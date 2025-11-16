package university.innopolis.f.runtime

import university.innopolis.f.grammar.FAtom
import university.innopolis.f.grammar.FElement

sealed class FFunction {
    class Builtin(
        val fn: (TargetWrapper<FValue?>, List<FValue>, FContext) -> Sequence<Result<FValue>>
    ) : FFunction() {
        override fun call(
            target: TargetWrapper<FValue?>,
            args: List<FValue>,
            parentContext: FContext,
        ): Sequence<Result<FValue>> = sequence {
            for (result in fn(target, args, parentContext)) {
                yield(result)
                if (result.isFailure) {
                    return@sequence
                }
            }
        }
    }

    class UserDefined(val name: FAtom?, val params: List<FAtom>, val body: FElement) : FFunction() {
        override fun call(
            target: TargetWrapper<FValue?>,
            args: List<FValue>,
            parentContext: FContext,
        ): Sequence<Result<FValue>> = sequence {
            if (args.size != params.size) {
                yield(Result.failure(FRuntimeException.InvalidNumOfArgs()))
                return@sequence
            }
            val context = FContext(parentContext)
            for ((name, value) in params.zip(args)) {
                context.set(name, value)
            }
            for (result in evaluateElementTo(target, body, context)) {
                yield(result)
                if (result.isFailure) {
                    return@sequence
                }
            }
        }
    }

    abstract fun call(
        target: TargetWrapper<FValue?>,
        args: List<FValue>,
        parentContext: FContext,
    ): Sequence<Result<FValue>>
}
