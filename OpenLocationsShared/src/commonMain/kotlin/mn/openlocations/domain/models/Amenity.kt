package mn.openlocations.domain.models

import mn.openlocations.data.models.OverpassNode
import mn.openlocations.data.models.intoFountainDto

sealed interface Amenity {
    val id: String
    val name: String
    val location: Location

    data class Restroom(
        override val id: String,
        override val name: String,
        override val location: Location,
    ): Amenity
}

fun OverpassNode.intoDomain(): Amenity? {
    return when (tags["amenity"]) {
        "drinking_water" -> intoFountainDto().intoDomain()
        "toilets" -> Amenity.Restroom(
            id = id.toString(),
            name = tags["name"] ?: "",
            location = Location(lat, lon),
        )
        else -> null
    }
}