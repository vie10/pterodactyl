package server

import api.PterodactylApi
import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.parameters.options.option
import com.github.ajalt.clikt.parameters.options.required
import kotlinx.cinterop.addressOf
import kotlinx.cinterop.toKString
import kotlinx.cinterop.usePinned
import kotlinx.coroutines.runBlocking
import okio.FileSystem
import okio.Path.Companion.toPath
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import platform.posix.getcwd

class UploadCommand : CliktCommand(
    name = "upload",
), KoinComponent {

    private val api: PterodactylApi by inject()
    private val filePath by option("--file", "-f", help = "File which you want to upload").required()
    private val destination by option("--destination", "-d", help = "Path to which you want to upload")

    override fun run() {
        val serverId = currentContext.findObject<Map<String, String>>()!!["server"]!!
        val actualPath = if (!filePath.startsWith("/")) {
            val currentDirectory = ByteArray(1024).usePinned { getcwd(it.addressOf(0), 1024) }!!.toKString()
            "$currentDirectory/$filePath"
        } else filePath
        if (!FileSystem.SYSTEM.exists(actualPath.toPath())) {
            println("File $actualPath doesn't exist.")
            return
        }
        val file = FileSystem.SYSTEM.source(actualPath.toPath())
        val destinationPath = destination ?: actualPath
        val destinationDir = destinationPath.substringBeforeLast("/", "")
        val destinationFileName = destinationPath.substringAfterLast("/")
        val isUploaded = runBlocking {
            api.uploadFileToServer(serverId, destinationFileName, file, destinationDir)
        }
        if (isUploaded) {
            println("File uploaded.")
        } else {
            println("Uploading file failed.")
        }
    }
}