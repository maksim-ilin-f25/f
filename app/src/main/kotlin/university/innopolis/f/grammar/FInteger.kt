package university.innopolis.f.grammar

import java.math.BigInteger

@JvmInline
value class FInteger(private val inner: BigInteger) {
    override fun toString() = this.inner.toString()

    companion object {
        fun List<FDecimalDigit>.toPositiveFInteger(): FInteger {
            TODO()
        }

        fun List<FDecimalDigit>.toNegativeFInteger(): FInteger {
            TODO()
        }
    }
}
