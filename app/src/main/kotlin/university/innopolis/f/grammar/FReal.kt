package university.innopolis.f.grammar

import java.math.BigDecimal

class FReal(val inner: BigDecimal) {
    override fun toString() = this.inner.toString()
}
