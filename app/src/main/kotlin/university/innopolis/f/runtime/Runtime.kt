package university.innopolis.f.runtime

import university.innopolis.f.grammar.FElement

fun run(ast: List<FElement>): Iterator<Result<String>> {
    return Runtime(ast).run()
}

class Runtime(val ast: List<FElement>) {
    fun run(): Iterator<Result<String>> {
        TODO()
    }
}
