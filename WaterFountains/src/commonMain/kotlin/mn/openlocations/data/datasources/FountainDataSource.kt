package mn.openlocations.data.datasources

import mn.openlocations.data.models.FountainDto
import mn.openlocations.data.models.FountainsResponseDto
import mn.openlocations.data.models.ServerResponse
import mn.openlocations.networking.ApiClient

// TODO: Improve this "in memory" cache
private var fountainsResponse: FountainsResponseDto? = null

class FountainDataSource {
    suspend fun all(url: String): FountainsResponseDto? {
        val apiClient = ApiClient(baseUrl = url)
        val response = apiClient.get<ServerResponse<FountainsResponseDto>>("v1/drinking-fountains")
        val fountains = response?.data
        if (fountains != null) {
            fountainsResponse = fountains
        }
        return fountains
    }

    fun get(fountainId: String): FountainDto? =
        fountainsResponse?.fountains?.firstOrNull { it.id == fountainId }
}
