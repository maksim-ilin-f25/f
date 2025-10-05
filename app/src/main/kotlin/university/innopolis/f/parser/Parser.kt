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

    fun parseToAst(tokens: List<FToken>): Result<List<FElement>> {
        var index = 0
        while (index in tokens.indices) {
            index =
                parseFirstElement(tokens, index).getOrElse {
                    return Result.failure(it)
                }
        }
        return Result.success(elements)
    }

    fun parseFirstElement(allTokens: List<FToken>, currentTokenIndex: Int): Result<Int> {
        if (currentTokenIndex == allTokens.lastIndex + 1) {
            return Result.success(currentTokenIndex) // parsing finished
        }

        val currentToken = allTokens[currentTokenIndex]
        when (currentToken) {
            is FToken.OpeningParenthesis -> {
                val (listAst, nextIndex) =
                    FList.parse(allTokens, currentTokenIndex + 1).getOrElse {
                        return Result.failure(it)
                    }
                elements.add(FElement.List(listAst))
                return Result.success(nextIndex)
            }
            is FToken.ClosingParenthesis -> {
                // TODO: change exception type
                return Result.failure(Exception(""))
            }
            is FToken.Atom -> {
                elements.add(FElement.Atom(currentToken.value))
            }
            is FToken.Literal -> {
                elements.add(FElement.Literal(currentToken.value))
            }
            is FToken.Quote -> {
                val index =
                    parseFirstElement(allTokens, currentTokenIndex + 1).getOrElse {
                        return Result.failure(it)
                    }
                elements[elements.lastIndex] = FElement.Quote(elements.last())
                return Result.success(index)
            }
            is FToken.Keyword -> {
                elements.add(FElement.Keyword(currentToken.value))
            }
        }

        return Result.success(currentTokenIndex + 1)
    }
}
