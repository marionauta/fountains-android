package mn.openlocations.data.routes

import mn.openlocations.networking.ApiRoute

internal class MapillaryRoute(id: String, token: String) : ApiRoute {
    override val route: String = "$id?fields=creator,thumb_1024_url"
    override val headers: Map<String, String> = mapOf(
        "Authorization" to "OAuth $token"
    )
    override val parameters: Map<String, String> = emptyMap()
}
