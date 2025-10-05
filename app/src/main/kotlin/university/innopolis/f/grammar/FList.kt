package university.innopolis.f.grammar

import university.innopolis.f.lexer.FToken
import university.innopolis.f.parser.ParseException

@JvmInline
value class FList(private val elements: MutableList<FElement>) {
    companion object {
        /** On success, returns the index after the matching closing parenthesis. */
        fun parse(allTokens: List<FToken>, firstElemIndex: Int): Result<Pair<FList, Int>> {
            val self = FList(emptyList<FElement>().toMutableList())

            var res = Pair(firstElemIndex, true)
            while (res.second) {
                res =
                    parseElement(allTokens, res.first, self.elements).getOrElse {
                        return Result.failure(it)
                    }
            }

            return Result.success(Pair(self, res.first))
        }

        private fun parseElement(
            allTokens: List<FToken>,
            currentElemIndex: Int,
            buffer: MutableList<FElement>,
        ): Result<Pair<Int, Boolean>> {
            val currentToken = allTokens.getOrNull(currentElemIndex)
            if (currentToken == null) {
                return Result.failure(ParseException.UnmatchedOpeningParen())
            }
            when (currentToken) {
                is FToken.OpeningParenthesis -> { // recursion
                    val (listAst, nextIndex) =
                        parse(allTokens, currentElemIndex + 1).getOrElse {
                            return Result.failure(it)
                        }
                    buffer.add(FElement.List(listAst))
                    return Result.success(Pair(nextIndex, true))
                }
                is FToken.ClosingParenthesis -> { // exit success
                    return Result.success(Pair(currentElemIndex + 1, false))
                }
                is FToken.Atom -> {
                    buffer.add(FElement.Atom(currentToken.value))
                }
                is FToken.Literal -> {
                    buffer.add(FElement.Literal(currentToken.value))
                }
                is FToken.Quote -> {
                    val res =
                        parseElement(allTokens, currentElemIndex + 1, buffer).getOrElse {
                            return Result.failure(it)
                        }
                    buffer[buffer.lastIndex] = FElement.Quote(buffer.last())
                    return Result.success(res)
                }
                is FToken.Keyword -> {
                    buffer.add(FElement.Keyword(currentToken.value))
                }
            }

            return Result.success(Pair(currentElemIndex + 1, true))
        }
    }
}
