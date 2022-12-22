import com.github.ajalt.clikt.completion.CompletionCommand
import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.core.subcommands
import common.Environment
import platform.posix.exit

class PterodactylCommand : CliktCommand(
    name = "pterodactyl",
) {

    init {
        subcommands(ServerCommand(), CompletionCommand())
    }

    override fun run() {
        if (Environment.API_TOKEN == null) {
            println("Environment PTERODACTYL_API_TOKEN not set.")
            exit(0)
        }
        if (Environment.HOST == null) {
            println("Environment PTERODACTYL_HOST not set.")
            exit(0)
        }
    }
}