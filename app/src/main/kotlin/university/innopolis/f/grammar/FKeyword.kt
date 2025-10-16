package university.innopolis.f.grammar

enum class FKeyword(val string: String) {
    QUOTE("quote"),
    SETQ("setq"),
    FUNC("func"),
    LAMBDA("lambda"),
    PROG("prog"),
    COND("cond"),
    WHILE("while"),
    RETURN("return"),
    BREAK("break");

    override fun toString() = """{ keyword: "${this.string}" }"""

    companion object {
        fun fromAtom(atom: FAtom): FKeyword? {
            return entries.firstOrNull { it.string == atom.name }
        }
    }
}
