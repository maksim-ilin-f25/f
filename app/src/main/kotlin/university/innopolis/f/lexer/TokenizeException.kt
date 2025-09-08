package university.innopolis.f.lexer

sealed class TokenizeException(override val message: String) : IllegalArgumentException(message) {
    class InvalidChar(c: Char) : TokenizeException("Invalid character $c")

    class UnexpectedChar(c: Char) : TokenizeException("Unexpected character $c")

    class InvalidToken(token: String) : TokenizeException("Invalid token $token")
}
