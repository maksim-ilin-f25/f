package university.innopolis.f.grammar

import java.math.BigInteger

@JvmInline
value class FInteger(val inner: BigInteger) {
    override fun toString() = this.inner.toString()
}
