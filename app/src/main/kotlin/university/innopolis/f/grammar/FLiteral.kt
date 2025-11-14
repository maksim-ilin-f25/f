package university.innopolis.f.grammar

sealed class FLiteral {
    class Integer(val inner: FInteger) : FLiteral() {
        override fun toString() = inner.toString()
    }

    class Real(val inner: FReal) : FLiteral() {
        override fun toString() = inner.toString()
    }

    class Boolean(val inner: FBoolean) : FLiteral() {
        override fun toString() = inner.toString()
    }

    object Null : FLiteral() {
        override fun toString() = "null"
    }
}
