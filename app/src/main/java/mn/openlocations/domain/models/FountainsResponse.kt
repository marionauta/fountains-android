package mn.openlocations.domain.models

import kotlinx.datetime.Instant
import mn.openlocations.data.models.FountainDto
import mn.openlocations.data.models.FountainsResponseDto
import mn.openlocations.data.models.OverpassNode

data class FountainsResponse(
    val lastUpdated: Instant,
    val fountains: List<ParsedOverpassNode>,
)

fun FountainsResponseDto.intoDomain(): FountainsResponse = FountainsResponse(
    lastUpdated = lastUpdated,
    fountains = fountains.mapNotNull(OverpassNode::intoDomain),
)
