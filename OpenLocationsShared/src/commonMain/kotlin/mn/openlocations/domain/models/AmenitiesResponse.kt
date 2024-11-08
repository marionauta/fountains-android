package mn.openlocations.domain.models

import mn.openlocations.data.models.AmenitiesResponseDto
import mn.openlocations.data.models.OverpassNode

data class AmenitiesResponse(
    val lastUpdated: PortableDate,
    val amenities: List<Amenity>,
)

internal fun AmenitiesResponseDto.intoDomain(): AmenitiesResponse = AmenitiesResponse(
    lastUpdated = lastUpdated.toPortableDate(),
    amenities = fountains.mapNotNull(OverpassNode::intoDomain),
)
