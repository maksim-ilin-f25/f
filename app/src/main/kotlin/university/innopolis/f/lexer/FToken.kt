package university.innopolis.f.lexer

import university.innopolis.f.grammar.FAtom
import university.innopolis.f.grammar.FLiteral

sealed class FToken() {
    object OpeningParenthesis : FToken() {
        override fun toString() = "OpeningParenthesis"
    }

    object ClosingParenthesis : FToken() {
        override fun toString() = "ClosingParenthesis"
    }

    data class Identifier(val name: FAtom) : FToken() {
        override fun toString() = this.name.toString()
    }

    data class Literal(val value: FLiteral) : FToken() {
        override fun toString() = this.value.toString()
    }

    object Quote : FToken() {
        override fun toString() = "Quote"
    }
}
