package mn.openlocations.data.datasources

import mn.openlocations.data.models.OverpassResponse
import mn.openlocations.data.routes.OverpassRoute
import mn.openlocations.networking.ApiClient
import mn.openlocations.networking.KnownUris

internal object OverpassDataSource {
    private val apiClient = ApiClient(baseUrl = KnownUris.overpass)

    suspend fun getNodes(
        north: Double,
        east: Double,
        south: Double,
        west: Double,
    ): OverpassResponse? {
        val route = OverpassRoute(north = north, east = east, south = south, west = west)
        return apiClient.get<OverpassResponse>(route = route)
    }
}
