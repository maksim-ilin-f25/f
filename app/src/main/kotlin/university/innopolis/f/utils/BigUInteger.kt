import java.math.BigInteger

@JvmInline
value class BigUInteger(private val inner: BigInteger) {
    init {
        require(this.inner > 0.toBigInteger())
    }

    companion object {
        fun List<FDecimalDigit>.toBigUInteger(): BigUInteger {
            TODO()
        }
    }
}
