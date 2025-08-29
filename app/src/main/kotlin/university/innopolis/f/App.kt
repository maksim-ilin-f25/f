package university.innopolis.f

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.core.main
import com.github.ajalt.clikt.parameters.arguments.argument
import com.github.ajalt.clikt.parameters.types.inputStream

class App : CliktCommand() {
    val input by argument(help = "Path to the file to run or '-' (read from stdin)").inputStream()

    override fun run() {
        println("Hello, world!")
    }
}

fun main(args: Array<String>) = App().main(args)
