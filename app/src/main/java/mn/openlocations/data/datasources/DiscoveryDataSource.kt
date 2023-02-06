package mn.openlocations.data.datasources

import mn.openlocations.data.models.ServerDiscoveryItemDto
import mn.openlocations.networking.ApiClient
import mn.openlocations.networking.KnownUris
import java.net.URL

class DiscoveryDataSource {
    companion object {
        private val discoveryAddress = URL(KnownUris.discovery)
    }

    private val apiClient = ApiClient(baseUrl = discoveryAddress)

    suspend fun all(): List<ServerDiscoveryItemDto> {
        val response = apiClient.get<List<ServerDiscoveryItemDto>?>("servers.json")
        return response ?: emptyList()
    }
}
