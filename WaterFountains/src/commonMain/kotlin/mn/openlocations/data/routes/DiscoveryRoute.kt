package mn.openlocations.data.routes

import mn.openlocations.networking.ApiRoute

object DiscoveryRoute : ApiRoute {
    override val route: String = "servers.json"
}
