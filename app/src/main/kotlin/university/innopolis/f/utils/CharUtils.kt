package university.innopolis.f.utils

object CharUtils {
    fun Char.isAsciiLetterOrUnderscore(): Boolean {
        return this == '_' || this in 'A'..'Z' || this in 'a'..'z'
    }

    fun Char.isAsciiDigit(): Boolean {
        return this in '0'..'9'
    }
}
