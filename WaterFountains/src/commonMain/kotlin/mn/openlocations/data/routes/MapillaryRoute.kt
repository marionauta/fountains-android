package mn.openlocations.data.routes

import mn.openlocations.networking.ApiRoute

data class MapillaryRoute(val id: String, val token: String) : ApiRoute {
    override val route: String = "$id?fields=thumb_1024_url"
    override val headers: Map<String, String> = mapOf(
        Pair("Authorization", "OAuth $token")
    )
    override val parameters: Map<String, String> = emptyMap()
}
