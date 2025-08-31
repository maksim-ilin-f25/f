package university.innopolis.f.grammar

sealed class FElement {
    class Atom(val inner: FAtom) : FElement()

    class Literal(val inner: FLiteral) : FElement()

    class List(val inner: FList) : FElement()
}
