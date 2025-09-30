package university.innopolis.f.parser

import university.innopolis.f.grammar.FElement
import university.innopolis.f.lexer.FToken
import university.innopolis.f.lexer.tokenize

fun parseToAst(sourceCode: String): Result<List<FElement>> {
    val tokens =
        tokenize(sourceCode).getOrElse {
            // TODO: use a better error type
            return Result.failure(it)
        }
    return Parser().parseToAst(tokens)
}

class Parser() {
    fun parseToAst(tokens: List<FToken>): Result<List<FElement>> {
        TODO()
    }
}
