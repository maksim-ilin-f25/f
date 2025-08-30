import FDecimalDigit.Companion.toFDecimalDigitOrNull
import FLetter.Companion.toFLetterOrNull

@JvmInline
value class FIdentifier(private val name: String) {
    init {
        require(name.first().toFLetterOrNull() != null)
        require(
                name.drop(1).all {
                    it.toFLetterOrNull() != null || it.toFDecimalDigitOrNull() != null
                },
        )
    }

    companion object {
        fun String.toFIdentifierOrNull(): FIdentifier? =
                runCatching { FIdentifier(this) }.getOrNull()
    }
}
