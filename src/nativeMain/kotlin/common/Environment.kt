package common

import kotlinx.cinterop.toKString
import platform.posix.getenv

object Environment {

    val API_TOKEN get() = getenv("PTERODACTYL_API_TOKEN")?.toKString()
    val HOST get() = getenv("PTERODACTYL_HOST")?.toKString()
}