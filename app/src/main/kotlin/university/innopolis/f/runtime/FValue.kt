package university.innopolis.f.runtime

import university.innopolis.f.grammar.*

sealed class FValue() : Display {
    data class Integer(val value: FInteger) : FValue() {
        override fun display() = value.toString()
    }

    data class Real(val value: FReal) : FValue() {
        override fun display() = value.toString()
    }

    data class Quote(val value: FElement) : FValue() {
        override fun display() = value.display()
    }

    data class Boolean(val value: FBoolean) : FValue() {
        override fun display() = value.toString()
    }

    data object Null : FValue() {
        override fun display() = "null"
    }

    data class Function(val name: FAtom?, val params: List<FAtom>, val body: FElement) : FValue() {
        override fun display(): String {
            TODO()
        }

        fun call(args: List<FValue>): Result<FValue> {
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
