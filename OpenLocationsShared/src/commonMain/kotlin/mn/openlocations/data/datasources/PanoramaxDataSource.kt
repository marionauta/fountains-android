package mn.openlocations.data.datasources

import mn.openlocations.data.models.PanoramaxResponseDto
import mn.openlocations.data.routes.PanoramaxRoute
import mn.openlocations.networking.ApiClient
import mn.openlocations.networking.KnownUris

internal object PanoramaxDataSource {
    private val apiClient = ApiClient(baseUrl = KnownUris.panoramax)

    internal suspend fun getImageData(id: String): PanoramaxResponseDto? {
        val route = PanoramaxRoute(id = id)
        val response = apiClient.get<PanoramaxResponseDto>(route)
        return response
    }
}
