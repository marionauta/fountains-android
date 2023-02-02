package mn.openlocations.domain.models

import kotlinx.datetime.Instant
import mn.openlocations.data.models.FountainDto
import mn.openlocations.data.models.FountainsResponseDto

data class FountainsResponse(
    val lastUpdated: Instant,
    val fountains: List<Fountain>,
)

fun FountainsResponseDto.intoDomain(): FountainsResponse = FountainsResponse(
    lastUpdated = lastUpdated,
    fountains = fountains.map(FountainDto::intoDomain),
)
