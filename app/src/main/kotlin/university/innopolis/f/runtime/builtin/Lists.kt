package university.innopolis.f.runtime.builtin

import university.innopolis.f.grammar.FElement
import university.innopolis.f.grammar.FListAst
import university.innopolis.f.runtime.FContext
import university.innopolis.f.runtime.FRuntimeException
import university.innopolis.f.runtime.FValue
import university.innopolis.f.runtime.TargetWrapper

fun head(
    target: TargetWrapper<FValue?>,
    args: List<FValue>,
    _context: FContext,
): Sequence<Result<FValue>> = sequence {
    if (args.size != 1) {
        yield(Result.failure(FRuntimeException.InvalidNumOfArgs()))
        return@sequence
    }

    val quote = args.first()
    if (quote !is FValue.Quote || quote.value !is FElement.List) {
        yield(Result.failure(FRuntimeException.TypeError()))
        return@sequence
    }

    val headElement = quote.value.value.elements.firstOrNull()
    if (headElement == null) {
        yield(Result.failure(FRuntimeException.NotEnoughElements()))
        return@sequence
    }

    target.value =
        FValue.Quote(
            when (headElement) {
                is FElement.Literal -> headElement
                is FElement.Atom -> headElement
                is FElement.Keyword -> headElement
                is FElement.List -> headElement
                is FElement.Quote -> headElement.value // SUS: check if inner is literal or whatever
            }
        )
}

fun tail(
    target: TargetWrapper<FValue?>,
    args: List<FValue>,
    _context: FContext,
): Sequence<Result<FValue>> = sequence {
    if (args.size != 1) {
        yield(Result.failure(FRuntimeException.InvalidNumOfArgs()))
        return@sequence
    }

    val quote = args.first()
    if (quote !is FValue.Quote || quote.value !is FElement.List) {
        yield(Result.failure(FRuntimeException.TypeError()))
        return@sequence
    }

    if (quote.value.value.elements.isEmpty()) {
        yield(Result.failure(FRuntimeException.NotEnoughElements()))
        return@sequence
    }

    val tailElements = quote.value.value.elements.subList(1, quote.value.value.elements.size)

    target.value = FValue.Quote(FElement.List(FListAst(tailElements)))
}
