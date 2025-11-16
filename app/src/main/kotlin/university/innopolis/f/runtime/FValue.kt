package university.innopolis.f.runtime

import university.innopolis.f.grammar.FElement

sealed class FValue() {
    data class Quote(val value: FElement) : FValue() {
        override fun toString(): String {
            val initial = value.toString()
            return if (!initial.startsWith("'") && !initial.startsWith("(")) {
                initial
            } else {
                "'$initial"
            }
        }
    }

    data class Function(val value: FFunction) : FValue() {
        override fun toString(): String {
            return value.toString()
        }
    }
}
