package university.innopolis.f.lexer

import university.innopolis.f.grammar.*
import university.innopolis.f.lexer.model.FSpecialForm
import university.innopolis.f.utils.CharUtils.isAsciiDigit
import university.innopolis.f.utils.CharUtils.isAsciiLetter

data class CurrentTokenState(private var _rawValue: String = "") {
    val rawValue
        get() = _rawValue

    fun add(c: Char) {
        _rawValue += c
    }

    fun isNotEmpty() = _rawValue.isNotEmpty()

    fun isEmpty() = _rawValue.isEmpty()

    fun canAddDot() = _rawValue.all { it.isAsciiDigit() } && this.isNotEmpty()

    fun canAddLetter() = _rawValue.firstOrNull()?.isAsciiLetter() ?: true

    fun build(): FToken? {
        val integerRegex = Regex("([+-])?[0-9]+")
        val realRegex = Regex("([+-])?[0-9]+\\.[0-9]+")
        val identifierRegex = Regex("[a-zA-Z][a-zA-Z0-9]*")

        return when {
            _rawValue.matches(integerRegex) -> {
                FToken.Literal(FLiteral.Integer(FInteger(_rawValue.toBigInteger())))
            }
            _rawValue.matches(realRegex) -> {
                FToken.Literal(FLiteral.Real(FReal(_rawValue.toBigDecimal())))
            }
            _rawValue.matches(identifierRegex) -> {
                when (_rawValue) {
                    "true" -> FToken.Literal(FLiteral.Boolean(FBoolean(true)))
                    "false" -> FToken.Literal(FLiteral.Boolean(FBoolean(false)))
                    "null" -> FToken.Literal(FLiteral.Null)
                    else -> {
                        val specialForm = FSpecialForm.fromString(_rawValue)
                        if (specialForm != null) {
                            FToken.SpecialForm(specialForm)
                        } else {
                            FToken.Identifier(FIdentifier(_rawValue))
                        }
                    }
                }
            }
            else -> null
        }
    }

    fun clear() {
        _rawValue = ""
    }
}
