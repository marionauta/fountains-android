package mn.openlocations.data.routes

import mn.openlocations.networking.ApiRoute

sealed class ServerRoute: ApiRoute {
    object Server : ServerRoute()
    object DrinkingFountains : ServerRoute()

    override val route: String
        get() = when (this) {
            Server -> "v1/server"
            DrinkingFountains -> "v1/drinking-fountains"
        }
    override val headers: Map<String, String> = emptyMap()
}
