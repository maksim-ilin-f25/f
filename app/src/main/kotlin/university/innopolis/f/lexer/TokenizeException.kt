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

sealed class InvalidTokenException(open val coordinate: Coordinate) : IllegalArgumentException() {
    class UnsupportedCharacter(val c: Char, override val coordinate: Coordinate) :
        InvalidTokenException(coordinate) {
        override fun toString() = "$coordinate: Unsupported character: $c"
    }

    class UnexpectedForm(val c: Char, override val coordinate: Coordinate) :
        InvalidTokenException(coordinate) {
        override fun toString() = "$coordinate: Unexpected character: $c"
    }

    class Build(val token: String, override val coordinate: Coordinate) :
        InvalidTokenException(coordinate) {
        override fun toString() = "$coordinate: Invalid token: $token"
    }
}
