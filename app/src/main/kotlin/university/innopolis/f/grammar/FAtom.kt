package university.innopolis.f.grammar

@JvmInline
value class FAtom(val name: String) {
    override fun toString() = """{ atom: "${this.name}" }"""
}
