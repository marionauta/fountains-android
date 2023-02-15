package mn.openlocations.data.datasources

import mn.openlocations.data.models.ServerDiscoveryItemDto
import mn.openlocations.data.routes.DiscoveryRoute
import mn.openlocations.networking.ApiClient
import mn.openlocations.networking.KnownUris

class DiscoveryDataSource {
    companion object {
        private const val discoveryAddress = KnownUris.discovery
    }

    private val apiClient = ApiClient(baseUrl = discoveryAddress)

    suspend fun all(): List<ServerDiscoveryItemDto> {
        val response = apiClient.get<List<ServerDiscoveryItemDto>?>(DiscoveryRoute)
        return response ?: emptyList()
    }
}
