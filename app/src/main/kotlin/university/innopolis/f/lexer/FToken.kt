package university.innopolis.f.lexer

import university.innopolis.f.grammar.FIdentifier
import university.innopolis.f.grammar.FLiteral
import university.innopolis.f.lexer.model.FSpecialForm

sealed class FToken() {
    object OpeningParenthesis : FToken()

    object ClosingParenthesis : FToken()

    data class Identifier(val name: FIdentifier) : FToken()

    data class Literal(val value: FLiteral) : FToken()

    data class SpecialForm(val name: FSpecialForm) : FToken()

    object Quote : FToken()
}

data class TokenCoordinate(val line: UInt, val column: UInt)
