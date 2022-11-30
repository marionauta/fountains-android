package mn.fountains.data.datasources

import mn.fountains.data.models.FountainDto
import mn.fountains.data.models.FountainsResponse
import mn.fountains.data.models.ServerResponse
import mn.fountains.networking.ApiClient
import java.net.URL

class FountainDataSource {
    companion object {
        // TODO: Improve this "in memory" cache
        private var fountains = listOf<FountainDto>()
    }

    suspend fun all(url: URL): List<FountainDto>? {
        val apiClient = ApiClient(baseUrl = url)
        val response = apiClient.get<ServerResponse<FountainsResponse>>("v1/drinking-fountains")
        val fountains = response?.data?.fountains
        if (fountains != null) {
            FountainDataSource.fountains = response.data.fountains
        }
        return fountains
    }

    fun get(fountainId: String): FountainDto? = fountains.firstOrNull { it.id == fountainId }
}
