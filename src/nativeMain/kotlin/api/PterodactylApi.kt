package api

import api.model.Signal
import okio.Source

interface PterodactylApi {

    suspend fun sendSignalToServer(serverId: String, signal: Signal): Boolean

    suspend fun uploadFileToServer(serverId: String, fileName: String, fileSource: Source, dir: String): Boolean
}