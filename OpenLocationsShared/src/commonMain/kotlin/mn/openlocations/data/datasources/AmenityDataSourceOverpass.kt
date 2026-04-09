package mn.openlocations.data.datasources

import mn.openlocations.data.models.LocationBounds
import mn.openlocations.data.models.OsmId
import mn.openlocations.data.models.OverpassNw

internal object AmenityDataSourceOverpass : AmenityDataSource {
    private val overpassDataSource = OverpassDataSource

    override suspend fun inside(bounds: LocationBounds): Result<Collection<OverpassNw>> {
        val response = overpassDataSource.getNodes(
            north = bounds.north, east = bounds.east, south = bounds.south, west = bounds.west,
        ) ?: return Result.failure(Exception("failed to load nws"))
        return Result.success(response.elements)
    }

    override suspend fun get(osmId: OsmId): OverpassNw? {
        return overpassDataSource.getById(osmId = osmId)?.elements?.first()
    }
}
