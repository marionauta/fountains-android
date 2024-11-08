package mn.openlocations.data.datasources

import mn.openlocations.data.models.AmenitiesResponseDto
import mn.openlocations.data.models.OverpassNode

// TODO: Improve this "in memory" cache
private var fountainsResponse: AmenitiesResponseDto? = null

internal class FountainDataSource {
    private val overpassDataSource = OverpassDataSource()

    suspend fun inside(
        north: Double,
        east: Double,
        south: Double,
        west: Double,
    ): AmenitiesResponseDto? {
        val response =
            overpassDataSource.getNodes(north = north, east = east, south = south, west = west)
                ?: return fountainsResponse
        fountainsResponse = AmenitiesResponseDto(
            lastUpdated = response.lastUpdated(),
            fountains = response.elements,
        )
        return fountainsResponse
    }

    fun get(fountainId: String): OverpassNode? =
        fountainsResponse?.fountains?.firstOrNull { it.id.toString() == fountainId }
}
