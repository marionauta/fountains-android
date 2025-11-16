package mn.openlocations.data.datasources

import mn.openlocations.data.models.OverpassNw

private var amenitiesResponse: AmenityInMemoryCache? = null

internal object AmenityDataSource {
    private val overpassDataSource = OverpassDataSource

    suspend fun inside(
        north: Double,
        east: Double,
        south: Double,
        west: Double,
    ): AmenityInMemoryCache? {
        val response =
            overpassDataSource.getNodes(north = north, east = east, south = south, west = west)
                ?: return amenitiesResponse
        amenitiesResponse = AmenityInMemoryCache(
            lastUpdated = response.lastUpdated(),
            amenities = response.elements.associateBy { it.id.toString() },
        )
        return amenitiesResponse
    }

    fun get(amenityId: String): OverpassNw? =
        amenitiesResponse?.amenities?.get(amenityId)
}
