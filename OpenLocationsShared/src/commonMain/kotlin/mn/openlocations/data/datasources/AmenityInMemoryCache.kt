package mn.openlocations.data.datasources

import kotlinx.datetime.Instant
import mn.openlocations.data.models.OverpassNw

internal class AmenityInMemoryCache(
    val lastUpdated: Instant,
    val amenities: Map<String, OverpassNw>,
)
