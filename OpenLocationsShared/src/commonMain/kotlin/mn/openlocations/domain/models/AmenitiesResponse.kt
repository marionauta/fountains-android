package mn.openlocations.domain.models

import mn.openlocations.data.models.OverpassNw

typealias AmenitiesResponse = Collection<Amenity>

fun Collection<OverpassNw>.intoDomain(languages: List<String>): AmenitiesResponse {
    return mapNotNull { it.intoDomain(languages) }
}
