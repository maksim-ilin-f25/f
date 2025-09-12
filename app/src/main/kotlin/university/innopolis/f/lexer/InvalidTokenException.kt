package university.innopolis.f.lexer

class TokenizeException(
    val errors: MutableList<InvalidTokenException> =
        emptyList<InvalidTokenException>().toMutableList()
) : IllegalArgumentException() {
    fun addError(error: InvalidTokenException) {
        this.errors.add(error)
    }

    fun isEmpty() = this.errors.isEmpty()

    override fun toString() = this.errors.joinToString("\n") { "- $it" }
}

sealed class InvalidTokenException : IllegalArgumentException() {
    class UnsupportedCharacter(val c: Char) : InvalidTokenException() {
        override fun toString() = "Unsupported character: $c"
    }

    class UnexpectedForm(val c: Char) : InvalidTokenException() {
        override fun toString() = "Unexpected character: $c"
    }

    class Build(val token: String) : InvalidTokenException() {
        override fun toString() = "Invalid token: $token"
    }
}
