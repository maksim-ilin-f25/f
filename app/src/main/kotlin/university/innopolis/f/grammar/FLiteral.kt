package university.innopolis.f.grammar

sealed class FLiteral {
    class Integer(val inner: FInteger) : FLiteral()

    class Real(val inner: FReal) : FLiteral()

    class Boolean(val inner: FBoolean) : FLiteral()

    object Null : FLiteral()

    override fun toString(): String {
        val inner =
            when (this) {
                is Integer -> this.inner.toString()
                is Real -> this.inner.toString()
                is Boolean -> this.inner.toString()
                is Null -> "null"
            }
        return "{ literal: $inner }"
    }
}
