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

    //    fun parseToAst(tokens: List<FToken>): Result<List<FElement>> {
    //        for (token in tokens) {
    //            when (token) {
    //                is FToken.OpeningParenthesis -> {}
    //                is FToken.ClosingParenthesis -> TODO()
    //                is FToken.Atom -> elements.add(FElement.Atom(token.value))
    //                is FToken.Literal -> elements.add(FElement.Literal(token.value))
    //                is FToken.Quote -> TODO()
    //            }
    //        }
    //        TODO()
    //    }

    fun parseFirstElement(allTokens: List<FToken>, currentTokenIndex: Int): Result<Unit> {
        if (currentTokenIndex == allTokens.lastIndex + 1) {
            return Result.success(Unit) // parsing finished
        }

        val currentToken = allTokens[currentTokenIndex]
        when (currentToken) {
            is FToken.OpeningParenthesis -> {
                val (listAst, nextIndex) =
                    FList.parse(allTokens, currentTokenIndex + 1).getOrElse {
                        return Result.failure(it)
                    }
                elements.add(FElement.List(listAst))
                parseFirstElement(allTokens, nextIndex).getOrElse {
                    return Result.failure(it)
                }
            }
            is FToken.ClosingParenthesis -> {
                // TODO: change exception type
                return Result.failure(Exception(""))
            }
            is FToken.Atom -> {
                elements.add(FElement.Atom(currentToken.value))
                parseFirstElement(allTokens, currentTokenIndex + 1).getOrElse {
                    return Result.failure(it)
                }
            }
            is FToken.Literal -> {
                elements.add(FElement.Literal(currentToken.value))
                parseFirstElement(allTokens, currentTokenIndex + 1).getOrElse {
                    return Result.failure(it)
                }
            }
            is FToken.Quote -> {
                parseFirstElement(allTokens, currentTokenIndex + 1).getOrElse {
                    return Result.failure(it)
                }
                elements[elements.lastIndex] = FElement.Quote(elements.last())
                TODO("continue where??")
            }
            is FToken.Keyword -> {
                elements.add(FElement.Keyword(currentToken.value))
                parseFirstElement(allTokens, currentTokenIndex + 1).getOrElse {
                    return Result.failure(it)
                }
            }
        }

        return Result.success(Unit)
    }
}
