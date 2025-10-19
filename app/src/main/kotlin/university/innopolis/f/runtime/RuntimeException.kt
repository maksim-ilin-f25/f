package university.innopolis.f.runtime

sealed class RuntimeException() : IllegalArgumentException() {
    class InvalidNumOfArgs() : RuntimeException() {
        override fun toString(): String = TODO()
    }
    class InvalidArgForm() : RuntimeException() {
        override fun toString(): String = TODO()
    }
}
