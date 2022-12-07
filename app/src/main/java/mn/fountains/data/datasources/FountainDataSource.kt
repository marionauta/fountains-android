package mn.fountains.data.datasources

import mn.fountains.data.models.FountainDto
import mn.fountains.data.models.FountainsResponseDto
import mn.fountains.data.models.ServerResponse
import mn.fountains.networking.ApiClient
import java.net.URL

class FountainDataSource {
    companion object {
        // TODO: Improve this "in memory" cache
        private var fountains: FountainsResponseDto? = null
    }

    suspend fun all(url: URL): FountainsResponseDto? {
        val apiClient = ApiClient(baseUrl = url)
        val response = apiClient.get<ServerResponse<FountainsResponseDto>>("v1/drinking-fountains")
        val fountains = response?.data
        if (fountains != null) {
            FountainDataSource.fountains = fountains
        }
        return fountains
    }

    fun get(fountainId: String): FountainDto? = fountains?.fountains?.firstOrNull { it.id == fountainId }
}
