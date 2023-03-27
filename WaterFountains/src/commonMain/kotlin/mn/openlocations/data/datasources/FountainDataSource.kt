package mn.openlocations.data.datasources

import kotlinx.datetime.Clock
import mn.openlocations.data.models.*
import mn.openlocations.data.routes.ServerRoute
import mn.openlocations.networking.ApiClient

// TODO: Improve this "in memory" cache
private var fountainsResponse: FountainsResponseDto? = null

class FountainDataSource {
    private val overpassDataSource = OverpassDataSource()

    suspend fun all(areaId: Long): FountainsResponseDto? {
        val nodes = overpassDataSource.getNodes(areaId = areaId)
        val fountains = nodes.map { node ->
            FountainDto(
                id = node.id.toString(),
                name = node.tags["name"] ?: "",
                location = LocationDto(
                    latitude = node.lat,
                    longitude = node.lon,
                ),
                properties = FountainPropertiesDto(
                    bottle = node.tags["bottle"] ?: "unknown",
                    wheelchair = node.tags["wheelchair"] ?: "unknown",
                    mapillaryId = node.tags["mapillary"],
                    checkDate = node.tags["check_date"],
                )
            )
        }
        fountainsResponse = FountainsResponseDto(
            lastUpdated = Clock.System.now(),
            fountains = fountains,
        )
        return fountainsResponse
    }

    fun get(fountainId: String): FountainDto? =
        fountainsResponse?.fountains?.firstOrNull { it.id == fountainId }
}
