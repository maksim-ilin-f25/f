package university.innopolis.f.grammar

sealed class FElement {
    data class Atom(val value: FAtom) : FElement()

    data class Literal(val value: FLiteral) : FElement()

    data class FunCall(val funName: FElement, val params: List<FElement>) : FElement()

    data class Keyword(val value: FKeyword) : FElement()

    sealed class Quote : FElement() {
        data class Atom(val value: FAtom) : FElement.Quote()

        data class Literal(val value: FLiteral) : FElement.Quote()

        data class List(val value: FList) : FElement.Quote()

        data class Quote(val value: FElement.Quote) : FElement.Quote()

        data class Keyword(val value: FKeyword) : FElement.Quote()
    }
}
