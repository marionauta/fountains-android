package mn.openlocations.data.datasources

import mn.openlocations.data.models.OverpassNw
import mn.openlocations.data.models.OsmId

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

    fun get(osmId: OsmId): OverpassNw? {
        val res = amenitiesResponse?.amenities?.get(osmId.id) ?: return null
        return when (osmId) {
            is OsmId.Node -> res as? OverpassNw.Node
            is OsmId.Way -> res as? OverpassNw.Way
        }
    }
}
