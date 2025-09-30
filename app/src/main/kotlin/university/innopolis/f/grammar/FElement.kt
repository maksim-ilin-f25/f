package university.innopolis.f.grammar

sealed class FElement {
    data class Atom(val inner: FAtom) : FElement()

    data class Literal(val inner: FLiteral) : FElement()

    data class List(val inner: FList) : FElement()

    class SpecialForm(val value: FSpecialForm) : FElement()
}
