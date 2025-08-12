package mn.openlocations.data.routes

import mn.openlocations.networking.BaseRoute

internal class PanoramaxRoute(id: String) : BaseRoute(
    route = "api/search",
    parameters = mapOf(
        "limit" to 1,
        "ids" to id,
    )
)
