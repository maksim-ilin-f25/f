package university.innopolis.f.grammar

import university.innopolis.f.runtime.Display

sealed class FLiteral : Display {
    class Integer(val inner: FInteger) : FLiteral() {
        override fun display() = inner.toString()
    }

    class Real(val inner: FReal) : FLiteral() {
        override fun display() = inner.toString()
    }

    class Boolean(val inner: FBoolean) : FLiteral() {
        override fun display() = inner.toString()
    }

    object Null : FLiteral() {
        override fun display() = "null"
    }

    override fun toString(): String {
        return "{ literal: ${this.display()} }"
    }
}
