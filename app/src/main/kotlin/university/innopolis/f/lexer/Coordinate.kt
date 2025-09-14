package university.innopolis.f.lexer

data class Coordinate(private var _line: UInt = 1u, private var _column: UInt = 1u) {
    override fun toString(): String = "$_line:$_column"

    fun clearColumn() {
        _column = 1u
    }

    fun incrementLine() {
        _line += 1u
    }

    fun incrementColumn() {
        _column += 1u
    }
}
