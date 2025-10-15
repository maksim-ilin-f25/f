package university.innopolis.f.grammar

import java.math.BigDecimal

class FReal(private val inner: BigDecimal) {
    override fun toString() = this.inner.toString()
}
