package university.innopolis.f.runtime

import university.innopolis.f.grammar.FBoolean
import university.innopolis.f.grammar.FElement
import university.innopolis.f.grammar.FInteger
import university.innopolis.f.grammar.FReal

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

    data class Function(val value: Nothing) : FValue() {
        override fun display(): String {
            TODO()
        }
    }
}
