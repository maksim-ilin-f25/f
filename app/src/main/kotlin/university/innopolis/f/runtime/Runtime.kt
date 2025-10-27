package university.innopolis.f.runtime

import university.innopolis.f.grammar.FElement

fun runF(ast: List<FElement>): Sequence<Result<String>> {
    return Runtime(ast).run()
}

class Runtime(val ast: List<FElement>) {
    fun run(): Sequence<Result<String>> = sequence {
        outer@ for (element in ast) {
            for (result in runElement(element)) {
                yield(result)
                if (result.isFailure) {
                    break@outer
                }
            }
        }
    }

    fun runElement(element: FElement): Sequence<Result<String>> = sequence {
        when (element) {
            is FElement.Atom -> {
                TODO()
            }
            is FElement.List -> {
                val funCall = element.value.toFunCallOrNull()
                if (funCall == null) {
                    yield(Result.failure(FRuntimeException.EmptyFunCall()))
                    return@sequence
                }
                TODO()
            }
            is FElement.Literal -> yield(Result.success(element.value.display()))
            is FElement.Quote -> yield(Result.success(element.value.display()))
            is FElement.Keyword -> yield(Result.failure(FRuntimeException.StandaloneKeyword()))
        }
    }
}
