package university.innopolis.f.lexer

import university.innopolis.f.utils.CharUtils.isAsciiDigit
import university.innopolis.f.utils.CharUtils.isAsciiLetter

fun tokenize(sourceCode: String): Result<List<FToken>> = Lexer().tokenize(sourceCode)

private class Lexer() {
    private val tokenBuffer = emptyList<FToken>().toMutableList()
    private val errors = TokenizeException()
    private var currentTokenState = CurrentTokenState()

    fun tokenize(sourceCode: String): Result<List<FToken>> {
        for (char in sourceCode) {
            when {
                char == '(' ->
                    processOpeningParenthesis().getOrElse {
                        this.errors.addError(it as InvalidTokenException)
                    }

                char == ')' ->
                    processClosingParenthesis().getOrElse {
                        this.errors.addError(it as InvalidTokenException)
                    }

                char == '\'' ->
                    processQuote().getOrElse { this.errors.addError(it as InvalidTokenException) }

                char == '+' ->
                    processPlus().getOrElse { this.errors.addError(it as InvalidTokenException) }

                char == '-' ->
                    processMinus().getOrElse { this.errors.addError(it as InvalidTokenException) }

                char == '.' ->
                    processDot().getOrElse { this.errors.addError(it as InvalidTokenException) }

                char.isAsciiDigit() -> processDigit(char)
                char.isAsciiLetter() ->
                    processLetter(char).getOrElse {
                        this.errors.addError(it as InvalidTokenException)
                    }

                char.isWhitespace() ->
                    processWhitespace().getOrElse {
                        this.errors.addError(it as InvalidTokenException)
                    }

                else -> this.errors.addError(InvalidTokenException.UnsupportedCharacter(char))
            }
        }

        return if (this.errors.isEmpty()) {
            Result.success(this.tokenBuffer)
        } else {
            Result.failure(this.errors)
        }
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
            Result.failure(InvalidTokenException.UnexpectedForm('+'))
        }
    }

    private fun processMinus(): Result<Unit> {
        return if (currentTokenState.isEmpty()) {
            currentTokenState.add('-')
            Result.success(Unit)
        } else {
            Result.failure(InvalidTokenException.UnexpectedForm('-'))
        }
    }

    private fun processDot(): Result<Unit> {
        if (!currentTokenState.canAddDot()) {
            return Result.failure(InvalidTokenException.UnexpectedForm('.'))
        }
        currentTokenState.add('.')
        return Result.success(Unit)
    }

    private fun processDigit(digit: Char) {
        currentTokenState.add(digit)
    }

    private fun processLetter(letter: Char): Result<Unit> {
        if (!currentTokenState.canAddLetter()) {
            return Result.failure(InvalidTokenException.UnexpectedForm(letter))
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
                        InvalidTokenException.Build(currentTokenState.rawValue)
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
