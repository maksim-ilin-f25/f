package university.innopolis.f.lexer.state

data class ParsingState(
    val currentToken: String = "",
    val column: UInt = 0u,
    val row: UInt = 0u,
    val waitFor: WaitFor = WaitFor.ANY,
)
