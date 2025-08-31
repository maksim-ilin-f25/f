package university.innopolis.f.grammar

import university.innopolis.f.grammar.FDecimalDigit.Companion.toFDecimalDigitOrNull
import university.innopolis.f.grammar.FLetter.Companion.toFLetterOrNull

@JvmInline
value class FIdentifier(private val name: String) {
    init {
        require(this.name.first().toFLetterOrNull() != null)
        require(
            this.name.drop(1).all {
                it.toFLetterOrNull() != null || it.toFDecimalDigitOrNull() != null
            }
        )
    }

    companion object {
        fun String.toFIdentifierOrNull(): FIdentifier? =
            runCatching { FIdentifier(this) }.getOrNull()
    }
}
