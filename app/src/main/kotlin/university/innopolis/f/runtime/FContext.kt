package university.innopolis.f.runtime

import university.innopolis.f.grammar.FAtom

data class FContext(val parent: FContext?) {
    val locals = mutableMapOf<FAtom, FValue>()

    fun valueOf(atom: FAtom): FValue? {
        val result = locals[atom]
        if (result != null) {
            return result
        }
        return parent?.valueOf(atom)
    }

    fun set(name: FAtom, value: FValue) {
        this.locals[name] = value
    }
}
