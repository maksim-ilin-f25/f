package university.innopolis.f.grammar

import university.innopolis.f.runtime.*

sealed class FSpecialForm {
    abstract fun evaluateTo(
        target: TargetWrapper<FValue?>,
        context: FContext,
    ): Sequence<Result<FValue>>

    class Quote(val value: FElement) : FSpecialForm() {
        override fun evaluateTo(
            target: TargetWrapper<FValue?>,
            context: FContext,
        ): Sequence<Result<FValue>> {
            target.value = FValue.Quote(value)
            return emptySequence()
        }

        companion object {
            fun from(args: List<FElement>): Result<Quote> {
                if (args.size != 1) {
                    return Result.failure(FRuntimeException.InvalidNumOfArgs())
                }
                return Result.success(Quote(value = args.first()))
            }
        }
    }

    class Setq(val name: FAtom, val value: FElement) : FSpecialForm() {
        override fun evaluateTo(
            target: TargetWrapper<FValue?>,
            context: FContext,
        ): Sequence<Result<FValue>> = sequence {
            val evaluated = TargetWrapper<FValue?>(null)
            for (result in evaluateElementTo(evaluated, value, context)) {
                yield(result)
                if (result.isFailure) {
                    return@sequence
                }
            }
            if (evaluated.value == null) {
                yield(Result.failure(FRuntimeException.UseOfNonexistentValue()))
                return@sequence
            }
            context.set(name, evaluated.value!!)
        }

        companion object {
            fun from(args: List<FElement>): Result<Setq> {
                if (args.size != 2) {
                    return Result.failure(FRuntimeException.InvalidNumOfArgs())
                }
                val firstArg = args.first()
                return when (firstArg) {
                    is FElement.Atom -> Result.success(Setq(name = firstArg.value, value = args[1]))
                    else -> Result.failure(FRuntimeException.InvalidArgForm())
                }
            }
        }
    }

    class Func(val name: FAtom, val params: List<FAtom>, val body: FElement) : FSpecialForm() {
        override fun evaluateTo(
            target: TargetWrapper<FValue?>,
            context: FContext,
        ): Sequence<Result<FValue>> {
            context.set(
                name,
                FValue.Function(FFunction.UserDefined(name = name, params = params, body = body)),
            )
            return emptySequence()
        }

        companion object {
            fun from(args: List<FElement>): Result<Func> {
                if (args.size != 3) {
                    return Result.failure(FRuntimeException.InvalidNumOfArgs())
                }
                val argName = args[0]
                val argParamsRaw = args[1]
                val argBody = args[2]
                if (argName !is FElement.Atom) {
                    return Result.failure(FRuntimeException.InvalidArgForm())
                }
                if (argParamsRaw !is FElement.List) {
                    return Result.failure(FRuntimeException.InvalidArgForm())
                }
                val argParams =
                    runCatching { argParamsRaw.value.elements.map { (it as FElement.Atom).value } }
                        .getOrNull()
                if (argParams == null) {
                    return Result.failure(FRuntimeException.InvalidArgForm())
                }
                val names = argParams.map { it.name }.toSet()
                if (names.size != argParams.size) {
                    return Result.failure(FRuntimeException.DuplicateParamNames())
                }
                return Result.success(
                    Func(name = argName.value, params = argParams, body = argBody)
                )
            }
        }
    }

    class Lambda(val params: List<FAtom>, val body: FElement) : FSpecialForm() {
        override fun evaluateTo(
            target: TargetWrapper<FValue?>,
            _context: FContext,
        ): Sequence<Result<FValue>> {
            target.value =
                FValue.Function(FFunction.UserDefined(name = null, params = params, body = body))
            return emptySequence()
        }

        companion object {
            fun from(args: List<FElement>): Result<Lambda> {
                if (args.size != 2) {
                    return Result.failure(FRuntimeException.InvalidNumOfArgs())
                }
                val argParamsRaw = args[0]
                val argBody = args[1]
                if (argParamsRaw !is FElement.List) {
                    return Result.failure(FRuntimeException.InvalidArgForm())
                }
                val argParams =
                    runCatching { argParamsRaw.value.elements.map { (it as FElement.Atom).value } }
                        .getOrNull()
                if (argParams == null) {
                    return Result.failure(FRuntimeException.InvalidArgForm())
                }
                val names = argParams.map { it.name }.toSet()
                if (names.size != argParams.size) {
                    return Result.failure(FRuntimeException.DuplicateParamNames())
                }
                return Result.success(Lambda(params = argParams, body = argBody))
            }
        }
    }

