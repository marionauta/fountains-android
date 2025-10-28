package mn.openlocations.data.datasources

import mn.openlocations.data.models.MapillaryResponseDto
import mn.openlocations.data.routes.MapillaryRoute
import mn.openlocations.networking.ApiClient
import mn.openlocations.networking.KnownUris

internal class MapillaryDataSource(private val token: String) {
    private val apiClient = ApiClient(baseUrl = KnownUris.mapillary)

    internal suspend fun getImageData(id: String): MapillaryResponseDto? {
        if (token.isBlank()) {
            return null
        }
        val route = MapillaryRoute(id = id, token = token)
        val response = apiClient.get<MapillaryResponseDto>(route)
        return response
    }
}
