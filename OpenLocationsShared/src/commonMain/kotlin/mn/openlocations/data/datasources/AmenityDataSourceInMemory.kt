package mn.openlocations.data.datasources

import mn.openlocations.data.models.LocationBounds
import mn.openlocations.data.models.OsmId
import mn.openlocations.data.models.OverpassNw
import mn.openlocations.data.routes.OverpassFilter

internal object AmenityDataSourceInMemory : AmenityDataSourceCached {
    private val cache: MutableMap<String, OverpassNw> = mutableMapOf()

    override fun save(amenities: Collection<OverpassNw>) {
        for (amenity in amenities) {
            cache[amenity.id.toString()] = amenity
        }
    }

    override suspend fun inside(
        bounds: LocationBounds,
        filters: Collection<OverpassFilter>
    ): Result<Collection<OverpassNw>> {
        // TODO: also filter by `filters`
        val filtered = cache.values.filter { it.location.isInside(bounds) }
        return Result.success(filtered)
    }

    override suspend fun get(osmId: OsmId): OverpassNw? {
        return cache[osmId.id]
    }
}
