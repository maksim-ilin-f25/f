package university.innopolis.f.grammar

sealed class FElement {
    data class List(val value: FListAst) : FElement() {
        override fun toString() = "(${this.value.elements.joinToString(" ")})"
    }

    data class Quote(val value: FElementQuoted) : FElement() {
        override fun toString() = value.toString()
    }
}

sealed class FElementQuoted {
    data class Atom(val value: FAtom) : FElementQuoted() {
        override fun toString() = value.name
    }

    data class Literal(val value: FLiteral) : FElementQuoted() {
        override fun toString() = value.toString()
    }

    data class List(val value: FListAst) : FElementQuoted() {
        override fun toString() = "(${this.value.elements.joinToString(" ")})"
    }

    data class Quote(val value: FElementQuoted) : FElementQuoted() {
        override fun toString() = "'$value" // TODO: ''1
    }

    data class Keyword(val value: FKeyword) : FElementQuoted() {
        override fun toString() = value.name
    }
}
