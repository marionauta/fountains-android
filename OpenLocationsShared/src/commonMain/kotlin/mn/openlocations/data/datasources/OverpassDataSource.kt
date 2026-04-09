package mn.openlocations.data.datasources

import io.ktor.client.plugins.logging.LogLevel
import mn.openlocations.data.models.OsmId
import mn.openlocations.data.models.OverpassResponse
import mn.openlocations.data.routes.OverpassRoute
import mn.openlocations.domain.models.FeatureFlag
import mn.openlocations.domain.models.toPortableUrl
import mn.openlocations.domain.repositories.FeatureFlagsRepository
import mn.openlocations.data.routes.SingleOverpassRoute
import mn.openlocations.networking.ApiClient

internal object OverpassDataSource {
    private var urlIndex = -1
    private var apiClient: ApiClient? = null

    private suspend fun cycleApiClient(): ApiClient {
        val knownUris = FeatureFlagsRepository
            .get(FeatureFlag.OverpassHosts)
            .mapNotNull(String::toPortableUrl)
        urlIndex = (urlIndex + 1) % knownUris.size
        val url = knownUris.getOrNull(urlIndex) ?: knownUris.first()
        return ApiClient(url, logLevel = LogLevel.NONE)
    }

    suspend fun getNodes(
        north: Double,
        east: Double,
        south: Double,
        west: Double,
    ): OverpassResponse? {
        val route = OverpassRoute(north = north, east = east, south = south, west = west)
        val client = apiClient ?: cycleApiClient().also { apiClient = it }
        val response = client.get<OverpassResponse>(route = route)
        if (response == null) {
            apiClient = cycleApiClient()
        }
        return response
    }

    suspend fun getById(osmId: OsmId): OverpassResponse? {
        val route = SingleOverpassRoute(osmId)
        val response = apiClient?.get<OverpassResponse>(route = route)
        return response
    }
}
