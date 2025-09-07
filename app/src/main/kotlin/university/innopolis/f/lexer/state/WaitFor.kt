package university.innopolis.f.lexer.state

enum class WaitFor {
    DIGIT,
    DIGIT_WHITESPACE,
    DIGIT_DOT_WHITESPACE,
    LETTER_WHITESPACE,
    ANY
}
