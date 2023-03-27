package mn.openlocations.data.routes

import mn.openlocations.networking.ApiRoute

data class NominatimRoute(val name: String) : ApiRoute {
    override val route: String = ""
    override val headers: Map<String, String> = emptyMap()
    override val parameters: Map<String, String> = mapOf(
        Pair("q", name),
        Pair("format", "json")
    )
}
