package mn.openlocations.data.datasources

import mn.openlocations.data.models.FountainDto
import mn.openlocations.data.models.FountainsResponseDto
import mn.openlocations.data.models.ServerResponse
import mn.openlocations.networking.ApiClient
import kotlin.native.concurrent.ThreadLocal

class FountainDataSource {
    // TODO: Investigate this ThreadLocal
    @ThreadLocal
    companion object {
        // TODO: Improve this "in memory" cache
        private var fountains: FountainsResponseDto? = null
    }

    suspend fun all(url: String): FountainsResponseDto? {
        val apiClient = ApiClient(baseUrl = url)
        val response = apiClient.get<ServerResponse<FountainsResponseDto>>("v1/drinking-fountains")
        val fountains = response?.data
        if (fountains != null) {
            Companion.fountains = fountains
        }
        return fountains
    }

    fun get(fountainId: String): FountainDto? =
        fountains?.fountains?.firstOrNull { it.id == fountainId }
}
