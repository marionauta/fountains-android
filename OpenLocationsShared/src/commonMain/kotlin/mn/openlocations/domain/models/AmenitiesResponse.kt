package mn.openlocations.domain.models

data class AmenitiesResponse(
    val lastUpdated: PortableDate,
    val amenities: List<Amenity>,
)
