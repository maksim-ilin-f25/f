package university.innopolis.f.parser

import university.innopolis.f.lexer.Coordinate
import university.innopolis.f.lexer.TokenizeException

sealed class ParseException() : IllegalArgumentException() {
    class UnmatchedOpeningParen(val coordinate: Coordinate) : ParseException() {
        override fun toString() = "$coordinate: Unmatched opening parenthesis"
    }

    class UnmatchedClosingParen(val coordinate: Coordinate) : ParseException() {
        override fun toString() = "$coordinate: Unmatched closing parenthesis"
    }

    class InvalidFunCall(val coordinate: Coordinate) : ParseException() {
        override fun toString() = "$coordinate: No function name provided"
    }

    class Tokenization(val e: TokenizeException) : ParseException() {
        override fun toString() = "Tokenization error:\n$e"
    }
}
