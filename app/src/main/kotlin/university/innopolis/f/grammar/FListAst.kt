package university.innopolis.f.grammar

import university.innopolis.f.grammar.FElementQuoted.*
import university.innopolis.f.lexer.Coordinate
import university.innopolis.f.lexer.FToken
import university.innopolis.f.parser.ParseException
import university.innopolis.f.runtime.FunCall

@JvmInline
value class FListAst(val elements: MutableList<FElement>) {
    override fun toString() = this.elements.toString()

    fun funNameOrNull(): FAtom? {
        val name = this.elements.firstOrNull()
        return when (name) {
            is FElement.Quote ->
                when (name.value) {
                    is FElementQuoted.Atom -> name.value.value
                    else -> null
                }
            else -> null
        }
    }

    fun argsOrNull(): List<FElement>? {
        if (this.elements.isEmpty()) {
            return null
        }
        return this.elements.subList(1, this.elements.size)
    }

    fun toFunCallOrNull(): FunCall? {
        val name = funNameOrNull() ?: return null
        val args = argsOrNull()!! // checked right above
        return FunCall(name, args)
    }

    companion object {
        /** On success, returns the index after the matching closing parenthesis. */
        fun parse(
            allTokens: List<FToken>,
            firstElemIndex: Int,
            isFirstRun: Boolean,
        ): Result<Pair<FListAst, Int>> {
            val self = FListAst(emptyList<FElement>().toMutableList())

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
                    buffer.add(FElement.Quote(FElementQuoted.Literal(currentToken.value)))
                }

                is FToken.Keyword -> {
                    buffer.add(FElement.Quote(FElementQuoted.Keyword(currentToken.value)))
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
                    val last = buffer.last()
                    buffer[buffer.lastIndex] =
                        FElement.Quote(
                            when (last) {
                                is FElement.Atom -> FElementQuoted.Atom(last.value)
                                is FElement.List -> FElementQuoted.List(last.value)
                                is FElement.Quote ->
                                    when (last.value) {
                                        is FElementQuoted.List ->
                                            Quote(FElementQuoted.List(last.value.value))
                                        is FElementQuoted.Atom -> Atom(last.value.value)
                                        is FElementQuoted.Keyword -> Keyword(last.value.value)
                                        is FElementQuoted.Literal -> Literal(last.value.value)
                                        is FElementQuoted.Quote -> Quote(last.value)
                                    }
                            }
                        )
                    return Result.success(res)
                }
            }

            return Result.success(Pair(currentElemIndex + 1, true))
        }
    }
}
