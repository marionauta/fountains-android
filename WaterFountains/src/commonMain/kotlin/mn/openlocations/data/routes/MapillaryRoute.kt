package mn.openlocations.data.routes

import mn.openlocations.networking.ApiRoute

data class MapillaryRoute(val id: String, val token: String) : ApiRoute {
    override val route: String = "$id?fields=thumb_1024_url&access_token=$token"
}
