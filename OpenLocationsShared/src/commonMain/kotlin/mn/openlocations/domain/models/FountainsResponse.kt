package mn.openlocations.domain.models

import mn.openlocations.data.models.FountainDto
import mn.openlocations.data.models.FountainsResponseDto

data class FountainsResponse(
    val lastUpdated: PortableDate,
    val fountains: List<Fountain>,
)

fun FountainsResponseDto.intoDomain(): FountainsResponse = FountainsResponse(
    lastUpdated = lastUpdated.toPortableDate(),
    fountains = fountains.map(FountainDto::intoDomain),
)
