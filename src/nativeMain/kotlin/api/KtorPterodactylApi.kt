package api

import api.model.Signal
import common.Environment
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.curl.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.logging.*
import io.ktor.client.request.*
import io.ktor.client.request.forms.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.utils.io.core.*
import kotlinx.serialization.json.*
import okio.Source
import okio.buffer

class KtorPterodactylApi : PterodactylApi {

    private val client: HttpClient = HttpClient(Curl) {
        install(ContentNegotiation) { json() }
        install(Logging) {
            level = LogLevel.BODY
            logger = Logger.EMPTY
        }
        expectSuccess = false
        defaultRequest {
            accept(ContentType.Application.Json)
            contentType(ContentType.Application.Json)
            bearerAuth(Environment.API_TOKEN!!)
        }
    }

    override suspend fun uploadFileToServer(
        serverId: String,
        fileName: String,
        fileSource: Source,
        dir: String
    ): Boolean {
        val uploadUrl = client.get("${Environment.HOST}/api/client/servers/$serverId/files/upload").run {
            if (!status.isSuccess()) return false
            body<JsonObject>()["attributes"]!!.jsonObject["url"]!!.jsonPrimitive.content
        }
        return client.post("${uploadUrl}&directory=${dir.encodeURLPath()}") {
            contentType(ContentType.Any)
            setBody(MultiPartFormDataContent(parts = formData {
                append("files", fileName) {
                    writeFully(fileSource.buffer().readByteArray())
                }
            }))
        }.status.isSuccess()
    }

    override suspend fun sendSignalToServer(serverId: String, signal: Signal): Boolean {
        return client.post("${Environment.HOST}/api/client/servers/$serverId/power") {
            setBody(
                buildJsonObject {
                    put("signal", signal.name.lowercase())
                }
            )
        }.status.isSuccess()
    }
}