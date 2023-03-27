package mn.openlocations.data.datasources

import mn.openlocations.data.models.OverpassNode
import mn.openlocations.data.models.OverpassResponse
import mn.openlocations.data.routes.OverpassRoute
import mn.openlocations.networking.ApiClient
import mn.openlocations.networking.KnownUris

class OverpassDataSource {
    companion object {
        private const val baseUrl = KnownUris.overpass
    }

    private val apiClient = ApiClient(baseUrl = baseUrl)

    suspend fun getNodes(areaId: Long): List<OverpassNode> {
        val route = OverpassRoute(areaId = areaId)
        return apiClient.get<OverpassResponse>(route = route)?.elements ?: emptyList()
    }
}
