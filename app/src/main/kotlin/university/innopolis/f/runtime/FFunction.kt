package university.innopolis.f.runtime

import university.innopolis.f.grammar.FAtom
import university.innopolis.f.grammar.FElement

sealed class FFunction {
    class Builtin(val fn: (List<FValue>) -> Sequence<Result<FValue>>) : FFunction() {
        override fun call(args: List<FValue>, parentContext: FContext): Sequence<Result<FValue>> = sequence {
            for (result in fn(args)) {
                yield(result)
                if (result.isFailure) {
                    return@sequence
                }
            }
        }
    }

    class UserDefined(val name: FAtom?, val params: List<FAtom>, val body: FElement) : FFunction() {
        override fun call(
            args: List<FValue>,
            parentContext: FContext
        ): Sequence<Result<FValue>> {
            TODO("Not yet implemented")
        }

    }

    abstract fun call(args: List<FValue>, parentContext: FContext): Sequence<Result<FValue>>
}
