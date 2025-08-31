package university.innopolis.f.grammar

@JvmInline
value class FLetter(private val inner: UByte) {
    init {
        val c = this.inner.toInt().toChar()
        require(c in 'A'..'Z' || c in 'a'..'z')
    }

    companion object {
        fun UByte.toFLetterOrNull(): FLetter? = runCatching { FLetter(this) }.getOrNull()

        fun Char.toFLetterOrNull(): FLetter? {
            if (this.code > 255) {
                return null
            }
            return runCatching { FLetter(this.code.toUByte()) }.getOrNull()
        }
    }
}
