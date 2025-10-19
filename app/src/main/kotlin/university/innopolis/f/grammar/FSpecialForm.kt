package university.innopolis.f.grammar

import university.innopolis.f.runtime.RuntimeException

sealed class FSpecialForm {
    class Quote(val value: FElement) : FSpecialForm() {
        fun from(args: List<FElement>): Result<Quote> {
            if (args.size != 1) {
                return Result.failure(RuntimeException.InvalidNumOfArgs())
            }
            return Result.success(Quote(args.first()))
        }
    }

    class Setq(val name: FAtom, val value: FElement) : FSpecialForm() {
        fun from(args: List<FElement>): Result<Setq> {
            if (args.size != 2) {
                return Result.failure(RuntimeException.InvalidNumOfArgs())
            }
            val firstArg = args.first()
            return when (firstArg) {
                is FElement.Atom -> Result.success(Setq(firstArg.value, args[1]))
                else -> Result.failure(RuntimeException.TypeMismatch())
            }
        }
    }

    class Func(val name: FAtom, val params: List<FAtom>, val body: FElement) : FSpecialForm() {
        fun from(args: List<FElement>): Result<Func> {
            if (args.size != 3) {
                return Result.failure(RuntimeException.InvalidNumOfArgs())
            }
            val funName = args[0]
            val rawFunParams = args[1]
            val funBody = args[2]
            if (funName !is FElement.Atom) {
                return Result.failure(RuntimeException.TypeMismatch())
            }
            if (rawFunParams !is FElement.List) {
                return Result.failure(RuntimeException.TypeMismatch())
            }
            val funParams =
                runCatching { rawFunParams.value.elements.map { (it as FElement.Atom).value } }
                    .getOrNull()
            if (funParams == null) {
                return Result.failure(RuntimeException.TypeMismatch())
            }
            return Result.success(Func(name = funName.value, params = funParams, body = funBody))
        }
    }

    class Lambda(val parameters: List<FAtom>, val body: FElement) : FSpecialForm()

    class Prog(val localContext: List<Pair<FAtom, FElement>>, val body: FElement) : FSpecialForm()

    class Cond(val condition: FElement, val thenBody: FElement, val elseBody: FElement?) :
        FSpecialForm()

    class While(val condition: FElement, val body: FElement) : FSpecialForm()

    class Return(val value: FElement) : FSpecialForm()

    object Break : FSpecialForm()

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
