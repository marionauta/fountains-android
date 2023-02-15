package mn.openlocations.data.datasources

import mn.openlocations.data.models.ServerInfoDto
import mn.openlocations.data.models.ServerResponse
import mn.openlocations.data.routes.ServerRoute
import mn.openlocations.networking.ApiClient

class ServerInfoDataSource {
    suspend fun get(baseUrl: String): ServerInfoDto? {
        val apiClient = ApiClient(baseUrl)
        return apiClient.get<ServerResponse<ServerInfoDto>>(ServerRoute.Server)?.data
    }
}
