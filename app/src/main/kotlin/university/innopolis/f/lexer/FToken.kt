package university.innopolis.f.lexer

import university.innopolis.f.grammar.FAtom
import university.innopolis.f.grammar.FKeyword
import university.innopolis.f.grammar.FLiteral

sealed class FToken(open val coordinate: Coordinate) {
    data class OpeningParenthesis(override val coordinate: Coordinate) : FToken(coordinate) {
        override fun toString() = "OpeningParenthesis"
    }

    data class ClosingParenthesis(override val coordinate: Coordinate) : FToken(coordinate) {
        override fun toString() = "ClosingParenthesis"
    }

    data class Keyword(val value: FKeyword, override val coordinate: Coordinate) :
        FToken(coordinate) {
        override fun toString() = this.value.toString()
    }

    data class Atom(val value: FAtom, override val coordinate: Coordinate) : FToken(coordinate) {
        override fun toString() = this.value.toString()
    }

    data class Literal(val value: FLiteral, override val coordinate: Coordinate) :
        FToken(coordinate) {
        override fun toString() = this.value.toString()
    }

    data class Quote(override val coordinate: Coordinate) : FToken(coordinate) {
        override fun toString() = "Quote"
    }
}
