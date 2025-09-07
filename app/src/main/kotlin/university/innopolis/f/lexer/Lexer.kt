package university.innopolis.f.lexer

import university.innopolis.f.lexer.state.ParsingState
import java.io.InputStream

class Lexer {
    private val tokenBuffer = emptyList<FToken>().toMutableList()
    private var parsingState = ParsingState()

    fun tokenize(sourceCode: InputStream): List<FToken> {
        val code = sourceCode.bufferedReader().use { it.readText() }
        for (char in code) {
            when {
                char == '(' -> processOpeningParenthesis()
                char == ')' -> processClosingParenthesis()
                char == '\'' -> processQuote()
                char == '+' -> TODO()
                char == '-' -> TODO()
                char == '.' -> TODO()
                char.isAsciiDigit() -> TODO()
                char.isAsciiLetter() -> TODO()
                char.isWhitespace() -> TODO()
                else -> TODO()
            }
        }
        return tokenBuffer
    }

    private fun processOpeningParenthesis() {
        tokenBuffer.add(FToken.OpeningParenthesis)
    }

    private fun processClosingParenthesis() {
        tokenBuffer.add(FToken.ClosingParenthesis)
    }

    private fun processQuote() {
        tokenBuffer.add(FToken.Quote)
    }

    private fun processPlus() {
        TODO()
    }

    private fun processMinus() {
        TODO()
    }

    private fun processDot() {
        TODO()
    }

    private fun processDigit() {
        TODO()
    }

    private fun processLetter() {
        TODO()
    }

    private fun processWhitespace() {
        TODO()
    }

    companion object {
        private fun Char.isAsciiLetter(): Boolean {
            return this in 'A'..'Z' || this in 'a'..'z'
        }

        private fun Char.isAsciiDigit(): Boolean {
            return this in '0'..'9'
        }
    }
}
