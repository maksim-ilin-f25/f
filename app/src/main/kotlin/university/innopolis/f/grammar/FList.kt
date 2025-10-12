package university.innopolis.f.grammar

import university.innopolis.f.lexer.Coordinate
import university.innopolis.f.lexer.FToken
import university.innopolis.f.parser.ParseException

@JvmInline
value class FList(val elements: MutableList<FElement>) {
    override fun toString() = "List${this.elements}"

    companion object {
        /** On success, returns the index after the matching closing parenthesis. */
        fun parse(
            allTokens: List<FToken>,
            firstElemIndex: Int,
            isFirstRun: Boolean,
        ): Result<Pair<FList, Int>> {
            val self = FList(emptyList<FElement>().toMutableList())

            var res = Pair(firstElemIndex, true)
            while (res.second) {
                res =
                    parseElement(
                            allTokens = allTokens,
                            currentElemIndex = res.first,
                            buffer = self.elements,
                            openParCoordinate = allTokens.getOrNull(firstElemIndex - 1)?.coordinate,
                            isFirstRun = isFirstRun,
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
            openParCoordinate: Coordinate?,
            isFirstRun: Boolean,
        ): Result<Pair<Int, Boolean>> {
            val currentToken = allTokens.getOrNull(currentElemIndex)

            if (currentToken == null) {
                if (isFirstRun) {
                    return Result.success(Pair(currentElemIndex, false))
                }
                return Result.failure(ParseException.UnmatchedOpeningParen(openParCoordinate!!))
            }
            when (currentToken) {
                is FToken.OpeningParenthesis -> { // recursion
                    val (listAst, nextIndex) =
                        parse(
                                allTokens = allTokens,
                                firstElemIndex = currentElemIndex + 1,
                                isFirstRun = false,
                            )
                            .getOrElse {
                                return Result.failure(it)
                            }
                    buffer.add(FElement.List(listAst))
                    return Result.success(Pair(nextIndex, true))
                }
                is FToken.ClosingParenthesis -> {
                    if (isFirstRun) {
                        return Result.failure(
                            ParseException.UnmatchedClosingParen(currentToken.coordinate)
                        )
                    }
                    return Result.success(Pair(currentElemIndex + 1, false))
                }
                is FToken.Atom -> {
                    buffer.add(FElement.Atom(currentToken.value))
                }
                is FToken.Literal -> {
                    buffer.add(FElement.Literal(currentToken.value))
                }
                is FToken.Keyword -> {
                    buffer.add(FElement.Keyword(currentToken.value))
                }
                is FToken.Quote -> {
                    val res =
                        parseElement(
                                allTokens = allTokens,
                                currentElemIndex = currentElemIndex + 1,
                                buffer = buffer,
                                openParCoordinate = openParCoordinate,
                                isFirstRun = false,
                            )
                            .getOrElse {
                                return Result.failure(it)
                            }
                    buffer[buffer.lastIndex] = FElement.Quote(buffer.last())
                    return Result.success(res)
                }
            }

            return Result.success(Pair(currentElemIndex + 1, true))
        }
    }
}
