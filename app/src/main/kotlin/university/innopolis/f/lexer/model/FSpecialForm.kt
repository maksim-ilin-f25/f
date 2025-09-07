package university.innopolis.f.lexer.model

enum class FSpecialForm(val string: String) {
    QUOTE("quote"),
    SETQ("setq"),
    FUNC("func"),
    LAMBDA("lambda"),
    PROG("prog"),
    COND("cond"),
    WHILE("while"),
    RETURN("return"),
    BREAK("break");

    companion object {
        fun fromString(string: String): FSpecialForm? {
            return entries.firstOrNull { it.string == string }
        }
    }
}