package mn.openlocations.data.datasources

import mn.openlocations.data.models.AmenitiesResponseDto
import mn.openlocations.data.models.OverpassNode

// TODO: Improve this "in memory" cache
private var amenitiesResponse: AmenitiesResponseDto? = null

internal object AmenityDataSource {
    private val overpassDataSource = OverpassDataSource

    suspend fun inside(
        north: Double,
        east: Double,
        south: Double,
        west: Double,
    ): AmenitiesResponseDto? {
        val response =
            overpassDataSource.getNodes(north = north, east = east, south = south, west = west)
                ?: return amenitiesResponse
        amenitiesResponse = AmenitiesResponseDto(
            lastUpdated = response.lastUpdated(),
            amenities = response.elements,
        )
        return amenitiesResponse
    }

    fun get(amenityId: String): OverpassNode? =
        amenitiesResponse?.amenities?.firstOrNull { it.id.toString() == amenityId }
}
