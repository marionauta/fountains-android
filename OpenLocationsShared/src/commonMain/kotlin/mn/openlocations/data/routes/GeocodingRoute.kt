package mn.openlocations.data.routes

import mn.openlocations.data.models.LocationDto
import mn.openlocations.networking.ApiRoute

internal class ReverseGeocodingRoute(coordinate: LocationDto, apiKey: String? = null) : ApiRoute {
    override val route: String = "reverse"
    override val headers: Map<String, String>
        get() = emptyMap()
    override val parameters = buildMap {
        put("lat", coordinate.latitude)
        put("lon", coordinate.longitude)
        if (apiKey != null) {
            put("api_key", apiKey)
        }
    }
}
