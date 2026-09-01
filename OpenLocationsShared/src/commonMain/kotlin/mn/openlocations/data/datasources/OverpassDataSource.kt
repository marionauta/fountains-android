package mn.openlocations.data.datasources

import mn.openlocations.data.models.LocationBounds
import mn.openlocations.data.models.OsmId
import mn.openlocations.data.models.OverpassResponse
import mn.openlocations.data.routes.OverpassRoute
import mn.openlocations.data.routes.OverpassRoute.Filter
import mn.openlocations.data.routes.SingleOverpassRoute
import mn.openlocations.domain.models.FeatureFlag
import mn.openlocations.domain.models.toPortableUrl
import mn.openlocations.domain.repositories.FeatureFlagsRepository
import mn.openlocations.networking.ApiClient

internal object OverpassDataSource {
    private val filters: List<Filter> = listOf(
        Filter("amenity" to "drinking_water"),
        Filter("amenity" to "toilets"),
        Filter("amenity" to "fountain", "drinking_water" to "yes"),
    )

    private var urlIndex = -1
    private var apiClient: ApiClient? = null

    private suspend fun cycleApiClient(): ApiClient {
        val knownUris = FeatureFlagsRepository
            .get(FeatureFlag.OverpassHosts)
            .mapNotNull(String::toPortableUrl)
        urlIndex = (urlIndex + 1) % knownUris.size
        val url = knownUris.getOrNull(urlIndex) ?: knownUris.first()
        return ApiClient(url)
    }

    suspend fun getNodes(bounds: LocationBounds): OverpassResponse? {
        val route = OverpassRoute(filters = filters, bounds = bounds)
        val client = apiClient ?: cycleApiClient().also { apiClient = it }
        val response = client.formOrError<OverpassResponse>(route = route)
        if (response.isFailure) {
            apiClient = cycleApiClient()
        }
        return response.getOrNull()
    }

    suspend fun getById(osmId: OsmId): OverpassResponse? {
        val route = SingleOverpassRoute(osmId)
        val response = apiClient?.get<OverpassResponse>(route = route)
        return response
    }
}
