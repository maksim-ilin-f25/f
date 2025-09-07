package university.innopolis.f.lexer

import university.innopolis.f.lexer.state.ParsingState
import university.innopolis.f.lexer.state.WaitFor
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
        TODO("if parsingState.currentToken is not empty, then process it")
        tokenBuffer.add(FToken.OpeningParenthesis)
    }

    private fun processClosingParenthesis() {
        TODO("if parsingState.currentToken is not empty, then process it")
        tokenBuffer.add(FToken.ClosingParenthesis)
    }

    private fun processQuote() {
        TODO("if parsingState.currentToken is not empty, then process it")
        tokenBuffer.add(FToken.Quote)
    }

    private fun processPlus() {
        TODO("idk what to do if parsingState.currentToken is not empty")
        parsingState = parsingState.copy(currentToken = "+", waitFor = WaitFor.DIGIT)
    }

    private fun processMinus() {
        TODO("idk what to do if parsingState.currentToken is not empty")
        parsingState = parsingState.copy(currentToken = "-", waitFor = WaitFor.DIGIT)
    }

    private fun processDot() {
        TODO("idk what to do if parsingState.currentToken contains letters")
        parsingState =
            parsingState.copy(
                currentToken = parsingState.currentToken + ".",
                waitFor = WaitFor.DIGIT,
            )
    }

    private fun processDigit(digit: Char) {
        val waitFor =
            if (parsingState.currentToken.contains(".")) WaitFor.DIGIT_WHITESPACE
            else WaitFor.DIGIT_DOT_WHITESPACE
        parsingState.copy(currentToken = parsingState.currentToken + digit, waitFor = waitFor)
    }

    private fun processLetter(letter: Char) {
        parsingState.copy(
            currentToken = parsingState.currentToken + letter,
            waitFor = WaitFor.LETTER_WHITESPACE,
        )
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
