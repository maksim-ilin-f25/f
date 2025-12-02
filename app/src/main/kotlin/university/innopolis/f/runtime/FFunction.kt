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

        override fun toString(): String {
            return "[Builtin Function]"
        }
    }

    class UserDefined(val name: FAtom?, val params: List<FAtom>, val body: FElement) : FFunction() {
        override fun call(
            target: TargetWrapper<FValue?>,
            args: List<FValue>,
            parentContext: FContext,
        ): Sequence<Result<FValue>> {
            val self = this
            return sequence {
                if (args.size != params.size) {
                    yield(Result.failure(FRuntimeException.InvalidNumOfArgs()))
                    return@sequence
                }
                val context = FContext(parentContext)
                if (name != null) {
                    context.set(name, FValue.Function(self))
                }
                for ((name, value) in params.zip(args)) {
                    context.set(name, value)
                }
                val bufferTarget = TargetWrapper<FValue?>(null)
                for (result in evaluateElementTo(bufferTarget, body, context)) {
                    yield(result)
                    if (result.isFailure) {
                        return@sequence
                    }
                }
                when (bufferTarget.value) {
                    is FValue.Break -> {
                        yield(Result.failure(FRuntimeException.InvalidBreak()))
                        return@sequence
                    }
                    is FValue.Return -> {
                        target.value = (bufferTarget.value as FValue.Return).value
                        return@sequence
                    }
                    else -> {
                        target.value = bufferTarget.value
                    }
                }
            }
        }

        override fun toString(): String {
            return if (name != null) {
                "[Function ${name}]"
            } else {
                "[Anonymous Function]"
            }
        }
    }

    abstract fun call(
        target: TargetWrapper<FValue?>,
        args: List<FValue>,
        parentContext: FContext,
    ): Sequence<Result<FValue>>
}
