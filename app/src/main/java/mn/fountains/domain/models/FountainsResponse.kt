package mn.fountains.domain.models

import kotlinx.datetime.Instant
import mn.fountains.data.models.FountainDto
import mn.fountains.data.models.FountainsResponseDto

data class FountainsResponse(
    val lastUpdated: Instant,
    val fountains: List<Fountain>,
)

fun FountainsResponseDto.intoDomain(): FountainsResponse = FountainsResponse(
    lastUpdated = lastUpdated,
    fountains = fountains.map(FountainDto::intoDomain),
)
