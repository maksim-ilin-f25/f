package university.innopolis.f.runtime

import university.innopolis.f.grammar.*

sealed class FValue() {
    data class Quote(val value: FValueQuoted) : FValue() {
        override fun toString() = value.toString()
    }

    data class Function(val value: FFunction) : FValue() {
        override fun toString(): String {
            TODO()
        }
    }

    companion object {
        fun fromLiteral(literal: FLiteral) =
            FValue.Quote(
                when (literal) {
                    is FLiteral.Integer -> FValueQuoted.Integer(literal.inner)
                    is FLiteral.Boolean -> FValueQuoted.Boolean(literal.inner)
                    is FLiteral.Null -> FValueQuoted.Null
                    is FLiteral.Real -> FValueQuoted.Real(literal.inner)
                }
            )
    }
}

sealed class FValueQuoted() {
    data class Atom(val value: FAtom) : FValueQuoted() {
        override fun toString() = value.toString()
    }

    data class Integer(val value: FInteger) : FValueQuoted() {
        override fun toString() = value.toString()
    }

    data class Real(val value: FReal) : FValueQuoted() {
        override fun toString() = value.toString()
    }

    data class Quote(val value: FValueQuoted) : FValueQuoted() {
        override fun toString() = value.toString()
    }

    data class Ast(val value: FElementQuoted) : FValueQuoted() {
        override fun toString() = TODO()
    }

    data class ValueList(val value: List<FValue>) : FValueQuoted() {
        override fun toString() = TODO()
    }

    data class Boolean(val value: FBoolean) : FValueQuoted() {
        override fun toString() = value.toString()
    }

    data object Null : FValueQuoted() {
        override fun toString() = "null"
    }
}
