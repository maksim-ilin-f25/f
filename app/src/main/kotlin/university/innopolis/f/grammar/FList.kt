package university.innopolis.f.grammar

import university.innopolis.f.lexer.FToken

@JvmInline
value class FList(private val elements: MutableList<FElement>) {
    companion object {
        /** On success, returns the index after the matching closing parenthesis. */
        fun parse(allTokens: List<FToken>, firstElemIndex: Int): Result<Pair<FList, Int>> {
            val self = FList(emptyList<FElement>().toMutableList())
            val indexLeftOff =
                parseElement(allTokens, firstElemIndex, self.elements).getOrElse {
                    return Result.failure(it)
                }
            return Result.success(Pair(self, indexLeftOff))
        }

        private fun parseElement(
            allTokens: List<FToken>,
            currentElemIndex: Int,
            buffer: MutableList<FElement>,
        ): Result<Int> {
            val currentToken = allTokens[currentElemIndex]
            when (currentToken) {
                is FToken.OpeningParenthesis -> { // recursion
                    val (listAst, nextIndex) =
                        FList.parse(allTokens, currentElemIndex + 1).getOrElse {
                            return Result.failure(it)
                        }
                    buffer.add(FElement.List(listAst))
                    parseElement(allTokens, nextIndex, buffer).getOrElse {
                        return Result.failure(it)
                    }
                }
                is FToken.ClosingParenthesis -> { // exit success
                    return Result.success(currentElemIndex + 1)
                }
                is FToken.Atom -> {
                    buffer.add(FElement.Atom(currentToken.value))
                    parseElement(allTokens, currentElemIndex + 1, buffer).getOrElse {
                        return Result.failure(it)
                    }
                }
                is FToken.Literal -> {
                    buffer.add(FElement.Literal(currentToken.value))
                    parseElement(allTokens, currentElemIndex + 1, buffer).getOrElse {
                        return Result.failure(it)
                    }
                }
                is FToken.Quote -> {
                    val indexLeftOff =
                        parseElement(allTokens, currentElemIndex + 1, buffer).getOrElse {
                            return Result.failure(it)
                        }
                    buffer[buffer.lastIndex] = FElement.Quote(buffer.last())
                    parseElement(allTokens, indexLeftOff, buffer).getOrElse {
                        return Result.failure(it)
                    }
                }
                is FToken.Keyword -> {
                    buffer.add(FElement.Keyword(currentToken.value))
                    parseElement(allTokens, currentElemIndex + 1, buffer).getOrElse {
                        return Result.failure(it)
                    }
                }
            }

            return Result.success(currentElemIndex + 1)
        }
    }
}
