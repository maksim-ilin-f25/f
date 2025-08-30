@JvmInline
value class FDecimalDigit(private val inner: UByte) {
    init {
        require(this.inner in 0u..9u)
    }

    companion object {
        fun UByte.toFDecimalDigitOrNull(): FDecimalDigit? =
                runCatching { FDecimalDigit(this) }.getOrNull()

        fun Char.toFDecimalDigitOrNull(): FDecimalDigit? {
            if (this.code > 255) {
                return null
            }
            return runCatching { FDecimalDigit(this.code.toUByte()) }.getOrNull()
        }
    }
}
