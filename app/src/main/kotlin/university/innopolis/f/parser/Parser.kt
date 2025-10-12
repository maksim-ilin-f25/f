package university.innopolis.f.parser

import university.innopolis.f.grammar.FElement
import university.innopolis.f.grammar.FList
import university.innopolis.f.lexer.FToken
import university.innopolis.f.lexer.TokenizeException
import university.innopolis.f.lexer.tokenize

fun parseToAst(sourceCode: String): Result<List<FElement>> {
    val tokens =
        tokenize(sourceCode).getOrElse {
            return Result.failure(ParseException.Tokenization(it as TokenizeException))
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
                val funCall = listAst.toFunCallOrNull()
                if (funCall == null) {
                    return Result.failure(ParseException.InvalidFunCall(currentToken.coordinate))
                }
                elements.add(funCall)
                return Result.success(nextIndex)
            }
            is FToken.ClosingParenthesis -> {
                return Result.failure(ParseException.UnmatchedClosingParen(currentToken.coordinate))
            }
            is FToken.Atom -> {
                elements.add(FElement.Atom(currentToken.value))
            }
            is FToken.Literal -> {
                elements.add(FElement.Literal(currentToken.value))
            }
            is FToken.Quote -> {
                TODO()
                val index =
                    parseFirstElement(allTokens, currentTokenIndex + 1).getOrElse {
                        return Result.failure(it)
                    }
//                elements[elements.lastIndex] = FElement.Quote(elements.last())
                return Result.success(index)
            }
            is FToken.Keyword -> {
                elements.add(FElement.Keyword(currentToken.value))
            }
        }

        return Result.success(currentTokenIndex + 1)
    }
}
