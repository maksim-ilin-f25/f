enum class FDecimalDigit {
    ZERO,
    ONE,
    TWO,
    THREE,
    FOUR,
    FIVE,
    SIX,
    SEVEN,
    EIGHT,
    NINE;

    companion object {
        fun fromCharOrNull(c: Char): FDecimalDigit? =
                when (c) {
                    '0' -> ZERO
                    '1' -> ONE
                    '2' -> TWO
                    '3' -> THREE
                    '4' -> FOUR
                    '5' -> FIVE
                    '6' -> SIX
                    '7' -> SEVEN
                    '8' -> EIGHT
                    '9' -> NINE
                    else -> null
                }
    }
}
