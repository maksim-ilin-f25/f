package university.innopolis.f.lexer

import university.innopolis.f.utils.CharUtils.isAsciiDigit
import university.innopolis.f.utils.CharUtils.isAsciiLetter

fun tokenize(sourceCode: String): Result<List<FToken>> = Lexer().tokenize(sourceCode)

private class Lexer() {
    private val tokenBuffer = emptyList<FToken>().toMutableList()
    private var currentTokenState = CurrentTokenState()

    fun tokenize(sourceCode: String): Result<List<FToken>> {
        for (char in sourceCode) {
            when {
                char == '(' ->
                    processOpeningParenthesis().getOrElse {
                        return Result.failure(it)
                    }

                char == ')' ->
                    processClosingParenthesis().getOrElse {
                        return Result.failure(it)
                    }

                char == '\'' ->
                    processQuote().getOrElse {
                        return Result.failure(it)
                    }

                char == '+' ->
                    processPlus().getOrElse {
                        return Result.failure(it)
                    }

                char == '-' ->
                    processMinus().getOrElse {
                        return Result.failure(it)
                    }

                char == '.' ->
                    processDot().getOrElse {
                        return Result.failure(it)
                    }

                char.isAsciiDigit() -> processDigit(char)
                char.isAsciiLetter() ->
                    processLetter(char).getOrElse {
                        return Result.failure(it)
                    }

                char.isWhitespace() ->
                    processWhitespace().getOrElse {
                        return Result.failure(it)
                    }

                else -> return Result.failure(TokenizeException.InvalidChar(char))
            }
        }
        return Result.success(tokenBuffer)
    }

    private fun processOpeningParenthesis(): Result<Unit> {
        val result = this.addCurrentTokenIfNotEmpty()
        this.addToken(FToken.OpeningParenthesis)
        return result
    }

    private fun processClosingParenthesis(): Result<Unit> {
        val result = this.addCurrentTokenIfNotEmpty()
        this.addToken(FToken.ClosingParenthesis)
        return result
    }

    private fun processQuote(): Result<Unit> {
        val result = this.addCurrentTokenIfNotEmpty()
        this.addToken(FToken.Quote)
        return result
    }

    private fun processPlus(): Result<Unit> {
        return if (currentTokenState.isEmpty()) {
            currentTokenState.add('+')
            Result.success(Unit)
        } else {
            Result.failure(TokenizeException.UnexpectedChar('+'))
        }
    }

    private fun processMinus(): Result<Unit> {
        return if (currentTokenState.isEmpty()) {
            currentTokenState.add('-')
            Result.success(Unit)
        } else {
            Result.failure(TokenizeException.UnexpectedChar('-'))
        }
    }

    private fun processDot(): Result<Unit> {
        if (!currentTokenState.canAddDot()) {
            return Result.failure(TokenizeException.UnexpectedChar('.'))
        }
        currentTokenState.add('.')
        return Result.success(Unit)
    }

    private fun processDigit(digit: Char) {
        currentTokenState.add(digit)
    }

    private fun processLetter(letter: Char): Result<Unit> {
        if (!currentTokenState.canAddLetter()) {
            return Result.failure(TokenizeException.UnexpectedChar(letter))
        }
        currentTokenState.add(letter)
        return Result.success(Unit)
    }

    private fun processWhitespace(): Result<Unit> {
        return addCurrentTokenIfNotEmpty()
    }

    private fun addCurrentTokenIfNotEmpty(): Result<Unit> {
        if (currentTokenState.isNotEmpty()) {
            val token =
                this.currentTokenState.build()
                    ?: return Result.failure(
                        TokenizeException.InvalidToken(currentTokenState.rawValue)
                    )
            this.addToken(token)
        }
        return Result.success(Unit)
    }

    private fun addToken(token: FToken) {
        tokenBuffer.add(token)
        currentTokenState.clear()
    }
}
