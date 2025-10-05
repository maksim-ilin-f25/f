package university.innopolis.f.lexer

import university.innopolis.f.grammar.*
import university.innopolis.f.utils.CharUtils.isAsciiDigit
import university.innopolis.f.utils.CharUtils.isAsciiLetter

data class CurrentTokenState(private var _rawValue: String = "", val startCoordinate: Coordinate) {
    val rawValue
        get() = _rawValue

    fun add(c: Char) {
        _rawValue += c
    }

    fun isNotEmpty() = _rawValue.isNotEmpty()

    fun isEmpty() = _rawValue.isEmpty()

    fun canAddDot(): Boolean {
        val withoutSign = this.isNotEmpty() && this.rawValue.all { it.isAsciiDigit() }
        val withSign =
            runCatching {
                    this.rawValue.first() in listOf('-', '+') &&
                        this.rawValue.substring(1).all { it.isAsciiDigit() }
                }
                .getOrElse { false }

        return withoutSign || withSign
    }

    fun canAddLetter() = _rawValue.firstOrNull()?.isAsciiLetter() ?: true

    fun build(): FToken? {
        val integerRegex = Regex("([+-])?[0-9]+")
        val realRegex = Regex("([+-])?[0-9]+\\.[0-9]+")
        val identifierRegex = Regex("[a-zA-Z][a-zA-Z0-9]*")

        return when {
            _rawValue.matches(integerRegex) -> {
                FToken.Literal(FLiteral.Integer(FInteger(_rawValue.toBigInteger())), startCoordinate)
            }
            _rawValue.matches(realRegex) -> {
                FToken.Literal(FLiteral.Real(FReal(_rawValue.toBigDecimal())), startCoordinate)
            }
            _rawValue.matches(identifierRegex) -> {
                when (_rawValue) {
                    "true" -> FToken.Literal(FLiteral.Boolean(FBoolean(true)), startCoordinate)
                    "false" -> FToken.Literal(FLiteral.Boolean(FBoolean(false)), startCoordinate)
                    "null" -> FToken.Literal(FLiteral.Null, startCoordinate)
                    else -> {
                        val atom = FAtom(_rawValue)
                        val maybeKeyword = FKeyword.fromAtom(atom)
                        if (maybeKeyword == null) {
                            FToken.Atom(atom, startCoordinate)
                        } else {
                            FToken.Keyword(maybeKeyword, startCoordinate)
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
