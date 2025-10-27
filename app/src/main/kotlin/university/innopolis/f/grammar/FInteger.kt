package university.innopolis.f.grammar

import java.math.BigInteger

@JvmInline
value class FInteger(private val inner: BigInteger) {
    override fun toString() = this.inner.toString()
}
