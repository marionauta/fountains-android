package mn.fountains.data.models

import kotlinx.datetime.Instant
import kotlinx.serialization.Serializable

@Serializable
data class FountainsResponseDto(
    val lastUpdated: Instant,
    val fountains: List<FountainDto>,
)
