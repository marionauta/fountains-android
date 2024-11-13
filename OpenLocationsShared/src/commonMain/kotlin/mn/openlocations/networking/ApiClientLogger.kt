package mn.openlocations.networking

import io.ktor.client.plugins.logging.Logger

internal class ApiClientLogger : Logger {
    override fun log(message: String) {
        println(message)
    }
}
