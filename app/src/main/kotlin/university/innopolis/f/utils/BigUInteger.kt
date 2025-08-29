import java.math.BigInteger

@JvmInline
value class BigUInteger(private val inner: BigInteger) {
    init {
        require(inner > 0.toBigInteger())
    }

    companion object {
        fun fromFDecimalDigits(digits: List<FDecimalDigit>): BigUInteger {
            TODO()
        }
    }
}