    class Prog(val bindings: List<Pair<FAtom, FElement>>, val body: List<FElement>) :
        FSpecialForm() {
        override fun evaluateTo(
            target: TargetWrapper<FValue?>,
            context: FContext,
        ): Sequence<Result<FValue>> = sequence {
            val innerContext = FContext(context)
            for (result in setupInnerContext(innerContext)) {
                yield(result)
                if (result.isFailure) {
                    return@sequence
                }
            }
            for (i in body.indices) {
                if (i == body.lastIndex) {
                    break
                }
                val innerTarget = TargetWrapper<FValue?>(null)
                for (result in evaluateElementTo(innerTarget, body[i], innerContext)) {
                    yield(result)
                    if (result.isFailure) {
                        return@sequence
                    }
                }
                if (innerTarget.value is FValue.Return) {
                    target.value = (innerTarget.value as FValue.Return).value
                    return@sequence
                }
                if (innerTarget.value is FValue.Break) {
                    target.value = FValue.Break
                    return@sequence
                }
                if (innerTarget.value != null) {
                    yield(Result.success(innerTarget.value!!))
                }
            }
            val innerTarget = TargetWrapper<FValue?>(null)
            for (result in evaluateElementTo(innerTarget, body.last(), innerContext)) {
                yield(result)
                if (result.isFailure) {
                    return@sequence
                }
            }
            if (innerTarget.value is FValue.Return) {
                target.value = (innerTarget.value as FValue.Return).value
                return@sequence
            }
            if (innerTarget.value is FValue.Break) {
                target.value = FValue.Break
                return@sequence
            }
            target.value = innerTarget.value
        }

        fun setupInnerContext(innerContext: FContext): Sequence<Result<FValue>> = sequence {
            for ((name, rawValue) in bindings) {
                val bindingTarget = TargetWrapper<FValue?>(null)
                for (result in evaluateElementTo(bindingTarget, rawValue, innerContext)) {
                    yield(result)
                    if (result.isFailure) {
                        return@sequence
                    }
                }
                if (bindingTarget.value == null) {
                    yield(Result.failure(FRuntimeException.UseOfNonexistentValue()))
                    return@sequence
                }
                innerContext.set(name, bindingTarget.value!!)
            }
        }

        companion object {
            fun from(args: List<FElement>): Result<Prog> {
                if (args.size < 2) {
                    return Result.failure(FRuntimeException.InvalidNumOfArgs())
                }
                val argLocalContext =
                    runCatching {
                            (args[0] as FElement.List).value.elements.map {
                                val x = (it as FElement.List).value.elements
                                assert(x.size == 2)
                                Pair((x[0] as FElement.Atom).value, x[1])
                            }
                        }
                        .getOrNull()
                if (argLocalContext == null) {
                    return Result.failure(FRuntimeException.InvalidArgForm())
                }
                val argBody = args.subList(1, args.size)
                return Result.success(Prog(bindings = argLocalContext, body = argBody))
            }
        }
    }

    class Cond(val condition: FElement, val thenBody: FElement, val elseBody: FElement) :
        FSpecialForm() {
        override fun evaluateTo(
            target: TargetWrapper<FValue?>,
            context: FContext,
        ): Sequence<Result<FValue>> = sequence {
            val conditionValue = TargetWrapper<FValue?>(null)
            for (result in evaluateElementTo(conditionValue, condition, context)) {
                yield(result)
                if (result.isFailure) {
                    return@sequence
                }
            }
            val condVal =
                evaluateAsBoolean(conditionValue.value).getOrElse {
                    yield(Result.failure(it))
                    return@sequence
                }
            val evalBody =
                if (condVal.inner) {
                    thenBody
                } else {
                    elseBody
                }
            for (result in evaluateElementTo(target, evalBody, context)) {
                yield(result)
                if (result.isFailure) {
                    return@sequence
                }
            }
        }

        private fun evaluateAsBoolean(conditionValue: FValue?): Result<FBoolean> {
            return if (conditionValue == null) {
                Result.failure(FRuntimeException.UseOfNonexistentValue())
            } else {
                if (conditionValue is FValue.Quote) {
                    if (
                        conditionValue.value is FElement.Literal &&
                            conditionValue.value.value is FLiteral.Boolean
                    ) {
                        Result.success(conditionValue.value.value.inner)
                    } else {
                        Result.failure(FRuntimeException.TypeError())
                    }
                } else {
                    Result.failure(FRuntimeException.TypeError())
                }
            }
        }

        companion object {
            fun from(args: List<FElement>): Result<Cond> {
                if (args.size !in 2..3) {
                    return Result.failure(FRuntimeException.InvalidNumOfArgs())
                }
                val argCond = args[0]
                val argThen = args[1]
                val argElse = args.getOrNull(2) ?: FElement.Literal(FLiteral.Null)
                return Result.success(
                    Cond(condition = argCond, thenBody = argThen, elseBody = argElse)
                )
            }
        }
    }

