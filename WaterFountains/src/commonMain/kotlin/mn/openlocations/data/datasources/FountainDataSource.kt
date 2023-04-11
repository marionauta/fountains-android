package mn.openlocations.data.datasources

import mn.openlocations.data.models.FountainDto
import mn.openlocations.data.models.FountainPropertiesDto
import mn.openlocations.data.models.FountainsResponseDto
import mn.openlocations.data.models.LocationDto

// TODO: Improve this "in memory" cache
private var fountainsResponse: FountainsResponseDto? = null

class FountainDataSource {
    private val overpassDataSource = OverpassDataSource()

    suspend fun all(areaId: Long): FountainsResponseDto? {
        val response = overpassDataSource.getNodes(areaId = areaId) ?: return fountainsResponse
        val fountains = response.elements.map { node ->
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
            lastUpdated = response.lastUpdated(),
            fountains = fountains,
        )
        return fountainsResponse
    }

    suspend fun inside(
        north: Double,
        east: Double,
        south: Double,
        west: Double,
    ): FountainsResponseDto? {
        val response =
            overpassDataSource.getNodes(north = north, east = east, south = south, west = west)
                ?: return fountainsResponse
        val fountains = response.elements.map { node ->
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
            lastUpdated = response.lastUpdated(),
            fountains = fountains,
        )
        return fountainsResponse
    }

    fun get(fountainId: String): FountainDto? =
        fountainsResponse?.fountains?.firstOrNull { it.id == fountainId }
}
