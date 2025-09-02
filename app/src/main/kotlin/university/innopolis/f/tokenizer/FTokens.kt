package university.innopolis.f.tokenizer

class FTokens(private val tokens: List<FToken>) {
    constructor(sourceCode: String) {
        return FTokens(emptyList())
    }
}