    class While(val condition: FElement, val body: FElement) : FSpecialForm() {
        override fun evaluateTo(
            target: TargetWrapper<FValue?>,
            context: FContext,
        ): Sequence<Result<FValue>> = sequence {
            while (true) {
                val conditionValue = TargetWrapper<FValue?>(null)
                for (result in evaluateElementTo(conditionValue, condition, context)) {
                    yield(result)
                    if (result.isFailure) {
                        return@sequence
                    }
                }
                val condVal =
                    evaluateAsBoolean(conditionValue.value).getOrElse {
                        yield(Result.failure(it))
                        return@sequence
                    }
                if (!condVal.inner) {
                    break
                }
                val innerTarget = TargetWrapper<FValue?>(null)
                for (result in evaluateElementTo(innerTarget, body, context)) {
                    yield(result)
                    if (result.isFailure) {
                        return@sequence
                    }
                }
                if (innerTarget.value is FValue.Break) {
                    break
                }
                if (innerTarget.value is FValue.Return) {
                    target.value = innerTarget.value
                    return@sequence
                }

                if (innerTarget.value != null) {
                    yield(Result.success(innerTarget.value!!))
                }
            }
            target.value = FValue.Quote(FElement.Literal(FLiteral.Null))
        }

        private fun evaluateAsBoolean(conditionValue: FValue?): Result<FBoolean> {
            return if (conditionValue == null) {
                Result.failure(FRuntimeException.UseOfNonexistentValue())
            } else {
                if (conditionValue is FValue.Quote) {
                    if (
                        conditionValue.value is FElement.Literal &&
                            conditionValue.value.value is FLiteral.Boolean
                    ) {
                        Result.success(conditionValue.value.value.inner)
                    } else {
                        Result.failure(FRuntimeException.TypeError())
                    }
                } else {
                    Result.failure(FRuntimeException.TypeError())
                }
            }
        }

        companion object {
            fun from(args: List<FElement>): Result<While> {
                if (args.size != 2) {
                    return Result.failure(FRuntimeException.InvalidNumOfArgs())
                }
                val argCond = args[0]
                val argBody = args[1]
                return Result.success(While(condition = argCond, body = argBody))
            }
        }
    }

    class Return(val value: FElement) : FSpecialForm() {
        override fun evaluateTo(
            target: TargetWrapper<FValue?>,
            context: FContext,
        ): Sequence<Result<FValue>> = sequence {
            val innerTarget = TargetWrapper<FValue?>(null)
            for (result in evaluateElementTo(innerTarget, value, context)) {
                yield(result)
                if (result.isFailure) {
                    return@sequence
                }
            }
            target.value =
                when (innerTarget.value) {
                    is FValue.Break -> FValue.Break
                    is FValue.Return -> innerTarget.value
                    else -> FValue.Return(innerTarget.value)
                }
        }

        companion object {
            fun from(args: List<FElement>): Result<Return> {
                if (args.size != 1) {
                    return Result.failure(FRuntimeException.InvalidNumOfArgs())
                }
                return Result.success(Return(args.first()))
            }
        }
    }

    object Break : FSpecialForm() {
        fun from(args: List<FElement>): Result<Break> {
            if (args.isNotEmpty()) {
                return Result.failure(FRuntimeException.InvalidNumOfArgs())
            }
            return Result.success(Break)
        }

        override fun evaluateTo(
            target: TargetWrapper<FValue?>,
            _context: FContext,
        ): Sequence<Result<FValue>> {
            target.value = FValue.Break
            return emptySequence()
        }
    }

    companion object {
        fun from(list: FListAst): Result<FSpecialForm?> {
            val first = list.elements.firstOrNull()
            val sfName =
                if (first is FElement.Keyword) {
                    first.value
                } else {
                    return Result.success(null)
                }
            val sfArgs = list.elements.subList(1, list.elements.size)

            return when (sfName) {
                FKeyword.COND -> Cond.from(sfArgs)
                FKeyword.QUOTE -> Quote.from(sfArgs)
                FKeyword.SETQ -> Setq.from(sfArgs)
                FKeyword.FUNC -> Func.from(sfArgs)
                FKeyword.LAMBDA -> Lambda.from(sfArgs)
                FKeyword.PROG -> Prog.from(sfArgs)
                FKeyword.WHILE -> While.from(sfArgs)
                FKeyword.RETURN -> Return.from(sfArgs)
                FKeyword.BREAK -> Break.from(sfArgs)
            }
        }
    }
}
