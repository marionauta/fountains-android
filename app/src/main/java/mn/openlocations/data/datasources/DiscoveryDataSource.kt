package mn.openlocations.data.datasources

import mn.openlocations.data.models.ServerDiscoveryItemDto
import mn.openlocations.networking.ApiClient
import java.net.URL

class DiscoveryDataSource {
    companion object {
        private val discoveryAddress = URL("https://marionauta.github.io/fountains-landing")
    }

    private val apiClient = ApiClient(baseUrl = discoveryAddress)

    suspend fun all(): List<ServerDiscoveryItemDto> {
        val response = apiClient.get<List<ServerDiscoveryItemDto>?>("servers.json")
        return response ?: emptyList()
    }
}
