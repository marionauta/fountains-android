package mn.openlocations.data.datasources

import mn.openlocations.data.models.LocationDto
import mn.openlocations.data.models.ReverseGeocodingResponseDto
import mn.openlocations.data.routes.GeocodingRoute
import mn.openlocations.networking.ApiClient
import mn.openlocations.networking.KnownUris
import kotlin.math.abs

class GeocodingDataSource(private val apiKey: String) {
    private val apiClient = ApiClient(baseUrl = KnownUris.geocoding)

    suspend fun reverse(coordinate: LocationDto): String? {
        if (abs(coordinate.latitude) < 1 && abs(coordinate.longitude) < 1) {
            return null
        }
        val route = GeocodingRoute.Reverse(coordinate = coordinate, apiKey = apiKey)
        val result = apiClient.getOrError<ReverseGeocodingResponseDto>(route = route)
        try {
            val response = result.getOrThrow()
            return if (response.name.isNullOrBlank()) null else response.name
        } catch (exception: Exception) {
            println("[GeocodingDataSource] ${exception.message}")
            return null
        }
    }
}
