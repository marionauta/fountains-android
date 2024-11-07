package mn.openlocations.domain.models

import mn.openlocations.data.models.OverpassNode
import mn.openlocations.data.models.intoFountainDto

sealed class Amenity {
    abstract val id: String
    abstract val name: String
    abstract val location: Location

    data class Restroom(
        override val id: String,
        override val name: String,
        override val location: Location,
        val properties: RestroomProperties,
    ): Amenity()
}

fun OverpassNode.intoDomain(): Amenity? {
    return when (tags["amenity"]) {
        "drinking_water" -> intoFountainDto().intoDomain()
        "toilets" -> Amenity.Restroom(
            id = id.toString(),
            name = tags["name"] ?: "",
            location = Location(lat, lon),
            properties = tags.toRestroomProperties()
        )
        else -> null
    }
}