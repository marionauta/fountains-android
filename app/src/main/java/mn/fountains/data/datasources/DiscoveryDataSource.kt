package mn.fountains.data.datasources

import mn.fountains.data.models.ServerDiscoveryItemDto
import mn.fountains.networking.ApiClient
import java.net.URL

class DiscoveryDataSource {
    companion object {
        private var discoveryAddress = URL("https://marionauta.github.io/fountains-landing")
    }

    private val apiClient = ApiClient(baseUrl = discoveryAddress)

    suspend fun all(): List<ServerDiscoveryItemDto> {
        val response = apiClient.get<List<ServerDiscoveryItemDto>?>("servers.json")
        return response ?: emptyList()
    }
}
