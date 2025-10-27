package university.innopolis.f.runtime

sealed class FRuntimeException() : IllegalArgumentException() {
    class InvalidNumOfArgs() : FRuntimeException() {
        override fun toString(): String = TODO()
    }

    class InvalidArgForm() : FRuntimeException() {
        override fun toString(): String = TODO()
    }

    class StandaloneKeyword() : FRuntimeException() {
        override fun toString(): String = TODO()
    }

    class EmptyFunCall() : FRuntimeException() {
        override fun toString(): String = TODO()
    }
}
