package mn.openlocations.networking

import kotlin.time.Duration

interface ApiRoute {
    val route: String
    val headers: Map<String, String>
    val parameters: Map<String, Any>
    val timeout: Duration?
}
