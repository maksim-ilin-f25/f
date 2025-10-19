package university.innopolis.f.runtime

import university.innopolis.f.grammar.FElement

data class FunCall(val name: FElement, val args: List<FElement>)
