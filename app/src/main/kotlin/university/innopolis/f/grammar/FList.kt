package university.innopolis.f.grammar

import university.innopolis.f.lexer.Coordinate
import university.innopolis.f.lexer.FToken
import university.innopolis.f.parser.ParseException

@JvmInline
value class FList(private val elements: MutableList<FElement>) {

    fun toFunCallOrNull(): FElement.FunCall? {
        val first = elements.firstOrNull()
        if (first == null) {
            return null
        }
        return FElement.FunCall(funName = first, params = elements.subList(1, elements.size))
    }

    companion object {
        /** On success, returns the index after the matching closing parenthesis. */
        fun parse(allTokens: List<FToken>, firstElemIndex: Int): Result<Pair<FList, Int>> {
            val self = FList(emptyList<FElement>().toMutableList())

            var res = Pair(firstElemIndex, true)
            while (res.second) {
                res =
                    parseElement(
                            allTokens = allTokens,
                            currentElemIndex = res.first,
                            buffer = self.elements,
                            openParCoordinate = allTokens[firstElemIndex - 1].coordinate,
                            inQuote = false,
                        )
                        .getOrElse {
                            return Result.failure(it)
                        }
            }

            return Result.success(Pair(self, res.first))
        }

        private fun parseElement(
            allTokens: List<FToken>,
            currentElemIndex: Int,
            buffer: MutableList<FElement>,
            openParCoordinate: Coordinate,
            inQuote: Boolean,
        ): Result<Pair<Int, Boolean>> {
            val currentToken = allTokens.getOrNull(currentElemIndex)
            if (currentToken == null) {
                return Result.failure(ParseException.UnmatchedOpeningParen(openParCoordinate))
            }
            when (currentToken) {
                is FToken.OpeningParenthesis -> { // recursion
                    val (listAst, nextIndex) =
                        parse(allTokens, currentElemIndex + 1).getOrElse {
                            return Result.failure(it)
                        }
                    if (inQuote) {
                        buffer.add(FElement.Quote.List(listAst))
                    } else {
                        val funCall = listAst.toFunCallOrNull()
                        if (funCall == null) {
                            return Result.failure(
                                ParseException.InvalidFunCall(currentToken.coordinate)
                            )
                        }
                        buffer.add(funCall)
                    }
                    return Result.success(Pair(nextIndex, true))
                }
                is FToken.ClosingParenthesis -> { // exit success
                    return Result.success(Pair(currentElemIndex + 1, false))
                }
                is FToken.Atom -> {
                    if (inQuote) {
                        buffer.add(FElement.Quote.Atom(currentToken.value))
                    } else {
                        buffer.add(FElement.Atom(currentToken.value))
                    }
                }
                is FToken.Literal -> {
                    if (inQuote) {
                        buffer.add(FElement.Quote.Literal(currentToken.value))
                    } else {
                        buffer.add(FElement.Literal(currentToken.value))
                    }
                }
                is FToken.Keyword -> {
                    if (inQuote) {
                        buffer.add(FElement.Quote.Keyword(currentToken.value))
                    } else {
                        buffer.add(FElement.Keyword(currentToken.value))
                    }
                }
                is FToken.Quote -> {
                    val res =
                        parseElement(
                                allTokens = allTokens,
                                currentElemIndex = currentElemIndex + 1,
                                buffer = buffer,
                                openParCoordinate = openParCoordinate,
                                inQuote = true,
                            )
                            .getOrElse {
                                return Result.failure(it)
                            }
                    return Result.success(res)
                }
            }

            return Result.success(Pair(currentElemIndex + 1, true))
        }
    }
}
