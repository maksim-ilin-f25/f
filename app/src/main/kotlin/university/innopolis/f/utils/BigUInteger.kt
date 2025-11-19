package university.innopolis.f.utils

import java.math.BigInteger

@JvmInline
value class BigUInteger(private val inner: BigInteger) {
    init {
        require(this.inner > 0.toBigInteger())
    }
}
