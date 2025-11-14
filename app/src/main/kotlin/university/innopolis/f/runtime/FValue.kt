package university.innopolis.f.runtime

import university.innopolis.f.grammar.FElementQuoted
import university.innopolis.f.grammar.FLiteral

sealed class FValue() {
    data class Quote(val value: FElementQuoted) : FValue() {
        override fun toString() = value.toString()
    }

    data class Function(val value: FFunction) : FValue() {
        override fun toString(): String {
            TODO()
        }
    }

    companion object {
        fun fromLiteral(literal: FLiteral) = FValue.Quote(FElementQuoted.Literal(literal))
    }
}
