package university.innopolis.f.runtime

import university.innopolis.f.grammar.FAtom
import university.innopolis.f.grammar.FElement
import university.innopolis.f.grammar.FSpecialForm
import university.innopolis.f.runtime.builtin.*

fun runF(ast: List<FElement>): Sequence<Result<String>> {
    val rootContext = FContext(parent = null)

    rootContext.set(
        FAtom("plus"),
        FValue.Function(FFunction.Builtin { target, args, context -> plus(target, args, context) }),
    )
    rootContext.set(
        FAtom("minus"),
        FValue.Function(FFunction.Builtin { target, args, context -> minus(target, args, context) }),
    )
    rootContext.set(
        FAtom("times"),
        FValue.Function(FFunction.Builtin { target, args, context -> times(target, args, context) }),
    )
    rootContext.set(
        FAtom("divide"),
        FValue.Function(
            FFunction.Builtin { target, args, context -> divide(target, args, context) }
        ),
    )
    rootContext.set(
        FAtom("head"),
        FValue.Function(FFunction.Builtin { target, args, context -> head(target, args, context) }),
    )

    val context = FContext(parent = rootContext)
    return sequence {
        outer@ for (element in ast) {
            val evaluatedValue = TargetWrapper<FValue?>(null)
            for (result in evaluateElementTo(evaluatedValue, element, context)) {
                yield(result.map { it.toString() })
                if (result.isFailure) {
                    break@outer
                }
            }
            if (evaluatedValue.value != null) {
                yield(Result.success(evaluatedValue.value.toString()))
            }
        }
    }
}

fun evaluateListTo(
    list: MutableList<FValue>,
    ast: List<FElement>,
    context: FContext,
): Sequence<Result<FValue>> = sequence {
    for (outputSequence in ast) {
        val element: TargetWrapper<FValue?> = TargetWrapper(null)
        for (result in evaluateElementTo(element, outputSequence, context)) {
            yield(result)
            if (result.isFailure) {
                return@sequence
            }
        }
        if (element.value == null) {
            yield(Result.failure(FRuntimeException.UseOfNonexistentValue()))
            return@sequence
        }
        list.add(element.value!!)
    }
}

class TargetWrapper<T>(var value: T)

fun evaluateElementTo(
    target: TargetWrapper<FValue?>,
    element: FElement,
    context: FContext,
): Sequence<Result<FValue>> = sequence {
    target.value =
        when (element) {
            is FElement.Literal -> FValue.Quote(element)
            is FElement.Keyword -> {
                yield(Result.failure(FRuntimeException.StandaloneKeyword()))
                return@sequence
            }
            is FElement.Atom -> {
                val value = context.valueOf(element.value)
                if (value == null) {
                    yield(Result.failure(FRuntimeException.UnboundAtom()))
                    return@sequence
                }
                value
            }
            is FElement.Quote -> FValue.Quote(element.value)
            is FElement.List -> {
                val specialForm =
                    FSpecialForm.from(element.value).getOrElse {
                        yield(Result.failure(it))
                        return@sequence
                    }
                if (specialForm != null) {
                    for (result in specialForm.evaluateTo(target, context)) {
                        yield(result)
                        if (result.isFailure) {
                            return@sequence
                        }
                    }
                    return@sequence
                }

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
                for (result in function.value.call(target, args, context)) {
                    yield(result)
                    if (result.isFailure) {
                        return@sequence
                    }
                }
                return@sequence
            }
        }
}
