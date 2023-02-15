package mn.openlocations.data.datasources

import mn.openlocations.data.models.MapillaryResponseDto
import mn.openlocations.data.routes.MapillaryRoute
import mn.openlocations.networking.ApiClient
import mn.openlocations.networking.KnownUris

class MapillaryDataSource(private val token: String) {
    companion object {
        private const val baseUrl = KnownUris.mapillary
    }

    private val apiClient = ApiClient(baseUrl = baseUrl)

    suspend fun getImage(id: String): String? {
        if (token.isBlank()) {
            return null
        }
        val route = MapillaryRoute(id = id, token = token)
        val response = apiClient.get<MapillaryResponseDto>(route)
        return response?.thumb_1024_url
    }
}
