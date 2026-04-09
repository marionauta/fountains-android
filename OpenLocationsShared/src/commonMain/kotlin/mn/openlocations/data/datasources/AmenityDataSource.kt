package mn.openlocations.data.datasources

import mn.openlocations.data.models.LocationBounds
import mn.openlocations.data.models.OsmId
import mn.openlocations.data.models.OverpassNw

internal interface AmenityDataSource {
    suspend fun inside(bounds: LocationBounds): Result<Collection<OverpassNw>>

    suspend fun get(osmId: OsmId): OverpassNw?
}
