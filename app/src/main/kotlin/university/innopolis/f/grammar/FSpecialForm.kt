package university.innopolis.f.grammar

import university.innopolis.f.runtime.FRuntimeException

sealed class FSpecialForm {
    class Quote(val value: FElement) : FSpecialForm() {

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
                return Result.success(
                    Func(name = argName.value, params = argParams, body = argBody)
                )
            }
        }
    }

    class Lambda(val params: List<FAtom>, val body: FElement) : FSpecialForm() {

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
                return Result.success(Lambda(params = argParams, body = argBody))
            }
        }
    }

    class Prog(val localContext: List<Pair<FAtom, FElement>>, val body: List<FElement>) :
        FSpecialForm() {

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
                return Result.success(Prog(localContext = argLocalContext, body = argBody))
            }
        }
    }

    class Cond(val condition: FElement, val thenBody: FElement, val elseBody: FElement) :
        FSpecialForm() {

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
    }

    //    private fun checkFunName(name: FElement, keyword: FKeyword): Result<Unit> {
    //        if (name != FElement.Keyword(keyword)) {
    //            return Result.failure(RuntimeException.MalformedCall())
    //        }
    //        return Result.success(Unit)
    //    }
    //
    //    private fun checkArgsSize(actual: Int, expected: Int): Result<Unit> {
    //        if (actual != expected) {
    //            return Result.failure(RuntimeException.MalformedCall())
    //        }
    //        return Result.success(Unit)
    //    }
}
