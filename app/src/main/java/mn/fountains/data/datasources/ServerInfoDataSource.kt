package mn.fountains.data.datasources

import mn.fountains.data.models.ServerInfoDto
import mn.fountains.data.models.ServerResponse
import mn.fountains.networking.ApiClient
import java.net.URL

class ServerInfoDataSource {
    suspend fun get(baseUrl: URL): ServerInfoDto? {
        val apiClient = ApiClient(baseUrl)
        return apiClient.get<ServerResponse<ServerInfoDto>?>("v1/server")?.data
    }
}