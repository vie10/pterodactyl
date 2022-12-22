package server

import api.PterodactylApi
import api.model.Signal
import com.github.ajalt.clikt.core.CliktCommand
import kotlinx.coroutines.runBlocking
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class StartCommand : CliktCommand(
    name = "start",
), KoinComponent {

    private val api: PterodactylApi by inject()

    override fun run() {
        val serverId = currentContext.findObject<Map<String, String>>()!!["server"]!!
        val isStopped = runBlocking { api.sendSignalToServer(serverId, Signal.Start) }
        if (isStopped) {
            println("Start signal just sent.")
        } else {
            println("Failed to send start signal.")
        }
    }
}