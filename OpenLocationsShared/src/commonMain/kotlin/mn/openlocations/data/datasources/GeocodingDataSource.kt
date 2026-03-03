package mn.openlocations.data.datasources

import mn.openlocations.data.models.LocationDto
import mn.openlocations.data.models.ReverseGeocodingResponseDto
import mn.openlocations.data.routes.ReverseGeocodingRoute
import mn.openlocations.networking.ApiClient
import mn.openlocations.networking.KnownUris
import kotlin.math.abs

class GeocodingDataSource(private val apiKey: String) {
    private val apiClientGeocoding = ApiClient(baseUrl = KnownUris.geocoding)
    private val apiClientNominatim = ApiClient(baseUrl = KnownUris.nominatim)

    suspend fun reverse(coordinate: LocationDto): String? {
        if (abs(coordinate.latitude) < 1 && abs(coordinate.longitude) < 1) {
            return null
        }
        val routes = listOf(
            ReverseGeocodingRoute(coordinate = coordinate, apiKey = apiKey) to apiClientGeocoding,
            ReverseGeocodingRoute(coordinate = coordinate) to apiClientNominatim,
        )
        for ((route, client) in routes) {
            val result = client.getOrError<ReverseGeocodingResponseDto>(route = route)
            try {
                val response = result.getOrThrow()
                if (response.name.isNullOrBlank()) {
                    throw Exception("missing name")
                }
                return response.name
            } catch (exception: Exception) {
                println("[GeocodingDataSource] ${exception.message}")
            }
        }
        return null
    }
}
