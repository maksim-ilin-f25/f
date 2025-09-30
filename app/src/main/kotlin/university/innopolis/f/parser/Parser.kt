package university.innopolis.f.parser

import university.innopolis.f.grammar.FElement
import university.innopolis.f.grammar.FList
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

class Parser {
    val elements = emptyList<FElement>().toMutableList()
    val buffer = emptyList<FElement>().toMutableList()

    fun parseToAst(tokens: List<FToken>): Result<List<FElement>> {
        for (token in tokens) {
            when (token) {
                is FToken.OpeningParenthesis -> {
                    buffer.add(FElement.List(FList(emptyList())))
                }
                is FToken.ClosingParenthesis -> TODO()
                is FToken.Atom -> elements.add(FElement.Atom(token.value))
                is FToken.Literal -> elements.add(FElement.Literal(token.value))
                is FToken.Quote -> TODO()
            }
        }
        TODO()
    }
}

// [(, 1, (, 2, 3, ), 4, ), x, ']
//  0  1  2  3  4  5  6  7  8  9

// fn(tokens) -> (FElement, InexLeftOff)
//               (FList([1, FList([2, 3]), 4]), 8)
