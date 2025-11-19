package university.innopolis.f.runtime

import university.innopolis.f.grammar.FAtom
import university.innopolis.f.grammar.FElement
import university.innopolis.f.grammar.FSpecialForm
import university.innopolis.f.runtime.FValue.Quote
import university.innopolis.f.runtime.builtin.*

fun loadBuiltinFunctions(context: FContext) {
    context.set(
        FAtom("plus"),
        FValue.Function(FFunction.Builtin { target, args, context -> plus(target, args, context) }),
    )
    context.set(
        FAtom("minus"),
        FValue.Function(FFunction.Builtin { target, args, context -> minus(target, args, context) }),
    )
    context.set(
        FAtom("times"),
        FValue.Function(FFunction.Builtin { target, args, context -> times(target, args, context) }),
    )
    context.set(
        FAtom("divide"),
        FValue.Function(
            FFunction.Builtin { target, args, context -> divide(target, args, context) }
        ),
    )
    context.set(
        FAtom("head"),
        FValue.Function(FFunction.Builtin { target, args, context -> head(target, args, context) }),
    )
    context.set(
        FAtom("tail"),
        FValue.Function(FFunction.Builtin { target, args, context -> tail(target, args, context) }),
    )
    context.set(
        FAtom("cons"),
        FValue.Function(FFunction.Builtin { target, args, context -> cons(target, args, context) }),
    )
    context.set(
        FAtom("equal"),
        FValue.Function(FFunction.Builtin { target, args, context -> equal(target, args, context) }),
    )
    context.set(
        FAtom("nonequal"),
        FValue.Function(
            FFunction.Builtin { target, args, context -> nonequal(target, args, context) }
        ),
    )
    context.set(
        FAtom("less"),
        FValue.Function(FFunction.Builtin { target, args, context -> less(target, args, context) }),
    )
    context.set(
        FAtom("lesseq"),
        FValue.Function(
            FFunction.Builtin { target, args, context -> lesseq(target, args, context) }
        ),
    )
    context.set(
        FAtom("greater"),
        FValue.Function(
            FFunction.Builtin { target, args, context -> greater(target, args, context) }
        ),
    )
    context.set(
        FAtom("greatereq"),
        FValue.Function(
            FFunction.Builtin { target, args, context -> greatereq(target, args, context) }
        ),
    )
    context.set(
        FAtom("isint"),
        FValue.Function(FFunction.Builtin { target, args, context -> isint(target, args, context) }),
    )
    context.set(
        FAtom("isreal"),
        FValue.Function(
            FFunction.Builtin { target, args, context -> isreal(target, args, context) }
        ),
    )
    context.set(
        FAtom("isbool"),
        FValue.Function(
            FFunction.Builtin { target, args, context -> isbool(target, args, context) }
        ),
    )
    context.set(
        FAtom("isnull"),
        FValue.Function(
            FFunction.Builtin { target, args, context -> isnull(target, args, context) }
        ),
    )
    context.set(
        FAtom("isatom"),
        FValue.Function(
            FFunction.Builtin { target, args, context -> isatom(target, args, context) }
        ),
    )
    context.set(
        FAtom("islist"),
        FValue.Function(
            FFunction.Builtin { target, args, context -> islist(target, args, context) }
        ),
    )
    context.set(
        FAtom("and"),
        FValue.Function(FFunction.Builtin { target, args, context -> and(target, args, context) }),
    )
    context.set(
        FAtom("or"),
        FValue.Function(FFunction.Builtin { target, args, context -> or(target, args, context) }),
    )
    context.set(
        FAtom("xor"),
        FValue.Function(FFunction.Builtin { target, args, context -> xor(target, args, context) }),
    )
    context.set(
        FAtom("not"),
        FValue.Function(FFunction.Builtin { target, args, context -> not(target, args, context) }),
    )
    context.set(
        FAtom("eval"),
        FValue.Function(FFunction.Builtin { target, args, context -> eval(target, args, context) }),
    )

    // Non-standard:

    context.set(
        FAtom("isempty"),
        FValue.Function(
            FFunction.Builtin { target, args, context -> isempty(target, args, context) }
        ),
    )
}

fun runF(ast: List<FElement>): Sequence<Result<String>> {
    val rootContext = FContext(parent = null)
    loadBuiltinFunctions(rootContext)

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
            when (evaluatedValue.value) {
                is FValue.Break -> {
                    yield(Result.failure(FRuntimeException.InvalidBreak()))
                    break@outer
                }
                is FValue.Return -> {
                    yield(Result.failure(FRuntimeException.InvalidReturn()))
                    break@outer
                }
                null -> {}
                else -> {
                    yield(Result.success(evaluatedValue.value.toString()))
                }
            }
        }
    }
}

