package university.innopolis.f.runtime

import university.innopolis.f.grammar.FAtom
import university.innopolis.f.grammar.FElement

fun runF(ast: List<FElement>): Sequence<Result<String>> {
    val rootContext = FContext(parent = null)

    rootContext.set(FAtom("plus"), FValue.Function(FFunction.Builtin { args, context -> plus(args, context) }))
    rootContext.set(FAtom("minus"), FValue.Function(FFunction.Builtin { args, context -> minus(args, context) }))
    rootContext.set(FAtom("times"), FValue.Function(FFunction.Builtin { args, context -> times(args, context) }))
    rootContext.set(FAtom("divide"), FValue.Function(FFunction.Builtin { args, context -> divide(args, context) }))
    rootContext.set(FAtom("head"), FValue.Function(FFunction.Builtin { args, context -> head(args, context) }))

    val context = FContext(parent = rootContext)
    return sequence {
        outer@ for (element in ast) {
            TODO("runElement no longer exists")
            for (result in runElement(element, context)) {
                yield(result.map { it.display() })
                if (result.isFailure) {
                    break@outer
                }
            }
        }
    }
}

fun evaluateListTo(list: MutableList<FValue>, ast: List<FElement>, context: FContext): Sequence<Result<FValue>> =
    sequence {
        for (outputSequence in ast) {
            var element: FValue? = null
            TODO("runElement no longer exists")
            for (result in runElement(outputSequence, context)) {
                yield(result)
                if (result.isFailure) {
                    return@sequence
                }
                element = result.getOrThrow()
            }
            list.add(element!!)
        }

    }

class Wrapper<T>(var value: T)

fun evaluateElementTo(
    target: Wrapper<FValue>,
    element: FElement,
    context: FContext
): Sequence<Result<FValue>> =
    sequence {
        when (element) {
            is FElement.Atom -> {
                val value = context.valueOf(element.value)
                if (value == null) {
                    yield(Result.failure(FRuntimeException.UnboundAtom()))
                    return@sequence
                }
                target.value = value
            }

            is FElement.List -> {
                val funCall = element.value.toFunCallOrNull()
                if (funCall == null) {
                    yield(Result.failure(FRuntimeException.MalformedFunCall()))
                    return@sequence
                }
                val function = context.valueOf(funCall.name)
                if (function == null) {
                    yield(Result.failure(FRuntimeException.UnboundAtom()))
                    return@sequence
                }
                if (function !is FValue.Function) {
                    yield(Result.failure(FRuntimeException.NotAFunction()))
                    return@sequence
                }

                val args = mutableListOf<FValue>()
                for (result in evaluateListTo(args, funCall.args, context)) {
                    yield(result)
                    if (result.isFailure) {
                        return@sequence
                    }
                }

                for (result in function.value.call(args, context)) {
                    yield(result)
                    if (result.isFailure) {
                        return@sequence
                    }
                }

                TODO("write the list to `target`")
            }

            is FElement.Literal -> {
                target.value = FValue.fromLiteral(element.value)
            }

            is FElement.Quote -> {
                target.value = FValue.Quote(element.value)
            }

            is FElement.Keyword -> yield(Result.failure(FRuntimeException.StandaloneKeyword()))
        }
    }
