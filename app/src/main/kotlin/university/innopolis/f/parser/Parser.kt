package university.innopolis.f.parser

import university.innopolis.f.grammar.FElement
import university.innopolis.f.grammar.FList
import university.innopolis.f.lexer.TokenizeException
import university.innopolis.f.lexer.tokenize

fun parseToAst(sourceCode: String): Result<List<FElement>> {
    val tokens =
        tokenize(sourceCode).getOrElse {
            return Result.failure(ParseException.Tokenization(it as TokenizeException))
        }
    val (listAst, _) =
        FList.parse(allTokens = tokens, 1, isFirstRun = true).getOrElse {
            return Result.failure(it)
        }
    return Result.success(listAst.elements)
}
