package mn.openlocations.data.routes

import mn.openlocations.networking.ApiRoute

data class NominatimRoute(val name: String) : ApiRoute {
    override val route: String = "?q=$name&format=json"
    override val headers: Map<String, String> = emptyMap()
}
