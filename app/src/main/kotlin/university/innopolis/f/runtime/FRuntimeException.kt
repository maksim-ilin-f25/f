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

    class MalformedFunCall() : FRuntimeException() {
        override fun toString(): String = TODO()
    }

    class UnboundAtom() : FRuntimeException() {
        override fun toString(): String = TODO()
    }

    class NotAFunction() : FRuntimeException() {
        override fun toString(): String = TODO()
    }

    class TypeError() : FRuntimeException() {
        override fun toString(): String = TODO()
    }
}
