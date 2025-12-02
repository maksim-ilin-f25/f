package university.innopolis.f.runtime

sealed class FRuntimeException() : IllegalArgumentException() {
    // TODO: add expected and actual
    class InvalidNumOfArgs() : FRuntimeException() {
        override fun toString(): String = "Invalid number of arguments"
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
        override fun toString(): String = "Invalid argument form"
    }

    /**
     * ```lisp
     * setq
     * ```
     */
    // TODO: add keyword
    class StandaloneKeyword() : FRuntimeException() {
        override fun toString(): String = "Standalone keyword"
    }

    class EmptyParentheses() : FRuntimeException() {
        override fun toString(): String = "Call to an empty list"
    }

    // TODO: add atom name
    class UnboundAtom() : FRuntimeException() {
        override fun toString(): String = "Atom not found"
    }

    /**
     * ```lisp
     * (setq foo 5)
     * (foo 6)
     * ```
     */
    // TODO: add not a function name
    class NotAFunction() : FRuntimeException() {
        override fun toString(): String = "Called value is not a function"
    }

    // TODO: add expected and actual
    class TypeError() : FRuntimeException() {
        override fun toString(): String = "Type mismatch"
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
        override fun toString(): String = "Assignment failed"
    }

    class DuplicateParamNames() : FRuntimeException() {
        override fun toString(): String = "Function has duplicate parameter names"
    }

    class InvalidBreak() : FRuntimeException() {
        override fun toString(): String = "Called break outside of a while loop"
    }

    class InvalidReturn() : FRuntimeException() {
        override fun toString(): String = "Called return outside of a local context"
    }
}
