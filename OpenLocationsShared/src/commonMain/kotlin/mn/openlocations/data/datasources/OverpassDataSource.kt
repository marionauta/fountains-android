package mn.openlocations.data.datasources

import io.ktor.client.plugins.logging.LogLevel
import mn.openlocations.data.models.OverpassResponse
import mn.openlocations.data.routes.OverpassRoute
import mn.openlocations.networking.ApiClient
import mn.openlocations.networking.KnownUris

internal object OverpassDataSource {
    private var urlIndex = -1
    private var apiClient: ApiClient = cycleApiClient()

    private fun cycleApiClient(): ApiClient {
        urlIndex = (urlIndex + 1) % KnownUris.overpass.size
        val url = KnownUris.overpass.getOrNull(urlIndex) ?: KnownUris.overpass.first()
        return ApiClient(url, logLevel = LogLevel.NONE)
    }

    suspend fun getNodes(
        north: Double,
        east: Double,
        south: Double,
        west: Double,
    ): OverpassResponse? {
        val route = OverpassRoute(north = north, east = east, south = south, west = west)
        val response = apiClient.get<OverpassResponse>(route = route)
        if (response == null) {
            apiClient = cycleApiClient()
        }
        return response
    }
}
