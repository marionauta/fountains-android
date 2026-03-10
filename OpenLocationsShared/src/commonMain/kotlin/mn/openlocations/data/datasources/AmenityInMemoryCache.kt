package mn.openlocations.data.datasources

import mn.openlocations.data.models.OverpassNw
import kotlin.time.Instant

internal class AmenityInMemoryCache(
    val lastUpdated: Instant,
    val amenities: Map<String, OverpassNw>,
)
