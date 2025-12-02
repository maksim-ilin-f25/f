package university.innopolis.f.grammar

import university.innopolis.f.runtime.FFunction

sealed class FElement {
    data class Atom(val value: FAtom) : FElement() {
        override fun toString() = value.name
    }

    data class Literal(val value: FLiteral) : FElement() {
        override fun toString() = value.toString()
    }

    data class List(val value: FListAst) : FElement() {
        override fun toString() = "(${this.value.elements.joinToString(" ")})"
    }

    data class Quote(val value: FElement) : FElement() {
        override fun toString() = "'$value"
    }

    data class Keyword(val value: FKeyword) : FElement() {
        override fun toString() = value.toString()
    }

    data class Function(val value: FFunction) : FElement() {
        override fun toString() = value.toString()
    }
}
