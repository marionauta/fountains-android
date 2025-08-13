package mn.openlocations.data.routes

import mn.openlocations.data.models.LocationDto
import mn.openlocations.networking.ApiRoute

internal sealed interface GeocodingRoute : ApiRoute {
    class Reverse(coordinate: LocationDto, apiKey: String) : GeocodingRoute {
        override val route: String = "reverse"
        override val headers: Map<String, String>
            get() = emptyMap()
        override val parameters = mapOf(
            "lat" to coordinate.latitude,
            "lon" to coordinate.longitude,
            "api_key" to apiKey,
        )
    }
}