fun evaluateListTo(
    target: TargetWrapper<EvaluateListResult>,
    ast: List<FElement>,
    context: FContext,
): Sequence<Result<FValue>> = sequence {
    val list = emptyList<FValue>().toMutableList()
    for (outputSequence in ast) {
        val element: TargetWrapper<FValue?> = TargetWrapper(null)
        for (result in evaluateElementTo(element, outputSequence, context)) {
            yield(result)
            if (result.isFailure) {
                return@sequence
            }
        }
        if (element.value is FValue.Break) {
            target.value = EvaluateListResult.ControlFlowWrapper(ControlFlow.Break)
            return@sequence
        }
        if (element.value is FValue.Return) {
            target.value =
                EvaluateListResult.ControlFlowWrapper(
                    ControlFlow.Return((element.value as FValue.Return).value)
                )
            return@sequence
        }

        if (element.value == null) {
            yield(Result.failure(FRuntimeException.UseOfNonexistentValue()))
            return@sequence
        }
        list.add(element.value!!)
    }
    target.value = EvaluateListResult.ValueWrapper(list)
}

class TargetWrapper<T>(var value: T)

sealed class ControlFlow {
    class Return(val value: FValue?) : ControlFlow()

    object Break : ControlFlow()
}

sealed class EvaluateListResult {
    class ValueWrapper(val list: MutableList<FValue>) : EvaluateListResult()

    class ControlFlowWrapper(val operation: ControlFlow) : EvaluateListResult()
}

fun evaluateElementTo(
    target: TargetWrapper<FValue?>,
    element: FElement,
    context: FContext,
): Sequence<Result<FValue>> = sequence {
    target.value =
        when (element) {
            is FElement.Literal -> Quote(element)
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
                val function =
                    if (funCall == null) {
                        val first = element.value.elements.firstOrNull()
                        if (first == null) {
                            yield(Result.failure(FRuntimeException.EmptyParentheses()))
                            return@sequence
                        }
                        val innerTarget = TargetWrapper<FValue?>(null)
                        for (result in evaluateElementTo(innerTarget, first, context)) {
                            yield(result)
                            if (result.isFailure) {
                                return@sequence
                            }
                        }
                        when (innerTarget.value) {
                            null -> {
                                yield(Result.failure(FRuntimeException.UseOfNonexistentValue()))
                                return@sequence
                            }
                            is FValue.Break -> {
                                target.value = FValue.Break
                                return@sequence
                            }
                            is FValue.Return -> {
                                target.value = innerTarget.value
                                return@sequence
                            }
                            is Quote -> {
                                yield(Result.failure(FRuntimeException.NotAFunction()))
                                return@sequence
                            }
                            is FValue.Function -> {
                                (innerTarget.value as FValue.Function).value
                            }
                        }
                    } else {
                        val maybeFunction = context.valueOf(funCall.name)
                        if (maybeFunction == null) {
                            yield(Result.failure(FRuntimeException.UnboundAtom()))
                            return@sequence
                        }
                        if (maybeFunction !is FValue.Function) {
                            yield(Result.failure(FRuntimeException.NotAFunction()))
                            return@sequence
                        }
                        maybeFunction.value
                    }

                val argsRaw =
                    funCall?.args ?: element.value.elements.subList(1, element.value.elements.size)

                val listTarget =
                    TargetWrapper<EvaluateListResult>(
                        EvaluateListResult.ValueWrapper(emptyList<FValue>().toMutableList())
                    )
                for (result in evaluateListTo(listTarget, argsRaw, context)) {
                    yield(result)
                    if (result.isFailure) {
                        return@sequence
                    }
                }
                val args =
                    when (listTarget.value) {
                        is EvaluateListResult.ValueWrapper ->
                            (listTarget.value as EvaluateListResult.ValueWrapper).list
                        is EvaluateListResult.ControlFlowWrapper -> {

                            when (
                                (listTarget.value as EvaluateListResult.ControlFlowWrapper)
                                    .operation
                            ) {
                                is ControlFlow.Break -> {
                                    target.value = FValue.Break
                                }
                                is ControlFlow.Return -> {
                                    val returnWrapper =
                                        (listTarget.value as EvaluateListResult.ControlFlowWrapper)
                                            .operation as ControlFlow.Return
                                    target.value = FValue.Return(returnWrapper.value)
                                }
                            }
                            return@sequence
                        }
                    }
                for (result in function.call(target, args, context)) {
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
