package university.innopolis.f.grammar

import university.innopolis.f.runtime.Display

sealed class FElement : Display {
    data class Atom(val value: FAtom) : FElement() {
        override fun toString() = value.toString()

        override fun display() = value.name
    }

    data class Literal(val value: FLiteral) : FElement() {
        override fun toString() = value.toString()

        override fun display() = this.value.display()
    }

    data class List(val value: FList) : FElement() {
        override fun toString() = value.toString()

        override fun display() = "(${this.value.elements.joinToString(" ") { it.display() }})"
    }

    data class Quote(val value: FElement) : FElement() {
        override fun toString() = "{ quote: $value }"

        override fun display() = "'${value.display()}"
    }

    data class Keyword(val value: FKeyword) : FElement() {
        override fun toString() = value.toString()

        override fun display() = value.name
    }
}
