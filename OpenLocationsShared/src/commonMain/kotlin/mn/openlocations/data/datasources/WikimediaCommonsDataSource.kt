package mn.openlocations.data.datasources

import mn.openlocations.data.models.WikimediaCommonsResponseDto
import mn.openlocations.data.routes.WikimediaCommonsRoute
import mn.openlocations.networking.ApiClient
import mn.openlocations.networking.KnownUris

internal object WikimediaCommonsDataSource {
    private val apiClient = ApiClient(baseUrl = KnownUris.wikimediaCommons)

    internal suspend fun getImageData(id: String): WikimediaCommonsResponseDto? {
        val route = WikimediaCommonsRoute(name = id)
        val response = apiClient.get<WikimediaCommonsResponseDto>(route)
        return response
    }
}
