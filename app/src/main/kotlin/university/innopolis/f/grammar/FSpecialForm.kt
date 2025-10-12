package university.innopolis.f.grammar

sealed class FSpecialForm {
    class Quote(val contents: FElement.Quote) : FSpecialForm()

    class Setq(val name: FAtom, val value: FElement) : FSpecialForm()

    class Func(val name: FAtom, val parameters: List<FAtom>, val body: FElement) : FSpecialForm()

    class Lambda(val parameters: List<FAtom>, val body: FElement) : FSpecialForm()

    class Prog(val localContext: List<Pair<FAtom, FElement>>, val body: FElement) : FSpecialForm()

    class Cond(val condition: FElement, val thenBody: FElement, val elseBody: FElement?) :
        FSpecialForm()

    class While(val condition: FElement, val body: FElement) : FSpecialForm()

    class Return(val value: FElement) : FSpecialForm()

    object Break : FSpecialForm()
}
