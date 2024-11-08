package mn.openlocations.data.models

import kotlinx.datetime.Instant
import kotlinx.serialization.Serializable

@Serializable
internal data class AmenitiesResponseDto(
    val lastUpdated: Instant,
    val fountains: List<OverpassNode>,
)
