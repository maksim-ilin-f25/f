package university.innopolis.f.grammar

@JvmInline
value class FBoolean(private val inner: Boolean) {
    override fun toString() = "Boolean(${this.inner})"
}
