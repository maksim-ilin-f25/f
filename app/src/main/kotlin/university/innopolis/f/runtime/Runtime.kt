package university.innopolis.f.runtime

import university.innopolis.f.grammar.FAtom
import university.innopolis.f.grammar.FElement
import university.innopolis.f.grammar.FSpecialForm
import university.innopolis.f.runtime.FRuntimeException.*
import university.innopolis.f.runtime.FValue.Quote
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
    rootContext.set(
        FAtom("tail"),
        FValue.Function(FFunction.Builtin { target, args, context -> tail(target, args, context) }),
    )
    rootContext.set(
        FAtom("cons"),
        FValue.Function(FFunction.Builtin { target, args, context -> cons(target, args, context) }),
    )
    rootContext.set(
        FAtom("equal"),
        FValue.Function(FFunction.Builtin { target, args, context -> equal(target, args, context) }),
    )
    rootContext.set(
        FAtom("nonequal"),
        FValue.Function(
            FFunction.Builtin { target, args, context -> nonequal(target, args, context) }
        ),
    )
    rootContext.set(
        FAtom("less"),
        FValue.Function(FFunction.Builtin { target, args, context -> less(target, args, context) }),
    )
    rootContext.set(
        FAtom("lesseq"),
        FValue.Function(
            FFunction.Builtin { target, args, context -> lesseq(target, args, context) }
        ),
    )
    rootContext.set(
        FAtom("greater"),
        FValue.Function(
            FFunction.Builtin { target, args, context -> greater(target, args, context) }
        ),
    )
    rootContext.set(
        FAtom("greatereq"),
        FValue.Function(
            FFunction.Builtin { target, args, context -> greatereq(target, args, context) }
        ),
    )
    rootContext.set(
        FAtom("isint"),
        FValue.Function(FFunction.Builtin { target, args, context -> isint(target, args, context) }),
    )
    rootContext.set(
        FAtom("isreal"),
        FValue.Function(
            FFunction.Builtin { target, args, context -> isreal(target, args, context) }
        ),
    )
    rootContext.set(
        FAtom("isbool"),
        FValue.Function(
            FFunction.Builtin { target, args, context -> isbool(target, args, context) }
        ),
    )
    rootContext.set(
        FAtom("isnull"),
        FValue.Function(
            FFunction.Builtin { target, args, context -> isnull(target, args, context) }
        ),
    )
    rootContext.set(
        FAtom("isatom"),
        FValue.Function(
            FFunction.Builtin { target, args, context -> isatom(target, args, context) }
        ),
    )
    rootContext.set(
        FAtom("islist"),
        FValue.Function(
            FFunction.Builtin { target, args, context -> islist(target, args, context) }
        ),
    )
    rootContext.set(
        FAtom("and"),
        FValue.Function(FFunction.Builtin { target, args, context -> and(target, args, context) }),
    )
    rootContext.set(
        FAtom("or"),
        FValue.Function(FFunction.Builtin { target, args, context -> or(target, args, context) }),
    )
    rootContext.set(
        FAtom("xor"),
        FValue.Function(FFunction.Builtin { target, args, context -> xor(target, args, context) }),
    )
    rootContext.set(
        FAtom("not"),
        FValue.Function(FFunction.Builtin { target, args, context -> not(target, args, context) }),
    )
    rootContext.set(
        FAtom("eval"),
        FValue.Function(FFunction.Builtin { target, args, context -> eval(target, args, context) }),
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
            is FElement.Literal -> Quote(element)
            is FElement.Keyword -> {
                yield(Result.failure(StandaloneKeyword()))
                return@sequence
            }
            is FElement.Atom -> {
                val value = context.valueOf(element.value)
                if (value == null) {
                    yield(Result.failure(UnboundAtom()))
                    return@sequence
                }
                value
            }
            is FElement.Quote -> Quote(element.value)
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
                    yield(Result.failure(MalformedFunCall()))
                    return@sequence
                }
                val function = context.valueOf(funCall.name)
                if (function == null) {
                    yield(Result.failure(UnboundAtom()))
                    return@sequence
                }
                if (function !is FValue.Function) {
                    yield(Result.failure(NotAFunction()))
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

            is FElement.Function -> FValue.Function(element.value)
        }
}
