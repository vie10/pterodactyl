package server

import api.PterodactylApi
import api.model.Signal
import com.github.ajalt.clikt.core.CliktCommand
import kotlinx.coroutines.runBlocking
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class StopCommand : CliktCommand(
    name = "stop",
), KoinComponent {

    private val api: PterodactylApi by inject()

    override fun run() {
        val serverId = currentContext.findObject<Map<String, String>>()!!["server"]!!
        val isStopped = runBlocking { api.sendSignalToServer(serverId, Signal.Stop) }
        if (isStopped) {
            println("Stop signal just sent.")
        } else {
            println("Failed to send stop signal.")
        }
    }
}