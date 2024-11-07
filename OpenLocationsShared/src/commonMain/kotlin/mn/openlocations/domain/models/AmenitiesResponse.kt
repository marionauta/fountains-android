package mn.openlocations.domain.models

import mn.openlocations.data.models.FountainsResponseDto
import mn.openlocations.data.models.OverpassNode

data class AmenitiesResponse(
    val lastUpdated: PortableDate,
    val amenities: List<Amenity>,
)

fun FountainsResponseDto.intoDomain(): AmenitiesResponse = AmenitiesResponse(
    lastUpdated = lastUpdated.toPortableDate(),
    amenities = fountains.mapNotNull(OverpassNode::intoDomain),
)
