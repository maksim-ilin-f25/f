sealed class FLiteral {
    class Integer(val inner: FInteger) : FLiteral()
    class Real(val inner: FReal) : FLiteral()
    class Boolean(val inner: FBoolean) : FLiteral()
    class Null : FLiteral()
}
