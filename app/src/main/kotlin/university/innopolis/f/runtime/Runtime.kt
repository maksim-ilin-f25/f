package university.innopolis.f.runtime

import university.innopolis.f.grammar.FElement

fun runF(ast: List<FElement>): Sequence<Result<String>> {
    return Runtime(ast).run()
}

class Runtime(val ast: List<FElement>) {
    val rootContext = FContext(parent = null)

    fun run(): Sequence<Result<String>> = sequence {
        outer@ for (element in ast) {
            for (result in runElement(element, rootContext)) {
                yield(result.map { it.display() })
                if (result.isFailure) {
                    break@outer
                }
            }
        }
    }

    fun runElement(element: FElement, context: FContext): Sequence<Result<FValue>> = sequence {
        when (element) {
            is FElement.Atom -> {
                val value = context.valueOf(element.value)
                if (value == null) {
                    yield(Result.failure(FRuntimeException.UnboundAtom()))
                    return@sequence
                }
                yield(Result.success(value))
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
                    yield(Result.failure(FRuntimeException.NoncallableCall()))
                    return@sequence
                }
                val args = mutableListOf<FValue>()
                for (x in funCall.args.map { runElement(it, context) }) {
                    var arg: FValue? = null
                    for (y in x) {
                        yield(y)
                        if (y.isFailure) {
                            return@sequence
                        }
                        arg = y.getOrThrow()
                    }
                    args.add(arg!!)
                }
                function.call(args)
            }
            is FElement.Literal -> yield(Result.success(FValue.fromLiteral(element.value)))
            is FElement.Quote -> yield(Result.success(FValue.Quote(element.value)))
            is FElement.Keyword -> yield(Result.failure(FRuntimeException.StandaloneKeyword()))
        }
    }
}
