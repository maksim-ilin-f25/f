package university.innopolis.f.tokenizer

import university.innopolis.f.grammar.FIdentifier
import university.innopolis.f.grammar.FLiteral

sealed class FToken {
    object OpeningParenthesis : FToken()
    object ClosingParenthesis : FToken()
    class Identifier(val name: FIdentifier) : FToken()
    class Literal(val value: FLiteral) : FToken()
    class SpecialForm(val name: FSpecialForm) : FToken()
    class PredefinedFunction(val name: FPredefinedFunction) : FToken()
    object Quote : FToken()
}
