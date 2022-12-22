import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.core.subcommands
import com.github.ajalt.clikt.parameters.arguments.argument
import server.StartCommand
import server.StopCommand
import server.UploadCommand

class ServerCommand : CliktCommand(
    name = "server",
) {

    init {
        subcommands(StopCommand(), StartCommand(), UploadCommand())
    }

    val server by argument("id", help = "ID of the server")

    override fun run() {
        currentContext.findOrSetObject { mapOf("server" to server) }
    }
}