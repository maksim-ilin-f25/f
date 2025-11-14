package university.innopolis.f.runtime

import university.innopolis.f.grammar.*

sealed class FValue() {
    data class Integer(val value: FInteger) : FValue() {
        override fun toString() = value.toString()
    }

    data class Real(val value: FReal) : FValue() {
        override fun toString() = value.toString()
    }

    data class Quote(val value: FElement) : FValue() {
        override fun toString() = value.toString()
    }

    data class Boolean(val value: FBoolean) : FValue() {
        override fun toString() = value.toString()
    }

    data object Null : FValue() {
        override fun toString() = "null"
    }

    data class Function(val value: FFunction) : FValue() {
        override fun toString(): String {
            TODO()
        }
    }

    companion object {
        fun fromLiteral(literal: FLiteral): FValue {
            return when (literal) {
                is FLiteral.Integer -> FValue.Integer(literal.inner)
                is FLiteral.Boolean -> FValue.Boolean(literal.inner)
                is FLiteral.Null -> FValue.Null
                is FLiteral.Real -> FValue.Real(literal.inner)
            }
        }
    }
}
