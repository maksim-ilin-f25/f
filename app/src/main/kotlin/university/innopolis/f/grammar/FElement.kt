package university.innopolis.f.grammar

sealed class FElement {
    data class Atom(val value: FAtom) : FElement()

    data class Literal(val value: FLiteral) : FElement()

    data class FunCall(val funName: FElement, val params: List<FElement>) : FElement()

    data class Quote(val value: FQuoteElement) : FElement()

    data class Keyword(val value: FKeyword) : FElement()
}

sealed class FQuoteElement {
    data class Atom(val value: FAtom) : FQuoteElement()

    data class Literal(val value: FLiteral) : FQuoteElement()

    data class List(val value: FList) : FQuoteElement()

    data class Quote(val value: FQuoteElement) : FQuoteElement()

    data class Keyword(val value: FKeyword) : FQuoteElement()
}
