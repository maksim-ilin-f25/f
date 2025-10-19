package university.innopolis.f.runtime

sealed class RuntimeException() : IllegalArgumentException() {
    class InvalidNumOfArgs() : RuntimeException() {
        override fun toString(): String = TODO()
    }
    class TypeMismatch() : RuntimeException() {
        override fun toString(): String = TODO()
    }
}
