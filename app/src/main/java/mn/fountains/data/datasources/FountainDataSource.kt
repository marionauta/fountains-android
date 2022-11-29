package mn.fountains.data.datasources

import mn.fountains.data.models.FountainDto
import mn.fountains.data.models.FountainsResponse
import mn.fountains.data.models.ServerResponse
import mn.fountains.networking.ApiClient
import java.net.URL

class FountainDataSource {
    suspend fun all(url: URL): List<FountainDto>? {
        val apiClient = ApiClient(baseUrl = url)
        return apiClient.get<ServerResponse<FountainsResponse>>("v1/drinking-fountains")?.data?.fountains
    }
}
