package mn.openlocations.data.datasources

import mn.openlocations.data.models.OverpassResponse
import mn.openlocations.data.routes.OverpassGeoRoute
import mn.openlocations.data.routes.OverpassRoute
import mn.openlocations.networking.ApiClient
import mn.openlocations.networking.KnownUris

class OverpassDataSource {
    companion object {
        private const val baseUrl = KnownUris.overpass
    }

    private val apiClient = ApiClient(baseUrl = baseUrl)

    suspend fun getNodes(areaId: Long): OverpassResponse? {
        val route = OverpassRoute(areaId = areaId)
        return apiClient.get<OverpassResponse>(route = route)
    }

    suspend fun getNodes(
        north: Double,
        east: Double,
        south: Double,
        west: Double,
    ): OverpassResponse? {
        val route = OverpassGeoRoute(north = north, east = east, south = south, west = west)
        return apiClient.get<OverpassResponse>(route = route)
    }
}
