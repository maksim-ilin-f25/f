package university.innopolis.f.utils

import java.math.BigInteger
import university.innopolis.f.grammar.FDecimalDigit

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
