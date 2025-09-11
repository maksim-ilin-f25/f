package university.innopolis.f.grammar

@JvmInline
value class FIdentifier(private val name: String) {
    override fun toString() = """Identifier("${this.name}")"""
}
