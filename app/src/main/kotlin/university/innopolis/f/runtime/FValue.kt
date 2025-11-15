package university.innopolis.f.runtime

import university.innopolis.f.grammar.FElement

sealed class FValue() {
    data class Quote(val value: FElement) : FValue() {
        override fun toString() = value.toString()
    }

    data class Function(val value: FFunction) : FValue() {
        override fun toString(): String {
            TODO()
        }
    }
}
