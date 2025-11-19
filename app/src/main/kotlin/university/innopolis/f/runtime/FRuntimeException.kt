package university.innopolis.f.runtime

sealed class FRuntimeException() : IllegalArgumentException() {
    // TODO: add expected and actual
    class InvalidNumOfArgs() : FRuntimeException() {
        override fun toString(): String = "Invalid number if arguments"
    }

    /**
     * For example, expect an Atom, but pass a Literal
     *
     * ```lisp
     * (setq 1 2)
     * ```
     */
    // TODO: add expected and actual
    class InvalidArgumentPattern() : FRuntimeException() {
        override fun toString(): String = "Invalid argument types"
    }

    /**
     * ```lisp
     * setq
     * ```
     */
    // TODO: add keyword
    class StandaloneKeyword() : FRuntimeException() {
        override fun toString(): String = "Uncalled standalone keyword"
    }

    class EmptyParentheses() : FRuntimeException() {
        override fun toString(): String = "Call empty list"
    }

    // TODO: add atom name
    class UnboundAtom() : FRuntimeException() {
        override fun toString(): String = "Context does not have an atom"
    }

    /**
     * ```lisp
     * (setq foo 5)
     * (foo 6)
     * ```
     */
    // TODO: add not a function name
    class NotAFunction() : FRuntimeException() {
        override fun toString(): String = "Attempt to call not a function"
    }

    // TODO: add expected and actual
    class TypeError() : FRuntimeException() {
        override fun toString(): String = "Invalid type"
    }

    // TODO: add fraction
    class DivisionByZero() : FRuntimeException() {
        override fun toString(): String = "Division by zero"
    }

    // TODO: add expected and actual
    class NotEnoughElements() : FRuntimeException() {
        override fun toString(): String = "Not enough elements in the list"
    }

    /** `(setq a (setq b 1))` */
    // TODO: add a nonexistent value at least as a string
    class UseOfNonexistentValue() : FRuntimeException() {
        override fun toString(): String = "Use a nonexistent value"
    }

    class DuplicateParamNames() : FRuntimeException() {
        override fun toString(): String = "Create a function with duplicate parameter names"
    }

    class InvalidBreak() : FRuntimeException() {
        override fun toString(): String = "Call break out of while context"
    }

    class InvalidReturn() : FRuntimeException() {
        override fun toString(): String = "Call return in global context"
    }
}
