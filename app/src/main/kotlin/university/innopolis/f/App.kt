package university.innopolis.f

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.core.main
import com.github.ajalt.clikt.parameters.arguments.argument
import com.github.ajalt.clikt.parameters.types.inputStream
import university.innopolis.f.lexer.tokenize

class App : CliktCommand() {
    val input by argument(help = "Path to the file to run or '-' (read from stdin)").inputStream()

    override fun run() {
        val sourceCode = this.input.bufferedReader().use { it.readText() }
        val tokens = tokenize(sourceCode)
        println(tokens)
    }
}

fun main(args: Array<String>) = App().main(args)
