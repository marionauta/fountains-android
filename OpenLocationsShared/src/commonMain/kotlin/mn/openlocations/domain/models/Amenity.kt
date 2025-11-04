package mn.openlocations.domain.models

import mn.openlocations.data.models.OverpassNw

// Needs to be a class for iOS interop
sealed class Amenity {
    abstract val id: String
    abstract val name: String
    abstract val location: Location
    abstract val properties: AmenityProperties

    data class Fountain(
        override val id: String,
        override val name: String,
        override val location: Location,
        override val properties: FountainProperties,
    ) : Amenity()

    data class Restroom(
        override val id: String,
        override val name: String,
        override val location: Location,
        override val properties: RestroomProperties,
    ) : Amenity()
}

fun OverpassNw.intoDomain(): Amenity? {
    return when (tags["amenity"]) {
        "drinking_water" -> Amenity.Fountain(
            id = id.toString(),
            name = tags["name"] ?: "",
            location = location.intoDomain(),
            properties = tags.toFountainProperties()
        )

        "toilets" -> Amenity.Restroom(
            id = id.toString(),
            name = tags["name"] ?: "",
            location = location.intoDomain(),
            properties = tags.toRestroomProperties()
        )

        else -> null
    }
}
