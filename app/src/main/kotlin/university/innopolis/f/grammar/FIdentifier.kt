@JvmInline
value class FIdentifier(private val name: String) {
    init {
        require(FLetter.fromCharOrNull(name.first()) != null)
        require(
                name.drop(1).all {
                    FLetter.fromCharOrNull(it) != null || FDecimalDigit.fromCharOrNull(it) != null
                }
        )
    }

    companion object {
        fun fromStringOrNull(s: String): FIdentifier? = runCatching { FIdentifier(s) }.getOrNull()
    }
}
