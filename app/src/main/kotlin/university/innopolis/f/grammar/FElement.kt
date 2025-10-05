package university.innopolis.f.grammar

sealed class FElement {
    data class Atom(val value: FAtom) : FElement()

    data class Literal(val value: FLiteral) : FElement()

    data class List(val value: FList) : FElement()

    data class Quote(val value: FElement) : FElement()

    data class Keyword(val value: FKeyword) : FElement()
}
