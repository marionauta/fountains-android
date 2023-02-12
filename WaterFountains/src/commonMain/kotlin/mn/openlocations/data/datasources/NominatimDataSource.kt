package mn.openlocations.data.datasources

import mn.openlocations.data.models.AreaOsm
import mn.openlocations.data.routes.NominatimRoute
import mn.openlocations.networking.ApiClient
import mn.openlocations.networking.KnownUris

class NominatimDataSource {
    companion object {
        private const val baseUrl = KnownUris.nominatim
    }

    private val apiClient = ApiClient(baseUrl = baseUrl)

    suspend fun search(name: String): List<AreaOsm>? {
        val route = NominatimRoute(name = name)
        return apiClient.get<List<AreaOsm>>(route = route)
    }
}
