package university.innopolis.f.runtime

import university.innopolis.f.grammar.FAtom
import university.innopolis.f.grammar.FElement

data class FunCall(val name: FAtom, val args: List<FElement>)
