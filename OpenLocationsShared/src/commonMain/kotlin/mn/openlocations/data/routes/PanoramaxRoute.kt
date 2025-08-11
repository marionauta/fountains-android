package mn.openlocations.data.routes

import mn.openlocations.networking.ApiRoute

internal class PanoramaxRoute(id: String) : ApiRoute {
    override val route: String = "/api/search?limit=1&ids=$id"
    override val headers: Map<String, String> = emptyMap()
    override val parameters: Map<String, String> = emptyMap()
}
