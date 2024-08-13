package mn.openlocations.data.routes

import mn.openlocations.data.models.LocationDto
import mn.openlocations.networking.ApiRoute

sealed interface GeocodingRoute : ApiRoute {
    class Reverse(coordinate: LocationDto, apiKey: String) : GeocodingRoute {
        override val route: String = "reverse"
        override val headers: Map<String, String> = emptyMap()
        override val parameters: Map<String, String> = mapOf(
            "lat" to coordinate.latitude.toString(),
            "lon" to coordinate.longitude.toString(),
            "api_key" to apiKey,
        )
    }
}
