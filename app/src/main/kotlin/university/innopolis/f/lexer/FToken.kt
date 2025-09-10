package university.innopolis.f.lexer

import university.innopolis.f.grammar.FIdentifier
import university.innopolis.f.grammar.FLiteral
import university.innopolis.f.lexer.model.FSpecialForm

sealed class FToken() {
    object OpeningParenthesis : FToken() {
        override fun toString() = "OpeningParenthesis"
    }

    object ClosingParenthesis : FToken() {
        override fun toString() = "ClosingParenthesis"
    }

    data class Identifier(val name: FIdentifier) : FToken() {
        override fun toString() = this.name.toString()
    }

    data class Literal(val value: FLiteral) : FToken() {
        override fun toString() = this.value.toString()
    }

    data class SpecialForm(val name: FSpecialForm) : FToken() {
        override fun toString() = this.name.toString()
    }

    object Quote : FToken() {
        override fun toString() = "Quote"
    }
}

data class TokenCoordinate(val line: UInt, val column: UInt)
