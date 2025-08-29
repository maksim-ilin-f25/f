import java.math.BigInteger

@JvmInline
value class FInteger(private val inner: BigInteger) {
    companion object {
        fun positiveFromFDecimalDigits(digits: List<FDecimalDigit>): FInteger {
            TODO()
        }

        fun negativeFromFDecimalDigits(digits: List<FDecimalDigit>): FInteger {
            TODO()
        }
    }
}
