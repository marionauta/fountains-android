package mn.openlocations.domain.models

import mn.openlocations.data.models.FountainsResponseDto
import mn.openlocations.data.models.OverpassNode

data class FountainsResponse(
    val lastUpdated: PortableDate,
    val fountains: List<Amenity>,
)

fun FountainsResponseDto.intoDomain(): FountainsResponse = FountainsResponse(
    lastUpdated = lastUpdated.toPortableDate(),
    fountains = fountains.mapNotNull(OverpassNode::intoDomain),
)
