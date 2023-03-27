package mn.openlocations.data.routes

import mn.openlocations.networking.ApiRoute

object DiscoveryRoute : ApiRoute {
    override val route: String = "servers.json"
    override val headers: Map<String, String> = emptyMap()
    override val parameters: Map<String, String> = emptyMap()
}
