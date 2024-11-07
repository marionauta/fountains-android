package mn.openlocations.data.datasources

import mn.openlocations.data.models.FountainDto
import mn.openlocations.data.models.FountainPropertiesDto
import mn.openlocations.data.models.FountainsResponseDto
import mn.openlocations.data.models.LocationDto
import mn.openlocations.data.models.OverpassNode
import mn.openlocations.data.models.intoFountainDto

// TODO: Improve this "in memory" cache
private var fountainsResponse: FountainsResponseDto? = null

class FountainDataSource {
    private val overpassDataSource = OverpassDataSource()

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
            node.intoFountainDto()
        }
        fountainsResponse = FountainsResponseDto(
            lastUpdated = response.lastUpdated(),
            fountains = response.elements,
        )
        return fountainsResponse
    }

    fun get(fountainId: String): OverpassNode? =
        fountainsResponse?.fountains?.firstOrNull { it.id.toString() == fountainId }
}
