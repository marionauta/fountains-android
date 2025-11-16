package mn.openlocations.data.datasources

import kotlinx.datetime.Instant
import mn.openlocations.data.models.OverpassNw
import mn.openlocations.domain.models.Amenity

internal class AmenityInMemoryCache(
    val lastUpdated: Instant,
    val amenities: Map<String, OverpassNw>,
)
