package mn.openlocations.data.datasources

import mn.openlocations.data.models.LocationDto
import mn.openlocations.data.models.ReverseGeocodingResponseDto
import mn.openlocations.data.routes.GeocodingRoute
import mn.openlocations.networking.ApiClient
import mn.openlocations.networking.KnownUris
import kotlin.math.abs

class GeocodingDataSource {
    companion object {
        private const val baseUrl = KnownUris.geocoding
    }

    private val apiClient = ApiClient(baseUrl = baseUrl)

    suspend fun reverse(coordinate: LocationDto): String? {
        if (abs(coordinate.latitude) < 1 && abs(coordinate.longitude) < 1) {
            return null
        }
        val route = GeocodingRoute.Reverse(coordinate = coordinate)
        return apiClient.get<ReverseGeocodingResponseDto>(route = route)?.name
    }
}
