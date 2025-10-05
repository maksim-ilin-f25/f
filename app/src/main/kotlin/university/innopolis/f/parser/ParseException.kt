package university.innopolis.f.parser

import university.innopolis.f.lexer.TokenizeException

sealed class ParseException() : IllegalArgumentException() {
    class UnmatchedOpeningParen() : ParseException() {
        override fun toString() = "Unmatched opening parenthesis"
    }

    class UnmatchedClosingParen() : ParseException() {
        override fun toString() = "Unmatched closing parenthesis"
    }

    class Tokenization(val e: TokenizeException) : ParseException() {
        override fun toString() = "Tokenization error:\n$e"
    }
}
